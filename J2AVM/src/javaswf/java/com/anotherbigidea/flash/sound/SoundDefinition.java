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

import com.anotherbigidea.flash.interfaces.SWFTagTypes;

/**
 * A Sound Symbol Definition
 */
public class SoundDefinition
{
    public int     format;
    public int     frequency;
    public boolean is16bit;
    public boolean isStereo;    public int     sampleCount;
    public byte[]  soundData;

	
    public SoundDefinition( int format, int frequency, boolean is16bit, boolean isStereo, int sampleCount, byte[] soundData ) 
    {
        this.format      = format;      
        this.frequency   = frequency;   
        this.is16bit     = is16bit;     
        this.isStereo    = isStereo;            this.sampleCount = sampleCount; 
        this.soundData   = soundData;   
    }    
    
	/**
	 * Write the symbol definition
	 * @param timeline the timeline to write to
	 * @param id the symbol id
	 */
    public void write( SWFTagTypes timeline, int id ) throws IOException {
        timeline.tagDefineSound( id, format, frequency, is16bit, isStereo, sampleCount, soundData );
    }
}