package org.javaswf.j2avm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javaswf.j2avm.model.ClassModel;

/**
 * A pipeline of translation steps.
 *
 * @author nickmain
 */
public final class TranslationPipeline implements TranslationStep {

    private final List<TranslationStep> steps = new ArrayList<TranslationStep>();
    
    /**
     * Append one or more steps to the end of the pipeline.
     * 
     * @param stepsToAdd the steps to append
     */
    public void addSteps( TranslationStep... stepsToAdd ) {
        steps.addAll( Arrays.asList( stepsToAdd ));
    }

    /**
     * Add one or more steps to the start of the pipeline
     * 
     * @param stepsToAdd the steps to add
     */
    public void prependSteps( TranslationStep... stepsToAdd ) {
        steps.addAll( 0, Arrays.asList( stepsToAdd ));
    }
    
    /** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.model.ClassModel, org.javaswf.j2avm.TranslationContext) */
    public boolean process(ClassModel classModel, TranslationContext context) {
        for( TranslationStep step : steps ) {
            if( ! step.process( classModel, context )) return false;
        }
        
        return true;
    }
}
