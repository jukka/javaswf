package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class InteractiveObject extends flash.display.DisplayObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public InteractiveObject(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native boolean getTabEnabled(  );

    @SlotId( -1 ) @Getter
    public native boolean getDoubleClickEnabled(  );

    @SlotId( -1 ) @Setter
    public native void setContextMenu( flash.ui.ContextMenu arg1 );

    @SlotId( -1 ) @Getter
    public native flash.accessibility.AccessibilityImplementation getAccessibilityImplementation(  );

    @SlotId( -1 ) @Setter
    public native void setDoubleClickEnabled( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native flash.ui.ContextMenu getContextMenu(  );

    @SlotId( -1 ) @Getter
    public native boolean getMouseEnabled(  );

    @SlotId( -1 ) @Setter
    public native void setFocusRect( flash.FlashObject arg1 );

    @SlotId( -1 ) @Getter
    public native int getTabIndex(  );

    @SlotId( -1 ) @Setter
    public native void setMouseEnabled( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getFocusRect(  );

    @SlotId( -1 ) @Setter
    public native void setTabEnabled( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setAccessibilityImplementation( flash.accessibility.AccessibilityImplementation arg1 );

    @SlotId( -1 ) @Setter
    public native void setTabIndex( int arg1 );

}
