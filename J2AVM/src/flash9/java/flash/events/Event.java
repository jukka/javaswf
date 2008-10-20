package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Event extends flash.FlashObject   {

    @SlotId( 2 )
    public static final String CANCEL = "cancel";	

    @SlotId( 15 )
    public static final String RESIZE = "resize";	

    @SlotId( 8 )
    public static final String ENTER_FRAME = "enterFrame";	

    @SlotId( 13 )
    public static final String REMOVED = "removed";	

    @SlotId( 18 )
    public static final String SOUND_COMPLETE = "soundComplete";	

    @SlotId( 6 )
    public static final String CONNECT = "connect";	

    @SlotId( 16 )
    public static final String SCROLL = "scroll";	

    @SlotId( 10 )
    public static final String INIT = "init";	

    @SlotId( 12 )
    public static final String OPEN = "open";	

    @SlotId( 14 )
    public static final String RENDER = "render";	

    @SlotId( 22 )
    public static final String UNLOAD = "unload";	

    @SlotId( 4 )
    public static final String CLOSE = "close";	

    @SlotId( 11 )
    public static final String MOUSE_LEAVE = "mouseLeave";	

    @SlotId( 20 )
    public static final String TAB_ENABLED_CHANGE = "tabEnabledChange";	

    @SlotId( 1 )
    public static final String ADDED = "added";	

    @SlotId( 19 )
    public static final String TAB_CHILDREN_CHANGE = "tabChildrenChange";	

    @SlotId( 21 )
    public static final String TAB_INDEX_CHANGE = "tabIndexChange";	

    @SlotId( 0 )
    public static final String ACTIVATE = "activate";	

    @SlotId( 7 )
    public static final String DEACTIVATE = "deactivate";	

    @SlotId( 3 )
    public static final String CHANGE = "change";	

    @SlotId( 9 )
    public static final String ID3 = "id3";	

    @SlotId( 5 )
    public static final String COMPLETE = "complete";	

    @SlotId( 17 )
    public static final String SELECT = "select";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Event( String arg1, boolean arg2, boolean arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Event( String arg1, boolean arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Event( String arg1 ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native boolean isDefaultPrevented(  );

    @SlotId( -1 ) 
    public native String formatToString( String arg1 );

    @SlotId( -1 ) 
    public native void stopImmediatePropagation(  );

    @SlotId( -1 ) @Getter
    public native int getEventPhase(  );

    @SlotId( -1 ) 
    public native void preventDefault(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getCurrentTarget(  );

    @SlotId( -1 ) @Getter
    public native boolean getBubbles(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getTarget(  );

    @SlotId( -1 ) @Getter
    public native boolean getCancelable(  );

    @SlotId( -1 ) 
    public native void stopPropagation(  );

    @SlotId( -1 ) @Getter
    public native String getType(  );

}
