package flash.utils;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Timer extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Timer( double arg1, int arg2 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Timer( double arg1 ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void stop(  );

    @SlotId( -1 ) @Getter
    public native double getDelay(  );

    @SlotId( -1 ) @Getter
    public native int getCurrentCount(  );

    @SlotId( -1 ) @Setter
    public native void setDelay( double arg1 );

    @SlotId( -1 ) 
    public native void reset(  );

    @SlotId( -1 ) @Setter
    public native void setRepeatCount( int arg1 );

    @SlotId( -1 ) @Getter
    public native int getRepeatCount(  );

    @SlotId( -1 ) 
    public native void start(  );

    @SlotId( -1 ) @Getter
    public native boolean getRunning(  );

}
