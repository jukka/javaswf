package org.epistem.j2avm.swf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A builder for a SWF file.
 *
 * @author nickmain
 */
public class SWFBuilder {
    
    private int width      = 500;
    private int height     = 450;
    private int frameRate  = 12;
    private int version    = 9;
    private int background = 0xffffff;
    private boolean compressed = true;  
    
    private FrameBuilder currentFrame; 
    private SortedMap<Integer,FrameBuilder> frames = new TreeMap<Integer, FrameBuilder>();
    
    /**
     * @param width the movie width in pixels
     */
    public void setWidth( int width ) {
        this.width = width;
    }

    /**
     * @param height the movie height in pixels
     */
    public void setHeight( int height ) {
        this.height = height;
    }

    /**
     * @param frameRate the frames per second
     */
    public void setFrameRate( int frameRate ) {
        this.frameRate = frameRate;
    }

    /**
     * @param version the flash version
     */
    public void setVersion( int version ) {
        this.version = version;
    }

    /**
     * @param background the RGB background color
     */
    public void setBackground( int background ) {
        this.background = background;
    }

    /**
     * @param compressed whether the movie is compressed
     */
    public void setCompressed( boolean compressed ) {
        this.compressed = compressed;
    }

    /**
     * Write the SWF that is currently specified to the given file.
     * 
     * @param swfFile the file to create.
     */
    public void write( File swfFile ) throws IOException {
        FileOutputStream out = new FileOutputStream( swfFile );
        try {
            write( out );
        } finally {
            out.close();
        }
    }
    
    /**
     * Write the currently specified SWF to the given output stream. The
     * stream is not closed.
     * 
     * @param out the target stream.
     */
    public void write( OutputStream out ) throws IOException {
        
    }
    
    /**
     * Get the current frame
     */
    public FrameBuilder currentFrame() {
        if( currentFrame == null ) {
            addFrame();
        }
        
        return currentFrame;
    }
    
    /**
     * Add a new frame at the end of the movie and make it the current frame
     * 
     * @return the new frame
     */
    public FrameBuilder addFrame() {
        int num = frames.isEmpty() ? 0 : frames.lastKey() + 1;
        currentFrame = getFrame( num );
        return currentFrame;
    }
    
    /**
     * Get the frame with the given number - adding it if necessary
     * 
     * @param number greater or equal to zero
     */
    public FrameBuilder getFrame( int number ) {
        FrameBuilder frame = frames.get( number );
        if( frame == null ) {
            frame = new FrameBuilder( number );
            frames.put( number, frame );
        }
        
        return frame;
    }
}
