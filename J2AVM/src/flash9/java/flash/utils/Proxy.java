package flash.utils;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Proxy extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Proxy(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native boolean deleteProperty( Object arg1 );

    @SlotId( -1 ) 
    public native Object nextValue( int arg1 );

    @SlotId( -1 ) 
    public native Object getDescendants( Object arg1 );

    @SlotId( -1 ) 
    public native Object getProperty( Object arg1 );

    @SlotId( -1 ) 
    public native boolean hasProperty( Object arg1 );

    @SlotId( -1 ) 
    public native boolean isAttribute( Object arg1 );

    @SlotId( -1 ) 
    public native Object callProperty( Object arg1 );

    @SlotId( -1 ) 
    public native int nextNameIndex( int arg1 );

    @SlotId( -1 ) 
    public native void setProperty( Object arg1, Object arg2 );

    @SlotId( -1 ) 
    public native String nextName( int arg1 );

}
