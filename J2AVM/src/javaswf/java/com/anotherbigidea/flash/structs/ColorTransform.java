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

public class ColorTransform
{
    protected double multRed   = 1.0;
    protected double multGreen = 1.0;
    protected double multBlue  = 1.0;
    
    protected int addRed   = 0;
    protected int addGreen = 0;
    protected int addBlue  = 0;
    
    protected double multAlpha = 1.0; //used by AlphaTransform
    protected int    addAlpha  = 0;   //used by AlphaTransform 
    
    public double getMultRed()   { return multRed; }
    public double getMultGreen() { return multGreen; }
    public double getMultBlue()  { return multBlue; }

    public int getAddRed()   { return addRed; }
    public int getAddGreen() { return addGreen; }
    public int getAddBlue()  { return addBlue; }

    public void setMultRed  ( double multRed   ) { this.multRed   = multRed; }
    public void setMultGreen( double multGreen ) { this.multGreen = multGreen; }
    public void setMultBlue ( double multBlue  ) { this.multBlue  = multBlue; }

    public void setAddRed   ( int addRed   ) { this.addRed   = addRed; }
    public void setAddGreen ( int addGreen ) { this.addGreen = addGreen; }
    public void setAddBlue  ( int addBlue  ) { this.addBlue  = addBlue; }    
    
    /**
     * An identity transform
     */
    public ColorTransform() {}
    
    public ColorTransform( double multRed, double multGreen, double multBlue,
                           int addRed,  int addGreen,  int addBlue )
    {
        this.multRed   = multRed;
        this.multGreen = multGreen;
        this.multBlue  = multBlue;
        this.addRed    = addRed;
        this.addGreen  = addGreen;
        this.addBlue   = addBlue;
    }

    public ColorTransform( int addRed,  int addGreen,  int addBlue )
    {
        this.addRed    = addRed;
        this.addGreen  = addGreen;
        this.addBlue   = addBlue;
    }

    public ColorTransform( double multRed, double multGreen, double multBlue )
    {
        this.multRed   = multRed;
        this.multGreen = multGreen;
        this.multBlue  = multBlue;
    }
        
    public ColorTransform( InStream in ) throws IOException 
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
        }
        
        if( hasAddTerms )
        {
            addRed   = in.readSBits( numBits );
            addGreen = in.readSBits( numBits );
            addBlue  = in.readSBits( numBits );
        }            
    }
    
    public void write( OutStream out ) throws IOException 
    {
        out.flushBits();
        
        boolean hasAddTerms =    ( addRed   != 0 ) 
                              || ( addGreen != 0 ) 
                              || ( addBlue  != 0 ); 

        boolean hasMultTerms =    ( multRed   != 1.0 ) 
                               || ( multGreen != 1.0 ) 
                               || ( multBlue  != 1.0 ); 
        
        int intMultRed   = (int)(multRed   * 256.0);
        int intMultGreen = (int)(multGreen * 256.0);
        int intMultBlue  = (int)(multBlue  * 256.0);
        
        //--Figure out the bit sizes
        int numBits = 1;
        
        if( hasAddTerms )
        {
            int redBits   = OutStream.determineSignedBitSize( addRed );
            int greenBits = OutStream.determineSignedBitSize( addGreen );
            int blueBits  = OutStream.determineSignedBitSize( addBlue );
            
            if( numBits < redBits   ) numBits = redBits;
            if( numBits < greenBits ) numBits = greenBits;
            if( numBits < blueBits  ) numBits = blueBits;
        }
        
        if( hasMultTerms )
        {
            int redBits   = OutStream.determineSignedBitSize( intMultRed );
            int greenBits = OutStream.determineSignedBitSize( intMultGreen );
            int blueBits  = OutStream.determineSignedBitSize( intMultBlue );
            
            if( numBits < redBits   ) numBits = redBits;
            if( numBits < greenBits ) numBits = greenBits;
            if( numBits < blueBits  ) numBits = blueBits;     
        }
        
        //--Add and mult are reversed
        out.writeUBits( 1, hasAddTerms  ? 1L : 0L );
        out.writeUBits( 1, hasMultTerms ? 1L : 0L );
        out.writeUBits( 4, numBits );

        if( hasMultTerms )
        {
            out.writeSBits( numBits, intMultRed   );
            out.writeSBits( numBits, intMultGreen );
            out.writeSBits( numBits, intMultBlue  );
        }        
        
        if( hasAddTerms )
        {
            out.writeSBits( numBits, addRed   );
            out.writeSBits( numBits, addGreen );
            out.writeSBits( numBits, addBlue  );
        }                
        
        out.flushBits();
    }
    
    public void writeWithoutAlpha( OutStream out ) throws IOException 
    {
        write( out );
    }
        
    public void writeWithAlpha( OutStream out ) throws IOException 
    {
        out.flushBits();
        
        boolean hasAddTerms =    ( addRed   != 0 ) 
                              || ( addGreen != 0 ) 
                              || ( addBlue  != 0 )
                              || ( addAlpha != 0 ); 

        boolean hasMultTerms =    ( multRed   != 1.0 ) 
                               || ( multGreen != 1.0 ) 
                               || ( multBlue  != 1.0 )
                               || ( multAlpha != 1.0 ); 
        
        int intMultRed   = (int)(multRed   * 256.0);
        int intMultGreen = (int)(multGreen * 256.0);
        int intMultBlue  = (int)(multBlue  * 256.0);
        int intMultAlpha = (int)(multAlpha * 256.0);
        
        //--Figure out the bit sizes
        int numBits = 1;
        
        if( hasAddTerms )
        {
            int redBits   = OutStream.determineSignedBitSize( addRed );
            int greenBits = OutStream.determineSignedBitSize( addGreen );
            int blueBits  = OutStream.determineSignedBitSize( addBlue );
            int alphaBits = OutStream.determineSignedBitSize( addAlpha );
            
            if( numBits < redBits   ) numBits = redBits;
            if( numBits < greenBits ) numBits = greenBits;
            if( numBits < blueBits  ) numBits = blueBits;
            if( numBits < alphaBits ) numBits = alphaBits;
        }
        
        if( hasMultTerms )
        {
            int redBits   = OutStream.determineSignedBitSize( intMultRed );
            int greenBits = OutStream.determineSignedBitSize( intMultGreen );
            int blueBits  = OutStream.determineSignedBitSize( intMultBlue );
            int alphaBits = OutStream.determineSignedBitSize( intMultAlpha );
            
            if( numBits < redBits   ) numBits = redBits;
            if( numBits < greenBits ) numBits = greenBits;
            if( numBits < blueBits  ) numBits = blueBits;     
            if( numBits < alphaBits ) numBits = alphaBits;
        }
        
        //--Add and mult are reversed
        out.writeUBits( 1, hasAddTerms  ? 1L : 0L );
        out.writeUBits( 1, hasMultTerms ? 1L : 0L );
        out.writeUBits( 4, numBits );

        if( hasMultTerms )
        {
            out.writeSBits( numBits, intMultRed   );
            out.writeSBits( numBits, intMultGreen );
            out.writeSBits( numBits, intMultBlue  );
            out.writeSBits( numBits, intMultAlpha );
        }        
        
        if( hasAddTerms )
        {
            out.writeSBits( numBits, addRed   );
            out.writeSBits( numBits, addGreen );
            out.writeSBits( numBits, addBlue  );
            out.writeSBits( numBits, addAlpha );
        }                
        
        out.flushBits();
    }    
    
    public String toString()
    {
        return " cxform(+rgb,*rgb)=(" + addRed + "," + addGreen + "," + addBlue
               + "," + multRed + "," + multGreen + "," + multBlue + ")";
    }
}
