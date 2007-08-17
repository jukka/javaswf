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
package com.anotherbigidea.flash.writers;

import java.io.*;

import org.epistem.io.*;

import com.anotherbigidea.flash.*;
import com.anotherbigidea.flash.structs.Rect;
import com.anotherbigidea.flash.interfaces.*;

/**
 * Implements the SWFTags interface and writes a SWF file to the output stream
 */
public class SWFWriter implements SWFTags, SWFFileSignature
{
    protected OutStream    mOut;
    protected OutputStream mOutputstream;
    protected ByteArrayOutputStream mByteout;
    protected String mSignature = null;
    protected boolean recalculateLengthAndCount = false;
    
    //--deferred header values
    protected int  frameCount;
    protected int  version;
    protected Rect frameSize;
    protected int  height;
    protected int  rate;
    
    /**
     * Write to a file.
     */
    public SWFWriter( String filename ) throws IOException {
    	this( new FileOutputStream( filename ) );
    }
    
    /**
     * Write to an output stream - closing it at the end.
     */
    public SWFWriter( OutputStream outputstream )
    {
        mOutputstream = outputstream;
        mOut = new OutStream( outputstream );
    } 
    
	/**
	 * Write to an out stream - closing it at the end.
	 */
    public SWFWriter( OutStream outstream )
    {
        mOut = outstream;
    }
    
    /**
     * Sets a flag such that the SWF length and frame count will be set to
     * unknown when they are passed in the header - this forces those values
     * to be recalculated.
     */
    public void recalculateLengthAndCount() {
        recalculateLengthAndCount = true;
    }
    
	/**
	 * Sets the file signature if it has not been set already.
	 * The setCompression(..) method calls this and thus prevents any further
	 * changes to the signature - overriding any signature passed down from the
	 * SWFReader.
	 * 
	 * @see SWFFileSignature#signature(String)
	 */    
    public void signature( String sig ) {
    	if( mSignature != null ) return;
		mSignature = sig;    	
    }
    
    /**
     * Set Flash MX+ zip-compression on or off.  
     * It sets the file signature and thus prevents any different signature
     * from being specified.  Only the first call to this method has any effect,
     * and only if signature(..) has not been called yet.
     * 
     * @param compressionOn true for compression, false for no compression.
     */
    public void setCompression( boolean compressionOn ) {
		signature( compressionOn ?
						SWFFileSignature.SIGNATURE_COMPRESSED :
						SWFFileSignature.SIGNATURE_NORMAL );
    }
    
    /**
     * Writes the header unless the length or framecount are unknown (as
     * signified by a negative value), in which case the header values are
     * stored and written later, when the unknown value(s) can be determined.
     * 
     * @see SWFHeader#header(int, long, int, int, int, int)
     */
    public void header( int version, long length,
                        int twipsWidth, int twipsHeight,
                        int frameRate, int frameCount ) throws IOException
    {
        frameSize = new Rect( 0, 0, twipsWidth, twipsHeight );        

        if( recalculateLengthAndCount ) {
            length     = -1;
            frameCount = -1;
        }
        
        //--Unknown values
        if( length < 0 || frameCount < 0 )
        {
            //--defer the header
            this.version    = version;
            this.rate       = frameRate;
            this.frameCount = 0;
                
            //--set up a byte array for the output
            if( mByteout == null )
            {
                mByteout = new ByteArrayOutputStream( 20000 );
                mOut = new OutStream( mByteout );
            }
            
			return;
        }            

        writeHeader( version, length, frameRate, frameCount );        
    }
    
    /**
     * @see SWFTags#tag(int, boolean, byte[])
     */
    public void tag( int tagType, boolean longTag, 
                     byte[] contents ) throws IOException
    {
        //System.out.println( "OUT Tag " + tagType + " " + longTag + " " + ( (contents==null) ? 0 : contents.length) );
        //System.out.println();
        
        int length = (contents != null ) ? contents.length : 0;
        longTag = ( length > 62 ) || longTag;
        
        int hdr = ( tagType << 6 ) + ( longTag ? 0x3f : length );

        mOut.writeUI16( hdr );
        
        if( longTag ) mOut.writeUI32( length );        
        
        if( contents != null ) mOut.write( contents );
        
        if( tagType == SWFConstants.TAG_SHOWFRAME ) frameCount++;        
        if( tagType == SWFConstants.TAG_END       ) finish();
    }
    
    protected void writeHeader( int version, long length,
                                int frameRate, int frameCount ) throws IOException 
    {        
		writeSignature();
        mOut.writeUI8( version );
        mOut.writeUI32( length );        
        
        //may be compressed from this point onwards
        if( mSignature.equals( SWFFileSignature.SIGNATURE_COMPRESSED ) ) {
        	mOut.writeCompressed();
        }
        
        frameSize.write( mOut );
        mOut.writeUI16( frameRate << 8 );
        mOut.writeUI16( frameCount );    
    }
    
    private void writeSignature() throws IOException {
    	if( mSignature == null ) signature( SWFFileSignature.SIGNATURE_NORMAL );
		mOut.write( mSignature.getBytes( "US-ASCII" ));
    }
    
    /**
     * Finish writing
     */
    protected void finish() throws IOException
    {
        //--Writing to a byte array - need to recalculate lengths
        if( mByteout != null )
        {
            byte[] bytes = mByteout.toByteArray();

            long length = 12L + frameSize.getLength() + bytes.length;

            mOut = new OutStream( mOutputstream );
            
            writeHeader( version, length, rate, frameCount );
            
            mOut.write( bytes );
        }

		mOut.close();
		mOut = null;
    }
    
    /**
     * Force close of output stream(s) - this is not normally necessary since
     * normal completion of writing a SWF will close the output.  This method
     * can be called in case of abnormal termination to ensure that the output
     * stream or file is closed. 
     */
    public void close() {        
        if( mOutputstream != null ) {
            try{ mOutputstream.close(); } catch( IOException ignored ) {}
        }

        if( mOut != null ) {
            try{ mOut.close(); } catch( IOException ignored ) {}
        }
    }
}
