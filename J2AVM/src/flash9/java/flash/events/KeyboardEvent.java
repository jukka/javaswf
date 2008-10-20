package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class KeyboardEvent extends flash.events.Event   {

    @SlotId( 0 )
    public static final String KEY_DOWN = "keyDown";	

    @SlotId( 1 )
    public static final String KEY_UP = "keyUp";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2, boolean arg3, int arg4, int arg5, int arg6, boolean arg7, boolean arg8, boolean arg9 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2, boolean arg3, int arg4, int arg5, int arg6, boolean arg7, boolean arg8 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2, boolean arg3, int arg4, int arg5, int arg6, boolean arg7 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2, boolean arg3, int arg4, int arg5, int arg6 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2, boolean arg3, int arg4, int arg5 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2, boolean arg3, int arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1, boolean arg2 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public KeyboardEvent( String arg1 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Setter
    public native void setCharCode( int arg1 );

    @SlotId( -1 ) @Getter
    public native int getKeyLocation(  );

    @SlotId( -1 ) @Setter
    public native void setAltKey( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setShiftKey( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getShiftKey(  );

    @SlotId( -1 ) @Setter
    public native void setKeyCode( int arg1 );

    @SlotId( -1 ) 
    public native void updateAfterEvent(  );

    @SlotId( -1 ) @Getter
    public native boolean getCtrlKey(  );

    @SlotId( -1 ) @Getter
    public native int getCharCode(  );

    @SlotId( -1 ) @Setter
    public native void setKeyLocation( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setCtrlKey( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getAltKey(  );

    @SlotId( -1 ) @Getter
    public native int getKeyCode(  );

}
