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
import org.javaswf.j2avm.abc.AVM2Code;
import org.javaswf.j2avm.abc.TargetABC;
import org.javaswf.j2avm.abc.ClassTranslation;
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
    
    private final TargetABC abc;
    private ClassTranslation classTrans;
    private ClassModel javaClass;
    private TranslationContext context;
	
    /**
     * @param abc the target ABC file data
     */
    public AVM2ClassEmitter( TargetABC abc ) {
        this.abc = abc;
    }
    
    /** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.model.ClassModel, org.javaswf.j2avm.TranslationContext) */
    public boolean process( ClassModel classModel, TranslationContext context ) {
        
        this.context   = context;
        this.javaClass = classModel;
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting " + classModel.type );
        }

        //FIXME: deal with interfaces, enums and annotations        
        //FIXME: enqueue superclass for translation
                
        classTrans = abc.forJavaClass( classModel );
        classTrans.realize(); //make the actual AVM2 class
        
        //translate the methods
        for( MethodModel method : classModel.methods.values() ) {
            translateMethod( method );
        }
        
        classTrans.defaultStaticInit(); //ensure there's a static initializer
        
        //translate the fields
        for( FieldModel field : classModel.fields.values() ) {
            translateField( field );
        }
        
        //FIXME: anything else needed ?
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Leaving " + classModel.type );  
        }
        
        return true;
    }
    
    /**
     * Translate a field
     */
    private void translateField( FieldModel field ) {

        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting field " + field.name );
        }
        
        //FIXME: translateField
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
