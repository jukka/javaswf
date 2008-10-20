package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class HTTPStatusEvent extends flash.events.Event   {

    @SlotId( 0 )
    public static final String HTTP_STATUS = "httpStatus";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public HTTPStatusEvent( String arg1, boolean arg2, boolean arg3, int arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public HTTPStatusEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public HTTPStatusEvent( String arg1, boolean arg2 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public HTTPStatusEvent( String arg1 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Getter
    public native int getStatus(  );

}
