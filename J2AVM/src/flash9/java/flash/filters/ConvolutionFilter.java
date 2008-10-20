package flash.filters;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class ConvolutionFilter extends flash.filters.BitmapFilter   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2, flash.FlashArray arg3, double arg4, double arg5, boolean arg6, boolean arg7, int arg8, double arg9 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2, flash.FlashArray arg3, double arg4, double arg5, boolean arg6, boolean arg7, int arg8 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2, flash.FlashArray arg3, double arg4, double arg5, boolean arg6, boolean arg7 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2, flash.FlashArray arg3, double arg4, double arg5, boolean arg6 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2, flash.FlashArray arg3, double arg4, double arg5 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2, flash.FlashArray arg3, double arg4 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2, flash.FlashArray arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1, double arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter( double arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ConvolutionFilter(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native flash.FlashArray getMatrix(  );

    @SlotId( -1 ) @Setter
    public native void setMatrix( flash.FlashArray arg1 );

    @SlotId( -1 ) @Getter
    public native int getColor(  );

    @SlotId( -1 ) @Setter
    public native void setPreserveAlpha( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native double getAlpha(  );

    @SlotId( -1 ) @Setter
    public native void setColor( int arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getPreserveAlpha(  );

    @SlotId( -1 ) @Setter
    public native void setAlpha( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setMatrixX( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setMatrixY( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setBias( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setClamp( boolean arg1 );

    @SlotId( -1 ) @Getter
    public native double getMatrixX(  );

    @SlotId( -1 ) @Getter
    public native double getMatrixY(  );

    @SlotId( -1 ) @Getter
    public native double getBias(  );

    @SlotId( -1 ) @Getter
    public native boolean getClamp(  );

    @SlotId( -1 ) @Setter
    public native void setDivisor( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getDivisor(  );

}
