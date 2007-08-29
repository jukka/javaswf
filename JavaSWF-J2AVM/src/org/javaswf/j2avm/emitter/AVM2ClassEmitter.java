/**
 * 
 */
package org.javaswf.j2avm.emitter;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;

/**
 * The final JVM to AVM2 bytecode translator.
 *
 * @author nickmain
 */
public class AVM2ClassEmitter implements TranslationStep {

    /**
     * @see org.javaswf.j2avm.TranslationStep#prepare(org.javaswf.j2avm.TranslationContext, org.objectweb.asm.ClassVisitor)
     */
    public ClassVisitor prepare( TranslationContext context,
                                 ClassVisitor downstreamVisitor) {
        
        return new EmitterClassVisitor( context, downstreamVisitor );
    }
}

class EmitterClassVisitor extends ClassAdapter {

    private final AVM2ABCFile abcFile;
    
    EmitterClassVisitor( TranslationContext context, 
                         ClassVisitor downstreamVisitor ) {
        super( downstreamVisitor );
        
        abcFile = context.getAbcFile();
    }
    
    /**
     * @see org.objectweb.asm.ClassVisitor#visit(int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    public void visit( int version, int access, String name, String signature, 
                       String superName, String[] interfaces ) {
        //TODO:
        
        System.out.println( "VISITING CLASS " + name );
    }
     
    /**
     * @see org.objectweb.asm.ClassVisitor#visitAnnotation(java.lang.String, boolean)
     */
    public AnnotationVisitor visitAnnotation( String desc, boolean visible ) {
        //TODO:
        return null;
    }

    /**
     * @see org.objectweb.asm.ClassVisitor#visitAttribute(org.objectweb.asm.Attribute)
     */
    public void visitAttribute( Attribute attr ) {
        //TODO:
         
    }

    /**
     * @see org.objectweb.asm.ClassVisitor#visitEnd()
     */
    public void visitEnd() {
        //TODO:
         
    }

    /**
     * @see org.objectweb.asm.ClassVisitor#visitField(int, java.lang.String, java.lang.String, java.lang.String, java.lang.Object)
     */
    public FieldVisitor visitField( int access, String name, String desc, 
                                    String signature, Object value ) {
        //TODO:
        return null;
    }

    /**
     * @see org.objectweb.asm.ClassVisitor#visitInnerClass(java.lang.String, java.lang.String, java.lang.String, int)
     */
    public void visitInnerClass( String name, String outerName, 
                                 String innerName, int access ) {
        //TODO:
         
    }

    /**
     * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    public MethodVisitor visitMethod( int access, String name, String desc,
                                      String signature, String[] exceptions ) {
        //TODO:
        return null;
    }

    /**
     * @see org.objectweb.asm.ClassVisitor#visitOuterClass(java.lang.String, java.lang.String, java.lang.String)
     */
    public void visitOuterClass( String owner, String name, String desc ) {
        //TODO:
         
    }

    /**
     * @see org.objectweb.asm.ClassVisitor#visitSource(java.lang.String, java.lang.String)
     */
    public void visitSource( String source, String debug ) { 
        //TODO:
         
    }    
}

