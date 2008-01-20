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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Vector;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFFileSignature;
import com.anotherbigidea.flash.interfaces.SWFShape;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.interfaces.SWFTags;
import com.anotherbigidea.flash.interfaces.SWFText;
import com.anotherbigidea.flash.interfaces.SWFVectors;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.flash.structs.AlphaColor;
import com.anotherbigidea.flash.structs.AlphaTransform;
import com.anotherbigidea.flash.structs.ButtonRecord;
import com.anotherbigidea.flash.structs.ButtonRecord2;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.structs.ColorTransform;
import com.anotherbigidea.flash.structs.Matrix;
import com.anotherbigidea.flash.structs.Rect;
import com.anotherbigidea.flash.structs.SoundInfo;

/**
 * A pass-through implementation of the SWFTagTypes and SWFFileSignature 
 * interfaces - useful as a base class
 */
public class SWFTagTypesImpl implements SWFTagTypes, SWFFileSignature 
{

    protected SWFTagTypes mTagtypes;
    
    /** No pass-thru */
    public SWFTagTypesImpl() {}
    
    /**
     * @param tags may be null
     */
    public SWFTagTypesImpl( SWFTagTypes tags )
    {
        mTagtypes = tags;
    }
    
    /**
     * SWFTags interface
     */    
    public void tag( int tagType, boolean longTag, byte[] contents ) 
        throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tag( tagType, longTag, contents );
    }

	/**
	 * @see SWFFileSignature#signature(String)
	 */
	public void signature( String sig ) {
		if( mTagtypes != null && mTagtypes instanceof SWFFileSignature ) {
			((SWFFileSignature) mTagtypes).signature( sig );
		}
	}

    /**
     * SWFHeader interface.
     * Sets movie length to -1 to force a recalculation since the length
     * cannot be guaranteed to be the same as the original.
     */
    public void header( int version, long length,
                        int twipsWidth, int twipsHeight,
                        int frameRate, int frameCount ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.header( version, length, twipsWidth, twipsHeight,
                                        frameRate, frameCount );
    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagEnd() throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagEnd();
    }

    /**
     * SWFTagTypes interface
     */
    public void tagDefineSound( int id, int format, int frequency,
                                boolean bits16, boolean stereo,
                                int sampleCount, byte[] soundData ) 
        throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagDefineSound( id, format, frequency,
                                bits16, stereo, sampleCount, soundData );    }        /**
     * SWFTagTypes interface
     */
    public void tagDefineButtonSound( int buttonId,                    int rollOverSoundId, SoundInfo rollOverSoundInfo,                    int rollOutSoundId,  SoundInfo rollOutSoundInfo,                    int pressSoundId,    SoundInfo pressSoundInfo,                    int releaseSoundId,  SoundInfo releaseSoundInfo )        throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagDefineButtonSound( buttonId,                    rollOverSoundId, rollOverSoundInfo,                    rollOutSoundId,  rollOutSoundInfo,                    pressSoundId,    pressSoundInfo,                    releaseSoundId,  releaseSoundInfo );    }    
    
    /**
     * SWFTagTypes interface
     */
    public void tagStartSound( int soundId, SoundInfo info ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagStartSound( soundId, info );    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagSoundStreamHead(         int playbackFrequency, boolean playback16bit, boolean playbackStereo,        int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,        int averageSampleCount ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagSoundStreamHead(             playbackFrequency, playback16bit, playbackStereo,            streamFormat, streamFrequency, stream16bit, streamStereo,            averageSampleCount );    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagSoundStreamHead2(         int playbackFrequency, boolean playback16bit, boolean playbackStereo,        int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,        int averageSampleCount ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagSoundStreamHead2(             playbackFrequency, playback16bit, playbackStereo,            streamFormat, streamFrequency, stream16bit, streamStereo,            averageSampleCount );    }
        /**
     * SWFTagTypes interface
     */
    public void tagSoundStreamBlock( byte[] soundData ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagSoundStreamBlock( soundData );    }        /**
     * SWFTagTypes interface
     */
    public void tagSerialNumber( byte[] serialNumber ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagSerialNumber( serialNumber );    }        /**
     * SWFTagTypes interface
     */
    public void tagGenerator( byte[] data ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagGenerator( data );    }        /**
     * SWFTagTypes interface
     */
    public void tagGeneratorText( byte[] data ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagGeneratorText( data );    }

    /**
     * SWFTagTypes interface
     */    public void tagGeneratorFont( byte[] data ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagGeneratorFont( data );    }         
    /**
     * SWFTagTypes interface
     */
    public void tagGeneratorCommand( byte[] data ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagGeneratorCommand( data );    }    

    /**
     * SWFTagTypes interface
     */
    public void tagNameCharacter( byte[] data ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagNameCharacter( data );    }                /**
     * SWFTagTypes interface
     */
    public void tagDefineBits( int id, byte[] imageData ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagDefineBits( id, imageData );    }                /**
     * SWFTagTypes interface
     */
    public void tagJPEGTables( byte[] jpegEncodingData ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagJPEGTables( jpegEncodingData );    }        
    /**
     * SWFTagTypes interface
     */
    public void tagDefineBitsJPEG3( int id, byte[] imageData, byte[] alphaData )         throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagDefineBitsJPEG3( id, imageData, alphaData );    }        
        
    /**
     * SWFTagTypes interface
     */
    public void tagShowFrame() throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagShowFrame();    }    
    /**
     * SWFTagTypes interface
     */
    public SWFActions tagDoAction() throws IOException    {        if( mTagtypes != null ) return mTagtypes.tagDoAction();
        return null;    }
    
	/**
	 * SWFTagTypes interface
	 */
	public SWFActions tagDoInitAction( int spriteId ) throws IOException
	{
		if( mTagtypes != null ) return mTagtypes.tagDoInitAction( spriteId );
		return null;
	}
	        /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape( int id, Rect outline ) throws IOException    {        if( mTagtypes != null ) return mTagtypes.tagDefineShape( id, outline );
        return null;    }
        /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape2( int id, Rect outline ) throws IOException    {
        if( mTagtypes != null ) return mTagtypes.tagDefineShape2( id, outline );        return null;    }
        /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape3( int id, Rect outline ) throws IOException    {        if( mTagtypes != null ) return mTagtypes.tagDefineShape3( id, outline );
        return null;    }    
    
    /**
     * SWFTagTypes interface
     */    
    public void tagFreeCharacter( int charId ) throws IOException 
    {
        if( mTagtypes != null ) mTagtypes.tagFreeCharacter( charId );     
    }
    
    /**
     * SWFTagTypes interface
     */    
    public void tagPlaceObject( int charId, int depth,                                 Matrix matrix, AlphaTransform cxform )         throws IOException
    {        if( mTagtypes != null ) mTagtypes.tagPlaceObject( charId, depth, matrix, cxform );
    }    
    /**
     * SWFTagTypes interface
     */    
    public SWFActions tagPlaceObject2( boolean isMove,
                                       int clipDepth,
                                       int depth,
                                       int charId,
                                       Matrix matrix,
                                       AlphaTransform cxform,
                                       int ratio,
                                       String name,
                                       int clipActionFlags )  
        throws IOException    
    {
        if( mTagtypes != null ) return mTagtypes.tagPlaceObject2( isMove, clipDepth, depth,
                                                        charId, matrix, cxform, ratio,
                                                        name, clipActionFlags );
        return null;
    }
        
    /**
     * SWFTagTypes interface
     */     public void tagRemoveObject( int charId, int depth ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagRemoveObject( charId, depth );    }
        
    /**
     * SWFTagTypes interface
     */     public void tagRemoveObject2(int depth ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagRemoveObject2( depth );    }

    /**
     * SWFTagTypes interface
     */     public void tagSetBackgroundColor( Color color ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagSetBackgroundColor( color );
    }    

    /**
     * SWFTagTypes interface
     */     public void tagFrameLabel( String label ) throws IOException    {
        if( mTagtypes != null ) mTagtypes.tagFrameLabel( label );    }
    
	/**
	 * SWFTagTypes interface
	 */     
	public void tagFrameLabel( String label, boolean isAnchor ) throws IOException {    
		if( mTagtypes != null ) mTagtypes.tagFrameLabel( label, isAnchor );
	}    
    
    /**
     * SWFTagTypes interface
     */     public SWFTagTypes tagDefineSprite( int id ) throws IOException
    {
        if( mTagtypes != null ) return mTagtypes.tagDefineSprite( id );
        return null;    }
    
    /**
     * SWFTagTypes interface
     */     public void tagProtect( byte[] password ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagProtect( password );    }    
    /**
     * SWFTagTypes interface
     */     public void tagEnableDebug( byte[] password ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagEnableDebug( password );    }
    
	/**
	 * SWFTagTypes interface
	 */ 
	public void tagEnableDebug2( byte[] password ) throws IOException
	{
		if( mTagtypes != null ) mTagtypes.tagEnableDebug2( password );
	}
        
    /**
     * SWFTagTypes interface
     */     public SWFVectors tagDefineFont( int id, int numGlyphs ) throws IOException
    {
        if( mTagtypes != null ) return mTagtypes.tagDefineFont( id, numGlyphs );        return null;    }

    /**
     * SWFTagTypes interface
     */     public void tagDefineFontInfo( int fontId, String fontName, int flags, int[] codes )
        throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagDefineFontInfo( fontId, fontName, flags, codes );    }
	/**
	 * SWFTagTypes interface
	 */ 
	public void tagDefineFontInfo2( int fontId, String fontName, int flags, int[] codes, int languageCode )
		throws IOException
	{
		if( mTagtypes != null ) mTagtypes.tagDefineFontInfo2( fontId, fontName, flags, codes, languageCode );
	}
	    
    /**
     * SWFTagTypes interface
     */     public SWFVectors tagDefineFont2( int id, int flags, String name, int numGlyphs,
                                      int ascent, int descent, int leading,
                                      int[] codes, int[] advances, Rect[] bounds,
                                      int[] kernCodes1, int[] kernCodes2,
                                      int[] kernAdjustments ) throws IOException
    {
        if( mTagtypes != null ) return mTagtypes.tagDefineFont2( id, flags, name, numGlyphs,
                                      ascent, descent, leading, codes, advances,
                                      bounds, kernCodes1, kernCodes2, kernAdjustments );
        return null;    }
    
    /**
     * SWFTagTypes interface
     */     public void tagDefineTextField( int fieldId, String fieldName,
                    String initialText, Rect boundary, int flags,
                    AlphaColor textColor, int alignment, int fontId, int fontSize, 
                    int charLimit, int leftMargin, int rightMargin, int indentation,
                    int lineSpacing ) 
        throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagDefineTextField( fieldId, fieldName, initialText,
                               boundary, flags, textColor, alignment, fontId,
                               fontSize, charLimit, leftMargin, rightMargin,
                               indentation, lineSpacing );
    }

    /**
     * SWFTagTypes interface
     */     public SWFText tagDefineText( int id, Rect bounds, Matrix matrix )
        throws IOException
    {         if( mTagtypes != null ) return mTagtypes.tagDefineText( id, bounds, matrix );        
        return null;    }    /**
     * SWFTagTypes interface
     */     public SWFText tagDefineText2( int id, Rect bounds, Matrix matrix ) throws IOException
    {         if( mTagtypes != null ) return mTagtypes.tagDefineText2( id, bounds, matrix );
        return null;    }    
    /**
     * SWFTagTypes interface
     */     public SWFActions tagDefineButton( int id, Vector<ButtonRecord> buttonRecords )
        throws IOException
    {
        if( mTagtypes != null ) return mTagtypes.tagDefineButton( id, buttonRecords );
        return null;    }
    
    /**
     * SWFTagTypes interface
     */     public void tagButtonCXForm( int buttonId, ColorTransform transform ) 
        throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagButtonCXForm( buttonId, transform );    }        
    /**
     * SWFTagTypes interface
     */     public SWFActions tagDefineButton2( int id,                                         boolean trackAsMenu,                                         Vector<ButtonRecord2> buttonRecord2s )
        throws IOException
    {
        if( mTagtypes != null ) return mTagtypes.tagDefineButton2( id, trackAsMenu, 
                                                         buttonRecord2s );        return null;    }
    /**
     * SWFTagTypes interface
     */     public void tagExport( String[] names, int[] ids ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagExport( names, ids );
    }
    
    /**
     * SWFTagTypes interface
     */     public void tagImport( String movieName, String[] names, int[] ids ) 
        throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagImport( movieName, names, ids );
    }

    /**
     * SWFTagTypes interface
     */ 
    public void tagImportAssets2( String movieName, String[] names, int[] ids ) 
        throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagImportAssets2( movieName, names, ids );
    }
    
    /**
     * SWFTagTypes interface
     */     public void tagDefineQuickTimeMovie( int id, String filename ) throws IOException
    {        if( mTagtypes != null ) mTagtypes.tagDefineQuickTimeMovie( id, filename );
    }    
    /**
     * SWFTagTypes interface
     */     public void tagDefineBitsJPEG2( int id, byte[] data ) throws IOException
    {        if( mTagtypes != null ) mTagtypes.tagDefineBitsJPEG2( id, data );
    }        /**
     * SWFTagTypes interface
     */     public void tagDefineBitsJPEG2( int id, InputStream jpegImage ) throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagDefineBitsJPEG2( id, jpegImage );    }
        /**
     * SWFTagTypes interface
     */     public SWFShape tagDefineMorphShape( int id, Rect startBounds, Rect endBounds )         throws IOException
    {
        if( mTagtypes != null ) return mTagtypes.tagDefineMorphShape( id, startBounds, endBounds );
        return null;    }    
    
    /**
     * SWFTagTypes interface
     */     public void tagDefineBitsLossless( int id, int format, int width, int height,                                       Color[] colors, byte[] imageData )        throws IOException
    {        if( mTagtypes != null ) mTagtypes.tagDefineBitsLossless( id , format, width, height,
                                                       colors, imageData );
    }
        /**
     * SWFTagTypes interface
     */ 
    public void tagDefineBitsLossless2( int id, int format, int width, int height,                                        Color[] colors, byte[] imageData )        throws IOException
    {
        if( mTagtypes != null ) mTagtypes.tagDefineBitsLossless2( id , format, width, height,
                                                        colors, imageData );
    }
    
    /**
     * @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagScriptLimits(int, int)
     */
    public void tagScriptLimits(int maxRecursionDepth, int scriptTimeoutSecs)
        throws IOException {
        if( mTagtypes != null ) mTagtypes.tagScriptLimits( maxRecursionDepth, scriptTimeoutSecs );
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes#tagTabOrder(int, int)
     */
    public void tagTabOrder(int depth, int tabOrder) throws IOException {
        if( mTagtypes != null ) mTagtypes.tagTabOrder( depth, tabOrder );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagDefineVideoStream(int, int, int, int, int, int) */
    public void tagDefineVideoStream(int id, int numFrames, int width,
            int height, int flags, int codec) throws IOException {
        if( mTagtypes != null ) 
            mTagtypes.tagDefineVideoStream( id, numFrames, width,height, flags, codec );
    }
    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagVideoFrame(int, int, byte[]) */
    public void tagVideoFrame(int streamId, int seqNum, int frameType, int codec, byte[] videoPacket)
            throws IOException {
        if( mTagtypes != null ) mTagtypes.tagVideoFrame( streamId, seqNum, frameType, codec, videoPacket );
    }
    
    /** @see com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes#tagDoABC(int, java.lang.String) */
    public ABC tagDoABC(int flags, String filename) throws IOException {
        if( mTagtypes != null ) return mTagtypes.tagDoABC( flags, filename );
        return null;
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes#tagSymbolClass(java.util.Map) */
    public void tagSymbolClass(Map<Integer, String> classes) throws IOException {
        if( mTagtypes != null ) mTagtypes.tagSymbolClass( classes );
    }
    
    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagFileAttributes(int) */
    public void tagFileAttributes(int flags) throws IOException {
        if( mTagtypes != null ) mTagtypes.tagFileAttributes( flags );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagDefineBinaryData(int, byte[]) */
    public void tagDefineBinaryData(int id, byte[] data) throws IOException {
        if( mTagtypes != null ) mTagtypes.tagDefineBinaryData( id, data );
    }
    
    /**
     * Convenience method to read from a SWF file and pass it to this instance.
     * 
     * @param swfFile the file to read.
     */
    public void readFromSWF( File swfFile ) throws IOException {
        SWFTags swfTags = new TagParser( this );

        InputStream in = new FileInputStream( swfFile );
        try {
            SWFReader swfReader = new SWFReader( swfTags, in );
            swfReader.readFile();
        } finally {
            in.close();
        }         
    }
}