package com.anotherbigidea.flash.avm1;

/**
 * Actions in a frame
 *
 * @author nickmain
 */
public class AVM1FrameActions extends AVM1Actions {

    /**
     * The symbol/character id for the timeline.  Zero is the main timeline.
     */
    public final int symbolId;
    
    /**
     * The frame number, zero based
     */
    public final int frameNumber;
    
    public AVM1FrameActions( int symbolId, int frameNumber ) {
        this.symbolId    = symbolId;
        this.frameNumber = frameNumber;
    }
}
