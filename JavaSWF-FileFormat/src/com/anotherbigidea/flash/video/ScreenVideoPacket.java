package com.anotherbigidea.flash.video;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.epistem.io.InStream;
import org.epistem.io.OutStream;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.structs.Matrix;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * A packet for the Screen Video codec.
 * 
 * @author nmain
 */
public class ScreenVideoPacket {
    
    private int imageWidth;
    private int imageHeight;
    private int blockWidth;
    private int blockHeight;
    private ScreenVideoImageBlock[] imageBlocks;
    
    /**
     * @param imageWidth in pixels
     * @param imageHeight in pixels
     * @param blockWidth 16 to 256 in steps of 16
     * @param blockHeight 16 to 256 in steps of 16
     * @param imageBlocks image data or delta data, top-left to bottom-right
     */
    public ScreenVideoPacket( int imageWidth, int imageHeight,
                              int blockWidth, int blockHeight,
                              ScreenVideoImageBlock[] imageBlocks ) {
        this.imageWidth  = imageWidth;
        this.imageHeight = imageHeight;
        this.blockWidth  = blockWidth;
        this.blockHeight = blockHeight;
        this.imageBlocks = imageBlocks;
    }
 
    /**
     * Parse a packet from an input stream.
     */
    public ScreenVideoPacket( InStream in ) throws IOException {
        blockWidth  = (int)( in.readUBits( 4 ) + 1 ) * 16;
        imageWidth  = (int) in.readUBits( 12 );
        blockHeight = (int)( in.readUBits( 4 ) + 1 ) * 16;
        imageHeight = (int) in.readUBits( 12 );
               
        List<ScreenVideoImageBlock> blocks = new ArrayList<ScreenVideoImageBlock>(); 
        
        for( int y = 0; y < imageHeight; y += blockHeight ) {
            for( int x = 0; x < imageWidth; x += blockWidth ) {
                int width  = Math.min( blockWidth,  imageWidth  - x );
                int height = Math.min( blockHeight, imageHeight - y );
                
                blocks.add( new ScreenVideoImageBlock( in, width, height ) );
            }            
        }

        imageBlocks = blocks.toArray( new ScreenVideoImageBlock[ blocks.size() ] );
    }
    
    public int getBlockHeight() {
        return blockHeight;
    }
    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }
    public int getBlockWidth() {
        return blockWidth;
    }
    public void setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;
    }
    public int getImageHeight() {
        return imageHeight;
    }
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
    public int getImageWidth() {
        return imageWidth;
    }
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }
    public ScreenVideoImageBlock[] getImageBlocks() {
        return imageBlocks;
    }
    public void setImageBlocks(ScreenVideoImageBlock[] imageBlocks) {
        this.imageBlocks = imageBlocks;
    }
    
    /** Write the packet in SWF format */
    public void write( OutStream out ) throws IOException {
        out.writeUBits( 4,  blockWidth/16 - 1 );
        out.writeUBits( 12, imageWidth );
        out.writeUBits( 4,  blockHeight/16 - 1 );
        out.writeUBits( 12, imageHeight );
        
        for (int i = 0; i < imageBlocks.length; i++) {
            imageBlocks[i].write( out );
        }
    }
    
    /** Get the bytes of the packet in SWF format */
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutStream out = new OutStream( bout );
        write( out );
        out.flush();
        return bout.toByteArray();
    }
    
    /** Make a 288*256 test pattern consisting of blue and yellow blocks */ 
    public static ScreenVideoPacket makeTestPattern() {
        ScreenVideoImageBlock blueBlock   = ScreenVideoImageBlock.makeSquareOfSingleColor( 0xff, 32 );
        ScreenVideoImageBlock yellowBlock = ScreenVideoImageBlock.makeSquareOfSingleColor( 0xffff00, 32 );
        
        ScreenVideoImageBlock[] blocks = new ScreenVideoImageBlock[ 72 ];
        for (int i = 0; i < blocks.length; i+=2) {
            blocks[i] = yellowBlock;
        }
        
        for (int i = 1; i < blocks.length; i+=2) {
            blocks[i] = blueBlock;
        }

        return new ScreenVideoPacket( 288, 256, 32, 32, blocks ); 
    }

    /** Make a 288*256 test pattern delta consisting of a green delta block at the given index */ 
    public static ScreenVideoPacket makeTestPatternDelta( int index ) {
        ScreenVideoImageBlock greenBlock = ScreenVideoImageBlock.makeSquareOfSingleColor( 0x00ff00, 32 );
        
        ScreenVideoImageBlock[] blocks = new ScreenVideoImageBlock[ 72 ];
        for (int i = 0; i < blocks.length; i++) {
            if( i == index ) blocks[i] = greenBlock;
            else             blocks[i] = ScreenVideoImageBlock.EMPTY_BLOCK;
        }

        return new ScreenVideoPacket( 288, 256, 32, 32, blocks ); 
    }

    
    /**
     * By way of a test and a code example.. writes a SWF movie
     * "ScreenVideoPacketTest.swf" to the current dir containing the test
     * pattern and a green square added each frame. 
     */
    public static void main(String[] args) throws IOException {
        SWFWriter writer = new SWFWriter( "ScreenVideoPacketTest.swf" );
        TagWriter tags   = new TagWriter( writer );
        
        tags.header( SWFConstants.FLASH_MX_VERSION_2004,
                     -1, 
                     800 * SWFConstants.TWIPS,
                     600 * SWFConstants.TWIPS,
                     12,
                     -1 );
        
        tags.tagSetBackgroundColor( new Color(255,0,0) );
        
        tags.tagDefineVideoStream( 1, 72, 288, 256, 
                                   SWFConstants.VIDEO_STREAM_SMOOTHING_OFF +
                                   SWFConstants.VIDEO_STREAM_DEBLOCKING_IN_PACKET, 
                                   SWFConstants.VIDEO_CODEC_SCREEN_VIDEO );
        tags.tagPlaceObject2( false, 0, 1, 1, new Matrix(), null, -1, null, 0 );
        tags.tagShowFrame();
        
        for (int i = 0; i < 72; i++) {
            tags.tagPlaceObject2( true, 0, 1, 0, null, null, i, null, 0 );
            tags.tagVideoFrame( 1, i, 
                               (i==0) ? SWFConstants.VIDEO_FRAME_KEYFRAME : SWFConstants.VIDEO_FRAME_INTERFRAME,
                               SWFConstants.VIDEO_CODEC_SCREEN_VIDEO,
                               (i==0) ? ScreenVideoPacket.makeTestPattern().toBytes() :
                                        ScreenVideoPacket.makeTestPatternDelta( i - 1 ).toBytes());
            tags.tagShowFrame();            
        }

        
        
        SWFActions acts = tags.tagDoAction();
        SWFActionBlock block = acts.start(0);
        //block.stop();
        block.end();
        acts.done();
        
        tags.tagShowFrame();

        tags.tagEnd();
    }
}
