package flash.media;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class SoundChannel extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SoundChannel(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void stop(  );

    @SlotId( -1 ) @Getter
    public native double getPosition(  );

    @SlotId( -1 ) @Getter
    public native double getRightPeak(  );

    @SlotId( -1 ) @Getter
    public native double getLeftPeak(  );

    @SlotId( -1 ) @Setter
    public native void setSoundTransform( flash.media.SoundTransform arg1 );

    @SlotId( -1 ) @Getter
    public native flash.media.SoundTransform getSoundTransform(  );

}
