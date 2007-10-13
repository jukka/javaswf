package org.javaswf.j2avm;

import java.io.IOException;
import java.util.logging.Logger;

import org.javaswf.j2avm.abc.TargetABC;
import org.javaswf.j2avm.emitter.AVM2ClassEmitter;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.steps.AVMGetterSetterRewriter;
import org.javaswf.j2avm.steps.NativeAndAbstractMethodsStep;

/**
 * The main J2AVM entry point.
 *
 * @author nickmain
 */
public final class J2AVM {

    public static final Logger log = Logger.getLogger( J2AVM.class.getName() );

    /**
     * @param loader the loader for class data
     * @param target the output target
     */
    public J2AVM( ClassLoader loader, TranslationTarget target ) {
        this.target  = target;
        this.context = new TranslationContext( loader );
        this.abc     = new TargetABC( context );
        addDefaultSteps();
    }
    
    /**
     * Add a class to be translated.  Any referenced classes will also be
     * translated.
     * 
     * @param className the fully qualified class name
     */
    public void addClassToBeTranslated( String className ) {
        context.addClass( className );
    }
    
    /**
     * Set the name of the main class for the target.
     * @param className the fully qualified class name
     */
    public void setMainClass( String className ) {
        this.mainClassName = className;
        addClassToBeTranslated( className );
    }
    
    /**
     * Add translation steps to the front of the translation pipeline
     * @param steps the new steps to add
     */
    public void addTranslationSteps( TranslationStep...steps ) {
        pipeline.prependSteps( steps );
    }
    
    /**
     * Perform the translation process.
     */
    public void translate() throws IOException {

        log.info( "Starting translation..." );       
        
        ClassModel classModel;
        while(( classModel = context.nextClassToBeTranslated()) != null ) {
            pipeline.process( classModel, context );            
        }

        log.info( "  finished translating" );       

        log.info( "  writing target " + target );
        abc.writeToTarget( target );
        abc.forJavaClass( mainClassName ).setAsMainClass( target );        
        target.finish();
        log.info( "...done" );
    }
    
    private void addDefaultSteps() {
        pipeline.addSteps(
        	new NativeAndAbstractMethodsStep(),
            new AVMGetterSetterRewriter(),
            new AVM2ClassEmitter( abc )
        ); 
    }
    
    private final TargetABC           abc;
    private final TranslationPipeline pipeline = new TranslationPipeline();
    
    private final TranslationContext context;
    private final TranslationTarget  target;
    
    private String mainClassName;
}
