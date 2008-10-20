package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class AsyncErrorEvent extends flash.events.ErrorEvent   {

    @SlotId( -1 )
    public flash.FlashError error;	

    @SlotId( 0 )
    public static final String ASYNC_ERROR = "asyncError";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public AsyncErrorEvent( String arg1, boolean arg2, boolean arg3, String arg4, flash.FlashError arg5 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public AsyncErrorEvent( String arg1, boolean arg2, boolean arg3, String arg4 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public AsyncErrorEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public AsyncErrorEvent( String arg1, boolean arg2 ) { 
		super( null, false, false, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public AsyncErrorEvent( String arg1 ) { 
		super( null, false, false, null );
	}




}
