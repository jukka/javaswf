package flash.geom;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Point extends flash.FlashObject   {

    @SlotId( -1 )
    public double x;	

    @SlotId( -1 )
    public double y;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Point( double arg1, double arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Point( double arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Point(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native double getLength(  );

    @SlotId( -1 ) 
    public native flash.geom.Point add( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native void normalize( double arg1 );

    @SlotId( -1 ) 
    public native flash.geom.Point subtract( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native void offset( double arg1, double arg2 );

    @SlotId( 2 ) 
    public static final native flash.geom.Point interpolate( flash.geom.Point arg1, flash.geom.Point arg2, double arg3 );

    @SlotId( 3 ) 
    public static final native double distance( flash.geom.Point arg1, flash.geom.Point arg2 );

    @SlotId( 4 ) 
    public static final native flash.geom.Point polar( double arg1, double arg2 );

}
