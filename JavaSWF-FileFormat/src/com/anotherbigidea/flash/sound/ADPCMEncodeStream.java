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
package com.anotherbigidea.flash.sound;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * ADPCM Encoder
 */
public class ADPCMEncodeStream implements ADPCMConstants 
{
    protected InputStream samplesIn;
    protected boolean is16bit;
    protected boolean isDone = false;
    protected boolean isSigned;
    
    public ADPCMEncodeStream( InputStream inputSamples, boolean is16bit, boolean isSigned )
    {
        this.samplesIn = inputSamples;
        this.is16bit   = is16bit;
        this.isSigned  = isSigned;
    }
  
    /**
     * @return true if the input samples are exhausted  
     */
    public boolean isDone() { return isDone; }
    
    public int getIndex() { return index; }
    
    /**
     * Get the next ADPCM delta
     */
    public int getDelta() throws IOException 
    {
        int sample = getSample();
        
        int step = stepsizeTable[index];
    	int diff = sample - valpred;
    	int sign = ( diff < 0 ) ? 8 : 0;
    	if( sign > 0 ) diff = -diff;
        
        int delta = 0;
    	int vpdiff = (step >> 3);
    	
    	if( diff >= step ) 
        {
    	    delta  =  4;
    	    diff   -= step;
    	    vpdiff +=  step;
    	}
            
        step >>= 1;
            
    	if( diff >= step  ) 
        {
    	    delta  |= 2;
    	    diff   -=  step;
    	    vpdiff += step;
    	}
            
        step >>= 1;
            
    	if ( diff >=  step ) 
        {
    	    delta  |= 1;
    	    vpdiff += step;
    	}

    	// Step 3 - Update previous value
    	if( sign > 0 ) valpred -= vpdiff;
    	else           valpred += vpdiff;

    	// Step 4 - Clamp previous value to 16 bits
    	if      ( valpred > 0x7FFF  ) valpred = 0x7FFF;
    	else if ( valpred < -0x8000 ) valpred = -0x8000;

    	// Step 5 - Assemble value, update index and step values
    	delta |= sign;
    	
    	index += indexTable[delta];
    	if ( index < 0  ) index = 0;
    	if ( index > 88 ) index = 88;

        return delta;
    }
    
    public int getSample() throws IOException 
    {
        if( ! sampleStack.isEmpty() )
        {
            Integer sample = (Integer)sampleStack.remove( sampleStack.size() - 1 );
            return sample.intValue();
        }
        
        int lo = read();
        
        if( ! is16bit )
        {
            lo -= 0x80;
            return lo * 0x100;
        }
        
        int hi = read();
        hi = hi<<8;
        hi += lo;
        
        if( isSigned && hi > 0x7fff ) hi -= 0x10000;
        return hi;
    }
    
    protected int read() throws IOException 
    {
        int b = samplesIn.read();
        
        if( b < 0 )
        {
            isDone = true;
            b = 0;
        }
        
        return b;
    }
    
    public void pushSample( int sample )
    {
        sampleStack.add( sample );
    }
    
    public int getFirstPacketSample() throws IOException
    {
        int sample = getSample();
        valpred = sample;
        return sample;
    }
    
    public int peekSample() throws IOException 
    {
        int sample = getSample();
        pushSample( sample );
        return sample;
    }
    
    /**
     * Set the index, given sample 1, to the closest value that gives the delta with the next sample
     * @return the index
     */
    public int setIndex( int sample1 ) throws IOException
    {
        int sample2 = peekSample();
        int diff = sample2 - sample1;
        if( diff < 0 ) diff = -diff;
        
        int i = 0;
        for( ; i < ADPCMConstants.stepsizeTable.length; i++ )
        {
            int step = ADPCMConstants.stepsizeTable[i];
            if( step > diff && i > 0 ) break;
        }
        
        index = i - 1;
        return index;
    }
    
    //--state
    protected ArrayList<Integer> sampleStack = new ArrayList<Integer>();
    protected int valpred = 0; // Previous output value
    protected int index   = 0; // Index into step size table            
}
