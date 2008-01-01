package com.anotherbigidea.flash.avm1;

/**
 * Implemented by operations that contain sub-blocks
 *
 * @author nickmain
 */
public interface AVM1BlockContainer {

    /**
     * Get the contained blocks
     */
    public AVM1ActionBlock[] subBlocks();
}
