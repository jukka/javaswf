package org.javaswf.j2avm;

import org.javaswf.j2avm.model.ClassModel;

/**
 * Implemented by steps in the translation pipeline.
 *
 * @author nickmain
 */
public interface TranslationStep {
    
    /**
     * Process a class
     * 
     * @param classModel the class to be processed - this will be passed to
     *                   subsequent steps in the pipeline so alterations will
     *                   persist
     * @param context the translation context
     * @return false to halt processing of the given class
     */
    public boolean process( ClassModel classModel, TranslationContext context );
    
}
