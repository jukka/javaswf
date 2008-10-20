package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Stage extends flash.display.DisplayObjectContainer   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Stage(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native flash.display.DisplayObject addChild( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native String getAlign(  );

    @SlotId( -1 ) @Setter
    public native void setStageFocusRect( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native String getScaleMode(  );

    @SlotId( -1 ) @Getter
    public native flash.text.TextSnapshot getTextSnapshot(  );

    @SlotId( -1 ) @Setter
    public native void setName( String arg1 );

    @SlotId( -1 ) 
    public native void setChildIndex( flash.display.DisplayObject arg1, int arg2 );

    @SlotId( -1 ) @Setter
    public native void setAlign( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setScaleMode( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setShowDefaultContextMenu( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setQuality( String arg1 );

    @SlotId( -1 ) @Getter
    public native double getHeight(  );

    @SlotId( -1 ) @Setter
    public native void setBlendMode( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setScale9Grid( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) 
    public native void invalidate(  );

    @SlotId( -1 ) @Setter
    public native void setAccessibilityImplementation( flash.accessibility.AccessibilityImplementation arg1 );

    @SlotId( -1 ) @Setter
    public native void setAccessibilityProperties( flash.accessibility.AccessibilityProperties arg1 );

    @SlotId( -1 ) @Setter
    public native void setCacheAsBitmap( boolean arg1 );

    @SlotId( -1 ) 
    public native boolean dispatchEvent( flash.events.Event arg1 );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject removeChildAt( int arg1 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3, int arg4, boolean arg5 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3, int arg4 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native void addEventListener( String arg1, flash.FlashFunction arg2 );

    @SlotId( -1 ) @Setter
    public native void setHeight( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setContextMenu( flash.ui.ContextMenu arg1 );

    @SlotId( -1 ) @Setter
    public native void setOpaqueBackground( flash.FlashObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setTabChildren( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native int getStageWidth(  );

    @SlotId( -1 ) @Setter
    public native void setTabIndex( int arg1 );

    @SlotId( -1 ) 
    public native void swapChildrenAt( int arg1, int arg2 );

    @SlotId( -1 ) @Setter
    public native void setStageHeight( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setWidth( double arg1 );

    @SlotId( -1 ) 
    public native boolean willTrigger( String arg1 );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject addChildAt( flash.display.DisplayObject arg1, int arg2 );

    @SlotId( -1 ) @Setter
    public native void setMouseChildren( boolean arg1 );

    @SlotId( -1 ) 
    public native boolean isFocusInaccessible(  );

    @SlotId( -1 ) @Getter
    public native boolean getShowDefaultContextMenu(  );

    @SlotId( -1 ) @Getter
    public native boolean getStageFocusRect(  );

    @SlotId( -1 ) @Setter
    public native void setScaleX( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setScaleY( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setScrollRect( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) @Getter
    public native int getNumChildren(  );

    @SlotId( -1 ) @Setter
    public native void setFocus( flash.display.InteractiveObject arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getTabChildren(  );

    @SlotId( -1 ) @Setter
    public native void setFocusRect( flash.FlashObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setAlpha( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setMouseEnabled( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getMouseChildren(  );

    @SlotId( -1 ) @Getter
    public native int getStageHeight(  );

    @SlotId( -1 ) @Getter
    public native double getWidth(  );

    @SlotId( -1 ) @Setter
    public native void setStageWidth( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setMask( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setFrameRate( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setTransform( flash.geom.Transform arg1 );

    @SlotId( -1 ) @Setter
    public native void setVisible( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setTabEnabled( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native double getFrameRate(  );

    @SlotId( -1 ) @Setter
    public native void setX( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setY( double arg1 );

    @SlotId( -1 ) 
    public native boolean hasEventListener( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setFilters( flash.FlashArray arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.InteractiveObject getFocus(  );

    @SlotId( -1 ) @Setter
    public native void setRotation( double arg1 );

    @SlotId( -1 ) @Getter
    public native String getQuality(  );

}
