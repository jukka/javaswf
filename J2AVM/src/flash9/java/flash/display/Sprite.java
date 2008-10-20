package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Sprite extends flash.display.DisplayObjectContainer   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Sprite(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native flash.media.SoundTransform getSoundTransform(  );

    @SlotId( -1 ) @Setter
    public native void setUseHandCursor( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setSoundTransform( flash.media.SoundTransform arg1 );

    @SlotId( -1 ) 
    public native void stopDrag(  );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getDropTarget(  );

    @SlotId( -1 ) @Setter
    public native void setHitArea( flash.display.Sprite arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.Graphics getGraphics(  );

    @SlotId( -1 ) @Getter
    public native boolean getUseHandCursor(  );

    @SlotId( -1 ) 
    public native void startDrag( boolean arg1, flash.geom.Rectangle arg2 );

    @SlotId( -1 ) 
    public native void startDrag( boolean arg1 );

    @SlotId( -1 ) 
    public native void startDrag(  );

    @SlotId( -1 ) @Getter
    public native flash.display.Sprite getHitArea(  );

    @SlotId( -1 ) @Setter
    public native void setButtonMode( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getButtonMode(  );

}
