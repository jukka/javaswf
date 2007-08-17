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
 * A Color with an Alpha component
 */
public class AlphaColor extends Color 
{
    protected int alpha;
    
    public int getAlpha() { return alpha; }
    public void setAlpha( int alpha ) { this.alpha = alpha; }
    
    public AlphaColor( int red, int green, int blue, int alpha )
    {
        super( red, green, blue );
        this.alpha = alpha;
    }
    
    public AlphaColor( Color color, int alpha )
    {
        this( color.getRed(), color.getGreen(), color.getBlue(), alpha );
    }
    
    public AlphaColor( InStream in ) throws IOException 
    {
        super( in );
        alpha = in.readUI8();
    }
    
    public void write( OutStream out ) throws IOException 
    {
        writeRGB( out );
        out.writeUI8( alpha );
    }

    public void writeWithAlpha( OutStream out ) throws IOException
    {
        write( out );
    }    
    
    public boolean equals( AlphaColor color )
    {
        return super.equals( color ) && ( alpha == color.getAlpha() );
    }
    
    public String toString()
    {
        return "RGBA("+red+","+green+","+blue+","+alpha+")";
    }    
}