package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class FocusEvent extends flash.events.Event   {

    @SlotId( 3 )
    public static final String MOUSE_FOCUS_CHANGE = "mouseFocusChange";	

    @SlotId( 1 )
    public static final String FOCUS_OUT = "focusOut";	

    @SlotId( 2 )
    public static final String KEY_FOCUS_CHANGE = "keyFocusChange";	

    @SlotId( 0 )
    public static final String FOCUS_IN = "focusIn";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FocusEvent( String arg1, boolean arg2, boolean arg3, flash.display.InteractiveObject arg4, boolean arg5, int arg6 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FocusEvent( String arg1, boolean arg2, boolean arg3, flash.display.InteractiveObject arg4, boolean arg5 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FocusEvent( String arg1, boolean arg2, boolean arg3, flash.display.InteractiveObject arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FocusEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FocusEvent( String arg1, boolean arg2 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FocusEvent( String arg1 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Getter
    public native boolean getShiftKey(  );

    @SlotId( -1 ) @Getter
    public native flash.display.InteractiveObject getRelatedObject(  );

    @SlotId( -1 ) @Setter
    public native void setShiftKey( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setRelatedObject( flash.display.InteractiveObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setKeyCode( int arg1 );

    @SlotId( -1 ) @Getter
    public native int getKeyCode(  );

}
