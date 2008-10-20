package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class SimpleButton extends flash.display.InteractiveObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SimpleButton( flash.display.DisplayObject arg1, flash.display.DisplayObject arg2, flash.display.DisplayObject arg3, flash.display.DisplayObject arg4 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SimpleButton( flash.display.DisplayObject arg1, flash.display.DisplayObject arg2, flash.display.DisplayObject arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SimpleButton( flash.display.DisplayObject arg1, flash.display.DisplayObject arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SimpleButton( flash.display.DisplayObject arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SimpleButton(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Setter
    public native void setEnabled( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getUseHandCursor(  );

    @SlotId( -1 ) @Setter
    public native void setUseHandCursor( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getOverState(  );

    @SlotId( -1 ) @Getter
    public native flash.media.SoundTransform getSoundTransform(  );

    @SlotId( -1 ) @Setter
    public native void setDownState( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getTrackAsMenu(  );

    @SlotId( -1 ) @Setter
    public native void setUpState( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getUpState(  );

    @SlotId( -1 ) @Getter
    public native boolean getEnabled(  );

    @SlotId( -1 ) @Setter
    public native void setHitTestState( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getDownState(  );

    @SlotId( -1 ) @Setter
    public native void setOverState( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getHitTestState(  );

    @SlotId( -1 ) @Setter
    public native void setTrackAsMenu( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setSoundTransform( flash.media.SoundTransform arg1 );

}
