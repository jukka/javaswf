package org.javaswf.j2avm.abc;

import static com.anotherbigidea.flash.avm2.Operation.OP_getlex;
import static com.anotherbigidea.flash.avm2.Operation.OP_getlocal0;
import static com.anotherbigidea.flash.avm2.Operation.OP_getscopeobject;
import static com.anotherbigidea.flash.avm2.Operation.OP_initproperty;
import static com.anotherbigidea.flash.avm2.Operation.OP_newclass;
import static com.anotherbigidea.flash.avm2.Operation.OP_popscope;
import static com.anotherbigidea.flash.avm2.Operation.OP_pushscope;
import static com.anotherbigidea.flash.avm2.Operation.OP_returnvoid;

import java.security.Signature;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.javaswf.j2avm.TranslationTarget;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.VoidType;

import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.instruction.InstructionList;
import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.AVM2ClassSlot;
import com.anotherbigidea.flash.avm2.model.AVM2Method;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.AVM2Script;
import com.anotherbigidea.flash.avm2.model.AVM2StandardName;

/**
 * Represents a class that has been or may be translated to AVM2 and holds the 
 * details of how the class was/will be translated.
 *
 * @author nickmain
 */
public final class ClassTranslation {

	/*pkg*/ final TargetABC abc;
    
    /**
     * The original Java class
     */
	/*pkg*/ final ClassModel javaClass;
    
    /**
     * The qualifies class name
     */
    private final AVM2QName className;    
    
    /**
     * The namespace for protected methods
     */
    private final AVM2Namespace protectedNamespace;
    
    /**
     * The superclass name
     */
    private final AVM2QName superName;
    
    /**
     * The scope depth of the class definition
     */
    /*pkg*/ int classScopeDepth;      

    public ClassTranslation( TargetABC abc, ClassModel javaClass ) {
        this.abc       = abc;
        this.javaClass = javaClass;
        this.className = nameForJavaClass( javaClass.type.name );
        
        if( javaClass.superclass != null ) {
            String supername = javaClass.superclass.name;
            this.superName = nameForJavaClass( supername );
        }
        else {
            this.superName = null;
        }
                
        String protName = className.namespace.name + ":" + className.name;
        this.protectedNamespace = 
            new AVM2Namespace( NamespaceKind.ProtectedNamespace, protName );
    }
    
    /**
     * Set this class as the main class for the given target.
     */
    public void setAsMainClass( TranslationTarget target ) {
        target.setMainClass( avm2Class );
    }
    
    /**
     * Get or make the target AVM2Class.
     */
    public AVM2Class avm2Class() {
        if( avm2Class == null ) {
            realize();            
        }
        
        return avm2Class;
    }
    
    /**
     * Get or generate the AVM2 name for the given Java class.
     * 
     * TODO: this would be the place to plug in some sort of strategy/factory
     *       for generating names
     * 
     * @param className the Java class name
     */
    private static final AVM2QName nameForJavaClass( String className ) {
        if( className.startsWith( "flash.Flash" ) ) {
            className = className.substring( 11 );
        }
            
        return new AVM2QName( className );
    }
    
    /**
     * Realize this as an AVM2 class in the ABC target.
     */
    public void realize() {
        if( avm2Class != null ) return;
        
        avm2Class = abc.abcFile.addClass( 
                         className, superName, 
                         true, //isSealed
                         javaClass.flags.contains( MethodFlag.MethodIsFinal ), 
                         false,  //FIXME: handle interfaces 
                         protectedNamespace );
        
        emitClassInitializationScript();
    }
    
    /**
     * Get the AVM2 QName corresponding to a Java type
     */
    public static AVM2QName qnameForJavaType( JavaType type ) {

    	if( type == VoidType.VOID         ) return AVM2StandardName.TypeVoid.qname; 
        if( type == PrimitiveType.BYTE    ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.BOOLEAN ) return AVM2StandardName.TypeBoolean.qname;
        if( type == PrimitiveType.SHORT   ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.CHAR    ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.INT     ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.FLOAT   ) return AVM2StandardName.TypeNumber.qname;
        if( type == PrimitiveType.LONG    ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.DOUBLE  ) return AVM2StandardName.TypeNumber.qname;

        if( type instanceof ArrayType  ) {
        	return AVM2StandardName.TypeArray.qname;
        }
        
    	if( type instanceof ObjectType ) {
    		return nameForJavaClass( type.name );
    	}
 
    	return null;
    }
    
    /**
     * Get the method with the given signature.
     * 
     * @return null if the given method does not exist
     */
    public MethodTranslation method( Signature sig ) {
    	MethodTranslation mt = methods.get( sig );
    	if( mt == null ) {
    		MethodModel method = javaClass.methods.get( sig );
    		if( method == null ) return null;
    		
    		mt = new MethodTranslation( this, method );
    		methods.put( sig, mt );
    	}
    	
    	return mt;
    }
    
    /**
     * The AVM2 class that represents the Java class.
     */
    /*pkg*/ AVM2Class avm2Class;

    private Map<Signature, MethodTranslation> methods = new HashMap<Signature, MethodTranslation>();
    
    /**
     * Emit the class initialization script and determine the scope depth.
     * 
     * The script has the avm2 class object as a trait and is run when the
     * class is referenced.  
     */
    private void emitClassInitializationScript() {
        
        AVM2Script     script = abc.abcFile.prependScript();
        AVM2MethodBody body   = script.script.methodBody;
                        
        AVM2QName classQName = avm2Class.name;
        AVM2ClassSlot slot = script.traits.addClass( classQName, classQName );
        slot.indexId = script.traits.traits.size() - 1;
        
        //get the inheritance chain of the class
        LinkedList<ClassModel> superclasses = new LinkedList<ClassModel>();

        ClassModel superclass = javaClass.factory.modelForName( javaClass.superclass.name );
        while( superclass != null ) {
            if( superclass.type.equals( ObjectType.OBJECT )) {
                break; //don't emit java.lang.Object
            }
            
            superclasses.addFirst( superclass );
            superclass = javaClass.factory.modelForName( superclass.superclass.name );
        }
        
        //build the scope stack for the new class (will be captured as a closure
        // by newclass)
        InstructionList ops = body.instructions;
        ops.append( OP_getlocal0 );  //the global object
        ops.append( OP_pushscope );        
        ops.append( OP_getscopeobject, 0 );

        //push the inheritance chain as the scope stack
        for( ClassModel sc : superclasses ) { 
            ops.append( OP_getlex, nameForJavaClass( sc.type.name ));
            ops.append( OP_pushscope );
        }

        //push the superclass and create the new class        
        ops.append( OP_getlex,   superName );
        ops.append( OP_newclass, classQName );
        
        //tear down the scope stack, except the global object
        for( int i = 0; i < superclasses.size(); i++ ) {
            ops.append( OP_popscope );            
        }
        
        //initialize the class slot (of the global object)
        ops.append( OP_initproperty, classQName );
        
        //end of script
        ops.append( OP_returnvoid );
                
        //set the max stack, scope regs etc.
        body.scopeDepth   = 1;
        body.maxStack     = 2;
        body.maxRegisters = 1;
        body.maxScope     = body.scopeDepth + superclasses.size() + 1;         
        
        classScopeDepth = body.maxScope;
    }
        
    /**
     * Generate the default static initializer if there is none.
     */    
    public void defaultStaticInit() {
        
    	if( avm2Class.staticInitializer != null ) return;
    	
        AVM2Method staticInit = avm2Class.staticInitializer = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
        AVM2MethodBody initBody = staticInit.methodBody;
        InstructionList ops = initBody.instructions;
        ops.append( OP_getlocal0 );  //the global object
        ops.append( OP_pushscope );
        ops.append( OP_returnvoid );
        
        initBody.maxRegisters = 1;
        initBody.maxStack     = 1;
        initBody.maxScope     = classScopeDepth + 1;
        initBody.scopeDepth   = classScopeDepth;                
    }
}
