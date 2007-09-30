/**
 * 
 */
package org.javaswf.j2avm.emitter;

import static com.anotherbigidea.flash.avm2.Operation.OP_callpropvoid;
import static com.anotherbigidea.flash.avm2.Operation.OP_constructsuper;
import static com.anotherbigidea.flash.avm2.Operation.OP_dup;
import static com.anotherbigidea.flash.avm2.Operation.OP_getlex;
import static com.anotherbigidea.flash.avm2.Operation.OP_getlocal0;
import static com.anotherbigidea.flash.avm2.Operation.OP_getlocal1;
import static com.anotherbigidea.flash.avm2.Operation.OP_getproperty;
import static com.anotherbigidea.flash.avm2.Operation.OP_getscopeobject;
import static com.anotherbigidea.flash.avm2.Operation.OP_initproperty;
import static com.anotherbigidea.flash.avm2.Operation.OP_newclass;
import static com.anotherbigidea.flash.avm2.Operation.OP_popscope;
import static com.anotherbigidea.flash.avm2.Operation.OP_pushbyte;
import static com.anotherbigidea.flash.avm2.Operation.OP_pushint;
import static com.anotherbigidea.flash.avm2.Operation.OP_pushscope;
import static com.anotherbigidea.flash.avm2.Operation.OP_returnvoid;
import static com.anotherbigidea.flash.avm2.Operation.OP_setlocal1;
import static com.anotherbigidea.flash.avm2.model.AVM2StandardName.TypeVoid;
import static com.anotherbigidea.flash.avm2.model.AVM2StandardNamespace.EmptyPackage;
import static org.javaswf.j2avm.emitter.EmitterUtils.isOverride;
import static org.javaswf.j2avm.emitter.EmitterUtils.qnameForJavaType;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.javaswf.j2avm.abc.TranslatedABC;
import org.javaswf.j2avm.abc.TranslatedClass;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.FieldModel;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.flags.ClassFlag;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.instruction.InstructionList;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2ClassSlot;
import com.anotherbigidea.flash.avm2.model.AVM2Method;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;
import com.anotherbigidea.flash.avm2.model.AVM2MethodSlot;
import com.anotherbigidea.flash.avm2.model.AVM2Name;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.AVM2Script;
import com.anotherbigidea.flash.avm2.model.AVM2StandardNamespace;



/**
 * The final JVM to AVM2 bytecode translator.
 * 
 * @author nickmain
 */
public class AVM2ClassEmitter implements TranslationStep {
        
    /**
     * @param abc the target ABC file data
     */
    public AVM2ClassEmitter( TranslatedABC abc ) {
        this.abc     = abc;
        this.abcFile = abc.abcFile;
    }
    
    /** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.model.ClassModel, org.javaswf.j2avm.TranslationContext) */
    public boolean process( ClassModel classModel, TranslationContext context ) {
        
        this.context = context;
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting " + classModel.type );
        }

        //TODO: deal with interfaces, enums and annotations
        
        //TODO: enqueue superclass for translation
        
        
        avm2Class = abc.newClass( classModel.type.name, 
        		                  classModel.superclass.name, 
        		                  classModel.flags.contains( ClassFlag.IsFinal ), 
        		                  false ); //not interface - TODO
        
        //initialization script for the class
        emitClassInitializationScript();
        
        //translate the methods
        for( MethodModel method : classModel.methods.values() ) {
            translateMethod( method );
        }
        
        //ensure that there is a static initializer
        if( avm2Class.avm2Class.staticInitializer == null ) {
            emitDefaultStaticInit();
        }
        
        //translate the fields
        for( FieldModel field : classModel.fields.values() ) {
            translateField( field );
        }
        
        //TODO: ???
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Leaving " + classModel.type );  
        }
        
        return true;
    }
    
    private final TranslatedABC abc;
    private TranslatedClass avm2Class;
    private ClassModel javaClass;
    private TranslationContext context;
    private AVM2ABCFile abcFile;
    
    /**
     * Emit the class initialization script and determine the scope depth.
     * 
     * The script has the avm2 class object as a trait and is run when the
     * class is referenced.  
     * 
     */
    private void emitClassInitializationScript() {
        
        AVM2Script     script = abcFile.prependScript();
        AVM2MethodBody body   = script.script.methodBody;
                        
        AVM2QName classQName = avm2Class.avm2Class.name;
        AVM2ClassSlot slot = script.traits.addClass( classQName, classQName );
        slot.indexId = script.traits.traits.size() - 1;
        
        //get the inheritance chain of the class
        LinkedList<ClassModel> superclasses = new LinkedList<ClassModel>();
        ClassModel superclass = context.modelForName( javaClass.superclass.name );
        while( superclass != null ) {
            if( superclass.type.equals( ObjectType.OBJECT )) {
            	break; //don't emit java.lang.Object
            }
            
            superclasses.addFirst( superclass );
            superclass = context.modelForName( superclass.superclass.name );
        }
        
        //build the scope stack for the new class (will be captured as a closure
        // by newclass)
        InstructionList ops = body.instructions;
        ops.append( OP_getlocal0 );  //the global object
        ops.append( OP_pushscope );        
        ops.append( OP_getscopeobject, 0 );

        //push the inheritance chain as the scope stack
        for( ClassModel sc : superclasses ) { 
            ops.append( OP_getlex, abc.nameForJavaClass( sc.type.name ));
            ops.append( OP_pushscope );
        }

        //push the superclass and create the new class        
        ops.append( OP_getlex,   avm2Class.superName );
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
        
        avm2Class.classScopeDepth = body.maxScope;
    }
    
    /**
     * Generate the default static initializer
     */    
    private void emitDefaultStaticInit() {
        
        AVM2Method staticInit = avm2Class.avm2Class.staticInitializer = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
        AVM2MethodBody initBody = staticInit.methodBody;
        InstructionList ops = initBody.instructions;
        ops.append( OP_getlocal0 );  //the global object
        ops.append( OP_pushscope );
        ops.append( OP_returnvoid );
        
        initBody.maxRegisters = 1;
        initBody.maxStack     = 1;
        initBody.maxScope     = avm2Class.classScopeDepth + 1;
        initBody.scopeDepth   = avm2Class.classScopeDepth;                
    }
    
    /**
     * Translate a field
     */
    private void translateField( FieldModel field ) {

        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting field " + field.name );
        }
        
        //TODO:
    }
    
    /**
     * Translate a method or constructor
     */
    private void translateMethod( MethodModel method ) {
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting method " 
                           + method.signature.toString() );
        }
        
        String methodName = method.signature.name;
        
        if( methodName.equals( MethodModel.CONSTRUCTOR_NAME )) {
//            translateConstructor( method );
            translateConstructor();
        }
        else if( methodName.equals( MethodModel.STATIC_INIT_NAME )) {
            translateStaticInit( method );
        }
        else {
            translateMemberMethod( method );
//            translateMethod();
        }
        
        //TODO: ???
    }
    
    /**
     * Translate a constructor
     */
    private void translateConstructor( MethodModel constructor ) {
        translateConstructor(); //DEBUG ONLY
        
        Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );
        
        AVM2Method method = new AVM2Method( null, flags );
        avm2Class.avm2Class.constructor = method;
        
        AVM2MethodBody body = method.methodBody;
        body.scopeDepth   = avm2Class.classScopeDepth + 1;
        
        //TODO:
    }
    
    /**
     * Translate the static initializer
     */
    private void translateStaticInit( MethodModel clinit ) {
        //TODO: translateStaticInit
    }

    /**
     * Translate a member method
     */
    private void translateMemberMethod( MethodModel method ) {
        
        Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );

        //determine the method visibility and get the corresponding namespace
        AVM2Namespace methodNamespace = 
        	method.flags.contains( MethodFlag.MethodIsPrivate ) ?
                AVM2Namespace.privateNamespace :
                AVM2StandardNamespace.EmptyPackage.namespace;
        
        AVM2QName  name       = new AVM2QName( methodNamespace, method.signature.name );                
        AVM2QName  returnType = qnameForJavaType( method.returnType, abc );
        AVM2Method amv2method = new AVM2Method( returnType, flags );
        
        //add the parameters
        ValueType[] paramTypes = method.signature.paramTypes;
        for( ValueType paramType : paramTypes ) {            
        	amv2method.addParameter( "", qnameForJavaType( paramType, abc ), null );
        }
        
        AVM2MethodSlot ms = avm2Class.avm2Class.traits.addMethod( 
               name, 
               amv2method, 
               method.flags.contains( MethodFlag.MethodIsFinal ),
               isOverride( javaClass, method, context ) );
        
        AVM2MethodBody body = amv2method.methodBody;
        body.scopeDepth = avm2Class.classScopeDepth;
        
        //instance methods have a larger scope depth due to the instance object
        if( ! method.flags.contains( MethodFlag.MethodIsStatic )) body.scopeDepth++;

        translateCode( method, body );
        
        //TODO: ???
    }
    
    /**
     * Translate Java bytecode to AVM2 bytecode
     */
    private void translateCode( MethodModel method, AVM2MethodBody body ) {
        
        //TODO: handle abstract methods
        
        AVM2Code code = new AVM2Code( body.instructions );
        code.setupInitialScope();
        
        InstructionVisitor visitor = new InstructionVisitor( code );        
        visitor.walk( javaClass, method );
                        
        //TODO: these values need further thought..
        //TODO: could reduce the stack size if longs or doubles are involved
        CodeAttribute codeAttr = method.code();
        body.maxRegisters = codeAttr.maxLocals;
        body.maxStack     = codeAttr.maxStack;
        body.maxScope     = body.scopeDepth + 1;        
    }
    
    //TODO: FOR DEBUG ONLY - REMOVE THIS
    private void translateMethod() {
            
          AVM2QName name = new AVM2QName( EmptyPackage.namespace, "drawTest" );
          
          Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );
          
          AVM2Name returnType = TypeVoid.qname;
          AVM2Method method = new AVM2Method( returnType, flags );
          
          boolean isOverride = false;
          AVM2MethodSlot ms = avm2Class.avm2Class.traits.addMethod( 
											                 name, 
											                 method, 
											                 false,
											                 isOverride );
          ms.indexId = 1;
          
          AVM2MethodBody body = method.methodBody;
          body.maxStack     = 3;
          body.maxRegisters = 2;
          body.maxScope     = 11;
          body.scopeDepth   = 10;

          AVM2Namespace pkg_flashDisplay = new AVM2Namespace( NamespaceKind.PackageNamespace, "flash.display" );
          
          InstructionList il = body.instructions;
          
          il.append( OP_getlocal0 );
          il.append( OP_getproperty, new AVM2QName( EmptyPackage.namespace, "graphics" ) );
//          il.append( OP_coerce, new AVM2QName( pkg_flashDisplay, "Graphics" ) );

          il.append( OP_setlocal1 );

          il.append( OP_getlocal1 );
          il.append( OP_pushint, 0xff88ff );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "beginFill" ), 1 );

          il.append( OP_getlocal1 );
          il.append( OP_pushbyte, 2 );
          il.append( OP_pushbyte, 0 );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "lineStyle" ), 2 );

          il.append( OP_getlocal1 );
          il.append( OP_pushbyte, 10 );
          il.append( OP_dup );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "moveTo" ), 2 );

          il.append( OP_getlocal1 );
          il.append( OP_pushbyte, 90 );
          il.append( OP_pushbyte, 10 );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "lineTo" ), 2 );

          il.append( OP_getlocal1 );
          il.append( OP_pushbyte, 90 );
          il.append( OP_pushbyte, 90 );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "lineTo" ), 2 );

          il.append( OP_getlocal1 );
          il.append( OP_pushbyte, 10 );
          il.append( OP_pushbyte, 90 );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "lineTo" ), 2 );

          il.append( OP_getlocal1 );
          il.append( OP_pushbyte, 10 );
          il.append( OP_pushbyte, 10 );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "lineTo" ), 2 );

          il.append( OP_getlocal1 );
          il.append( OP_callpropvoid, new AVM2QName( EmptyPackage.namespace, "endFill" ), 0 );

          il.append( OP_returnvoid );
          
          //TODO:        
    }
    
    //TODO: FOR DEBUG ONLY - REMOVE THIS
    private void translateConstructor( ) {
        
        Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );
        
        AVM2Method method = new AVM2Method( null, flags );
        avm2Class.avm2Class.constructor = method;
        
        AVM2MethodBody body = method.methodBody;
        body.maxStack     = 1;
        body.maxRegisters = 1;
        body.maxScope     = 11;
        body.scopeDepth   = 10;
        
        InstructionList il = body.instructions;
      
//        il.append( OP_getlocal0 );
//        il.append( OP_pushscope );
        il.append( OP_getlocal0 );
        il.append( OP_constructsuper, 0 );
        
//        il.append( OP_findpropstrict, new AVM2QName( PUBLIC_NAMESPACE, "drawTest" ));
        il.append( OP_getlocal0 );
        
        il.append( OP_callpropvoid,   new AVM2QName( EmptyPackage.namespace, "drawTest" ), 0 );

        il.append( OP_returnvoid );
    }
}
