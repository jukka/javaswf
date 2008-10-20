package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class MouseEvent extends flash.events.Event   {

    @SlotId( 7 )
    public static final String MOUSE_WHEEL = "mouseWheel";	

    @SlotId( 3 )
    public static final String MOUSE_MOVE = "mouseMove";	

    @SlotId( 8 )
    public static final String ROLL_OUT = "rollOut";	

    @SlotId( 5 )
    public static final String MOUSE_OVER = "mouseOver";	

    @SlotId( 0 )
    public static final String CLICK = "click";	

    @SlotId( 4 )
    public static final String MOUSE_OUT = "mouseOut";	

    @SlotId( 6 )
    public static final String MOUSE_UP = "mouseUp";	

    @SlotId( 1 )
    public static final String DOUBLE_CLICK = "doubleClick";	

    @SlotId( 2 )
    public static final String MOUSE_DOWN = "mouseDown";	

    @SlotId( 9 )
    public static final String ROLL_OVER = "rollOver";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4, double arg5, flash.display.InteractiveObject arg6, boolean arg7, boolean arg8, boolean arg9, boolean arg10, int arg11 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4, double arg5, flash.display.InteractiveObject arg6, boolean arg7, boolean arg8, boolean arg9, boolean arg10 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4, double arg5, flash.display.InteractiveObject arg6, boolean arg7, boolean arg8, boolean arg9 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4, double arg5, flash.display.InteractiveObject arg6, boolean arg7, boolean arg8 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4, double arg5, flash.display.InteractiveObject arg6, boolean arg7 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4, double arg5, flash.display.InteractiveObject arg6 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4, double arg5 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3, double arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MouseEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Setter
    public native void setRelatedObject( flash.display.InteractiveObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setLocalX( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getStageY(  );

    @SlotId( -1 ) @Setter
    public native void setLocalY( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getStageX(  );

    @SlotId( -1 ) @Getter
    public native int getDelta(  );

    @SlotId( -1 ) 
    public native void updateAfterEvent(  );

    @SlotId( -1 ) @Getter
    public native flash.display.InteractiveObject getRelatedObject(  );

    @SlotId( -1 ) @Getter
    public native double getLocalX(  );

    @SlotId( -1 ) @Getter
    public native double getLocalY(  );

    @SlotId( -1 ) @Setter
    public native void setCtrlKey( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setButtonDown( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setAltKey( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getButtonDown(  );

    @SlotId( -1 ) @Getter
    public native boolean getAltKey(  );

    @SlotId( -1 ) @Setter
    public native void setDelta( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setShiftKey( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getCtrlKey(  );

    @SlotId( -1 ) @Getter
    public native boolean getShiftKey(  );

}
