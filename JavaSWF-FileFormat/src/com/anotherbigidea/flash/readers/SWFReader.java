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
package com.anotherbigidea.flash.readers;

import java.io.*;

import org.epistem.io.*;

import com.anotherbigidea.flash.structs.Rect;
import com.anotherbigidea.flash.*;
import com.anotherbigidea.flash.interfaces.*;
import com.anotherbigidea.flash.writers.SWFWriter;

/**
 * Reads a SWF input stream and drives the SWFTags interface.
 * 
 * Automatically decompresses the file if necessary.
 */
public class SWFReader
{
    protected SWFTags     mConsumer;
    protected InStream    mIn;
    protected InputStream mInputstream;
    protected boolean     mCompressed;
    protected String      mFilename;
    
    /**
     * True to skip over tags that cause a problem during parsing, rather than
     * throwing an exception.
     */
    public boolean ignoreProblemTags = false;
    
    /**
     * True to cleanly end the file (generate an end tag) if an unexpected
     * end-of-file occurs on the input. 
     */
    public boolean copeWithUnexpectedEOF = false;
    
	/**
	 * Read from a file.
	 * Must call readFile() in order to properly close the file.
	 * 
	 * @param consumer may also implement the SWFFileSignature interface.
	 */
	public SWFReader( SWFTags consumer, String filename ) throws IOException {
		this( consumer, new FileInputStream( filename ) );
		mFilename = filename;
	}
    
    /**
     * @param consumer may also implement the SWFFileSignature interface.
     */
    public SWFReader( SWFTags consumer, InputStream inputstream )
    {
        mConsumer    = consumer;
        mInputstream = inputstream;
        mIn          = new InStream( inputstream );
    }

	/**
	 * @param consumer may also implement the SWFFileSignature interface.
	 */
    public SWFReader( SWFTags consumer, InStream instream )
    {
        mConsumer = consumer;
        mIn       = instream;
    }    
    
    /**
     * Drive the consumer by reading a SWF File - including the header and all tags
     */
    public void readFile() throws IOException
    {
        try {
	        readHeader();
	        readTags();
        } finally {
            //ensure that input is closed
			if( mFilename != null ) mInputstream.close();
        }
    }
    
    /**
     * Drive the consumer by reading SWF tags only.  The full header must have
     * been read prior to this.
     */
    public void readTags() throws IOException 
    {
        while( readOneTag() != SWFConstants.TAG_END );
    }
    
    /**
     * Drive the consumer by reading one tag
     * @return the tag type, -1 if the tag could not be parsed and 
     *         ignoreProblemTags is true
     */
    public int readOneTag() throws IOException 
    {
        boolean problemTag = false;
        
        try {
	        int header = mIn.readUI16();
	        
	        int  type   = header >> 6;    //only want the top 10 bits
	        int  length = header & 0x3F;  //only want the bottom 6 bits
	        boolean longTag = (length == 0x3F);
	        
	        if( longTag ) {
	            length = (int)mIn.readUI32();
	        }
	        	        
	        
            byte[] contents = mIn.read( length );

            try {
                mConsumer.tag( type, longTag, contents );
            } catch( IOException ioe ) {
                if( ignoreProblemTags ) {
                    return -1;
                }
                    
                problemTag = true;
                throw ioe;
            }
	        
                
            return type;

        } catch( IOException ioe ) {

            //terminate 
            if( copeWithUnexpectedEOF && ! problemTag ) {
                mConsumer.tag( SWFConstants.TAG_END, false, new byte[0] );
                return SWFConstants.TAG_END;
            }
            
            throw ioe;
        }
    }
    
    /**
     * Read and verify just the file signature.
     */
    public void readSignature() throws IOException {
		int[] sig = { mIn.readUI8(), mIn.readUI8(), mIn.readUI8() };
    	
		//--Verify File Signature
		if( ( sig[0] != 0x46 && sig[0] != 0x43 ) || // "F" or "C"
			( sig[1] != 0x57 ) ||  // "W"
			( sig[2] != 0x53 ) )   // "S"
		{
			throw new IOException( "Invalid SWF File Signature" );
		}

		mCompressed = sig[0] == 0x43;
		
		if( mConsumer instanceof SWFFileSignature ) {
			((SWFFileSignature) mConsumer).signature( 
				mCompressed ?
					SWFFileSignature.SIGNATURE_COMPRESSED :
					SWFFileSignature.SIGNATURE_NORMAL );  	
		}
    }
    
    /** 
     * Read the SWF file header - including the signature.
     */
    public void readHeader() throws IOException {
    	readSignature();
    	readRemainderOfHeader();
    }
    
    /**
     * Read the header after the signature.  The signature must have been read
     * prior to this.
     */
    public void readRemainderOfHeader() throws IOException {

        int  version   = mIn.readUI8();
        long length    = mIn.readUI32();
        
        //may be compressed from this point onwards
        if( mCompressed ) mIn.readCompressed();
        
        Rect frameSize = new Rect( mIn );
        int frameRate  = mIn.readUI16() >> 8;
        int frameCount = mIn.readUI16();                
        
        mConsumer.header( version, length, 
                          frameSize.getMaxX(), frameSize.getMaxY(), 
                          frameRate, frameCount );                         
    }
    
    /**
     * Test: read from args[0] and write to args[1].
     * 
     * If args[2] is '+' then output is forced to be compressed, if it is '-'
     * then output is forced to be uncompressed - otherwise the output is the
     * same as the input.
     */
    public static void main( String[] args ) throws IOException {    	
        SWFWriter writer = new SWFWriter( args[1] );
        
        if( args.length > 2 ) {
        	if     ( args[2].equals( "+" )) writer.setCompression( true  );
        	else if( args[2].equals( "-" )) writer.setCompression( false );
        }        
        
        SWFReader reader = new SWFReader( writer, args[0] );
        reader.readFile();
    }
}
