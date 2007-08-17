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

import java.io.IOException;

import org.epistem.io.InStream;
import org.epistem.io.OutStream;

public class Matrix
{
    protected double scaleX = 1.0;
    protected double scaleY = 1.0;
    
    protected double skew0 = 0.0;
    protected double skew1 = 0.0;
    
    protected double translateX = 0.0;
    protected double translateY = 0.0;      
    
    public double getScaleX()  { return scaleX; }
    public double getScaleY()  { return scaleY; }
    public double getSkew0 ()  { return skew0; }
    public double getSkew1 ()  { return skew1; }
    public double getTranslateX() { return translateX; }
    public double getTranslateY() { return translateY; }

    public void setScaleX    ( double scaleX  ) { this.scaleX = scaleX; }
    public void setScaleY    ( double scaleY  ) { this.scaleY = scaleY; }
    public void setSkew0     ( double skew0   ) { this.skew0  = skew0; }
    public void setSkew1     ( double skew1   ) { this.skew1  = skew1; }
    public void setTranslateX( double translateX ) { this.translateX = translateX; }
    public void setTranslateY( double translateY ) { this.translateY = translateY; }    
    
    /**
     * An identity matrix
     */
    public Matrix()
    {
        this( 1.0, 1.0, 0.0, 0.0, 0, 0 );
    }
    
    public Matrix( double translateX, double translateY )
    {
        this( 1.0, 1.0, 0.0, 0.0, translateX, translateY );
    }    
    
    /**
     * Copy another matrix
     */
    public Matrix( Matrix copy )
    {
        if( copy == null ) return;
        scaleX     = copy.scaleX;
        scaleY     = copy.scaleY;
        skew0      = copy.skew0;
        skew1      = copy.skew1;
        translateX = copy.translateX;
        translateY = copy.translateY;      
    }
    
    public Matrix( double scaleX,  double scaleY, 
                   double skew0,   double skew1,
                   double translateX, double translateY )
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        this.skew0 = skew0;
        this.skew1 = skew1;
        
        setTranslateX( translateX );
        setTranslateY( translateY );
    }
    
    public Matrix( InStream in ) throws IOException 
    {
        in.synchBits();
        
        if( in.readUBits(1) == 1 ) //has scale values
        {
            int scaleBits = (int)in.readUBits(5);
            scaleX = ((double)in.readSBits( scaleBits ))/65536.0;
            scaleY = ((double)in.readSBits( scaleBits ))/65536.0;
        }

        if( in.readUBits(1) == 1 ) //has rotate/skew values
        {
            int skewBits  = (int)in.readUBits(5);
            skew0 = ((double)in.readSBits( skewBits ))/65536.0;
            skew1 = ((double)in.readSBits( skewBits ))/65536.0;
        }
        
        int translateBits = (int)in.readUBits(5);
        translateX = in.readSBits( translateBits );
        translateY = in.readSBits( translateBits );        
    }
    
    public void write( OutStream out ) throws IOException 
    {
        out.flushBits();
        
        if( scaleX != 1.0 || scaleY != 1.0 ) //if non-default values
        {
            int intScaleX = (int)(scaleX * 65536.0);
            int intScaleY = (int)(scaleY * 65536.0);
            
            int scaleBits  = OutStream.determineSignedBitSize( intScaleX );
            int scaleBits2 = OutStream.determineSignedBitSize( intScaleY );
            if( scaleBits < scaleBits2 ) scaleBits = scaleBits2;
            
            out.writeUBits( 1, 1 );
            out.writeUBits( 5, scaleBits );
            out.writeSBits( scaleBits, intScaleX );
            out.writeSBits( scaleBits, intScaleY );            
        }
        else
        {
            out.writeUBits( 1, 0 );
        }
                
        if( skew0 != 0.0 || skew1 != 0.0 ) //if non-default values
        {
            int intSkew0 = (int)(skew0 * 65536.0);
            int intSkew1 = (int)(skew1 * 65536.0);
            
            int skewBits  = OutStream.determineSignedBitSize( intSkew0 );
            int skewBits2 = OutStream.determineSignedBitSize( intSkew1 );
            if( skewBits < skewBits2 ) skewBits = skewBits2;
            
            out.writeUBits( 1, 1 );
            out.writeUBits( 5, skewBits );
            out.writeSBits( skewBits, intSkew0 );
            out.writeSBits( skewBits, intSkew1 );            
        }
        else
        {
            out.writeUBits( 1, 0 );
        }

        if( translateX == 0 && translateY == 0 )
        {
            out.writeUBits( 5, 0 );
        }
        else
        {
            int translateBits  = OutStream.determineSignedBitSize( (int)translateX );
            int translateBits2 = OutStream.determineSignedBitSize( (int)translateY );
            if( translateBits < translateBits2 ) translateBits = translateBits2;   
        
            out.writeUBits( 5, translateBits );
            out.writeSBits( translateBits, (int)translateX );
            out.writeSBits( translateBits, (int)translateY );      
        }
        
        out.flushBits();
    }
    
    public String toString()
    {
        return " Matrix(sx,sy,s0,s1,tx,ty)=(" +
               scaleX + "," + scaleY + "," + skew0 + "," + skew1 + "," +
               translateX + "," + translateY + ")";
    }
}
