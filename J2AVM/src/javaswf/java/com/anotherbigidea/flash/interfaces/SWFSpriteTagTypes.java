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
package com.anotherbigidea.flash.interfaces;

import java.io.IOException;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.structs.AlphaTransform;
import com.anotherbigidea.flash.structs.Matrix;
import com.anotherbigidea.flash.structs.SoundInfo;

/**
 * Interface for passing SWF tag types that can be used in a movie or a sprite
 */
public interface SWFSpriteTagTypes extends SWFTags 
{    /**
     * Start/stop playing a sound
     */
    public void tagStartSound( int soundId, SoundInfo info ) throws IOException;
    
    /**
     * Only allows ADPCM encoding.
     * 
     * @param streamFormat must be SWFConstants.SOUND_FORMAT_ADPCM
     * @param playbackFrequency one of the SWFConstants.SOUND_FREQ_* constants
     * @param streamFrequency   one of the SWFConstants.SOUND_FREQ_* constants
     */    public void tagSoundStreamHead(         int playbackFrequency, boolean playback16bit, boolean playbackStereo,        int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,        int averageSampleCount ) throws IOException;
    
    /**
     * Allows any encoding.
     * 
     * @param streamFormat      one of the SWFConstants.SOUND_FORMAT_* constants
     * @param playbackFrequency one of the SWFConstants.SOUND_FREQ_* constants
     * @param streamFrequency   one of the SWFConstants.SOUND_FREQ_* constants
     */    public void tagSoundStreamHead2(         int playbackFrequency, boolean playback16bit, boolean playbackStereo,        int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,        int averageSampleCount ) throws IOException;
        /**
     * @param soundData format-dependent sound data
     */
    public void tagSoundStreamBlock( byte[] soundData ) throws IOException;      
     
    public void tagEnd() throws IOException;  
    
    public void tagShowFrame() throws IOException;

    /**
     * @return SWFActions to receive actions - or null to skip the data
     */    public SWFActions tagDoAction() throws IOException;

	/**
	 * @param spriteId the sprite that the actions relate to.
	 * @return SWFActions to receive actions - or null to skip the data
	 */
	public SWFActions tagDoInitAction( int spriteId ) throws IOException;
    
    /**
     * The AVM2 ABC tag
     * 
     * @param flags combination of the DO_ABC_* flags
     * @param filename the filename of the ABC file
     * @return interface to receive one or more ABC data files - null to skip
     */
    public ABC tagDoABC( int flags, String filename ) throws IOException;
    
    /**
     * Remove a symbol definition from the dictionary
     * @param charId the symbol id
     */
    public void tagFreeCharacter( int charId ) throws IOException;    
    /**
     * Set the tab order of the symbol at a given depth. 
     * @param depth the depth of the symbol.
     * @param tabOrder the tab order.
     */
    public void tagTabOrder( int depth, int tabOrder ) throws IOException;
        /**
     * @param cxform may be null
     */
    public void tagPlaceObject( int charId, int depth, Matrix matrix, AlphaTransform cxform ) throws IOException;    
    public void tagRemoveObject( int charId, int depth ) throws IOException;        /**
     * @param clipDepth < 1 if not relevant
     * @param charId < 1 if not relevant
     * @param name of sprite instance - null if not relevant
     * @param ratio < 0 if not relevant
     * @param matrix null if not relevant
     * @param cxform null if not relevant
     * @param clipActionFlags == 0 if there are no clip actions - otherwise
     *        this is the OR of the condition flags on all the clip action blocks
     * @return null if there are no clip actions or they are irrelevant
     */
    public SWFActions tagPlaceObject2( boolean isMove,
                                       int clipDepth,
                                       int depth,
                                       int charId,
                                       Matrix matrix,
                                       AlphaTransform cxform,
                                       int ratio,
                                       String name,
                                       int clipActionFlags )  throws IOException;    
    public void tagRemoveObject2( int depth ) throws IOException;
	/**
	 * Define the label for the current frame
	 */    
    public void tagFrameLabel( String label ) throws IOException;

	/**
	 * Define the label for the current frame
	 * @param isAnchor true if this is an anchor frame (for browser navigation),
	 * Flash MX+ only.
	 */    
	public void tagFrameLabel( String label, boolean isAnchor ) throws IOException;
}
