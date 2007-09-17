package org.javaswf.j2avm.model.code;

/**
 * Base for instruction list walkers and rewriters.
 * 
 *
 * @author nickmain
 */
public abstract class InstructionListWalker {

    /**
     * Remove the instruction currently being visited
     */
    protected final void remove() {
        //TODO:
    }
    
    /**
     * Get an interface for inserting instructions before the one being visited.
     */
    protected final Instructions insertBefore() {
        //TODO:
    }

    /**
     * Get an interface for inserting instructions after the one being visited.
     */
    protected final Instructions insertAfter() {
        //TODO:
    }

}
