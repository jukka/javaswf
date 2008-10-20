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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * MP3 Utilities
 */
public class MP3Helper
{
    public static SoundDefinition getSoundDefinition( InputStream mp3 ) throws IOException
    {
        MP3Frame frame = MP3Frame.readFrame( mp3 );
        
        //int samplesPerFrame = frame.getSamplesPerFrame();
        int sampleRate = frame.getSampleRate();
        
        boolean isStereo = frame.isStereo();
        
        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if     ( sampleRate >= 44000 ) rate = SWFConstants.SOUND_FREQ_44KHZ;
        else if( sampleRate >= 22000 ) rate = SWFConstants.SOUND_FREQ_22KHZ;
        else if( sampleRate >= 11000 ) rate = SWFConstants.SOUND_FREQ_11KHZ;        
        
        int sampleCount = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        while( true && frame != null )
        {        
            //--Write DelaySeek of zero
            bout.write(0);
            bout.write(0);
                    
            while( frame != null )
            {
                sampleCount += frame.getSamplesPerFrame();
                frame.write( bout );
                frame = MP3Frame.readFrame( mp3 );
            }
            
            bout.flush();
        }
        
        mp3.close();     
        
        byte[] data = bout.toByteArray();        
        
        return new SoundDefinition( SWFConstants.SOUND_FORMAT_MP3, rate, true, isStereo, sampleCount, data );
    }
    
    /**
     * Read an MP3 input file.
     * Return a list of byte[] - one for each Streaming Sound Block
     * 
     * @param blocks the list to place the block into
     * @return the stream header
     */
    public static SoundStreamHead streamingBlocks( InputStream mp3, int framesPerSecond, ArrayList<byte[]> blocks ) 
        throws IOException
    {
        MP3Frame frame = MP3Frame.readFrame( mp3 );
        
        //int samplesPerFrame = frame.getSamplesPerFrame();
        int sampleRate = frame.getSampleRate();
        int totalSamples = 0;
        
        int samplesPerSWFFrame = sampleRate / framesPerSecond;
                
        boolean isStereo = frame.isStereo();
        
        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if     ( sampleRate >= 44000 ) rate = SWFConstants.SOUND_FREQ_44KHZ;
        else if( sampleRate >= 22000 ) rate = SWFConstants.SOUND_FREQ_22KHZ;
        else if( sampleRate >= 11000 ) rate = SWFConstants.SOUND_FREQ_11KHZ;
        
        SoundStreamHead head = new SoundStreamHead( rate, true, isStereo, SWFConstants.SOUND_FORMAT_MP3,
                                                    rate, true, isStereo, samplesPerSWFFrame );
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        while( frame != null )
        {        
            //--Write dummy sample count
            bout.write(0);
            bout.write(0);
            
            //--Write DelaySeek of zero
            bout.write(0);
            bout.write(0);
        
            int sampleCount = 0;
            int targetSampleCount = samplesPerSWFFrame * (blocks.size() + 1);
            
            while( frame != null && ( totalSamples + sampleCount < targetSampleCount ) )
            {
                sampleCount += frame.getSamplesPerFrame();
                frame.write( bout );
                frame = MP3Frame.readFrame( mp3 );
            }
            
            bout.flush();
            byte[] bytes = bout.toByteArray();
            bytes[0] = (byte)(sampleCount & 0xFF);
            bytes[1] = (byte)(sampleCount >> 8);
            
            totalSamples += sampleCount;
            
            blocks.add( bytes );
            bout.reset();            
        }
        
        mp3.close();
        
        double soundLength = ((double)totalSamples) / ((double)sampleRate);
        int requiredFrames = (int)(soundLength * framesPerSecond);
        
        //System.out.println( "Required=" + requiredFrames + " actual=" + blocks.size() );
        
        //--Add null blocks to the end to make up the correct number of SWF frames
        while( blocks.size() < requiredFrames ) blocks.add( null );
        
        return head;
    }
    
    /**
     * Makes a streaming SWF from an MP3.
     * args[0] = MP3 in filename
     * args[1] = SWF out filename
     */
    public static void main( String[] args ) throws IOException
    {
        InputStream mp3       = new FileInputStream( args[0] );
        SWFWriter   swfwriter = new SWFWriter( args[1] );
        
        SWFTagTypes tags = new TagWriter( swfwriter );
        
        tags.header( 5, -1, 2000, 2000, 30, -1 );
        tags.tagSetBackgroundColor( new Color(255,255,255));
        
        ArrayList<byte[]> blocks = new ArrayList<byte[]>();
        SoundStreamHead head = MP3Helper.streamingBlocks( mp3, 30, blocks );
        Iterator<byte[]> i = blocks.iterator();                 
		
		//write the stream header and the first block
        head.write( tags );
		
		if( i.hasNext() ) {
			byte[] soundData = (byte[]) i.next();
			tags.tagSoundStreamBlock( soundData );			
		}
		tags.tagShowFrame();

		//write subsequent blocks
        while( i.hasNext() )
        {
			byte[] soundData = i.next();
			tags.tagSoundStreamBlock( soundData );			
			tags.tagShowFrame();
        }
        
        SWFActions acts = tags.tagDoAction();
        SWFActionBlock actblock = acts.start(0);
        actblock.stop();
        actblock.end();
        acts.done();
        
        tags.tagShowFrame();
        tags.tagEnd();
    }	
}
