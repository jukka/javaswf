package flash.utils;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public interface IDataOutput   {



    @SlotId( -1 ) 
    public void writeMultiByte( String arg1, String arg2 );

    @SlotId( -1 ) 
    public void writeUTFBytes( String arg1 );

    @SlotId( -1 ) 
    public void writeShort( int arg1 );

    @SlotId( -1 ) 
    public void writeByte( int arg1 );

    @SlotId( -1 ) 
    public void writeUTF( String arg1 );

    @SlotId( -1 ) 
    public void writeBoolean( boolean arg1 );

    @SlotId( -1 ) @Getter
    public String getEndian(  );

    @SlotId( -1 ) @Setter
    public void setObjectEncoding( int arg1 );

    @SlotId( -1 ) 
    public void writeBytes( flash.utils.ByteArray arg1, int arg2, int arg3 );

    @SlotId( -1 ) 
    public void writeBytes( flash.utils.ByteArray arg1, int arg2 );

    @SlotId( -1 ) 
    public void writeBytes( flash.utils.ByteArray arg1 );

    @SlotId( -1 ) 
    public void writeInt( int arg1 );

    @SlotId( -1 ) @Getter
    public int getObjectEncoding(  );

    @SlotId( -1 ) @Setter
    public void setEndian( String arg1 );

    @SlotId( -1 ) 
    public void writeDouble( double arg1 );

    @SlotId( -1 ) 
    public void writeUnsignedInt( int arg1 );

    @SlotId( -1 ) 
    public void writeFloat( double arg1 );

    @SlotId( -1 ) 
    public void writeObject( Object arg1 );

}
