package org.javaswf.j2avm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.epistem.io.OutStream;
import org.javaswf.j2avm.emitter.AVM2ClassEmitter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.writers.ABCWriter;

/**
 * The main J2AVM entry point.
 *
 * @author nickmain
 */
public class J2AVM {

    private final TranslationPipeline pipeline = new TranslationPipeline();
    private TranslationContext context;
    private final File targetFile;
    
    public J2AVM( File targetFile ) {
        this.targetFile = targetFile;

        pipeline.addStep( new AVM2ClassEmitter() );        
    }
    
    /**
     * Add a class to be translated
     */
    public void addClass( Class<?> clazz ) {
        context.addClass( clazz );
    }
    
    /**
     * Perform the translation process.
     */
    public void translate() throws IOException {

        AVM2ABCFile abcFile = new AVM2ABCFile( targetFile.getName(),
                ABC.MAJOR_VERSION_46,
                ABC.MINOR_VERSION_16 );

        context = new TranslationContext( abcFile );

        //TODO: set up ABC file
        
        ClassVisitor visitor = pipeline.prepare( context );
        Class<?> clazz;
        
        while((clazz = context.classToBeTranslated()) != null ) {
            visitClass( clazz, visitor );
        }

        OutputStream fileOut = new FileOutputStream( targetFile );
        try {
            OutStream out       = new OutStream( fileOut );
            ABCWriter abcWriter = new ABCWriter( out );
            
            abcFile.writeStandalone( abcWriter );
        } finally {
            fileOut.close();
        }
                
        //TODO: complete the ABC file
        //TODO: SWF packaging
    }
    
    /**
     * Parse the given class and visit it
     */
    private void visitClass( Class<?> clazz, ClassVisitor visitor ) throws IOException {

        String fileName = clazz.getName().replace( '.', '/' ) + ".class";
        
        InputStream in = clazz.getClassLoader().getResourceAsStream( fileName ); 
        ClassReader reader = new ClassReader( in );
        
        reader.accept( visitor, 0 );
    }
}
