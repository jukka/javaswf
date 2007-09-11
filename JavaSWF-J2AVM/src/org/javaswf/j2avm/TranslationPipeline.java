package org.javaswf.j2avm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A pipeline of translation steps.
 *
 * @author nickmain
 */
public final class TranslationPipeline implements TranslationStep {

    private final List<TranslationStep> steps = new ArrayList<TranslationStep>();
    
    /**
     * Append one or more steps to the pipeline.
     * 
     * @param step the step to append
     * @return this
     */
    public TranslationPipeline addSteps( TranslationStep... step ) {
        steps.addAll( Arrays.asList( step ));
        return this;
    }
    
    /** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.JavaClass, org.javaswf.j2avm.TranslationContext) */
    public boolean process( JavaClass javaClass, TranslationContext context) {
        for( TranslationStep step : steps ) {
            if( ! step.process( javaClass, context )) return false;
        }
        
        return true;
    }
}
