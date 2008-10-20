package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class XMLSocket extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLSocket( String arg1, int arg2 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLSocket( String arg1 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLSocket(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void send( Object arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getConnected(  );

    @SlotId( -1 ) 
    public native void connect( String arg1, int arg2 );

    @SlotId( -1 ) 
    public native void close(  );

}
