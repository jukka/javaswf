package flash.geom;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Transform extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Transform( flash.display.DisplayObject arg1 ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native flash.geom.Matrix getConcatenatedMatrix(  );

    @SlotId( -1 ) @Getter
    public native flash.geom.Matrix getMatrix(  );

    @SlotId( -1 ) @Setter
    public native void setMatrix( flash.geom.Matrix arg1 );

    @SlotId( -1 ) @Getter
    public native flash.geom.ColorTransform getConcatenatedColorTransform(  );

    @SlotId( -1 ) @Getter
    public native flash.geom.ColorTransform getColorTransform(  );

    @SlotId( -1 ) @Setter
    public native void setColorTransform( flash.geom.ColorTransform arg1 );

    @SlotId( -1 ) @Getter
    public native flash.geom.Rectangle getPixelBounds(  );

}
