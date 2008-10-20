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
package com.anotherbigidea.flash.structs;

import java.io.*;

import org.epistem.io.*;

/**
 * A SWF Rectangle structure
 */
public class Rect
{
    protected int bitSize = -1;
    protected int minX;
    protected int minY;
    protected int maxX;
    protected int maxY;
    
    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }

    public void setMinX( int minX ) { this.minX = minX; bitSize = -1; }
    public void setMinY( int minY ) { this.minY = minY; bitSize = -1; }
    public void setMaxX( int maxX ) { this.maxX = maxX; bitSize = -1; }
    public void setMaxY( int maxY ) { this.maxY = maxY; bitSize = -1; }
       
    public Rect( int minX, int minY, int maxX, int maxY )
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public Rect( InStream in ) throws IOException
    {
        in.synchBits();
        bitSize = (int)in.readUBits( 5 );
        minX    = (int)in.readSBits( bitSize );
        maxX    = (int)in.readSBits( bitSize );
        minY    = (int)in.readSBits( bitSize );
        maxY    = (int)in.readSBits( bitSize );
    }
    
    public Rect()
    {
        this( 0, 0, 11000, 8000 ); //default size
    }
    
    /**
     * Calculate the minimum bit size based on the current values
     */
    protected int getBitSize()
    {
        if( bitSize == -1 ) //bitsize not defined
        {
            int bsMinX = OutStream.determineSignedBitSize( minX );
            int bsMaxX = OutStream.determineSignedBitSize( maxX );
            int bsMinY = OutStream.determineSignedBitSize( minY );
            int bsMaxY = OutStream.determineSignedBitSize( maxY );
            
            bitSize = bsMinY;
            if( bitSize < bsMaxX ) bitSize = bsMaxX;
            if( bitSize < bsMinX ) bitSize = bsMinX;
            if( bitSize < bsMaxY ) bitSize = bsMaxY;
        }
              
        return bitSize;
    }
    
    public long getLength()
    {
        int bits  = 5 + ( getBitSize() * 4 );        
        int bytes = bits / 8;
        
        if( bytes * 8 < bits ) bytes++;
        
        return bytes;
    }
    
    /**
     * Write the rect contents to the output stream
     */
    public void write( OutStream out ) throws IOException
    {
        out.flushBits();       
        
        out.writeUBits( 5, getBitSize() );
        out.writeSBits( bitSize, minX );
        out.writeSBits( bitSize, maxX );
        out.writeSBits( bitSize, minY );
        out.writeSBits( bitSize, maxY );
        
        out.flushBits();        
    }
    
    public String toString()
    {
        return "Rect bitsize=" + bitSize +
               " (" + minX + "," + minY + ")-("+ maxX + "," + maxY + ")";
    }
}
