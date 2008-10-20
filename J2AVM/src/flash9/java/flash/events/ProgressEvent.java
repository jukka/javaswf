package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class ProgressEvent extends flash.events.Event   {

    @SlotId( 0 )
    public static final String PROGRESS = "progress";	

    @SlotId( 1 )
    public static final String SOCKET_DATA = "socketData";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ProgressEvent( String arg1, boolean arg2, boolean arg3, int arg4, int arg5 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ProgressEvent( String arg1, boolean arg2, boolean arg3, int arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ProgressEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ProgressEvent( String arg1, boolean arg2 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ProgressEvent( String arg1 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Getter
    public native int getBytesLoaded(  );

    @SlotId( -1 ) @Getter
    public native int getBytesTotal(  );

    @SlotId( -1 ) @Setter
    public native void setBytesLoaded( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setBytesTotal( int arg1 );

}
