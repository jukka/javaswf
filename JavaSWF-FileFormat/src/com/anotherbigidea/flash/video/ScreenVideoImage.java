package com.anotherbigidea.flash.video;

import java.awt.image.BufferedImage;

/**
 * A image that can be encoded using ScreenVideo.
 * 
 * @author nmain
 */
public class ScreenVideoImage {

    private int imageWidth;
    private int imageHeight;
    private int[] pixels;
    
    /**
     * @param imageWidth image width
     * @param imageHeight image height
     * @param pixels 0x00rrggbb pixels from bottom left to top right in rows
     */
    public ScreenVideoImage( int imageWidth, int imageHeight, int[] pixels ) {
        this.imageWidth  = imageWidth;
        this.imageHeight = imageHeight;
        this.pixels      = pixels;
    }
    
    /**
     * Create from an AWT image.
     */
    public ScreenVideoImage( BufferedImage awtImage ) {
        int width  = this.imageWidth  = awtImage.getWidth();
        int height = this.imageHeight = awtImage.getHeight();
        
        pixels = new int[ width * height ];
        int[] awtPixels = awtImage.getRGB( 0, 0, width, height, null, 0, width );
        
        for( int y = 0; y < height; y++ ) {
            int destRowIndex = y * width;
            int srcRowIndex  = (height - y - 1) * width;
            
            for( int x = 0; x < width; x++ ) {
                //copy the pixel - without the alpha data
                pixels[ destRowIndex + x ] = awtPixels[ srcRowIndex + x ] & 0x00ffffff;
            }            
        }
    }

    /**
     * Create from a subset of an AWT image.
     */
    public ScreenVideoImage( BufferedImage awtImage, int x, int y, int width, int height ) {        
        this( awtImage.getSubimage( x, y, width, height ) );
    }
    
    /**
     * Convert the image to ScreenVideo blocks.
     * 
     * @param blockWidth block width
     * @param blockHeight block height
     */
    public ScreenVideoImageBlock[] toBlocks( int blockWidth, int blockHeight ) {
        
        int numBlocks = ((imageWidth /blockWidth ) + ((imageWidth  % blockWidth  == 0) ? 0 : 1 ))
                      * ((imageHeight/blockHeight) + ((imageHeight % blockHeight == 0) ? 0 : 1 ));
        
        ScreenVideoImageBlock[] blocks = new ScreenVideoImageBlock[ numBlocks ];
        int blockIndex = 0;
        
        //process blocks from BL to TR
        for( int blockY = 0; blockY < imageHeight; blockY += blockHeight ) {
            for( int blockX = 0; blockX < imageWidth; blockX += blockWidth ) {
                int currBlockWidth  = Math.min( blockWidth,  imageWidth  - blockX );
                int currBlockHeight = Math.min( blockHeight, imageHeight - blockY );
                
                int[] pixelData = new int[ currBlockWidth * currBlockHeight ];
                int pixelIndex = 0;
                
                //gather pixels in the block from BL to TR
                for( int y = 0; y < currBlockHeight; y++ ) {
                    int sourceRowIndex = ((blockY + y) * imageWidth) + blockX; 
                    
                    for( int x = 0; x < currBlockWidth; x++ ) {
                        pixelData[ pixelIndex++ ] = pixels[ sourceRowIndex + x ];
                    }                    
                }
                
                blocks[ blockIndex++ ] = new ScreenVideoImageBlock( pixelData );
            }            
        }

        return blocks;
    }
    
    /**
     * Get the pixel at the given location, where the origin is at the bottom
     * left.
     * 
     * @param x zero-based x coordinate
     * @param y zero-based y coordinate
     * @return 0x00rrggbb pixel value, zero if x or y is out of range
     */
    public int getPixelAt( int x, int y ) {
        int idx = (y * imageWidth) + x;
        if( idx > pixels.length ) return 0;
        return pixels[ idx ];
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
    public int[] getPixels() {
        return pixels;
    }
    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
}
