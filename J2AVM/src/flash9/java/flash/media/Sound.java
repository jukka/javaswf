package flash.media;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Sound extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Sound( flash.net.URLRequest arg1, flash.media.SoundLoaderContext arg2 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Sound( flash.net.URLRequest arg1 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Sound(  ) { 
		super( null );
	}




    @SlotId( -1 ) @Getter
    public native int getBytesLoaded(  );

    @SlotId( -1 ) @Getter
    public native int getBytesTotal(  );

    @SlotId( -1 ) @Getter
    public native boolean getIsBuffering(  );

    @SlotId( -1 ) 
    public native void load( flash.net.URLRequest arg1, flash.media.SoundLoaderContext arg2 );

    @SlotId( -1 ) 
    public native void load( flash.net.URLRequest arg1 );

    @SlotId( -1 ) @Getter
    public native String getUrl(  );

    @SlotId( -1 ) 
    public native flash.media.SoundChannel play( double arg1, int arg2, flash.media.SoundTransform arg3 );

    @SlotId( -1 ) 
    public native flash.media.SoundChannel play( double arg1, int arg2 );

    @SlotId( -1 ) 
    public native flash.media.SoundChannel play( double arg1 );

    @SlotId( -1 ) 
    public native flash.media.SoundChannel play(  );

    @SlotId( -1 ) @Getter
    public native double getLength(  );

    @SlotId( -1 ) @Getter
    public native flash.media.ID3Info getId3(  );

    @SlotId( -1 ) 
    public native void close(  );

}
