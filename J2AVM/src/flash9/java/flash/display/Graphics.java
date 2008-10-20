package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class Graphics extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Graphics(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native void drawRoundRectComplex( double arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8 );

    @SlotId( -1 ) 
    public native void drawCircle( double arg1, double arg2, double arg3 );

    @SlotId( -1 ) 
    public native void drawRect( double arg1, double arg2, double arg3, double arg4 );

    @SlotId( -1 ) 
    public native void curveTo( double arg1, double arg2, double arg3, double arg4 );

    @SlotId( -1 ) 
    public native void beginFill( int arg1, double arg2 );

    @SlotId( -1 ) 
    public native void beginFill( int arg1 );

    @SlotId( -1 ) 
    public native void lineGradientStyle( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5, String arg6, String arg7, double arg8 );

    @SlotId( -1 ) 
    public native void lineGradientStyle( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5, String arg6, String arg7 );

    @SlotId( -1 ) 
    public native void lineGradientStyle( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5, String arg6 );

    @SlotId( -1 ) 
    public native void lineGradientStyle( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5 );

    @SlotId( -1 ) 
    public native void lineGradientStyle( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4 );

    @SlotId( -1 ) 
    public native void drawRoundRect( double arg1, double arg2, double arg3, double arg4, double arg5, double arg6 );

    @SlotId( -1 ) 
    public native void moveTo( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native void clear(  );

    @SlotId( -1 ) 
    public native void lineTo( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native void drawEllipse( double arg1, double arg2, double arg3, double arg4 );

    @SlotId( -1 ) 
    public native void beginBitmapFill( flash.display.BitmapData arg1, flash.geom.Matrix arg2, boolean arg3, boolean arg4 );

    @SlotId( -1 ) 
    public native void beginBitmapFill( flash.display.BitmapData arg1, flash.geom.Matrix arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native void beginBitmapFill( flash.display.BitmapData arg1, flash.geom.Matrix arg2 );

    @SlotId( -1 ) 
    public native void beginBitmapFill( flash.display.BitmapData arg1 );

    @SlotId( -1 ) 
    public native void beginGradientFill( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5, String arg6, String arg7, double arg8 );

    @SlotId( -1 ) 
    public native void beginGradientFill( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5, String arg6, String arg7 );

    @SlotId( -1 ) 
    public native void beginGradientFill( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5, String arg6 );

    @SlotId( -1 ) 
    public native void beginGradientFill( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4, flash.geom.Matrix arg5 );

    @SlotId( -1 ) 
    public native void beginGradientFill( String arg1, flash.FlashArray arg2, flash.FlashArray arg3, flash.FlashArray arg4 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1, int arg2, double arg3, boolean arg4, String arg5, String arg6, String arg7, double arg8 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1, int arg2, double arg3, boolean arg4, String arg5, String arg6, String arg7 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1, int arg2, double arg3, boolean arg4, String arg5, String arg6 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1, int arg2, double arg3, boolean arg4, String arg5 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1, int arg2, double arg3, boolean arg4 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1, int arg2, double arg3 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1, int arg2 );

    @SlotId( -1 ) 
    public native void lineStyle( double arg1 );

    @SlotId( -1 ) 
    public native void endFill(  );

}
