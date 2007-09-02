package org.javaswf.j2avm.emitter;

import org.javaswf.j2avm.TranslationContext;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

import static org.javaswf.j2avm.util.ASMUtils.*;

/**
 * The class visitor for the emitter
 *
 * @author nickmain
 */
public class EmitterClassVisitor extends ClassAdapter {

    private final AVM2ABCFile abcFile;
    private final TranslationContext context;
    private AVM2Class avm2class;
    private AVM2Namespace protectedNamespace; //namespace for protected methods
    
    EmitterClassVisitor( TranslationContext context, 
                         ClassVisitor downstreamVisitor ) {
        super( downstreamVisitor );
        
        this.context = context;
        abcFile = context.getAbcFile();
    }
    
    /**
     * @see org.objectweb.asm.ClassVisitor#visit(int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    public void visit( int version, int access, String name, String signature, 
                       String superName, String[] interfaces ) {

        if( context.debug ) context.debug( "*** AVM2ClassEmitter - VISITING CLASS " + name );
        
        //TODO: deal with interfaces, enums and annotations
        
        AVM2QName superclass = new AVM2QName( externalName( superName ) );
        //TODO: enqueue superclass for translation
        
        String    className = externalName( name );
        AVM2QName qname     = new AVM2QName( className );
        
        protectedNamespace = new AVM2Namespace( NamespaceKind.ProtectedNamespace, 
                                                className );
        
        avm2class = abcFile.addClass( qname, 
                                      superclass, 
                                      true,         //isSealed
                                      isFinal( access ),
                                      isInterface( access ),
                                      protectedNamespace );
        
        //TODO:
        
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
        if( context.debug ) context.debug( "*** AVM2ClassEmitter - END CLASS " + avm2class.name.toQualString() );         
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
        
        System.out.println( "Visiting method " + name + " (" + signature + "): " + desc );
        
        //TODO:
        return new EmptyVisitor() {

            /** @see org.objectweb.asm.commons.EmptyVisitor#visitCode() */
            @Override
            public void visitCode() {
                // TODO Auto-generated method stub
                super.visitCode();
            }

          
            
            
        };
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

