package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class NetStream extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public NetStream( flash.net.NetConnection arg1 ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void togglePause(  );

    @SlotId( -1 ) 
    public native void send( String arg1 );

    @SlotId( -1 ) 
    public native void seek( double arg1 );

    @SlotId( -1 ) 
    public native void attachAudio( flash.media.Microphone arg1 );

    @SlotId( -1 ) 
    public native void publish( String arg1, String arg2 );

    @SlotId( -1 ) 
    public native void publish( String arg1 );

    @SlotId( -1 ) 
    public native void publish(  );

    @SlotId( -1 ) @Getter
    public native int getBytesTotal(  );

    @SlotId( -1 ) @Getter
    public native flash.media.SoundTransform getSoundTransform(  );

    @SlotId( -1 ) @Setter
    public native void setClient( flash.FlashObject arg1 );

    @SlotId( -1 ) @Getter
    public native double getBufferTime(  );

    @SlotId( -1 ) @Getter
    public native double getCurrentFPS(  );

    @SlotId( -1 ) @Getter
    public native int getObjectEncoding(  );

    @SlotId( -1 ) 
    public native void play(  );

    @SlotId( -1 ) @Setter
    public native void setSoundTransform( flash.media.SoundTransform arg1 );

    @SlotId( -1 ) 
    public native void resume(  );

    @SlotId( -1 ) 
    public native void receiveAudio( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native double getBufferLength(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getClient(  );

    @SlotId( -1 ) @Getter
    public native int getBytesLoaded(  );

    @SlotId( -1 ) 
    public native void receiveVideo( boolean arg1 );

    @SlotId( -1 ) 
    public native void attachCamera( flash.media.Camera arg1, int arg2 );

    @SlotId( -1 ) 
    public native void attachCamera( flash.media.Camera arg1 );

    @SlotId( -1 ) @Setter
    public native void setBufferTime( double arg1 );

    @SlotId( -1 ) @Getter
    public native int getVideoCodec(  );

    @SlotId( -1 ) @Getter
    public native int getAudioCodec(  );

    @SlotId( -1 ) @Getter
    public native double getTime(  );

    @SlotId( -1 ) @Setter
    public native void setCheckPolicyFile( boolean arg1 );

    @SlotId( -1 ) 
    public native void pause(  );

    @SlotId( -1 ) @Getter
    public native double getLiveDelay(  );

    @SlotId( -1 ) @Getter
    public native int getDecodedFrames(  );

    @SlotId( -1 ) @Getter
    public native boolean getCheckPolicyFile(  );

    @SlotId( -1 ) 
    public native void close(  );

}
