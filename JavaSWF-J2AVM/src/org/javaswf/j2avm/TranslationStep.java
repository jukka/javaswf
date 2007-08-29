package org.javaswf.j2avm;

import org.objectweb.asm.ClassVisitor;

/**
 * Implemented by steps in the translation pipeline.
 *
 * @author nickmain
 */
public interface TranslationStep {
    
    /**
     * Prepare for the translation of a class.
     * 
     * Returns the ClassVisitor that corresponds to this translation step.
     * 
     * This method is called multiple times - once for each class that is
     * passed through the translation pipeline.
     * 
     * @param context the context for the translation
     * @param downstreamVisitor the next visitor in the pipeline
     * @return the visitor implementation for this step
     */
    public ClassVisitor prepare( TranslationContext context,
                                 ClassVisitor downstreamVisitor );
}
