package flash.media;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class Camera extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Camera(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void setMotionLevel( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native void setMotionLevel( int arg1 );

    @SlotId( -1 ) @Getter
    public native int getMotionTimeout(  );

    @SlotId( -1 ) @Getter
    public native int getIndex(  );

    @SlotId( -1 ) @Getter
    public native boolean getLoopback(  );

    @SlotId( -1 ) 
    public native void setLoopback( boolean arg1 );

    @SlotId( -1 ) 
    public native void setLoopback(  );

    @SlotId( -1 ) @Getter
    public native int getWidth(  );

    @SlotId( -1 ) 
    public native void setCursor( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native String getName(  );

    @SlotId( -1 ) 
    public native void setMode( int arg1, int arg2, double arg3, boolean arg4 );

    @SlotId( -1 ) 
    public native void setMode( int arg1, int arg2, double arg3 );

    @SlotId( -1 ) @Getter
    public native int getHeight(  );

    @SlotId( -1 ) @Getter
    public native double getFps(  );

    @SlotId( -1 ) @Getter
    public native boolean getMuted(  );

    @SlotId( -1 ) @Getter
    public native int getMotionLevel(  );

    @SlotId( -1 ) @Getter
    public native double getCurrentFPS(  );

    @SlotId( -1 ) @Getter
    public native int getBandwidth(  );

    @SlotId( -1 ) @Getter
    public native int getKeyFrameInterval(  );

    @SlotId( -1 ) @Getter
    public native double getActivityLevel(  );

    @SlotId( -1 ) 
    public native void setKeyFrameInterval( int arg1 );

    @SlotId( -1 ) 
    public native void setQuality( int arg1, int arg2 );

    @SlotId( -1 ) @Getter
    public native int getQuality(  );

    @SlotId( 2 ) @Getter
    public static final native flash.FlashArray getNames(  );

    @SlotId( 3 ) 
    public static final native flash.media.Camera getCamera( String arg1 );

    @SlotId( 3 ) 
    public static final native flash.media.Camera getCamera(  );

}
