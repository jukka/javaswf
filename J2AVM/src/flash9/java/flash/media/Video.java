package flash.media;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Video extends flash.display.DisplayObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Video( int arg1, int arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Video( int arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Video(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Setter
    public native void setDeblocking( int arg1 );

    @SlotId( -1 ) @Getter
    public native int getVideoHeight(  );

    @SlotId( -1 ) @Getter
    public native int getVideoWidth(  );

    @SlotId( -1 ) @Getter
    public native boolean getSmoothing(  );

    @SlotId( -1 ) @Getter
    public native int getDeblocking(  );

    @SlotId( -1 ) 
    public native void attachNetStream( flash.net.NetStream arg1 );

    @SlotId( -1 ) 
    public native void attachCamera( flash.media.Camera arg1 );

    @SlotId( -1 ) @Setter
    public native void setSmoothing( boolean arg1 );

    @SlotId( -1 ) 
    public native void clear(  );

}
