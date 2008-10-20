package flash.geom;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Rectangle extends flash.FlashObject   {

    @SlotId( -1 )
    public double width;	

    @SlotId( -1 )
    public double height;	

    @SlotId( -1 )
    public double y;	

    @SlotId( -1 )
    public double x;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Rectangle( double arg1, double arg2, double arg3, double arg4 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Rectangle( double arg1, double arg2, double arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Rectangle( double arg1, double arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Rectangle( double arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Rectangle(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native boolean containsPoint( flash.geom.Point arg1 );

    @SlotId( -1 ) @Setter
    public native void setSize( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native boolean isEmpty(  );

    @SlotId( -1 ) @Getter
    public native double getLeft(  );

    @SlotId( -1 ) @Getter
    public native flash.geom.Point getSize(  );

    @SlotId( -1 ) 
    public native void inflatePoint( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native void setEmpty(  );

    @SlotId( -1 ) @Setter
    public native void setRight( double arg1 );

    @SlotId( -1 ) 
    public native void offset( double arg1, double arg2 );

    @SlotId( -1 ) @Getter
    public native double getTop(  );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle intersection( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) @Getter
    public native double getRight(  );

    @SlotId( -1 ) @Setter
    public native void setLeft( double arg1 );

    @SlotId( -1 ) 
    public native flash.geom.Rectangle union( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) 
    public native void inflate( double arg1, double arg2 );

    @SlotId( -1 ) @Setter
    public native void setBottomRight( flash.geom.Point arg1 );

    @SlotId( -1 ) 
    public native boolean containsRect( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) @Setter
    public native void setBottom( double arg1 );

    @SlotId( -1 ) 
    public native boolean contains( double arg1, double arg2 );

    @SlotId( -1 ) @Setter
    public native void setTop( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getBottom(  );

    @SlotId( -1 ) @Getter
    public native flash.geom.Point getBottomRight(  );

    @SlotId( -1 ) 
    public native boolean intersects( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) @Setter
    public native void setTopLeft( flash.geom.Point arg1 );

    @SlotId( -1 ) @Getter
    public native flash.geom.Point getTopLeft(  );

    @SlotId( -1 ) 
    public native void offsetPoint( flash.geom.Point arg1 );

}
