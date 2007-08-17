package com.anotherbigidea.flash.video;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.epistem.io.InStream;
import org.epistem.io.OutStream;


/**
 * An image block within a Screen Video packet.
 *
 * @author nmain
 */
public class ScreenVideoImageBlock {

    /**
     * A block that denotes no change from previous video frame.
     */
    public static final ScreenVideoImageBlock EMPTY_BLOCK = new ScreenVideoImageBlock( new int[0] );
    
    private int[] pixelData;

    /**
     * The pixel data is in 0x00rrggbb format from bottom-left to top-right.
     * 
     * @param pixelData not null
     */
    public ScreenVideoImageBlock( int[] pixelData ) {
        if( pixelData == null ) pixelData = new int[0];
        this.pixelData = pixelData; 
    }
    
    /**
     * Compare two blocks and return true only if all pixels are the same
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object other ) {
        if( other == null 
         || ! (other instanceof ScreenVideoImageBlock)) {
            return false;
        }

        ScreenVideoImageBlock otherBlock = (ScreenVideoImageBlock) other;
        if( pixelData.length != otherBlock.pixelData.length ) return false;
        
        return Arrays.equals( pixelData, otherBlock.pixelData );
    }
    
    /**
     * Parse a block.
     * @param in the input data
     * @param width the width of the block
     * @param height the height of the block
     */
    public ScreenVideoImageBlock( InStream in, int width, int height ) 
    	throws IOException {
        int length = (int) in.readUBits( 16 );
        if( length == 0 ) {
            pixelData = new int[ 0 ];
            return;
        }
        
        byte[] data = in.read( length );
        
        ByteArrayInputStream bin = new ByteArrayInputStream( data );
        InflaterInputStream inflater = new InflaterInputStream( bin );
        
        int numPixels = width*height;

        pixelData = new int[ numPixels ];

        for (int i = 0; i < numPixels; i++) {
            int blue  = inflater.read(); 
            if( blue  < 0 ) {
                throw new IOException( "EOF when reading pixel blue" );
            }
            int green = inflater.read(); 
            if( green < 0 ) {
                throw new IOException( "EOF when reading pixel green" );
            }
            int red   = inflater.read(); 
            if( red   < 0 ) {
                throw new IOException( "EOF when reading pixel red" );
            }
            
            pixelData[i] = (red << 16) + (green << 8) + blue;
        }
        
        if( inflater.read() != -1 ) {
            throw new IOException( "Unexpected data after pixels in ScreenVideoImageBlock" );
        }
    }
     
    
    /**
     * Get the pixel data.
     * 
     * @return int[0] if the block is empty
     */
    public int[] getPixelData() {
        return pixelData;
    }
    
    /** 
     * Set the pixel data
     * @param pixelData not null 
     */
    public void setPixelData( int[] pixelData ) {
        if( pixelData == null ) pixelData = new int[0];
        this.pixelData = pixelData;
    }
    
    /**
     * Write the block in SWF format.
     */
    public void write( OutStream out ) throws IOException {
        
        if( pixelData == null || pixelData.length == 0 ) {
            out.writeUBits( 16, 0 );
            return;
        }
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        DeflaterOutputStream deflater = new DeflaterOutputStream( bout );

        for (int i = 0; i < pixelData.length; i++) {
            int pixel = pixelData[i];
            int red   = (pixel >> 16) & 0xff;;
            int green = (pixel >> 8 ) & 0xff;
            int blue  = pixel & 0xff;
            
            deflater.write( blue );
            deflater.write( green );
            deflater.write( red );
        }

        deflater.finish();
        
        byte[] data = bout.toByteArray();
        
        //System.out.println( data.length );
        
        out.writeUBits( 16, data.length );
        out.write( data );
    }

    /**
     * Make a square block containing a single color 
     * @param color the 0x00rrggbb color to use
     * @param size 16 to 256 in steps of 16
     */
    public static ScreenVideoImageBlock makeSquareOfSingleColor( int color, int size ) {
        int[] data = new int[ size * size ];
                
        for (int i = 0; i < data.length; i++) {
            if( i == 0 ) data[i] = 0xffffff;
            else data[i] = color;
        }
        
        return new ScreenVideoImageBlock( data );
    }
}
