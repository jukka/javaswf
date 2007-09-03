/**
 * 
 */
package org.javaswf.j2avm.emitter;

import static org.javaswf.j2avm.util.ASMUtils.*;

import java.util.List;

import org.javaswf.j2avm.JavaClass;
import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * The final JVM to AVM2 bytecode translator.
 *
 * @author nickmain
 */
public class AVM2ClassEmitter implements TranslationStep {

    private JavaClass javaClass;
    private ClassNode classNode;
    private TranslationContext context;
    private AVM2ABCFile abcFile;
    private AVM2Class avm2class;
    private AVM2Namespace protectedNamespace; //namespace for protected methods
    
    /** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.JavaClass, org.javaswf.j2avm.TranslationContext) */
    public boolean process( JavaClass javaClass, TranslationContext context ) {
        
        this.context   = context;
        this.javaClass = javaClass;
        this.classNode = javaClass.node;
        this.abcFile   = context.getAbcFile();
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting " + javaClass.clazz );
        }

        //TODO: deal with interfaces, enums and annotations
        
        AVM2QName superclass = new AVM2QName( externalName( classNode.superName ) );
        //TODO: enqueue superclass for translation
        
        String    className = externalName( classNode.name );
        AVM2QName qname     = new AVM2QName( className );
        
        protectedNamespace = new AVM2Namespace( NamespaceKind.ProtectedNamespace, 
                                                className );
        
        avm2class = abcFile.addClass( qname, 
                                      superclass, 
                                      true,         //isSealed
                                      isFinal( classNode.access ),
                                      isInterface( classNode.access ),
                                      protectedNamespace );
        
        //TODO:
        
        @SuppressWarnings("unchecked")
        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        for( MethodNode method : methods ) {
            translateMethod( method );
        }
        
        
        
        // TODO Auto-generated method stub
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Leaving " + javaClass.clazz );  
        }
        
        return true;
    }
    
    private void translateMethod( MethodNode method ) {
        
        if( context.debug ) {
            context.debug( "*** AVM2ClassEmitter - Visiting method " 
                           + method.name + method.desc );
        }
    }
}
