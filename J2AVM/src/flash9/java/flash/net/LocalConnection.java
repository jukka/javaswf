package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class LocalConnection extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public LocalConnection(  ) { 
		super( null );
	}




    @SlotId( -1 ) @Getter
    public native String getDomain(  );

    @SlotId( -1 ) 
    public native void send( String arg1, String arg2 );

    @SlotId( -1 ) 
    public native void allowInsecureDomain(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getClient(  );

    @SlotId( -1 ) @Setter
    public native void setClient( flash.FlashObject arg1 );

    @SlotId( -1 ) 
    public native void connect( String arg1 );

    @SlotId( -1 ) 
    public native void allowDomain(  );

    @SlotId( -1 ) 
    public native void close(  );

}
