package flash.accessibility;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class AccessibilityImplementation extends flash.FlashObject   {

    @SlotId( -1 )
    public int errno;	

    @SlotId( -1 )
    public boolean stub;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public AccessibilityImplementation(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native String get_accValue( int arg1 );

    @SlotId( -1 ) 
    public native int get_accFocus(  );

    @SlotId( -1 ) 
    public native String get_accName( int arg1 );

    @SlotId( -1 ) 
    public native void accDoDefaultAction( int arg1 );

    @SlotId( -1 ) 
    public native int get_accRole( int arg1 );

    @SlotId( -1 ) 
    public native flash.FlashArray getChildIDArray(  );

    @SlotId( -1 ) 
    public native flash.FlashArray get_accSelection(  );

    @SlotId( -1 ) 
    public native Object accLocation( int arg1 );

    @SlotId( -1 ) 
    public native boolean isLabeledBy( flash.geom.Rectangle arg1 );

    @SlotId( -1 ) 
    public native int get_accState( int arg1 );

    @SlotId( -1 ) 
    public native void accSelect( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native String get_accDefaultAction( int arg1 );

}
