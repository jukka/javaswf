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

import java.io.IOException;

import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;

/**
 * A writer that implements the SWFActions interface and writes
 * action bytes to an Tag
 */
public class ActionWriter implements SWFActions
{
    protected TagWriter mTagWriter;
    protected int       mFlashVersion;
    
    
    public ActionWriter( TagWriter tagWriter, int flashVersion ) {
        mTagWriter = tagWriter;        
        mFlashVersion = flashVersion;
    }
    
    /**
     * SWFActions interface
     */
    public SWFActionBlock start( int conditions ) throws IOException {
        return new ActionBlockWriter( mFlashVersion ) {
            public void end() throws IOException {
                super.end();
                byte[] data = getActionData();
                writeBytes( data );
            }
        };
    }    
    
    /**
     * SWFActions interface
     */
    public SWFActionBlock start( int conditions, int keycode ) throws IOException {
    	return start( conditions ); //keycode not written here    
    }
    
    /**
     * SWFActions interface
     */
    public void done() throws IOException {
    	mTagWriter.completeTag();
    }
    
    protected void writeBytes( byte[] bytes ) throws IOException {
        mTagWriter.getOutStream().write( bytes );
    }
}
