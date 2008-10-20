package flash.media;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class Microphone extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Microphone(  ) { 
		super( null );
	}




    @SlotId( -1 ) @Setter
    public native void setRate( int arg1 );

    @SlotId( -1 ) 
    public native void setUseEchoSuppression( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native String getName(  );

    @SlotId( -1 ) @Getter
    public native int getIndex(  );

    @SlotId( -1 ) @Getter
    public native boolean getUseEchoSuppression(  );

    @SlotId( -1 ) @Getter
    public native flash.media.SoundTransform getSoundTransform(  );

    @SlotId( -1 ) @Getter
    public native int getRate(  );

    @SlotId( -1 ) @Getter
    public native int getSilenceTimeout(  );

    @SlotId( -1 ) @Getter
    public native double getSilenceLevel(  );

    @SlotId( -1 ) 
    public native void setSilenceLevel( double arg1, int arg2 );

    @SlotId( -1 ) 
    public native void setSilenceLevel( double arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getMuted(  );

    @SlotId( -1 ) 
    public native void setLoopBack( boolean arg1 );

    @SlotId( -1 ) 
    public native void setLoopBack(  );

    @SlotId( -1 ) @Getter
    public native double getActivityLevel(  );

    @SlotId( -1 ) @Setter
    public native void setSoundTransform( flash.media.SoundTransform arg1 );

    @SlotId( -1 ) @Setter
    public native void setGain( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getGain(  );

    @SlotId( 2 ) 
    public static final native flash.media.Microphone getMicrophone( int arg1 );

    @SlotId( 2 ) 
    public static final native flash.media.Microphone getMicrophone(  );

    @SlotId( 3 ) @Getter
    public static final native flash.FlashArray getNames(  );

}
