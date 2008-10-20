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
 * An RGB Color without alpha
 */
public class Color
{
    public static final Color  WHITE   = new Color( 255, 255, 255 );
    public static final Color  BLACK   = new Color( 0, 0, 0 );
    public static final Color  BLUE    = new Color( 0, 0, 255 );
    public static final Color  RED     = new Color( 255, 0, 0 );
    public static final Color  GREEN   = new Color( 0, 255, 0 );
    public static final Color  YELLOW  = new Color( 255, 255, 0 );
    public static final Color  CYAN    = new Color( 0, 255, 255 );
    public static final Color  MAGENTA = new Color( 255, 0, 255 );
    public static final Color  GREY    = new Color( 128, 128, 128 );
    
    protected int red;
    protected int green;
    protected int blue;
    
    public int getRed()   { return red; }
    public int getGreen() { return green; }
    public int getBlue()  { return blue; }

    public void setRed  ( int red   ) { this.red   = red; }
    public void setGreen( int green ) { this.green = green; }
    public void setBlue ( int blue  ) { this.blue  = blue; }
    
    /**
     * @param rgb the color in the form 0x00rrggbb
     */
    public Color( int rgb ) {
        this( (rgb >> 16) & 0xff, 
              (rgb >> 8 ) & 0xff,
              rgb         & 0xff );
    }
    
    public Color( int red, int green, int blue )
    {
        this.red   = red;
        this.green = green;
        this.blue  = blue;
    }
    
    public Color( InStream in ) throws IOException 
    {
        red   = in.readUI8();
        green = in.readUI8();
        blue  = in.readUI8();
    }
    
    public void write( OutStream out ) throws IOException 
    {
        writeRGB( out );
    }
    
    public boolean equals( Color color )
    {
        return   ( red   == color.getRed() )
               &&( green == color.getGreen() )
               &&( blue  == color.getBlue() );
    }    
    
    public void writeRGB( OutStream out ) throws IOException 
    {
        out.writeUI8( red   );
        out.writeUI8( green );
        out.writeUI8( blue  );        
    }
    
    public void writeWithAlpha( OutStream out ) throws IOException
    {
        writeRGB( out );
        out.writeUI8( 0xff );  //fully opaque alpha
    }    
    
    public String toString()
    {
        return "RGB("+red+","+green+","+blue+")";
    }
}