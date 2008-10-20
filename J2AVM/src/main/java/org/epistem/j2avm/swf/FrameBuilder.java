package org.epistem.j2avm.swf;

/**
 * Builder for a SWF frame
 *
 * @author nickmain
 */
public class FrameBuilder {

    private int number;
    
    /**
     * Get the frame number (zero based)
     */
    public int frameNumber() {
        return number;
    }
    
    /**
     * @param number the frame number
     */
    /*pkg*/ FrameBuilder( int number ) {
        this.number = number;
    }
}
