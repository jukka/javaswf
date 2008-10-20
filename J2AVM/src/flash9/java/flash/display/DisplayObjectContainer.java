package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class DisplayObjectContainer extends flash.display.InteractiveObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public DisplayObjectContainer(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native flash.display.DisplayObject addChild( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject getChildByName( String arg1 );

    @SlotId( -1 ) @Getter
    public native flash.text.TextSnapshot getTextSnapshot(  );

    @SlotId( -1 ) 
    public native int getChildIndex( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setMouseChildren( boolean arg1 );

    @SlotId( -1 ) 
    public native void setChildIndex( flash.display.DisplayObject arg1, int arg2 );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject addChildAt( flash.display.DisplayObject arg1, int arg2 );

    @SlotId( -1 ) 
    public native boolean contains( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native int getNumChildren(  );

    @SlotId( -1 ) 
    public native void swapChildrenAt( int arg1, int arg2 );

    @SlotId( -1 ) @Getter
    public native boolean getTabChildren(  );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject getChildAt( int arg1 );

    @SlotId( -1 ) 
    public native void swapChildren( flash.display.DisplayObject arg1, flash.display.DisplayObject arg2 );

    @SlotId( -1 ) 
    public native flash.FlashArray getObjectsUnderPoint( flash.geom.Point arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getMouseChildren(  );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject removeChildAt( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setTabChildren( boolean arg1 );

    @SlotId( -1 ) 
    public native boolean areInaccessibleObjectsUnderPoint( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject removeChild( flash.display.DisplayObject arg1 );

}
