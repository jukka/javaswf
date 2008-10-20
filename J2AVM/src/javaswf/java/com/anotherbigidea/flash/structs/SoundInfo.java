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
 * A Sound Information structure - defines playback style and envelope
 */
public class SoundInfo 
{    
    /**
     * A Point in a sound envelope
     */
    public static class EnvelopePoint        
    {
        public int mark44;   
        public int level0;
        public int level1;
        
        public EnvelopePoint( int mark44, int level0, int level1 )
        {
            this.mark44 = mark44;
            this.level0 = level0;
            this.level1 = level1;
        }
    }
    
    protected boolean noMultiplePlay;  //only one instance can play at a time
    protected boolean stopPlaying;
    
    protected EnvelopePoint[] envelope;
    protected int inPoint;
    protected int outPoint;
    protected int loopCount;

    /**
     * @param noMultiplePlay true = only play if not already playing
     * @param stopSound      true = stop playing the sound
     * @param envelope       may be null or empty for no envelope
     * @param inPoint        -1 for no in-point
     * @param outPoint       -1 for no out-point
     * @param loopCount      >1 for a loop count
     */
    public SoundInfo( boolean noMultiplePlay, boolean stopSound,
                      EnvelopePoint[] envelope, 
                      int inPoint, int outPoint, int loopCount )
    {
        this.noMultiplePlay = noMultiplePlay;
        this.stopPlaying    = stopSound;
        this.envelope       = envelope;
        this.inPoint        = inPoint;
        this.outPoint       = outPoint;
        this.loopCount      = loopCount;
    }
    
    public boolean isNoMultiplePlay()    { return this.noMultiplePlay; }
    public boolean isStopPlaying()       { return this.stopPlaying; }    
    public EnvelopePoint[] getEnvelope() { return this.envelope; }
    public int getInPoint()              { return this.inPoint; }
    public int getOutPoint()             { return this.outPoint; }    
    public int getLoopCount()            { return this.loopCount; }
    
    public SoundInfo( InStream in ) throws IOException 
    {
        int flags = in.readUI8();
        
        noMultiplePlay      = ( (flags & 16) != 0 );
        stopPlaying         = ( (flags & 32) != 0 );
        boolean hasEnvelope = ( (flags &  8) != 0 );
        boolean hasLoops    = ( (flags &  4) != 0 );
        boolean hasOutPoint = ( (flags &  2) != 0 );
        boolean hasInPoint  = ( (flags &  1) != 0 );
        
        if( hasInPoint ) inPoint = (int)in.readUI32();
        else             inPoint = -1;
        
        if( hasOutPoint ) outPoint = (int)in.readUI32();
        else              outPoint = -1;
        
        if( hasLoops ) loopCount = in.readUI16();
        else           loopCount = 1;
        
        int envsize = 0;
        if( hasEnvelope ) envsize = in.readUI8();
        
        envelope = new EnvelopePoint[ envsize ];
        
        for( int i = 0; i < envsize; i++ )
        {
            envelope[i] = new EnvelopePoint( (int)in.readUI32(), 
                                             in.readUI16(),
                                             in.readUI16() );
        }
    }
    
    public void write( OutStream out ) throws IOException 
    {
        int flags = 0;
        if( noMultiplePlay ) flags += 1;
        if( stopPlaying    ) flags += 2;
        
        out.writeUBits( 4, flags );

        boolean hasEnvelope = (envelope != null && envelope.length > 0);
        boolean hasLoops    = ( loopCount > 1 );
        boolean hasOutPoint = ( outPoint >= 0 );
        boolean hasInPoint  = ( inPoint  >= 0 );
        
        flags = 0;
        if( hasEnvelope ) flags += 8;
        if( hasLoops    ) flags += 4;
        if( hasOutPoint ) flags += 2;
        if( hasInPoint  ) flags += 1;
        
        out.writeUBits( 4, flags );
        
        if( hasInPoint  ) out.writeUI32( inPoint );
        if( hasOutPoint ) out.writeUI32( outPoint );
        if( hasLoops    ) out.writeUI16( loopCount );
    
        if( hasEnvelope )
        {
            out.writeUI8( envelope.length );
            
            for( int i = 0; i < envelope.length; i++ )
            {
                out.writeUI32( envelope[i].mark44 );
                out.writeUI16( envelope[i].level0 );
                out.writeUI16( envelope[i].level1 );
            }
        }
    }
    
    public String toString()
    {
        return "SoundInfo: no-multiplay=" + noMultiplePlay + 
               " stop=" + stopPlaying +
               " envelope=" + ((envelope==null)? "none": (""+ envelope.length + " points")) +
               " in-point=" + inPoint +
               " out-point=" + outPoint +
               " loop-count=" + loopCount;
    }    
}