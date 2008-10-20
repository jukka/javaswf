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

public class ButtonRecord2 extends ButtonRecord 
{
    protected AlphaTransform transform;
    
    public AlphaTransform getTransform() { return transform; }
    public void setTransform( AlphaTransform transform ) { this.transform = transform; }
      
    /**
     * Read a button record array
     */
    public static Vector<ButtonRecord2> read2( InStream in ) throws IOException
    {
        Vector<ButtonRecord2> records = new Vector<ButtonRecord2>();
        
        int firstByte = 0;
        while( (firstByte = in.readUI8()) != 0 )
        {
            records.addElement( new ButtonRecord2( in, firstByte ));
        }
        
        return records;
    }
    
    /**
     * Write a button record array
     */
    public static void write( OutStream out, Vector<ButtonRecord2> records ) throws IOException
    {
        for( ButtonRecord2 rec : records ) {
            rec.write( out );
        }
        
        out.writeUI8( 0 );
    }    
    
    public ButtonRecord2( int id, 
                          int layer, 
                          Matrix matrix,
                          AlphaTransform transform,
                          int flags )
    {
        super( id, layer, matrix, flags );
        this.transform = transform;
    }
    
    protected ButtonRecord2( InStream in, int firstByte ) throws IOException
    {
        super( in, firstByte );
        transform = new AlphaTransform( in );
    }
    
    protected void write( OutStream out ) throws IOException
    {
        super.write( out );
        transform.write( out );
    }
    
    public String toString()
    {
        return super.toString() + " " + transform;
    }
}
