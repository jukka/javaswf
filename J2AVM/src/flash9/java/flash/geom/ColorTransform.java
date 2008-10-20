package flash.geom;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class ColorTransform extends flash.FlashObject   {

    @SlotId( -1 )
    public double redOffset;	

    @SlotId( -1 )
    public double greenMultiplier;	

    @SlotId( -1 )
    public double blueOffset;	

    @SlotId( -1 )
    public double alphaOffset;	

    @SlotId( -1 )
    public double redMultiplier;	

    @SlotId( -1 )
    public double blueMultiplier;	

    @SlotId( -1 )
    public double greenOffset;	

    @SlotId( -1 )
    public double alphaMultiplier;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1, double arg2, double arg3, double arg4, double arg5, double arg6 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1, double arg2, double arg3, double arg4, double arg5 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1, double arg2, double arg3, double arg4 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1, double arg2, double arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1, double arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform( double arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ColorTransform(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native int getColor(  );

    @SlotId( -1 ) @Setter
    public native void setColor( int arg1 );

    @SlotId( -1 ) 
    public native void concat( flash.geom.ColorTransform arg1 );

}
