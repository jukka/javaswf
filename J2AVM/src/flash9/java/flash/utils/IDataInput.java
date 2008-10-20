package flash.utils;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public interface IDataInput   {



    @SlotId( -1 ) 
    public int readUnsignedInt(  );

    @SlotId( -1 ) @Getter
    public int getBytesAvailable(  );

    @SlotId( -1 ) 
    public int readShort(  );

    @SlotId( -1 ) @Getter
    public int getObjectEncoding(  );

    @SlotId( -1 ) 
    public String readMultiByte( int arg1, String arg2 );

    @SlotId( -1 ) 
    public double readFloat(  );

    @SlotId( -1 ) 
    public double readDouble(  );

    @SlotId( -1 ) 
    public int readUnsignedShort(  );

    @SlotId( -1 ) 
    public boolean readBoolean(  );

    @SlotId( -1 ) 
    public int readUnsignedByte(  );

    @SlotId( -1 ) 
    public void readBytes( flash.utils.ByteArray arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public void readBytes( flash.utils.ByteArray arg1, int arg2 );

    @SlotId( -1 ) 
    public void readBytes( flash.utils.ByteArray arg1 );

    @SlotId( -1 ) @Setter
    public void setEndian( String arg1 );

    @SlotId( -1 ) 
    public String readUTF(  );

    @SlotId( -1 ) 
    public int readInt(  );

    @SlotId( -1 ) 
    public String readUTFBytes( int arg1 );

    @SlotId( -1 ) @Getter
    public String getEndian(  );

    @SlotId( -1 ) 
    public Object readObject(  );

    @SlotId( -1 ) @Setter
    public void setObjectEncoding( int arg1 );

    @SlotId( -1 ) 
    public int readByte(  );

}
