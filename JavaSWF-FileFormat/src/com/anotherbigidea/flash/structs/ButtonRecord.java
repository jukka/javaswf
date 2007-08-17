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
import java.util.*;

import org.epistem.io.*;

public class ButtonRecord
{      
    public static final int BUTTON_HITTEST = 0x08;
    public static final int BUTTON_DOWN    = 0x04;
    public static final int BUTTON_OVER    = 0x02;
    public static final int BUTTON_UP      = 0x01;
    
    protected int    flags;
    protected int    id;
    protected int    layer;
    protected Matrix matrix;
    
    public int    getCharId() { return id; }
    public int    getLayer()  { return layer; }
    public Matrix getMatrix() { return matrix; }
    public int    getFlags()  { return flags; }
    
    public boolean isHitTest() { return ( (flags & BUTTON_HITTEST ) != 0 ); }
    public boolean isDown()    { return ( (flags & BUTTON_DOWN    ) != 0 ); }
    public boolean isOver()    { return ( (flags & BUTTON_OVER    ) != 0 ); } 
    public boolean isUp()      { return ( (flags & BUTTON_UP      ) != 0 ); }    

    public void setCharId( int id ) { this.id = id; }
    public void setLayer( int layer ) { this.layer = layer; }
    public void setMatrix( Matrix matrix ) { this.matrix = matrix; }
    public void setFlags( int flags ) { this.flags = flags; }
      
    /**
     * Read a button record array
     */
    public static Vector<ButtonRecord> read( InStream in ) throws IOException
    {
        Vector<ButtonRecord> records = new Vector<ButtonRecord>();
        
        int firstByte = 0;
        while( (firstByte = in.readUI8()) != 0 )
        {
            records.addElement( new ButtonRecord( in, firstByte ));
        }
        
        return records;
    }
    
    /**
     * Write a button record array
     */
    public static void write( OutStream out, Vector<ButtonRecord> records ) throws IOException
    {
        for( ButtonRecord rec : records ) {
            rec.write( out );
        }
        
        out.writeUI8( 0 );
    }    
    
    public ButtonRecord( int id, int layer, Matrix matrix, int flags )
    {
        this.id     = id;
        this.layer  = layer;
        this.matrix = matrix;
        this.flags  = flags;
    }
    
    protected ButtonRecord( InStream in, int firstByte ) throws IOException
    {
        flags  = firstByte;
        id     = in.readUI16();
        layer  = in.readUI16();
        matrix = new Matrix( in );
    }
    
    protected void write( OutStream out ) throws IOException
    {
        out.writeUI8 ( flags );
        out.writeUI16( id );
        out.writeUI16( layer );
        matrix.write ( out );
    }
    
    public String toString()
    {
        return "layer=" + layer + " id=" + id + 
               " flags=" + Integer.toBinaryString(flags) + " " + matrix;
    }
}
