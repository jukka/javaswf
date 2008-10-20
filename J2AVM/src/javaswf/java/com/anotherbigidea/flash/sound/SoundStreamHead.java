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

import com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes;

/**
 * Sound Stream Header Information
 */
public class SoundStreamHead
{
    public int     playbackFrequency;
    public boolean playback16bit;
    public boolean playbackStereo;                    public int     streamFormat;
    public int     streamFrequency;
    public boolean stream16bit;
    public boolean streamStereo;    public int     averageSampleCount;
    
    public SoundStreamHead( int playbackFrequency, boolean playback16bit, boolean playbackStereo,                            int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,                            int averageSampleCount )
    {
        this.playbackFrequency  = playbackFrequency;  
        this.playback16bit      = playback16bit;      
        this.playbackStereo     = playbackStereo;                        this.streamFormat       = streamFormat;       
        this.streamFrequency    = streamFrequency;    
        this.stream16bit        = stream16bit;        
        this.streamStereo       = streamStereo;               this.averageSampleCount = averageSampleCount; 
    }
    
    /**
     * Playback and streaming parameters are assumed to be the same
     */
    public SoundStreamHead( int frequency, boolean is16bit, boolean isStereo,                            int streamFormat, int averageSampleCount )
    {
        this.playbackFrequency  = frequency;  
        this.playback16bit      = is16bit;      
        this.playbackStereo     = isStereo;                        this.streamFormat       = streamFormat;       
        this.streamFrequency    = frequency;    
        this.stream16bit        = is16bit;        
        this.streamStereo       = isStereo;               this.averageSampleCount = averageSampleCount; 
    }
    
    public void write( SWFSpriteTagTypes swfTags ) throws IOException 
    {
        swfTags.tagSoundStreamHead2( playbackFrequency, playback16bit, playbackStereo,                                     streamFormat, streamFrequency, stream16bit, streamStereo,                                     averageSampleCount );
    }
}
