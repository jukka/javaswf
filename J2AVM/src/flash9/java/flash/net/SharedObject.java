package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class SharedObject extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SharedObject(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native String flush( int arg1 );

    @SlotId( -1 ) 
    public native String flush(  );

    @SlotId( -1 ) @Getter
    public native int getSize(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getClient(  );

    @SlotId( -1 ) 
    public native void setDirty( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setFps( double arg1 );

    @SlotId( -1 ) 
    public native void clear(  );

    @SlotId( -1 ) @Setter
    public native void setClient( flash.FlashObject arg1 );

    @SlotId( -1 ) 
    public native void send(  );

    @SlotId( -1 ) 
    public native void setProperty( String arg1, flash.FlashObject arg2 );

    @SlotId( -1 ) 
    public native void setProperty( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setObjectEncoding( int arg1 );

    @SlotId( -1 ) 
    public native void connect( flash.net.NetConnection arg1, String arg2 );

    @SlotId( -1 ) 
    public native void connect( flash.net.NetConnection arg1 );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getData(  );

    @SlotId( -1 ) @Getter
    public native int getObjectEncoding(  );

    @SlotId( -1 ) 
    public native void close(  );

    @SlotId( 7 ) @Setter
    public static final native void setDefaultObjectEncoding( int arg1 );

    @SlotId( 3 ) 
    public static final native int getDiskUsage( String arg1 );

    @SlotId( 6 ) @Getter
    public static final native int getDefaultObjectEncoding(  );

    @SlotId( 4 ) 
    public static final native flash.net.SharedObject getLocal( String arg1, String arg2, boolean arg3 );

    @SlotId( 4 ) 
    public static final native flash.net.SharedObject getLocal( String arg1, String arg2 );

    @SlotId( 4 ) 
    public static final native flash.net.SharedObject getLocal( String arg1 );

    @SlotId( 2 ) 
    public static final native int deleteAll( String arg1 );

    @SlotId( 5 ) 
    public static final native flash.net.SharedObject getRemote( String arg1, String arg2, flash.FlashObject arg3, boolean arg4 );

    @SlotId( 5 ) 
    public static final native flash.net.SharedObject getRemote( String arg1, String arg2, flash.FlashObject arg3 );

    @SlotId( 5 ) 
    public static final native flash.net.SharedObject getRemote( String arg1, String arg2 );

    @SlotId( 5 ) 
    public static final native flash.net.SharedObject getRemote( String arg1 );

}
