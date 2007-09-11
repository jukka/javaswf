package org.javaswf.j2avm;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.javaswf.j2avm.emitter.AVM2ClassEmitter;
import org.javaswf.j2avm.steps.AVMGetterSetterRewriter;
import org.javaswf.j2avm.steps.CallAndAccessRewriteStep;
import org.javaswf.j2avm.swf.TargetSWF;

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
        
        pipeline.addSteps(
            new CallAndAccessRewriteStep(
                new AVMGetterSetterRewriter()
            ),
            new AVM2ClassEmitter()
        );        

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

        JavaClass javaClass;
        
        while((javaClass = context.classToBeTranslated()) != null ) {
            log.fine( "..translating " + javaClass );       

            pipeline.process( javaClass, context );
        }

        targetSWF.addABC( context.getAbcFile(), "J2AVM", true );
        
        log.info( "writing target SWF" );       
        targetSWFFile.getParentFile().mkdirs(); //ensure dir structure exists
        targetSWF.write( targetSWFFile );
    }
}
