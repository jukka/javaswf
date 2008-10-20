package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class EventDispatcher extends flash.FlashObject implements flash.events.IEventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public EventDispatcher( flash.events.IEventDispatcher arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public EventDispatcher(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native boolean dispatchEvent( flash.events.Event arg1 );

    @SlotId( -1 ) 
    public native boolean willTrigger( String arg1 );

    @SlotId( -1 ) 
    public native void removeEventListener( String arg1, flash.FlashFunction arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native void removeEventListener( String arg1, flash.FlashFunction arg2 );

    @SlotId( -1 ) 
    public native boolean hasEventListener( String arg1 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3, int arg4, boolean arg5 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3, int arg4 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2 );

}
