package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Socket extends flash.events.EventDispatcher implements flash.utils.IDataOutput, flash.utils.IDataInput   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Socket( String arg1, int arg2 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Socket( String arg1 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Socket(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void writeUTFBytes( String arg1 );

    @SlotId( -1 ) 
    public native void flush(  );

    @SlotId( -1 ) @Getter
    public native boolean getConnected(  );

    @SlotId( -1 ) 
    public native void writeByte( int arg1 );

    @SlotId( -1 ) 
    public native void writeInt( int arg1 );

    @SlotId( -1 ) 
    public native int readShort(  );

    @SlotId( -1 ) 
    public native int readUnsignedShort(  );

    @SlotId( -1 ) @Getter
    public native String getEndian(  );

    @SlotId( -1 ) @Setter
    public native void setObjectEncoding( int arg1 );

    @SlotId( -1 ) 
    public native void writeDouble( double arg1 );

    @SlotId( -1 ) 
    public native Object readObject(  );

    @SlotId( -1 ) @Getter
    public native int getBytesAvailable(  );

    @SlotId( -1 ) 
    public native double readDouble(  );

    @SlotId( -1 ) 
    public native String readUTF(  );

    @SlotId( -1 ) 
    public native boolean readBoolean(  );

    @SlotId( -1 ) 
    public native String readUTFBytes( int arg1 );

    @SlotId( -1 ) 
    public native void writeMultiByte( String arg1, String arg2 );

    @SlotId( -1 ) 
    public native void writeFloat( double arg1 );

    @SlotId( -1 ) 
    public native int readByte(  );

    @SlotId( -1 ) 
    public native void writeUTF( String arg1 );

    @SlotId( -1 ) 
    public native void writeBoolean( boolean arg1 );

    @SlotId( -1 ) 
    public native int readUnsignedInt(  );

    @SlotId( -1 ) 
    public native void writeBytes( flash.utils.ByteArray arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public native void writeBytes( flash.utils.ByteArray arg1, int arg2 );

    @SlotId( -1 ) 
    public native void writeBytes( flash.utils.ByteArray arg1 );

    @SlotId( -1 ) @Setter
    public native void setEndian( String arg1 );

    @SlotId( -1 ) 
    public native int readUnsignedByte(  );

    @SlotId( -1 ) 
    public native void writeUnsignedInt( int arg1 );

    @SlotId( -1 ) 
    public native void writeShort( int arg1 );

    @SlotId( -1 ) @Getter
    public native int getObjectEncoding(  );

    @SlotId( -1 ) 
    public native double readFloat(  );

    @SlotId( -1 ) 
    public native void connect( String arg1, int arg2 );

    @SlotId( -1 ) 
    public native String readMultiByte( int arg1, String arg2 );

    @SlotId( -1 ) 
    public native void close(  );

    @SlotId( -1 ) 
    public native int readInt(  );

    @SlotId( -1 ) 
    public native void readBytes( flash.utils.ByteArray arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public native void readBytes( flash.utils.ByteArray arg1, int arg2 );

    @SlotId( -1 ) 
    public native void readBytes( flash.utils.ByteArray arg1 );

    @SlotId( -1 ) 
    public native void writeObject( Object arg1 );

}
