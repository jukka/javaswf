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
package org.epistem.util;
import java.io.*;

/**
 * Hex dump conversion utilities
 */
public class Hex
{
    public static String dump( byte[] data, long startAddress, String indent )
    {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter( writer, true );
        
        dump( printer, data, startAddress, indent, false );
        
        return writer.toString();
    }    
    
    public static String dumpWithBinary( byte[] data, long startAddress, String indent )
    {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter( writer, true );
        
        dump( printer, data, startAddress, indent, true );
        
        return writer.toString();
    }    
    
    public static void dump( PrintWriter out, 
                             byte[] data, 
                             long startAddress, 
                             String indent, 
                             boolean includeBinary )
    {
        dump( out, data, 0, data.length, startAddress, indent, includeBinary );
    }    
    
    public static void dumpSysout( byte[] data ) {
        PrintWriter pw = new PrintWriter( System.out );
        dump( pw, data, 0, "", false );
    }
    
    public static void dump( PrintWriter out, 
                             byte[] data,
                             int  startIndex,
                             int  length,
                             long startAddress, 
                             String indent, 
                             boolean includeBinary )
    {
        if( data == null ) return;  //nothing to dump
        
        int i = startIndex; 
        int endIndex = startIndex + length - 1;
        if( endIndex >= data.length ) endIndex = data.length - 1;
        
        while( i <= endIndex )
        {
            String hex   = "";
            String chars = " ";
            String binary = "";
            
            for( int j = 0; j < 16; j++ )
            {
                if( i + j <= endIndex )
                {
                    hex += " " + leadingZeros( Integer.toHexString( getByte(data[i+j])), 2 );

                    if( includeBinary )
                    {
                        binary += " " + leadingZeros( Integer.toBinaryString( getByte(data[i+j])), 8 );
                    }                    
                    
                    if( data[i+j] < 32 )
                    {
                        chars += ".";
                    }
                    else
                    {
                        chars += new String( new byte[] { data[i+j] } );
                    }
                }
                else
                {
                    hex   += " --";
                    chars += " ";

                    if( includeBinary )
                    {
                        binary += " --------";
                    }
                }
            }
            
            out.println( ((indent != null) ? indent : "") 
                         + leadingZeros( Long.toHexString( startAddress ), 8 ) 
                         + hex 
                         + chars
                         + binary );
            
            i += 16;
            startAddress += 16;
        }
        
        out.flush();
    }
    

    /**
     * Pad the string with leading zeros until it it reached the given size
     */
    public static String leadingZeros( String string, int size )
    {
        String s = string;
        
        while( s.length() < size )
        {
            s = "0" + s;
        }
        
        return s;
    }

    /**
     * Get unsigned byte
     */
    protected static int getByte( byte b )
    {
        if( b >= 0 ) return (int)b;
        
        //else byte is negative and needs conversion to an unsigned int
        return b + 256;
    }
    
    /**
     * Dump the named file to System.out
     */
    public static void main( String[] args ) throws IOException 
    {
        RandomAccessFile file = new RandomAccessFile( args[0], "r" );
        byte[] bytes = new byte[ (int)file.length() ];
        file.readFully( bytes );
        
        PrintWriter writer = new PrintWriter( System.out );
        dump( writer, bytes, 0L, "", false );
        writer.flush();
    }
}
