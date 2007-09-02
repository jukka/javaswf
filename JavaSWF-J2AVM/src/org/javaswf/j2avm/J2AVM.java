package org.javaswf.j2avm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.javaswf.j2avm.emitter.AVM2ClassEmitter;
import org.javaswf.j2avm.swf.TargetSWF;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;

/**
 * The main J2AVM entry point.
 *
 * @author nickmain
 */
public class J2AVM {

    private final TranslationPipeline pipeline = new TranslationPipeline();
    private TranslationContext context;
    private final File targetSWFFile;
    private final TargetSWF targetSWF;
    
    public static final Logger log = Logger.getLogger( J2AVM.class.getName() );
    
    /**
     * @param targetSWFFile the swf file to create
     * @param mainClass the main class for the swf
     * @param width the stage width in pixels
     * @param height the stage height in pixels
     * @param frameRate the frames-per-second
     * @param backgroundColor the RGB background color
     */
    public J2AVM( File targetSWFFile, Class<?> mainClass,
                  int width, int height, int frameRate, int backgroundColor ) {
        
        log.info( "target SWF = " + targetSWFFile.getAbsolutePath() );
        log.info( "main class = " + mainClass.getName() );       
        
        this.targetSWFFile = targetSWFFile;
        this.targetSWF     = new TargetSWF( width, height, frameRate, backgroundColor );
        
        pipeline.addStep( new AVM2ClassEmitter() );        

        AVM2ABCFile abcFile = new AVM2ABCFile( ABC.MAJOR_VERSION_46,
                                               ABC.MINOR_VERSION_16 );

        targetSWF.addSymbolClass( 0, mainClass.getName() );
        
        context = new TranslationContext( abcFile );
        context.addClass( mainClass );
    }
    
    /**
     * Perform the translation process.
     */
    public void translate() throws IOException {

        ClassVisitor visitor = pipeline.prepare( context );
        Class<?> clazz;
        
        while((clazz = context.classToBeTranslated()) != null ) {
            log.fine( "..translating class " + clazz.getName() );       
            visitClass( clazz, visitor );
        }

        targetSWF.addABC( context.getAbcFile(), "J2AVM", true );
        
        log.info( "writing target SWF" );       
        targetSWFFile.getParentFile().mkdirs(); //ensure dir structure exists
        targetSWF.write( targetSWFFile );
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
