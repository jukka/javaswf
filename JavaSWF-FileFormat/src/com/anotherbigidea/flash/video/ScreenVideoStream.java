package com.anotherbigidea.flash.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.structs.Matrix;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * Helper for creating a ScreenVideo stream instance.
 * All the images must be the same size.
 * 
 * @author nmain
 */
public class ScreenVideoStream {

    private SWFTagTypes timeline;
    private int depth;
    private int streamId;
    private int frameNumber = 0;
    private int blockWidth;
    private int blockHeight;
    private ScreenVideoImageBlock[] imageBlocks;
    
    /**
     * @param timeline the timeline to write to
     */
    public ScreenVideoStream( SWFTagTypes timeline ) {
        this.timeline = timeline;
    }
    
    /**
     * Start the stream and place the instance.
     * 
     * @param x the top left of the instance
     * @param y the top left of the instance
     * @param id the instance symbol id
     * @param depth the depth for the instance
     * @param firstImage the first image - all other images must be the same
     *                   size
     * @param imageCount the total number of images in the stream
     * @param blockWidth the block width to use from here on
     * @param blockHeight the block height to use from here on
     */
    public void startStream( int x, int y, int id, int depth, 
                             ScreenVideoImage firstImage, 
                             int imageCount,
                             int blockWidth, int blockHeight ) throws IOException {
        this.depth       = depth;
        this.streamId    = id;
        
        timeline.tagDefineVideoStream( 
                        id, 
                        imageCount, 
                        firstImage.getImageWidth(), 
                        firstImage.getImageHeight(), 
		                SWFConstants.VIDEO_STREAM_SMOOTHING_OFF +
		                SWFConstants.VIDEO_STREAM_DEBLOCKING_IN_PACKET, 
		                SWFConstants.VIDEO_CODEC_SCREEN_VIDEO );
        
        
        timeline.tagPlaceObject2( false, 0, depth, id, 
                                  new Matrix( SWFConstants.TWIPS * x,
                                              SWFConstants.TWIPS * y ), 
                                  null, -1, null, 0 );
        
        keyFrame( firstImage, blockWidth, blockHeight );
    }
    
    /**
     * Make an inter-frame.  The supplied image will be compared against the
     * previous one and only those blocks which have changed will be included
     * in the frame.
     * 
     * This does not create an actual timeline frame - that should be done
     * explicitly.  Only one keyframe/startStream or inter-frame may exist in
     * any timeline frame.  
     */
    public void interFrame( ScreenVideoImage image ) throws IOException {

        ScreenVideoImageBlock[] newBlocks = image.toBlocks( blockWidth, blockHeight );

        //make a set of delta blocks
        ScreenVideoImageBlock[] deltaBlocks = new ScreenVideoImageBlock[ newBlocks.length ];
        for( int i = 0; i < deltaBlocks.length; i++ ) {
            
            //if new and old block are the same then use an empty block
            deltaBlocks[i] = newBlocks[i].equals( imageBlocks[i] ) ?
                                 ScreenVideoImageBlock.EMPTY_BLOCK :
                                 newBlocks[i];
        }
        imageBlocks = newBlocks;
        
        ScreenVideoPacket packet = new ScreenVideoPacket( 
                                           image.getImageWidth(),
                                           image.getImageHeight(),
                                           blockWidth,
                                           blockHeight,
                                           deltaBlocks );
        
        timeline.tagPlaceObject2( true, 0, depth, 0, null, null, frameNumber, null, 0 );
        timeline.tagVideoFrame( streamId, 
                                frameNumber++, 
                                SWFConstants.VIDEO_FRAME_INTERFRAME,
                                SWFConstants.VIDEO_CODEC_SCREEN_VIDEO,
                                packet.toBytes() );  
    }
    
    /**
     * Make a key frame.  All blocks in the given image will be written.
     * 
     * This does not create an actual timeline frame - that should be done
     * explicitly.  Only one keyframe/startStream or inter-frame may exist in
     * any timeline frame.  
     *  
     * @param image the image to place
     * @param blockWidth the block width to use from here on
     * @param blockHeight the block height to use from here on
     */
    public void keyFrame( ScreenVideoImage image, int blockWidth, int blockHeight ) 
    	throws IOException {
        
        this.blockWidth  = blockWidth;
        this.blockHeight = blockHeight;

        imageBlocks = image.toBlocks( blockWidth, blockHeight );
        
        ScreenVideoPacket packet = new ScreenVideoPacket( 
                                           image.getImageWidth(),
                                           image.getImageHeight(),
                                           blockWidth,
                                           blockHeight,
                                           imageBlocks );
        
        timeline.tagPlaceObject2( true, 0, depth, 0, null, null, frameNumber, null, 0 );
        timeline.tagVideoFrame( streamId, 
                                frameNumber++, 
                                SWFConstants.VIDEO_FRAME_KEYFRAME,
                                SWFConstants.VIDEO_CODEC_SCREEN_VIDEO,
                                packet.toBytes() );
    }
    
    
    /**
     * By way of a test and a code example.. writes a SWF movie
     * "ScreenVideoPacketTest.swf" to the current dir containing the test
     * pattern and a green square added each frame. 
     */
    public static void main(String[] args) throws IOException {
        
        File dir = new File( "c:\\temp\\screenshots" );
        String[] screenshots = dir.list();
        Arrays.sort( screenshots );
        
        BufferedImage img = ImageIO.read( new File(dir, screenshots[0]) );
        ScreenVideoImage firstImage = new ScreenVideoImage( img, 0, 0, 800, 600 );
        
        SWFWriter writer = new SWFWriter( "c:\\temp\\screenshots.swf" );
        TagWriter tags   = new TagWriter( writer );

        writer.setCompression( true );
        tags.header( SWFConstants.FLASH_MX_VERSION_2004,
                     -1, 
                     firstImage.getImageWidth()  * SWFConstants.TWIPS,
                     firstImage.getImageHeight() * SWFConstants.TWIPS,
                     1,
                     -1 );
        
        tags.tagSetBackgroundColor( new Color(255,0,0) );
        
        ScreenVideoStream stream = new ScreenVideoStream( tags );
        stream.startStream( 0, 0, 1, 1, firstImage, screenshots.length, 64, 64 );
        tags.tagShowFrame();
        
        for( int i = 1; i < screenshots.length; i++ ) {
            img = ImageIO.read( new File(dir, screenshots[i]) );
            ScreenVideoImage image = new ScreenVideoImage( img, 0, 0, 800, 600 );
            
            System.out.println( "Adding image " + screenshots[i] );
            
            stream.interFrame( image );
            tags.tagShowFrame();
        }
        
        
        SWFActions acts = tags.tagDoAction();
        SWFActionBlock block = acts.start(0);
        //block.stop();
        block.end();
        acts.done();
        
        tags.tagShowFrame();
        tags.tagEnd();
        
        System.out.println( "Done" );
    }
}
