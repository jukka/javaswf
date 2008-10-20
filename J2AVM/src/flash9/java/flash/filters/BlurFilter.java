package flash.filters;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class BlurFilter extends flash.filters.BitmapFilter   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public BlurFilter( double arg1, double arg2, int arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public BlurFilter( double arg1, double arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public BlurFilter( double arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public BlurFilter(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native double getBlurX(  );

    @SlotId( -1 ) @Setter
    public native void setBlurX( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setBlurY( double arg1 );

    @SlotId( -1 ) @Getter
    public native int getQuality(  );

    @SlotId( -1 ) @Getter
    public native double getBlurY(  );

    @SlotId( -1 ) @Setter
    public native void setQuality( int arg1 );

}
