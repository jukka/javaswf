package flash.geom;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Matrix extends flash.FlashObject   {

    @SlotId( -1 )
    public double ty;	

    @SlotId( -1 )
    public double tx;	

    @SlotId( -1 )
    public double a;	

    @SlotId( -1 )
    public double b;	

    @SlotId( -1 )
    public double c;	

    @SlotId( -1 )
    public double d;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Matrix( double arg1, double arg2, double arg3, double arg4, double arg5, double arg6 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Matrix( double arg1, double arg2, double arg3, double arg4, double arg5 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Matrix( double arg1, double arg2, double arg3, double arg4 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Matrix( double arg1, double arg2, double arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Matrix( double arg1, double arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Matrix( double arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Matrix(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native void translate( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native void scale( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native flash.geom.Point transformPoint( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native void concat( flash.geom.Matrix arg1 );

    @SlotId( -1 ) 
    public native void invert(  );

    @SlotId( -1 ) 
    public native void rotate( double arg1 );

    @SlotId( -1 ) 
    public native flash.geom.Point deltaTransformPoint( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native void createGradientBox( double arg1, double arg2, double arg3, double arg4, double arg5 );

    @SlotId( -1 ) 
    public native void createGradientBox( double arg1, double arg2, double arg3, double arg4 );

    @SlotId( -1 ) 
    public native void createGradientBox( double arg1, double arg2, double arg3 );

    @SlotId( -1 ) 
    public native void createGradientBox( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native void createBox( double arg1, double arg2, double arg3, double arg4, double arg5 );

    @SlotId( -1 ) 
    public native void createBox( double arg1, double arg2, double arg3, double arg4 );

    @SlotId( -1 ) 
    public native void createBox( double arg1, double arg2, double arg3 );

    @SlotId( -1 ) 
    public native void createBox( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native void identity(  );

}
