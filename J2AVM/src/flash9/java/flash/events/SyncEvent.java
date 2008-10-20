package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class SyncEvent extends flash.events.Event   {

    @SlotId( 0 )
    public static final String SYNC = "sync";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SyncEvent( String arg1, boolean arg2, boolean arg3, flash.FlashArray arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SyncEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SyncEvent( String arg1, boolean arg2 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SyncEvent( String arg1 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Getter
    public native flash.FlashArray getChangeList(  );

    @SlotId( -1 ) @Setter
    public native void setChangeList( flash.FlashArray arg1 );

}
