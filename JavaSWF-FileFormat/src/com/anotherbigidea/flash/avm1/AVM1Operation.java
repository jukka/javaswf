package com.anotherbigidea.flash.avm1;

import java.io.IOException;

import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * An AVM1 Operation
 *
 * @author nickmain
 */
public abstract class AVM1Operation {

    /*pkg*/ AVM1Operation prev;
    /*pkg*/ AVM1Operation next;
    /*pkg*/ AVM1ActionBlock block;

    /**
     * Get the operation after this one
     * 
     * @return null if this is the last operation or it is not in a block, or
     *              has been aggregated
     */
    public final AVM1Operation next() {
        return next;
    }

    /**
     * Get the operation before this one
     * 
     * @return null if this is the first operation or it is not in a block, or
     *              has been aggregated
     */
    public final AVM1Operation prev() {
        return prev;
    }

    /**
     * Remove this operation from the block it is in
     */
    public final void remove() {
        if( block != null ) block.remove( this );
    }
    
    /**
     * Write the operation to a block
     */
    public abstract void write( SWFActionBlock block ) throws IOException;

    /**
     * Whether this operation evaluates to a non-zero value
     */
    public boolean isNonZero() {
        return true;
    }
    
    /**
     * Get the int value
     */
    public int intValue() {
        return 0;
    }    
    
    /** 
     * Get any label referenced by this operation
     * @return null if this operation does not reference a label
     */
    public String labelReference() {
        return null;
    }
}
