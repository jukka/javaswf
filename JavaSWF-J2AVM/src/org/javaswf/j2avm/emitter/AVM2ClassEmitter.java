/**
 * 
 */
package org.javaswf.j2avm.emitter;

import static com.anotherbigidea.flash.avm2.Operation.*;
import static com.anotherbigidea.flash.avm2.model.AVM2StandardName.TypeVoid;
import static com.anotherbigidea.flash.avm2.model.AVM2StandardNamespace.EmptyPackage;
import static org.javaswf.j2avm.emitter.EmitterUtils.isOverride;
import static org.javaswf.j2avm.emitter.EmitterUtils.qnameForJavaType;
import static org.javaswf.j2avm.util.ASMUtils.isFinal;
import static org.javaswf.j2avm.util.ASMUtils.isInterface;
import static org.javaswf.j2avm.util.ASMUtils.isStatic;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.javaswf.j2avm.JavaClass;
import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

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

/**
 * The final JVM to AVM2 bytecode translator.
 * 
 * Requirements for the ClassNode model:
 * 
 *   - If there is more than one constructor then they must have been
 *     converted to static factory methods and the "aggregated" constructor
 *     must have been added to the class
 *
 * @author nickmain
 */
public class AVM2ClassEmitter implements TranslationStep {
    
    private JavaClass javaClass;
    private ClassNode classNode;
    private TranslationContext context;
    private AVM2ABCFile abcFile;
    private TranslationInfo info;
    
    /** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.JavaClass, org.javaswf.j2avm.TranslationContext) */
    public boolean process( JavaClass javaClass, TranslationContext context ) {
        
        this.context   = context;
        this.javaClass = javaClass;
        this.classNode = javaClass.node;
        this.abcFile   = context.getAbcFile();
        this.info      = javaClass.info;
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting " + javaClass.clazz );
        }

        //TODO: deal with interfaces, enums and annotations
        
        //TODO: enqueue superclass for translation
        
        info.avm2class = abcFile.addClass( javaClass.classQName, 
                                           javaClass.superclass.classQName, 
                                           true,         //isSealed
                                           isFinal    ( classNode.access ),
                                           isInterface( classNode.access ),
                                           javaClass.protectedNamespace );
        
        //initialization script for the class
        emitClassInitializationScript();
        
        //translate the methods
        @SuppressWarnings("unchecked")
        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        for( MethodNode method : methods ) {
            translateMethod( method );
        }
        
        //ensure that there is a static initializer
        if( info.avm2class.staticInitializer == null ) {
            emitDefaultStaticInit();
        }
        
        //translate the fields
        @SuppressWarnings("unchecked")
        List<FieldNode> fields = (List<FieldNode>) classNode.fields;
        for( FieldNode field : fields ) {
            translateField( field );
        }
        
        //TODO: ???
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Leaving " + javaClass.clazz );  
        }
        
        return true;
    }
    
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
                        
        AVM2ClassSlot slot = script.traits.addClass( javaClass.classQName, 
                                                     javaClass.classQName );
        slot.indexId = script.traits.traits.size() - 1;
        
        //get the inheritance chain of the class
        LinkedList<JavaClass> superclasses = new LinkedList<JavaClass>();
        JavaClass superclass = javaClass.superclass;
        while( superclass != null ) {
            if( superclass.clazz == Object.class ) break; //don't emit java.lang.Object
            superclasses.addFirst( superclass );
            superclass = superclass.superclass;
        }
        
        //build the scope stack for the new class (will be captured as a closure
        // by newclass)
        InstructionList ops = body.instructions;
        ops.append( OP_getlocal0 );  //the global object
        ops.append( OP_pushscope );        
        ops.append( OP_getscopeobject, 0 );

        //push the inheritance chain as the scope stack
        for( JavaClass sc : superclasses ) { 
            ops.append( OP_getlex, sc.classQName );
            ops.append( OP_pushscope );
        }

        //push the superclass and create the new class        
        ops.append( OP_getlex,   javaClass.superclass.classQName );
        ops.append( OP_newclass, javaClass .classQName );
        
        //tear down the scope stack, except the global object
        for( int i = 0; i < superclasses.size(); i++ ) {
            ops.append( OP_popscope );            
        }
        
        //initialize the class slot (of the global object)
        ops.append( OP_initproperty, javaClass.classQName );
        
        //end of script
        ops.append( OP_returnvoid );
                
        //set the max stack, scope regs etc.
        body.scopeDepth   = 1;
        body.maxStack     = 2;
        body.maxRegisters = 1;
        body.maxScope     = body.scopeDepth + superclasses.size() + 1;         
        
        info.classScopeDepth = body.maxScope;
    }
    
    /**
     * Generate the default static initializer
     */    
    private void emitDefaultStaticInit() {
        
        AVM2Method staticInit = info.avm2class.staticInitializer = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
        AVM2MethodBody initBody = staticInit.methodBody;
        InstructionList ops = initBody.instructions;
        ops.append( OP_getlocal0 );  //the global object
        ops.append( OP_pushscope );
        ops.append( OP_returnvoid );
        
        initBody.maxRegisters = 1;
        initBody.maxStack     = 1;
        initBody.maxScope     = info.classScopeDepth + 1;
        initBody.scopeDepth   = info.classScopeDepth;                
    }
    
    /**
     * Translate a field
     */
    private void translateField( FieldNode field ) {

        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting field " + field.name );
        }
        
        //TODO:
    }
    
    /**
     * Translate a method or constructor
     */
    private void translateMethod( MethodNode method ) {
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting method " 
                           + method.name + method.desc );
        }
        
        if( method.name.equals( "<init>" )) {
//            translateConstructor( method );
            translateConstructor();
        }
        else if( method.name.equals( "<clinit>" )) {
            translateStaticInit( method );
        }
        else {
//            translateMemberMethod( method );
            translateMethod();
        }
        
        //TODO: ???
    }
    
    /**
     * Translate a constructor
     */
    private void translateConstructor( MethodNode constructor ) {
        translateConstructor(); //DEBUG ONLY
        
        Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );
        
        AVM2Method method = new AVM2Method( null, flags );
        info.avm2class.constructor = method;
        
        AVM2MethodBody body = method.methodBody;
        body.scopeDepth   = javaClass.info.classScopeDepth + 1;
        
        //TODO:
    }
    
    /**
     * Translate the static initializer
     */
    private void translateStaticInit( MethodNode clinit ) {
        //TODO: translateStaticInit
    }

    /**
     * Translate a member method
     */
    private void translateMemberMethod( MethodNode methodNode ) {
        
        
        Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );

        AVM2QName  name       = new AVM2QName( EmptyPackage.namespace, methodNode.name );                
        AVM2QName  returnType = qnameForJavaType( Type.getReturnType( methodNode.desc ) );
        AVM2Method method     = new AVM2Method( returnType, flags );
        
        AVM2MethodSlot ms = info.avm2class.traits.addMethod( 
               name, 
               method, 
               isFinal   ( methodNode.access ),
               isOverride( javaClass, methodNode ) );
        
        AVM2MethodBody body = method.methodBody;
        //body.maxStack     = tbd;
        //body.maxRegisters = tbd;
        //body.maxScope     = tbd;
        body.scopeDepth = javaClass.info.classScopeDepth;
        
        //instance methods have a larger scope depth due to the instance object
        if( ! isStatic( methodNode.access )) body.scopeDepth++;
        
        translateCode( methodNode, body );
        
        //TODO: ???
    }
    
    /**
     * Translate Java bytecode to AVM2 bytecode
     */
    private void translateCode( MethodNode methodNode, AVM2MethodBody body ) {
        
        //TODO: handle abstract methods
        
        AVM2Code code = new AVM2Code( body.instructions );
        ASMInstructionVisitor visitor = 
            new ASMInstructionVisitor( javaClass, methodNode, code );
        
        try {
            visitor.visitAll();
        } catch( AnalyzerException aex ) {
            throw new RuntimeException( aex );
        }
                
        //TODO: fix the max stack locals and scope
    }
    
    //TODO: FOR DEBUG ONLY - REMOVE THIS
    private void translateMethod() {
            
          AVM2QName name = new AVM2QName( EmptyPackage.namespace, "drawTest" );
          
          Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );
          
          AVM2Name returnType = TypeVoid.qname;
          AVM2Method method = new AVM2Method( returnType, flags );
          
          boolean isOverride = false;
          AVM2MethodSlot ms = info.avm2class.traits.addMethod( 
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
        info.avm2class.constructor = method;
        
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
