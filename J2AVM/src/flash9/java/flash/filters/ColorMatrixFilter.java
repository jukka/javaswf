package flash.filters;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class ColorMatrixFilter extends flash.filters.BitmapFilter   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorMatrixFilter( flash.FlashArray arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorMatrixFilter(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native flash.FlashArray getMatrix(  );

    @SlotId( -1 ) @Setter
    public native void setMatrix( flash.FlashArray arg1 );

}
