package flash.utils;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class ObjectOutput extends flash.FlashObject implements flash.utils.IDataOutput   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ObjectOutput(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native void writeMultiByte( String arg1, String arg2 );

    @SlotId( -1 ) 
    public native void writeUTFBytes( String arg1 );

    @SlotId( -1 ) 
    public native void writeUTF( String arg1 );

    @SlotId( -1 ) 
    public native void writeBytes( flash.utils.ByteArray arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public native void writeBytes( flash.utils.ByteArray arg1, int arg2 );

    @SlotId( -1 ) 
    public native void writeBytes( flash.utils.ByteArray arg1 );

    @SlotId( -1 ) 
    public native void writeDouble( double arg1 );

    @SlotId( -1 ) 
    public native void writeInt( int arg1 );

    @SlotId( -1 ) 
    public native void writeBoolean( boolean arg1 );

    @SlotId( -1 ) 
    public native void writeShort( int arg1 );

    @SlotId( -1 ) 
    public native void writeUnsignedInt( int arg1 );

    @SlotId( -1 ) 
    public native void writeByte( int arg1 );

    @SlotId( -1 ) @Getter
    public native String getEndian(  );

    @SlotId( -1 ) @Setter
    public native void setObjectEncoding( int arg1 );

    @SlotId( -1 ) @Getter
    public native int getObjectEncoding(  );

    @SlotId( -1 ) @Setter
    public native void setEndian( String arg1 );

    @SlotId( -1 ) 
    public native void writeFloat( double arg1 );

    @SlotId( -1 ) 
    public native void writeObject( Object arg1 );

}
