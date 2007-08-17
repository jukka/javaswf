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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.epistem.io.OutStream;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * ADPCM Utilities
 */
public class ADPCMHelper
{
    public class ADPCMPacket
    {
        public int initialLeftSample  = 0;
        public int initialLeftIndex   = 0;
        public int initialRightSample = 0;
        public int initialRightIndex  = 0;
        public int[] leftData;
        public int[] rightData;
        public int sampleCount;
    }
    
    protected AudioInputStream audioIn;
    protected boolean isStereo;
    protected boolean is16Bit;
    protected int sampleRate;
    protected int rate;
    protected int samplesPerFrame;
    protected boolean isSigned;
    
    protected int sampleCount = 0;
    
    protected ADPCMEncodeStream leftEncoder;
    protected ADPCMEncodeStream rightEncoder;
    protected ADPCMPacket currentPacket;
    
    public ADPCMHelper( InputStream audioFile, int framesPerSecond )
        throws IOException, UnsupportedAudioFileException
    {
        audioIn = AudioSystem.getAudioInputStream( new BufferedInputStream( audioFile ) );
        
        AudioFormat format = audioIn.getFormat();
        int frameSize    = format.getFrameSize();
        isStereo = format.getChannels() == 2;
        is16Bit  = format.getSampleSizeInBits() > 8;
        sampleRate = (int)format.getSampleRate();
        
        isSigned = format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;
        
        //System.out.println( format + " FrameSize=" + frameSize );
        
        if     ( sampleRate >= 44000 ) sampleRate = 44000;
        else if( sampleRate >= 22000 ) sampleRate = 22000;
        else if( sampleRate >= 11000 ) sampleRate = 11000;
        else sampleRate = 5500;

        rate = SWFConstants.SOUND_FREQ_5_5KHZ;        
        if     ( sampleRate == 44000 ) rate = SWFConstants.SOUND_FREQ_44KHZ;
        else if( sampleRate == 22000 ) rate = SWFConstants.SOUND_FREQ_22KHZ;
        else if( sampleRate == 11000 ) rate = SWFConstants.SOUND_FREQ_11KHZ;        
        
        samplesPerFrame = sampleRate / framesPerSecond;
        
        FramedInputStream frameIn = new FramedInputStream( audioIn, frameSize );
        
        leftEncoder = new ADPCMEncodeStream( frameIn, is16Bit, isSigned );
        
        if( isStereo )
        {
            rightEncoder = new ADPCMEncodeStream( frameIn, is16Bit, isSigned );
        }
    }
    
    public SoundDefinition getSoundDefinition() throws IOException
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutStream out = new OutStream( bout );
        sampleCount = 0;
        boolean first = true;
        
        for( ADPCMPacket packet = readPacket( ADPCMConstants.PACKET_SIZE );
             packet != null;
             packet = readPacket( ADPCMConstants.PACKET_SIZE ) )
        {
            sampleCount += packet.sampleCount + 1;
            writePacket( packet, out, first );
            first = false;
            
            //System.out.println( "Packet size = " + packet.leftData.length );
        }
        
        out.flush();
        byte[] soundData = bout.toByteArray();
        return new SoundDefinition( SWFConstants.SOUND_FORMAT_ADPCM, rate, true, isStereo, sampleCount, soundData );
    }
    
    /**
     * @return null if no more packets are available
     */
    public ADPCMPacket readPacket( int packetSize ) throws IOException
    {
        ADPCMPacket packet = new ADPCMPacket();
        packet.initialLeftSample = leftEncoder.getFirstPacketSample();
        if( leftEncoder.isDone() ) return null;

        int count = 0;
        
        if( isStereo )
        {
            packet.initialRightSample = rightEncoder.getFirstPacketSample();
        }
        
        packet.initialLeftIndex = leftEncoder.setIndex( packet.initialLeftSample );
        
        if( isStereo )
        {
            packet.initialRightIndex  = rightEncoder.setIndex( packet.initialRightSample );
        }
        
        packet.leftData = new int[ packetSize-1 ];;
        if( isStereo ) packet.rightData = new int[ packetSize-1 ];;
        
        for( int i = 0; i < packetSize-1; i++ )
        {
            packet.leftData[i] = leftEncoder.getDelta();

            if( ! leftEncoder.isDone() ) count++;
            
            if( isStereo ) packet.rightData[i] = rightEncoder.getDelta();
        }
        
        packet.sampleCount = count;
        return packet;
    }
    
    /**
     * Streaming block
     * @return null if no more blocks
     */
    public byte[] getBlockData( boolean firstBlock ) throws IOException
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutStream os = new OutStream( bout );
        
        currentPacket = readPacket( samplesPerFrame );
        if( currentPacket == null ) return null;
        
        writePacket( currentPacket, os, true );        
        os.flushBits();
        
        return bout.toByteArray();
    }

    public void writePacket( ADPCMPacket packet, OutStream out, boolean includeBitCount ) throws IOException 
    {
        if( packet == null ) return;

        int sample = packet.initialLeftSample;
        
        if( includeBitCount ) out.writeUBits( 2, 2 );
        out.writeUBits( 16, sample );
        out.writeUBits( 6, packet.initialLeftIndex );
               
        if( isStereo )
        {
            sample = packet.initialRightSample;
        
            out.writeUBits( 16, sample );
            out.writeUBits( 6, packet.initialRightIndex );
        }
        
        for( int i = 0; i < packet.sampleCount; i++ )
        {
            out.writeUBits( 4, packet.leftData[i] );
            if( isStereo ) out.writeUBits( 4, packet.rightData[i] );            
        }
    }
    
    public SoundStreamHead getStreamHeader()
    {
        return new SoundStreamHead( rate, true, isStereo,
                                    SWFConstants.SOUND_FORMAT_ADPCM,
                                    rate, true, isStereo, 
                                    samplesPerFrame );
    }
    
    /**
     * InputStream wrapper that ensures AudioInputStream is read on a frame-by-frame basis
     */
    public static class FramedInputStream extends InputStream 
    {
        protected InputStream in;
        protected byte[] frameData;
        protected int dataPtr;
        protected int frameSize;
        protected boolean done = false;
        
        public FramedInputStream( InputStream in, int frameSize )
        {
            this.in = in;
            this.frameSize  = frameSize;
            frameData = new byte[ frameSize ];
            dataPtr = frameSize;
        }
        
        public int read() throws IOException
        {
            if( dataPtr < frameData.length )
            {
                int val = frameData[dataPtr++];
                if( val < 0 ) val = val + 0x100;
                return val;
            }
            
            if( done ) return -1;
            
            dataPtr = 0;
            int read = 0;
            
            while( dataPtr < frameSize && (read = in.read( frameData, dataPtr, frameSize-dataPtr)) >= 0 )
            {
                dataPtr += read;
            }
            
            if( dataPtr == 0 )
            {
                done = true;
                return -1;
            }
            
            while( dataPtr < frameData.length ) frameData[dataPtr++] = 0;
            dataPtr = 0;
            
            return read();
        }
    }
   
    
    
    /**
     * Makes a streaming SWF from a Java Sound compatible audio file.
     * args[0] = audio in filename
     * args[1] = SWF out filename
     */
    public static void main( String[] args ) throws Exception
    {
        InputStream audioFile     = new FileInputStream( args[0] );
        SWFWriter       swfwriter = new SWFWriter( args[1] );
        
        SWFTagTypes tags = new TagWriter( swfwriter );
        
        tags.header( 5, -1, 200, 200, 12, -1 );
        tags.tagSetBackgroundColor( new Color(255,255,255));
        
        ADPCMHelper helper = new ADPCMHelper( audioFile, 12 );
                          
        SoundStreamHead header = helper.getStreamHeader();
        
        header.write( tags );

        byte[] block = helper.getBlockData( true );
        
        while( block != null )
        {
            tags.tagSoundStreamBlock( block );
            tags.tagShowFrame();           
            
            block = helper.getBlockData( false );
        }
        
        SWFActions acts = tags.tagDoAction();
        SWFActionBlock actblock = acts.start(0);
        actblock.stop();
        actblock.end();
        acts.done();
        
        tags.tagShowFrame();
        tags.tagEnd();
        
        audioFile.close();
    }
}
