package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class BitmapData extends flash.FlashObject implements flash.display.IBitmapDrawable   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public BitmapData( int arg1, int arg2, boolean arg3, int arg4 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public BitmapData( int arg1, int arg2, boolean arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public BitmapData( int arg1, int arg2 ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native void copyPixels( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.display.BitmapData arg4, flash.geom.Point arg5, boolean arg6 );

    @SlotId( -1 ) 
    public native void copyPixels( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.display.BitmapData arg4, flash.geom.Point arg5 );

    @SlotId( -1 ) 
    public native void copyPixels( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.display.BitmapData arg4 );

    @SlotId( -1 ) 
    public native void copyPixels( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3 );

    @SlotId( -1 ) 
    public native void setPixel( int arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public native boolean hitTest( flash.geom.Point arg1, int arg2, flash.FlashObject arg3, flash.geom.Point arg4, int arg5 );

    @SlotId( -1 ) 
    public native boolean hitTest( flash.geom.Point arg1, int arg2, flash.FlashObject arg3, flash.geom.Point arg4 );

    @SlotId( -1 ) 
    public native boolean hitTest( flash.geom.Point arg1, int arg2, flash.FlashObject arg3 );

    @SlotId( -1 ) @Getter
    public native int getWidth(  );

    @SlotId( -1 ) 
    public native void colorTransform( flash.geom.Rectangle arg1, flash.geom.ColorTransform arg2 );

    @SlotId( -1 ) 
    public native void draw( flash.display.IBitmapDrawable arg1, flash.geom.Matrix arg2, flash.geom.ColorTransform arg3, String arg4, flash.geom.Rectangle arg5, boolean arg6 );

    @SlotId( -1 ) 
    public native void draw( flash.display.IBitmapDrawable arg1, flash.geom.Matrix arg2, flash.geom.ColorTransform arg3, String arg4, flash.geom.Rectangle arg5 );

    @SlotId( -1 ) 
    public native void draw( flash.display.IBitmapDrawable arg1, flash.geom.Matrix arg2, flash.geom.ColorTransform arg3, String arg4 );

    @SlotId( -1 ) 
    public native void draw( flash.display.IBitmapDrawable arg1, flash.geom.Matrix arg2, flash.geom.ColorTransform arg3 );

    @SlotId( -1 ) 
    public native void draw( flash.display.IBitmapDrawable arg1, flash.geom.Matrix arg2 );

    @SlotId( -1 ) 
    public native void draw( flash.display.IBitmapDrawable arg1 );

    @SlotId( -1 ) 
    public native void applyFilter( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.filters.BitmapFilter arg4 );

    @SlotId( -1 ) 
    public native void fillRect( flash.geom.Rectangle arg1, int arg2 );

    @SlotId( -1 ) 
    public native int getPixel( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle generateFilterRect( flash.geom.Rectangle arg1, flash.filters.BitmapFilter arg2 );

    @SlotId( -1 ) @Getter
    public native boolean getTransparent(  );

    @SlotId( -1 ) 
    public native void unlock( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) 
    public native void unlock(  );

    @SlotId( -1 ) 
    public native void scroll( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native void copyChannel( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, int arg4, int arg5 );

    @SlotId( -1 ) 
    public native int pixelDissolve( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, int arg4, int arg5, int arg6 );

    @SlotId( -1 ) 
    public native int pixelDissolve( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, int arg4, int arg5 );

    @SlotId( -1 ) 
    public native int pixelDissolve( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, int arg4 );

    @SlotId( -1 ) 
    public native int pixelDissolve( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3 );

    @SlotId( -1 ) 
    public native void noise( int arg1, int arg2, int arg3, int arg4, boolean arg5 );

    @SlotId( -1 ) 
    public native void noise( int arg1, int arg2, int arg3, int arg4 );

    @SlotId( -1 ) 
    public native void noise( int arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public native void noise( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native void noise( int arg1 );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle getColorBoundsRect( int arg1, int arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle getColorBoundsRect( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native void dispose(  );

    @SlotId( -1 ) 
    public native void floodFill( int arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public native void setPixel32( int arg1, int arg2, int arg3 );

    @SlotId( -1 ) @Getter
    public native flash.geom.Rectangle getRect(  );

    @SlotId( -1 ) 
    public native flash.FlashObject compare( flash.display.BitmapData arg1 );

    @SlotId( -1 ) 
    public native void perlinNoise( double arg1, double arg2, int arg3, int arg4, boolean arg5, boolean arg6, int arg7, boolean arg8, flash.FlashArray arg9 );

    @SlotId( -1 ) 
    public native void perlinNoise( double arg1, double arg2, int arg3, int arg4, boolean arg5, boolean arg6, int arg7, boolean arg8 );

    @SlotId( -1 ) 
    public native void perlinNoise( double arg1, double arg2, int arg3, int arg4, boolean arg5, boolean arg6, int arg7 );

    @SlotId( -1 ) 
    public native void perlinNoise( double arg1, double arg2, int arg3, int arg4, boolean arg5, boolean arg6 );

    @SlotId( -1 ) @Getter
    public native int getHeight(  );

    @SlotId( -1 ) 
    public native void paletteMap( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.FlashArray arg4, flash.FlashArray arg5, flash.FlashArray arg6, flash.FlashArray arg7 );

    @SlotId( -1 ) 
    public native void paletteMap( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.FlashArray arg4, flash.FlashArray arg5, flash.FlashArray arg6 );

    @SlotId( -1 ) 
    public native void paletteMap( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.FlashArray arg4, flash.FlashArray arg5 );

    @SlotId( -1 ) 
    public native void paletteMap( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, flash.FlashArray arg4 );

    @SlotId( -1 ) 
    public native void paletteMap( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3 );

    @SlotId( -1 ) 
    public native flash.utils.ByteArray getPixels( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) 
    public native int threshold( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, String arg4, int arg5, int arg6, int arg7, boolean arg8 );

    @SlotId( -1 ) 
    public native int threshold( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, String arg4, int arg5, int arg6, int arg7 );

    @SlotId( -1 ) 
    public native int threshold( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, String arg4, int arg5, int arg6 );

    @SlotId( -1 ) 
    public native int threshold( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, String arg4, int arg5 );

    @SlotId( -1 ) 
    public native int getPixel32( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native void lock(  );

    @SlotId( -1 ) 
    public native void setPixels( flash.geom.Rectangle arg1, flash.utils.ByteArray arg2 );

    @SlotId( -1 ) 
    public native void merge( flash.display.BitmapData arg1, flash.geom.Rectangle arg2, flash.geom.Point arg3, int arg4, int arg5, int arg6, int arg7 );

}
