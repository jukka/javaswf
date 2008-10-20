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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Input Stream Wrapper
 */
public class InStream
{
	protected InputStream in;
	protected long bytesRead = 0L;
    
	private static final int MAX_TRANSFER_BUFFER_SIZE = 10000;

	//--Bit buffer..
	protected int bitBuf;
	protected int bitPos;
    
	public InStream( InputStream in )
	{
		this.in = in;
        
		synchBits();        
	}
            
	public InStream( byte[] bytes )
	{
		this( new ByteArrayInputStream( bytes ) );
	}
    
	/**
	 * Start reading compressed data - all further input is
	 * assumed to come from a zip compressed stream.
	 */
	public void readCompressed() 
	{
		in = new InflaterInputStream( in );
	}
    
	/**
	 * Transfer a number of bytes to an output stream
	 * 
	 * @param length the number of bytes to transfer
	 */
	public void transfer( OutputStream out, int length )
		throws IOException 
	{
		int buffSize = (length < MAX_TRANSFER_BUFFER_SIZE) ?
							length : MAX_TRANSFER_BUFFER_SIZE;
    						
		byte[] buffer = new byte[ buffSize ];
    	
		int read;
    	
		while( (read = in.read( buffer )) > 0 ) {
			out.write( buffer, 0, read );
		}
	}
    
	/**
	 * Read a string from the input stream
	 */
	public byte[] readStringBytes() throws IOException {
		synchBits();
        
		ByteArrayOutputStream chars = new ByteArrayOutputStream();
		byte[] aChar = new byte[1];
		while( in.read( aChar ) == 1 ) {
			bytesRead++;
            
			if( aChar[0] == 0 ) { //string terminator
				return chars.toByteArray();
			}
            
			chars.write( aChar );
		}
        
		throw new EOFException( "Unterminated string - reached end of input before null char" );
	}

    /**
     * Read a UTF-8 string prefixed by its length as a VU30
     */
    public String readVU30String() throws IOException {
        int len = readVU30();
        byte[] chars = read( len );
        return  new String( chars, "UTF-8" );
    }
    
	/**
	 * Read a null terminated string using the given character encoding
	 */
	public String readString( String encoding ) throws IOException 
	{
		return new String( readStringBytes(), encoding );
	}
    
	/**
	 * Read all remaining bytes from the stream
	 */
	public byte[] read() throws IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
		int b = 0;
		while( ( b = in.read()) >= 0 )
		{
			bout.write( b );
		}
        
		return bout.toByteArray();
	}
    
	/**
	 * Read bytes from the input stream - throw up if all bytes are not available
	 */
	public byte[] read( int length ) throws IOException
	{
		byte[] data = new byte[ length ];
        
		if( length > 0 )
		{
			int read = 0;
            
			while( read < length )
			{
				int count = in.read( data, read, length-read );
				if( count < 0 )
				{
					bytesRead += read;
					throw new EOFException( "Unexpected end of input while reading a specified number of bytes" );
				}
                
				read += count;
			}
            
			bytesRead += read;
		}
        
		return data;
	}
    
	/**
	 * Read as many bytes as possible (up to the limit of the passed-in array)
	 * @return the number of bytes read
	 */
	public int read( byte[] bytes ) throws IOException
	{
		int length = bytes.length;
        
		if( length > 0 )
		{
			int read = 0;
            
			while( read < length )
			{
				int count = in.read( bytes, read, length-read );
				if( count < 0 )
				{
					bytesRead += read;
					return read;
				}
                
				read += count;
			}
            
			bytesRead += read;
			return read;
		}
        
		return 0;
	}    
    
	/**
	 * Reset the bit buffer
	 */
	public void synchBits()
	{
		bitBuf = 0;
		bitPos = 0;
	}
    
	public long getBytesRead() { return bytesRead; }
	public void setBytesRead( long read ) { bytesRead = read; }
    
	/**
	 * Skip a number of bytes from the input stream
	 */
	public void skipBytes( long length ) throws IOException 
	{
		long skipped = 0;
        
		while( skipped < length )
		{
			int val = in.read();
            
			if( val < 0 ) throw new EOFException( "Unexpected end of input" );
            
			skipped++;
		}
        
		bytesRead += length;
	}
    
	/**
	 * Read an unsigned value from the given number of bits
	 */
	public long readUBits( int numBits ) throws IOException
	{
		if( numBits == 0 ) return 0;
        
		int  bitsLeft = numBits;
		long result   = 0;

		if( bitPos == 0 ) //no value in the buffer - read a byte
		{
			bitBuf = in.read();
			bitPos = 8;
            
			bytesRead++;
		}
        
		while( true )
		{
			int shift = bitsLeft - bitPos;
			if( shift > 0 )
			{
				// Consume the entire buffer
				result |= bitBuf << shift;
				bitsLeft -= bitPos;

				// Get the next byte from the input stream
				bitBuf = in.read();
				bitPos = 8;
            
				bytesRead++;
			}
			else
			{
				// Consume a portion of the buffer
				result |= bitBuf >> -shift;
				bitPos -= bitsLeft;
				bitBuf &= 0xff >> (8 - bitPos);	// mask off the consumed bits

				return result;
			}
		}        
	}

	/**
	 * Read an unsigned 8 bit value
	 */
	public int readUI8() throws IOException
	{
		synchBits();
        
		int ui8 = in.read();
		if( ui8 < 0 ) throw new EOFException( "Unexpected end of input" );

		bytesRead++;
        
		return ui8;        
	}

    /**
     * Read a signed 8 bit value
     */
    public byte readSI8() throws IOException
    {
        synchBits();
        
        int ui8 = in.read();
        if( ui8 < 0 ) throw new EOFException( "Unexpected end of input" );

        bytesRead++;
        
        return (byte) ( (ui8 > 127) ? (ui8 - 256) : ui8);        
    }

	/**
	 * Read an unsigned 16 bit value
	 */
	public int readUI16() throws IOException
	{
		synchBits();
        
		int ui16 = in.read();
		if( ui16 < 0 ) {
		    throw new EOFException( "Unexpected end of input" );
		}
        
		int val = in.read();
		if( val < 0 ) {
		    throw new EOFException( "Unexpected end of input" );
		}

		ui16 += val << 8;

		bytesRead += 2;
        
		return ui16;
	}  
    
	/**
	 * Read a signed 16 bit value
	 */
	public short readSI16() throws IOException
	{
		synchBits();

		int lowerByte = in.read();       
		if( lowerByte < 0 ) throw new EOFException( "Unexpected end of input" );
        
		byte[] aByte = new byte[1];  
		int count = in.read( aByte );
		if( count < 1 ) throw new EOFException( "Unexpected end of input" );

		bytesRead += 2;
        
		return (short)((aByte[0]*256) + lowerByte);
	}  
    
	/**
	 * Read an unsigned 32 bit value
	 */
	public long readUI32() throws IOException
	{
		synchBits();
        
		long ui32 = in.read();
		if( ui32 < 0 ) throw new EOFException( "Unexpected end of input" );
        
		long val = in.read();
		if( val < 0 ) throw new EOFException( "Unexpected end of input" );

		ui32 += val << 8;
        
		val = in.read();
		if( val < 0 ) throw new EOFException( "Unexpected end of input" );

		ui32 += val << 16;
               
		val = in.read();
		if( val < 0 ) throw new EOFException( "Unexpected end of input" );
        
		ui32 += val << 24;

		bytesRead += 4;       
        
		return ui32;
	}  
    
	/**
	 * Read a signed value from the given number of bits
	 */
	public int readSBits( int numBits ) throws IOException
	{
		// Get the number as an unsigned value.
		long uBits = readUBits( numBits );

		// Is the number negative?
		if( ( uBits & (1L << (numBits - 1))) != 0 )
		{
			// Yes. Extend the sign.
			uBits |= -1L << numBits;
		}

		return (int)uBits;        
	}        

    
    /**
     * Read a 24 bit signed number
     */
    public int readSI24() throws IOException 
    {
        synchBits();

        int b0 = in.read();       
        if( b0 < 0 ) throw new EOFException( "Unexpected end of input" );

        int b1 = in.read();       
        if( b1 < 0 ) throw new EOFException( "Unexpected end of input" );

        int b2 = in.read();       
        if( b2 < 0 ) throw new EOFException( "Unexpected end of input" );
        
        bytesRead += 3;
        
        int v = (int)((b2*256*256)
                      + (b1*256)
                      + b0 );
        
        if( v > 0x007fffff ) {
            return v - 0x00ffffff - 1;
        }
        
        return v;
    }
    
	/**
	 * Read a 32 bit signed number
	 */
	public int readSI32() throws IOException 
	{
		synchBits();

		int b0 = in.read();       
		if( b0 < 0 ) throw new EOFException( "Unexpected end of input" );

		int b1 = in.read();       
		if( b1 < 0 ) throw new EOFException( "Unexpected end of input" );

		int b2 = in.read();       
		if( b2 < 0 ) throw new EOFException( "Unexpected end of input" );
        
		byte[] aByte = new byte[1];  
		int count = in.read( aByte );
		if( count < 1 ) throw new EOFException( "Unexpected end of input" );

		bytesRead += 4;
        
		return (int)((aByte[0]*256*256*256) 
					   + (b2*256*256)
					   + (b1*256)
					   + b0 );
	}
    
    /**
     * Return a variable-byte-length unsigned integer (per the AVM2 ABC spec)
     */
    public int readVU30() throws IOException {
        return (int) readVU32(); //TODO: check for the 30 bit limit ?
    }

    /**
     * Return a variable-byte-length unsigned integer (per the AVM2 ABC spec)
     */
    public long readVU32() throws IOException {
        long val = 0;
        
        for( int i = 0; i < 5; i++ ) {
            int b = readUI8();
            int v = b & 0x7f;  //chop off the high bit
            
            val += v << (7 * i);
            
            if( b < 128 ) break; //high bit not set
        }
        
        return val;
    }

    /**
     * Return a variable-byte-length signed integer (per the AVM2 ABC spec)
     */
    public int readVS24() throws IOException {
        return readVS32(); //TODO: check for byte limit ?
    }
    
    /**
     * Return a variable-byte-length signed integer (per the AVM2 ABC spec)
     */
    public int readVS32() throws IOException {
        //FIXME: fix this
        
        int val = 0;
        
        for( int i = 0; i < 5; i++ ) {
            int b = readUI8();
            int v = b & 0x7f;  //chop off the high bit
            
            val += v << (7 * i);
            
            if( b < 128 ) break; //high bit not set
        }
        
        return val;
    }

	/**
	 * Read a 32 bit floating point number
	 */
	public float readFloat() throws IOException 
	{
		return Float.intBitsToFloat( readSI32() );
	}
    
	/**
	 * Read a 64 bit floating point number
	 */
	public double readDouble() throws IOException
	{
		byte[] bytes = read( 8 );
        
		byte[] bytes2 = new byte[8];
        
		bytes2[0] = bytes[3];
		bytes2[1] = bytes[2];
		bytes2[2] = bytes[1];
		bytes2[3] = bytes[0];
		bytes2[4] = bytes[7];
		bytes2[5] = bytes[6];
		bytes2[6] = bytes[5];
		bytes2[7] = bytes[4];
        
		ByteArrayInputStream bin = new ByteArrayInputStream( bytes2 );

		return new DataInputStream( bin ).readDouble();
	}
    
    /**
     * Read a little-endian 64 bit floating point number
     */
    public double readDoubleLE() throws IOException
    {
        byte[] bytes = read( 8 );
        
        byte[] bytes2 = new byte[8];
        
        bytes2[0] = bytes[7];
        bytes2[1] = bytes[6];
        bytes2[2] = bytes[5];
        bytes2[3] = bytes[4];
        bytes2[4] = bytes[3];
        bytes2[5] = bytes[2];
        bytes2[6] = bytes[1];
        bytes2[7] = bytes[0];
        
        ByteArrayInputStream bin = new ByteArrayInputStream( bytes2 );

        return new DataInputStream( bin ).readDouble();
    }
    
	/**
	 * Util to convert an unsigned byte to an unsigned int
	 */
	public static int ubyteToInt( byte b )
	{
		boolean highbit = b < 0;
        
		b &= 0x7f;
        
		int i = (int)b;
        
		if( highbit ) i += 128;
        
		return i;
	}
    
	/**
	 * Util to convert 2 bytes to a signed value
	 */
	public static int bytesToSigned( byte lo, byte hi )
	{
		int low  = ubyteToInt( lo );
		int high = ubyteToInt( hi );
        
		int value = (high << 8) + low;
        
		if( value > 0x7fff )
		{ 
			value -= 65536;
		}
        
		return value;
	}
}
