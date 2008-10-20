package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class NetConnection extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public NetConnection(  ) { 
		super( null );
	}




    @SlotId( -1 ) @Getter
    public native String getProxyType(  );

    @SlotId( -1 ) @Getter
    public native boolean getConnected(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getClient(  );

    @SlotId( -1 ) 
    public native void addHeader( String arg1, boolean arg2, flash.FlashObject arg3 );

    @SlotId( -1 ) 
    public native void addHeader( String arg1, boolean arg2 );

    @SlotId( -1 ) 
    public native void addHeader( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setProxyType( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setClient( flash.FlashObject arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getUsingTLS(  );

    @SlotId( -1 ) 
    public native void call( String arg1, flash.net.Responder arg2 );

    @SlotId( -1 ) @Setter
    public native void setObjectEncoding( int arg1 );

    @SlotId( -1 ) 
    public native void connect( String arg1 );

    @SlotId( -1 ) @Getter
    public native String getUri(  );

    @SlotId( -1 ) @Getter
    public native int getObjectEncoding(  );

    @SlotId( -1 ) 
    public native void close(  );

    @SlotId( -1 ) @Getter
    public native String getConnectedProxyType(  );

    @SlotId( 3 ) @Setter
    public static final native void setDefaultObjectEncoding( int arg1 );

    @SlotId( 2 ) @Getter
    public static final native int getDefaultObjectEncoding(  );

}
