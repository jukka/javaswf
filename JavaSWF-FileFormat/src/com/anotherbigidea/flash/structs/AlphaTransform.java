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

public class AlphaTransform extends ColorTransform 
{    
    public double getMultAlpha() { return multAlpha; }
    public int    getAddAlpha()  { return addAlpha; }
    
    public void setMultAlpha( double multAlpha ) { this.multAlpha = multAlpha; }
    public void setAddAlpha ( int addAlpha     ) { this.addAlpha = addAlpha; }    
    
    /**
     * An identity transform
     */
    public AlphaTransform() {}    
    
    /**
     * Copy another transform
     */
    public AlphaTransform( ColorTransform copy )
    {
        if( copy == null ) return;
        this.addRed   = copy.addRed;
        this.addGreen = copy.addGreen;
        this.addBlue  = copy.addBlue;
        this.addAlpha = copy.addAlpha;
        
        this.multRed   = copy.multRed;
        this.multGreen = copy.multGreen;
        this.multBlue  = copy.multBlue;
        this.multAlpha = copy.multAlpha;
    }
    
    public AlphaTransform( double multRed, double multGreen, double multBlue,
                           double multAlpha,
                           int addRed,  int addGreen,  int addBlue,
                           int addAlpha )
    {
        super( multRed, multGreen, multBlue, addRed, addGreen, addBlue );
        this.multAlpha = multAlpha;
        this.addAlpha  = addAlpha;
    }

    public AlphaTransform( int addRed,  int addGreen,  int addBlue, int addAlpha )
    {
        super( addRed, addGreen, addBlue );
        this.addAlpha = addAlpha;
    }

    public AlphaTransform( double multRed, double multGreen, double multBlue,
                           double multAlpha )
    {
        super( multRed, multGreen, multBlue );
        this.multAlpha = multAlpha;
    }
    
    public AlphaTransform( InStream in ) throws IOException 
    {
        in.synchBits();
        
        //--Add and mult are reversed
        boolean hasAddTerms  = ( in.readUBits(1) == 1 );
        boolean hasMultTerms = ( in.readUBits(1) == 1 );
               
        int numBits = (int)in.readUBits(4);
        
        if( hasMultTerms )
        {
            multRed   = ((double)in.readSBits( numBits ))/256.0;
            multGreen = ((double)in.readSBits( numBits ))/256.0;
            multBlue  = ((double)in.readSBits( numBits ))/256.0;            
            multAlpha = ((double)in.readSBits( numBits ))/256.0;            
        }
        
        if( hasAddTerms )
        {
            addRed   = in.readSBits( numBits );
            addGreen = in.readSBits( numBits );
            addBlue  = in.readSBits( numBits );
            addAlpha = in.readSBits( numBits );
        }    
    }
    
    public void write( OutStream out ) throws IOException 
    {
        writeWithAlpha( out );
    }
    
    public void writeWithoutAlpha( OutStream out ) throws IOException 
    {
        super.write( out );
    }    
    
    public String toString()
    {
        return " cxform(+rgba,*rgba)=(" + addRed + "," + addGreen + "," + addBlue
               + "," + addAlpha + "," + multRed + "," + multGreen + "," + 
               multBlue + "," + multAlpha + ")";
    }    
}
