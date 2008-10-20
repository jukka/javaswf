/****************************************************************
 * Copyright (c) 2001, David N. Main, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the 
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer. 
 * 
 * 2. Redistributions in binary form must reproduce the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * 3. The name of the author may not be used to endorse or 
 * promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************/
package org.epistem.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Output Stream Wrapper
 */
public class OutStream extends OutputStreamWrapper
{
	//--Bit buffer..
	private int mBitBuf;
	private int mBitPos;
    
	public OutStream( OutputStream out )
	{
		super( out );
        
		initBits();
	}

	protected OutStream() {
		super(null);
	}

	/**
	 * Write a signed value to the output stream in the given number of bits.
	 * The value must actually fit in that number of bits or it will be garbled
	 */
	public void writeSBits( int numBits, int value ) throws IOException 
	{
		//--Mask out any sign bit
		long lval = value & 0x7FFFFFFF;
        
		if( value < 0 ) //add the sign bit
		{
			lval |= 1L << (numBits-1);
		}
        
		//--Write the bits as if unsigned
		writeUBits( numBits, lval );
	}
    
	public void flush() throws IOException
	{
		flushBits();
		super.flush();
	}
    
	/**
	 * Compress all subsequent data
	 */
	public void writeCompressed() {
		setOutputStream( new DeflaterOutputStream( getOutputStream(), 
						 new Deflater( Deflater.BEST_COMPRESSION )));
	}
    
	/**
	 * Flush the bit buffer to the output stream and reset values
	 */
	public void flushBits() throws IOException
	{
		if( mBitPos == 0 ) return;  //nothing to flush
        
		super.write( mBitBuf );
		mBitBuf = 0;
		mBitPos = 0;
	}
    
	/**
	 * Write an unsigned value to the output stream in the given number of bits
	 */
	public void writeUBits( int numBits, long value ) throws IOException 
	{   
		if( numBits == 0 ) return;
        
		if( mBitPos == 0 ) mBitPos = 8;  //bitBuf was empty
        
		int bitNum = numBits;
        
		while( bitNum > 0 )  //write all bits
		{
			while( mBitPos > 0 && bitNum > 0 ) //write into all position of the bit buffer
			{
				if( getBit( bitNum, value ) ) mBitBuf = setBit( mBitPos, mBitBuf );
                
				bitNum--;
				mBitPos--;
			}
            
			if( mBitPos == 0 ) //bit buffer is full - write it
			{
				writeUI8( mBitBuf );
				mBitBuf = 0;
				if( bitNum > 0 ) mBitPos = 8; //prepare for more bits
			}
		}
	}
        
    
	/**
	 * Get the given bit (where lowest bit is numbered 1)
	 */
	public static boolean getBit( int bitNum, long value )
	{
		return (value & (1L << (bitNum - 1))) != 0;
	}
    
	/**
	 * Set the given bit (where lowest bit is numbered 1)
	 */    
	public static int setBit( int bitNum, int value )
	{
		return value | ( 1 << (bitNum-1) );
	}    
    
	/**
	 * Write the given bytes to the output stream
	 */
	public void write( byte[] bytes ) throws IOException
	{
		flushBits();
        
		if( bytes != null && bytes.length > 0 )
		{
			super.write( bytes );
		}
	}

	/**
	 * Write the given bytes to the output stream
	 */
	public void write( byte[] bytes, int start, int length ) throws IOException
	{
		flushBits();
        
		if( bytes != null && length > 0 )
		{
			super.write( bytes, start, length );
		}
	}    
    
	/**
	 * Write an 8 bit unsigned value to the out stream
	 */
	public void writeUI8( int value ) throws IOException
	{
		flushBits();
        
		super.write( value  );
	}  
    
	/**
	 * Write a 16 bit unsigned value to the out stream
	 */
	public void writeUI16( int value ) throws IOException
	{
		flushBits();
        
		super.write( value & 0xff );
		super.write( value >> 8   );
	}

	/**
	 * Write a 16 bit signed value to the out stream
	 */
	public void writeSI16( short value ) throws IOException
	{
		flushBits();
        
		super.write( value & 0xff );
		super.write( value >> 8 );
	}
    
    
	/**
	 * Write a 32 bit unsigned value to the out stream
	 */
	public void writeUI32( long value ) throws IOException
	{
		flushBits();
        
		super.write( (int)( value & 0xff ) );
		super.write( (int)( value >>  8  ) );
		super.write( (int)( value >> 16  ) );
		super.write( (int)( value >> 24  ) );
	}  
    
    /**
     * Write a variable-byte-length unsigned integer (per AVM2 ABC spec)
     */
    public void writeVU30( int value ) throws IOException {
        long lval = value & 0x3fffffff;
        writeVU32( lval );
    }
    
    /**
     * Write a variable-byte-length unsigned integer (per AVM2 ABC spec)
     */
    public void writeVU32( long value ) throws IOException {
        for (int i = 0; i < 6; i++) {
            int bite = (int)( value & 0x7f );            
            value >>>= 7;
            if( value != 0 ) bite |= 0x80; //set hi-bit if further byte required
            writeUI8( bite );
            if( bite < 0x80 ) break;  
        }
    }

    /**
     * Determine the number of bytes needed to encode the given value in VU
     * format.  The VU format encodes in the bottom 7 bits of each byte and
     * uses the high bit to indicate that a further byte is needed.
     * 
     * @param value MUST BE POSITIVE
     */
    public static int sizeOfVU( long value ) {
        if( value < 0 ) return 0;
        
        int count = 1;
        while( true ) {
            value >>>= 7;
            if( value == 0 ) break; 
            count++;  
        }
        return count;
    }
    
    /**
     * Write a variable-byte-length signed 24 bit integer.
     */
    public void writeVS24( int value ) throws IOException {
        long lval = value & 0x00ffffff;
        writeVU32( lval );
    }

    /**
     * Write a variable-byte-length signed 32 bit integer.
     */
    public void writeVS32( int value ) throws IOException {
        long lval = value;
        lval &= 0xffffffffL;
        writeVU32( lval );
    }

    /**
     * Write a 3-byte signed integer value
     */
    public void writeSI24( int value ) throws IOException {
        flushBits();
        
        super.write( value & 0xff );
        super.write( (value >> 8 ) & 0xff );        
        super.write( (value >> 16) & 0xff );        
    }
    
	/**
	 * Write a string to the output stream using the default encoding and add terminating null
	 * @return the byte size of the written string (including the null terminator). 
	 */
	public int writeString( String s, String encoding ) throws IOException 
	{
		if( s == null ) s = "";
		byte[] bytes = s.getBytes( encoding );
		writeString( bytes );
		return bytes.length + 1;
	}
    
	/**
	 * Write a string to the output stream and add terminating null
	 */
	public void writeString( byte[] string ) throws IOException 
	{
		flushBits();
        
		if( string != null ) super.write( string );
		super.write( 0 );  //terminate string
	}   
    
    /**
     * Write a UTF-8 string with a VU30 length prefix.
     */
    public void writeVU30String( String s ) throws IOException {
        byte[] bytes = s.getBytes( "UTF-8" );
        writeVU30( bytes.length );
        write( bytes );
    }
    
	/**
	 * Calculate the byte length of a string as it would be written to the
	 * output stream
	 */
	public static int getStringLength( byte[] string )
	{
		if( string == null ) return 1;
		return string.length + 1; //to include the terminating null
	}

	/**
	 * Calculate the byte length of a string as it would be written to the
	 * output stream using the default character encoding
	 */
	public static int getStringLength( String string )
	{
		if( string == null ) return 1;
		byte[] bytes = string.getBytes();
        
		return bytes.length + 1; //to include the terminating null
	}    
    
	/**
	 * Reset the bit buffer
	 */
	private void initBits()
	{
		mBitBuf = 0;
		mBitPos = 0;
	}
    
	/**
	 * Determine the minimum number of bits required to hold the given
	 * signed value
	 */
	public static int determineSignedBitSize( int value )
	{
		if( value >= 0 ) return determineUnsignedBitSize( value ) + 1;
        
		//--This is probably a really bad way of doing this...
		int  topBit = 31;
		long mask = 0x40000000L;
        
		while( topBit > 0 )
		{
			if( (value & mask) == 0 ) break;
            
			mask >>= 1;
			topBit--;
		}
        
		if( topBit == 0 ) return 2;  //must have been -1
        
		//HACK: Flash represents -16 as 110000 rather than 10000 etc..
		int val2 = value & (( 1 << topBit) - 1 );
		if( val2 == 0 )
		{
			topBit++;
		}
        
		return topBit + 1;
	}

	/**
	 * Determine the minimum number of bits required to hold the given
	 * unsigned value (may be zero)
	 */
	public static int determineUnsignedBitSize( long value )
	{
		//--This is probably a really bad way of doing this...
		int  topBit = 32;
		long mask = 0x80000000L;
        
		while( topBit > 0 )
		{
			if( (value & mask) != 0 ) return topBit;
            
			mask >>= 1;
			topBit--;
		}
        
		return 0;
	}
    
	/**
	 * Write a float value
	 */
	public void writeFloat( float value ) throws IOException
	{
		writeSI32( Float.floatToIntBits( value ) );
	}
    
	/**
	 * Write a double value
	 */
	public void writeDouble( double value ) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream( baos );
        
		dout.writeDouble( value );
        
		dout.flush();
        
		byte[] bytes = baos.toByteArray();
		byte[] bytes2 = new byte[8];
        
		bytes2[0] = bytes[3];
		bytes2[1] = bytes[2];
		bytes2[2] = bytes[1];
		bytes2[3] = bytes[0];
		bytes2[4] = bytes[7];
		bytes2[5] = bytes[6];
		bytes2[6] = bytes[5];
		bytes2[7] = bytes[4];        
        
		write( bytes2 );
	}

    /**
     * Write a little-endian double value
     */
    public void writeDoubleLE( double value ) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream( baos );
        
        dout.writeDouble( value );
        
        dout.flush();
        
        byte[] bytes = baos.toByteArray();
        byte[] bytes2 = new byte[8];
        
        bytes2[0] = bytes[7];
        bytes2[1] = bytes[6];
        bytes2[2] = bytes[5];
        bytes2[3] = bytes[4];
        bytes2[4] = bytes[3];
        bytes2[5] = bytes[2];
        bytes2[6] = bytes[1];
        bytes2[7] = bytes[0];        
        
        write( bytes2 );
    }

    
	/**
	 * Write a 32 bit signed value
	 */
	public void writeSI32( int value ) throws IOException 
	{
		flushBits();
        
		super.write( value & 0xff );
		super.write( value >> 8 );
		super.write( value >> 16 );
		super.write( value >> 24 );
	}
    
	/**
	 * Util to convert a signed int to 2 bytes
	 */
	public static byte[] sintTo2Bytes( int value )
	{
		return new byte[]
		{
			uintToByte( value & 0xff ),
			uintToByte( value >> 8   )
		};
	}
    
	/**
	 * Util to convert an unsigned int to 2 bytes
	 */
	public static byte[] uintTo2Bytes( int value )
	{
		return new byte[]
		{
			uintToByte( value & 0xff ),
			uintToByte( value >> 8   )
		};
	}

	/**
	 * Util to convert an unsigned int to 4 bytes
	 */
	public static byte[] uintTo4Bytes( int value )
	{
		return new byte[]
		{
			uintToByte( value & 0xff ),
			uintToByte( value >> 8   ),
			uintToByte( value >> 16  ),
			uintToByte( value >> 24  )
		};
	}    
    
	/**
	 * Util to convert an unsigned int to an unsigned byte
	 */
	public static byte uintToByte( int value )
	{
		int lowbit = value & 1;
		value >>= 1;
        
		byte b = (byte)value;
		b <<= 1;
		b |= (byte)lowbit;
        
		return b;
	}  
    
	/**
	 * @see java.io.OutputStream#write(int)
	 */
	public void write( int b ) throws IOException {
		flushBits();
		super.write( b );
	}
	
	/**
	 * Override to ensure that bits are flushed
	 */
	public void close() throws IOException {
		flush();
		super.close();
	}

}
