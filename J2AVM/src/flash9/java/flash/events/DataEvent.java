package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class DataEvent extends flash.events.TextEvent   {

    @SlotId( 0 )
    public static final String DATA = "data";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public DataEvent( String arg1, boolean arg2, boolean arg3, String arg4 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public DataEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public DataEvent( String arg1, boolean arg2 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public DataEvent( String arg1 ) { 
		super( null, false, false, null );
	}




    @SlotId( -1 ) @Getter
    public native String getData(  );

    @SlotId( -1 ) @Setter
    public native void setData( String arg1 );

}
