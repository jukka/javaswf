package flash.media;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class SoundMixer extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SoundMixer(  ) { 
		super(  );
	}




    @SlotId( 7 ) @Setter
    public static final native void setSoundTransform( flash.media.SoundTransform arg1 );

    @SlotId( 8 ) 
    public static final native boolean areSoundsInaccessible(  );

    @SlotId( 4 ) @Getter
    public static final native int getBufferTime(  );

    @SlotId( 3 ) 
    public static final native void computeSpectrum( flash.utils.ByteArray arg1, boolean arg2, int arg3 );

    @SlotId( 3 ) 
    public static final native void computeSpectrum( flash.utils.ByteArray arg1, boolean arg2 );

    @SlotId( 3 ) 
    public static final native void computeSpectrum( flash.utils.ByteArray arg1 );

    @SlotId( 5 ) @Setter
    public static final native void setBufferTime( int arg1 );

    @SlotId( 6 ) @Getter
    public static final native flash.media.SoundTransform getSoundTransform(  );

    @SlotId( 2 ) 
    public static final native void stopAll(  );

}
