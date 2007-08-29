package org.javaswf.j2avm;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * The manager of the pipeline of translation steps.
 *
 * @author nickmain
 */
public final class TranslationPipeline {

    private final List<TranslationStep> steps = new ArrayList<TranslationStep>();
    
    /**
     * Append a step to the pipeline.
     * 
     * @param step the step to append
     * @return this
     */
    public TranslationPipeline addStep( TranslationStep step ) {
        steps.add( step );
        return this;
    }
    
    /**
     * Prepare the pipeline for translating a class.
     * 
     * @param context the translation context to use
     * @return the visitor for receiving the class file
     */
    public ClassVisitor prepare( TranslationContext context ) {
        
        ClassVisitor visitor = new EmptyVisitor();
        
        //prepare all the steps from end to start
        for( int i = steps.size() - 1; i >= 0; i-- ) {
            TranslationStep step = steps.get( i );
            visitor = step.prepare( context, visitor );
        }
        
        return visitor;
    }
}
