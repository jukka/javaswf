package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class StatusEvent extends flash.events.Event   {

    @SlotId( 0 )
    public static final String STATUS = "status";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public StatusEvent( String arg1, boolean arg2, boolean arg3, String arg4, String arg5 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public StatusEvent( String arg1, boolean arg2, boolean arg3, String arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public StatusEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public StatusEvent( String arg1, boolean arg2 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public StatusEvent( String arg1 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Getter
    public native String getLevel(  );

    @SlotId( -1 ) @Setter
    public native void setLevel( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setCode( String arg1 );

    @SlotId( -1 ) @Getter
    public native String getCode(  );

}
