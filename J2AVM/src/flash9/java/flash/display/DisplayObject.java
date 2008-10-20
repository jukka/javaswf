package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class DisplayObject extends flash.events.EventDispatcher implements flash.display.IBitmapDrawable   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public DisplayObject(  ) { 
		super( null );
	}




    @SlotId( -1 ) @Getter
    public native double getY(  );

    @SlotId( -1 ) @Getter
    public native flash.geom.Transform getTransform(  );

    @SlotId( -1 ) @Getter
    public native flash.display.Stage getStage(  );

    @SlotId( -1 ) 
    public native flash.geom.Point localToGlobal( flash.geom.Point arg1 );

    @SlotId( -1 ) @Getter
    public native String getName(  );

    @SlotId( -1 ) @Setter
    public native void setWidth( double arg1 );

    @SlotId( -1 ) @Getter
    public native String getBlendMode(  );

    @SlotId( -1 ) @Getter
    public native flash.geom.Rectangle getScale9Grid(  );

    @SlotId( -1 ) @Setter
    public native void setName( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setScaleX( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setScaleY( double arg1 );

    @SlotId( -1 ) @Getter
    public native flash.accessibility.AccessibilityProperties getAccessibilityProperties(  );

    @SlotId( -1 ) @Setter
    public native void setScrollRect( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getCacheAsBitmap(  );

    @SlotId( -1 ) 
    public native flash.geom.Point globalToLocal( flash.geom.Point arg1 );

    @SlotId( -1 ) @Getter
    public native double getHeight(  );

    @SlotId( -1 ) @Setter
    public native void setBlendMode( String arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObjectContainer getParent(  );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle getBounds( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getOpaqueBackground(  );

    @SlotId( -1 ) @Setter
    public native void setScale9Grid( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) @Setter
    public native void setAlpha( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setAccessibilityProperties( flash.accessibility.AccessibilityProperties arg1 );

    @SlotId( -1 ) @Getter
    public native double getWidth(  );

    @SlotId( -1 ) 
    public native boolean hitTestPoint( double arg1, double arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native boolean hitTestPoint( double arg1, double arg2 );

    @SlotId( -1 ) @Getter
    public native double getScaleX(  );

    @SlotId( -1 ) @Getter
    public native double getScaleY(  );

    @SlotId( -1 ) @Getter
    public native double getMouseX(  );

    @SlotId( -1 ) @Setter
    public native void setHeight( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setMask( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle getRect( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Getter
    public native double getMouseY(  );

    @SlotId( -1 ) @Getter
    public native double getAlpha(  );

    @SlotId( -1 ) @Setter
    public native void setTransform( flash.geom.Transform arg1 );

    @SlotId( -1 ) @Getter
    public native flash.geom.Rectangle getScrollRect(  );

    @SlotId( -1 ) @Getter
    public native flash.display.LoaderInfo getLoaderInfo(  );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getRoot(  );

    @SlotId( -1 ) @Setter
    public native void setVisible( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setOpaqueBackground( flash.FlashObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setCacheAsBitmap( boolean arg1 );

    @SlotId( -1 ) 
    public native boolean hitTestObject( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) @Setter
    public native void setX( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setY( double arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getMask(  );

    @SlotId( -1 ) @Setter
    public native void setFilters( flash.FlashArray arg1 );

    @SlotId( -1 ) @Getter
    public native double getX(  );

    @SlotId( -1 ) @Getter
    public native boolean getVisible(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getFilters(  );

    @SlotId( -1 ) @Setter
    public native void setRotation( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getRotation(  );

}
