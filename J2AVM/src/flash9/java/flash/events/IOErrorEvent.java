package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class IOErrorEvent extends flash.events.ErrorEvent   {

    @SlotId( 2 )
    public static final String DISK_ERROR = "diskError";	

    @SlotId( 1 )
    public static final String NETWORK_ERROR = "networkError";	

    @SlotId( 3 )
    public static final String VERIFY_ERROR = "verifyError";	

    @SlotId( 0 )
    public static final String IO_ERROR = "ioError";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public IOErrorEvent( String arg1, boolean arg2, boolean arg3, String arg4 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public IOErrorEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public IOErrorEvent( String arg1, boolean arg2 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public IOErrorEvent( String arg1 ) { 
		super( null, false, false, null );
	}




}
