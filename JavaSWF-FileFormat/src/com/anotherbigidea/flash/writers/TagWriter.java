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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import java.util.zip.DeflaterOutputStream;

import org.epistem.io.OutStream;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.interfaces.*;
import com.anotherbigidea.flash.structs.*;

/**
 * A writer that implements the SWFTagTypes interface and writes
 * to a SWFTags interface
 */
public class TagWriter implements SWFTagTypes, SWFConstants, SWFFileSignature 
{
    protected SWFTags mTags;
        
    protected OutStream out;
    protected ByteArrayOutputStream bytes;
    protected int     tagType;
    protected boolean longTag;
    protected int version;
    protected String mStringEncoding = SWFConstants.STRING_ENCODING_MX;
    
    public TagWriter( SWFTags tags )
    {
        mTags = tags;
    }
    
    protected OutStream getOutStream() { return out; }
    
    protected SWFActions factorySWFActions()
    {
        return new ActionWriter( this, version );    }
    
    protected SWFShape factorySWFShape( boolean hasAlpha, boolean hasStyle )
    {
        return new SWFShapeImpl( this, hasAlpha, hasStyle );
    }
    
	/**
	 * @see SWFFileSignature#signature(String)
	 */
	public void signature( String sig ) {
		if( mTags instanceof SWFFileSignature ) {
			((SWFFileSignature) mTags).signature( sig );
		}
	}    
    
    /**
     * Start a new tag context
     */
    protected void startTag( int tagType, boolean longTag )
    {
        this.tagType = tagType;
        this.longTag = longTag;
        
        bytes = new ByteArrayOutputStream( 10000 );
        out   = new OutStream( bytes );
    }
    
    /**
     * Start a new definition tag context
     */
    protected void startTag( int tagType, int id, boolean longTag )
        throws IOException 
    {
        startTag( tagType, longTag );        
        out.writeUI16( id );
    }
        
    /**
     * Finish the tag context and write the tag
     */
    protected void completeTag() throws IOException 
    {
        out.flush();
        byte[] contents = bytes.toByteArray();
        
        out = null;
        bytes = null;
        
        mTags.tag( tagType, longTag, contents );        
    }
    
    /**
     * SWFTags interface
     */    
    public void tag( int tagType, boolean longTag, byte[] contents ) 
        throws IOException
    {
        mTags.tag( tagType, longTag, contents );
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
        this.version = version;
        mTags.header( version, -1, twipsWidth, twipsHeight, frameRate, frameCount );
        
        //set encoding to Ascii if pre-MX
        if( version < SWFConstants.FLASH_MX_VERSION ) {
        	mStringEncoding = SWFConstants.STRING_ENCODING_PRE_MX; 
        } 
    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagEnd() throws IOException
    {
        mTags.tag( TAG_END, false, null );
    }

    /**
     * SWFTagTypes interface
     */
    public void tagShowFrame() throws IOException
    {        mTags.tag( TAG_SHOWFRAME, false, null );
    }    
    /**
     * SWFTagTypes interface
     */
    public void tagDefineSound( int id, int format, int frequency,
                                boolean bits16, boolean stereo,
                                int sampleCount, byte[] soundData ) 
        throws IOException
    {
        startTag( TAG_DEFINESOUND, id, true );        out.writeUBits( 4, format );
        out.writeUBits( 2, frequency );
        out.writeUBits( 1, bits16 ? 1 : 0 );
        out.writeUBits( 1, stereo ? 1 : 0 );
        out.writeUI32 ( sampleCount );
        out.write( soundData );        completeTag();
    }        /**
     * SWFTagTypes interface
     */
    public void tagDefineButtonSound( int buttonId,                    int rollOverSoundId, SoundInfo rollOverSoundInfo,                    int rollOutSoundId,  SoundInfo rollOutSoundInfo,                    int pressSoundId,    SoundInfo pressSoundInfo,                    int releaseSoundId,  SoundInfo releaseSoundInfo )        throws IOException    {        startTag( TAG_DEFINEBUTTONSOUND, buttonId, true );
                out.writeUI16( rollOverSoundId );
        if( rollOverSoundId != 0 ) rollOverSoundInfo.write( out );        out.writeUI16( rollOutSoundId );
        if( rollOutSoundId != 0 ) rollOutSoundInfo.write( out );        
        out.writeUI16( pressSoundId );
        if( pressSoundId != 0 ) pressSoundInfo.write( out );
        out.writeUI16( releaseSoundId );
        if( releaseSoundId != 0 ) releaseSoundInfo.write( out );
        
        completeTag();    }
        /**
     * SWFTagTypes interface
     */
    public void tagStartSound( int soundId, SoundInfo info ) throws IOException    {        startTag( TAG_STARTSOUND, soundId, false );
        info.write( out );
        completeTag();    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagSoundStreamHead(         int playbackFrequency, boolean playback16bit, boolean playbackStereo,        int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,        int averageSampleCount ) throws IOException    {
        writeSoundStreamHead( TAG_SOUNDSTREAMHEAD,
            playbackFrequency, playback16bit, playbackStereo,            streamFormat, streamFrequency, stream16bit, streamStereo,            averageSampleCount );    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagSoundStreamHead2(         int playbackFrequency, boolean playback16bit, boolean playbackStereo,        int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,        int averageSampleCount ) throws IOException    {
        writeSoundStreamHead( TAG_SOUNDSTREAMHEAD2,
            playbackFrequency, playback16bit, playbackStereo,            streamFormat, streamFrequency, stream16bit, streamStereo,            averageSampleCount );    }
    
    public void writeSoundStreamHead( int tag,        int playbackFrequency, boolean playback16bits, boolean playbackStereo,        int streamFormat, int streamFrequency, boolean stream16bits, boolean streamStereo,        int averageSampleCount ) throws IOException        {
        startTag( tag, false );
                out.writeUBits(4,0);
        out.writeUBits(2,playbackFrequency);
        out.writeUBits(1, playback16bits ? 1 : 0 );
        out.writeUBits(1, playbackStereo ? 1 : 0 );
        
        out.writeUBits(4,streamFormat);
        out.writeUBits(2,streamFrequency);
        out.writeUBits(1, stream16bits ? 1 : 0 );
        out.writeUBits(1, streamStereo ? 1 : 0 );        
        out.writeUI16 (averageSampleCount);
        
        if( streamFormat == SWFConstants.SOUND_FORMAT_MP3 )
        {
            out.writeUI16 (0); //unknown
        }
        
        completeTag();    }
        /**
     * SWFTagTypes interface
     */
    public void tagSoundStreamBlock( byte[] soundData ) throws IOException
    {        startTag( TAG_SOUNDSTREAMBLOCK, true );
        out.write( soundData );        completeTag();
    }        /**
     * SWFTagTypes interface
     */
    public void tagSerialNumber( byte[] serialNumber ) throws IOException
    {        startTag( TAG_SERIALNUMBER, false );        out.write( serialNumber );        completeTag();
    }        /**
     * SWFTagTypes interface
     */
    public void tagGenerator( byte[] data ) throws IOException
    {        startTag( TAG_FLASHGENERATOR, false );        out.write( data );        completeTag();
    }        /**
     * SWFTagTypes interface
     */
    public void tagGeneratorText( byte[] data ) throws IOException
    {        startTag( TAG_GENERATOR_TEXT, false );        out.write( data );        completeTag();
    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagGeneratorCommand( byte[] data ) throws IOException
    {        startTag( TAG_TEMPLATECOMMAND, false );        out.write( data );        completeTag();
    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagGeneratorFont( byte[] data ) throws IOException
    {        startTag( TAG_GEN_EXTERNAL_FONT, false );        out.write( data );        completeTag();
    }
    
    /**
     * SWFTagTypes interface
     */
    public void tagNameCharacter( byte[] data ) throws IOException
    {        startTag( TAG_NAMECHARACTER, false );        out.write( data );        completeTag();
    }        /**
     * SWFTagTypes interface
     */
    public void tagDefineBits( int id, byte[] imageData ) throws IOException
    {        startTag( TAG_DEFINEBITS, id, true );        out.write( imageData );        completeTag();
    }          /**
     * SWFTagTypes interface
     */
    public void tagJPEGTables( byte[] jpegEncodingData ) throws IOException    {
        startTag( TAG_JPEGTABLES, true );        out.write( jpegEncodingData );        completeTag();
    }
    /**
     * SWFTagTypes interface
     */
    public void tagDefineBitsJPEG3( int id, byte[] imageData, byte[] alphaData ) throws IOException    {
        startTag( TAG_DEFINEBITSJPEG3, id, true );
        out.writeUI32( imageData.length );        out.write( imageData );        out.write( alphaData );        completeTag();            }            
    /**
     * SWFTagTypes interface
     */
    public SWFActions tagDoAction() throws IOException    {
        startTag( TAG_DOACTION, true );
        return factorySWFActions();    }
    
	/**
	 * SWFTagTypes interface
	 */
	public SWFActions tagDoInitAction( int spriteId ) throws IOException {
		startTag( TAG_DOINITACTION, spriteId, true );
		return factorySWFActions();		    
	}
        /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape( int id, Rect outline ) throws IOException    {
        startShape( TAG_DEFINESHAPE, id, outline );
        return factorySWFShape( false, true );    }
        /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape2( int id, Rect outline ) throws IOException    {        startShape( TAG_DEFINESHAPE2, id, outline );
        return factorySWFShape( false, true );    }
        /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape3( int id, Rect outline ) throws IOException    {
        startShape( TAG_DEFINESHAPE3, id, outline );
        return factorySWFShape( true, true );            }    
    
    /**
     * SWFTagTypes interface
     */    
    public void tagFreeCharacter( int charId ) throws IOException 
    {
        startTag( TAG_FREECHARACTER, false );
        out.writeUI16( charId );
        completeTag();
    }
    
    /**
     * SWFTagTypes interface
     */    
    public void tagPlaceObject( int charId, int depth,                                 Matrix matrix, AlphaTransform cxform )         throws IOException
    {
        startTag( TAG_PLACEOBJECT, false );        out.writeUI16( charId );        
        out.writeUI16( depth );
        matrix.write ( out );
        if( cxform != null ) cxform.write( out );        completeTag();
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
        boolean hasClipActions = ( clipActionFlags != 0 );
        
        startTag( TAG_PLACEOBJECT2, false );
        
        out.writeUBits( 1, hasClipActions   ? 1 : 0 );
        out.writeUBits( 1, (clipDepth > 0)  ? 1 : 0 );
        out.writeUBits( 1, (name != null)   ? 1 : 0 );
        out.writeUBits( 1, (ratio >= 0)     ? 1 : 0 );
        out.writeUBits( 1, (cxform != null) ? 1 : 0 );
        out.writeUBits( 1, (matrix != null) ? 1 : 0 );
        out.writeUBits( 1, (charId > 0)     ? 1 : 0 );
        out.writeUBits( 1, isMove           ? 1 : 0 );
    
        out.writeUI16( depth );
        
        if( charId > 0     ) out   .writeUI16( charId );
        if( matrix != null ) matrix.write    ( out );
        if( cxform != null ) cxform.write    ( out );
        if( ratio >= 0     ) out   .writeUI16( ratio );        
        if( name != null   ) {
            out.writeString( name, mStringEncoding );
            longTag = true;
        }
		if( clipDepth > 0  ) out   .writeUI16( clipDepth );
        
        if( hasClipActions )
        {
			final boolean isMX = version >= SWFConstants.FLASH_MX_VERSION;        	
        	
            out.writeUI16( 0 );  //unknown
            
            if( isMX ) out.writeUI32( clipActionFlags );
            else       out.writeUI16( clipActionFlags );

            return new ActionWriter( this, version )
            {
            	private int mKeycode = -1;
            	
                public SWFActionBlock start( int conditions ) throws IOException
                {
					if( isMX ) mTagWriter.out.writeUI32( conditions );
					else       mTagWriter.out.writeUI16( conditions );
					return super.start( conditions );
                }      

				public SWFActionBlock start( int conditions, int keycode ) throws IOException
				{
					mKeycode = keycode;
					return start( conditions );
				}      
                
                protected void writeBytes( byte[] bytes ) throws IOException
                {
                	if( mKeycode >= 0 ) {
						mTagWriter.out.writeUI32( bytes.length + 1 );
						mTagWriter.out.writeUI8( mKeycode );
                	} else {
						mTagWriter.out.writeUI32( bytes.length );
                	}
                    super.writeBytes( bytes );
                }
               
                public void done() throws IOException
                {
					if( isMX ) mTagWriter.out.writeUI32( 0 );
					else       mTagWriter.out.writeUI16( 0 );
                    super.done();
                }                
            };
        }
        
        completeTag();
        return null;
    }
        
    /**
     * SWFTagTypes interface
     */     public void tagRemoveObject( int charId, int depth ) throws IOException
    {        startTag( TAG_REMOVEOBJECT, false );
        out.writeUI16( charId );
        out.writeUI16( depth );        completeTag();
    }
        
    /**
     * SWFTagTypes interface
     */     public void tagRemoveObject2(int depth ) throws IOException
    {        startTag( TAG_REMOVEOBJECT2, false );
        out.writeUI16( depth );        completeTag();
    }

    /**
     * SWFTagTypes interface
     */     public void tagSetBackgroundColor( Color color ) throws IOException
    {
        startTag( TAG_SETBACKGROUNDCOLOR, false ); 
        color.writeRGB( out );
        completeTag();
    }

    /**
     * SWFTagTypes interface
     */     public void tagFrameLabel( String label ) throws IOException    {        tagFrameLabel( label, false );
    }
    
	/**
	 * SWFTagTypes interface
	 */     
	public void tagFrameLabel( String label, boolean isAnchor ) throws IOException {    
		startTag( TAG_FRAMELABEL, false ); 
		out.writeString( label, mStringEncoding );
		if( isAnchor ) out.writeUI8( 1 );		
		completeTag();
	}
    
    /**
     * SWFTagTypes interface
     */     public SWFTagTypes tagDefineSprite( int id ) throws IOException
    {
        startTag( TAG_DEFINESPRITE, id, true ); 
        
        out.writeUI16( 0 ); //framecount - to be filled in later
        
        TagWriter writer = new TagWriter( new SpriteTags() );
        writer.version = version;
        
        return writer;
    }
    
    /**
     * SWFTagTypes interface
     */     public void tagProtect( byte[] password ) throws IOException
    {        mTags.tag( TAG_PROTECT, false, password ); 
    }    
    /**
     * SWFTagTypes interface
     */     public void tagEnableDebug( byte[] password ) throws IOException
    {        mTags.tag( TAG_ENABLEDEBUG, false, password ); 
    }  
	/**
	 * SWFTagTypes interface
	 */ 
	public void tagEnableDebug2( byte[] password ) throws IOException
	{
		startTag( TAG_ENABLEDEBUGGER2, 0, false );
		out.write( password );
		completeTag(); 
	}        
	
    /**
     * SWFTagTypes interface
     */     public SWFVectors tagDefineFont( int id, int numGlyphs ) throws IOException
    {        startTag( TAG_DEFINEFONT, id, true );                    return new SWFShapeImpl( this, numGlyphs );    }

    /**
     * SWFTagTypes interface
     */     public void tagDefineFontInfo( int fontId, String fontName, int flags, int[] codes )
        throws IOException
    {        startTag( TAG_DEFINEFONTINFO, true );
        out.writeUI16( fontId );
        
        byte[] chars = fontName.getBytes();
        out.writeUI8( chars.length );
        out.write( chars );
        
        out.writeUI8( flags );
        
        boolean wide = (flags & FONT_WIDECHARS) != 0;
        
        for( int i = 0; i < codes.length; i++ )
        {
            if( wide ) out.writeUI16( codes[i] );
            else       out.writeUI8 ( codes[i] );
        }                             
                completeTag();
    }
	/**
	 * SWFTagTypes interface
	 */ 
	public void tagDefineFontInfo2( int fontId, String fontName, int flags, int[] codes, int languageCode )
		throws IOException
	{
		startTag( TAG_DEFINEFONTINFO2, true );
		out.writeUI16( fontId );
        
		byte[] chars = fontName.getBytes();
		out.writeUI8( chars.length );
		out.write( chars );
        
        flags |= SWFConstants.FONT_WIDECHARS; //always wide
		out.writeUI8( flags );
		out.writeUI8( languageCode );
        
		for( int i = 0; i < codes.length; i++ )
		{
			out.writeUI16( codes[i] );
		}                             
        
		completeTag();
	}
    
    /**
     * SWFTagTypes interface
     */     public SWFVectors tagDefineFont2( int id, int flags, String name, int numGlyphs,
                                      int ascent, int descent, int leading,
                                      int[] codes, int[] advances, Rect[] bounds,
                                      int[] kernCodes1, int[] kernCodes2,
                                      int[] kernAdjustments ) throws IOException
    {
        startTag( TAG_DEFINEFONT2, id, true );

        out.writeUI8( flags );
        out.writeUI8( 0  );     //reserved flags
        
        byte[] nameBytes = name.getBytes();
        out.writeUI8 ( nameBytes.length );
        out.write    ( nameBytes );
        out.writeUI16( numGlyphs );
        
        return new Font2ShapeImpl( this, flags, numGlyphs, ascent, descent, leading,
                                   codes, advances, bounds, 
                                   kernCodes1, kernCodes2, kernAdjustments );
    }
    
    /**
     * SWFTagTypes interface
     */     public void tagDefineTextField( int fieldId, String fieldName,
                    String initialText, Rect boundary, int flags,
                    AlphaColor textColor, int alignment, int fontId, int fontSize, 
                    int charLimit, int leftMargin, int rightMargin, int indentation,
                    int lineSpacing ) 
        throws IOException
    {
        flags |= 0x2005;
        
        startTag( TAG_DEFINETEXTFIELD, fieldId, true ); 
                
        boundary.write( out );
        
        out.writeUI16( flags );
        out.writeUI16( fontId );
        out.writeUI16( fontSize );
        textColor.write( out );
        
        if( (flags & TEXTFIELD_LIMIT_CHARS ) != 0 )
        {
            out.writeUI16( charLimit );
        }
        
        out.writeUI8 ( alignment   );
        out.writeUI16( leftMargin  );
        out.writeUI16( rightMargin );
        out.writeUI16( indentation );
        out.writeUI16( lineSpacing );
                
        out.writeString( fieldName, mStringEncoding );
        
        if( (flags & TEXTFIELD_HAS_TEXT ) != 0 )
        {
            out.writeString( initialText, mStringEncoding );
        }                
        
        completeTag();
    }

    /**
     * SWFTagTypes interface
     */     public SWFText tagDefineText( int id, Rect bounds, Matrix matrix )
        throws IOException
    { 
        startTag( TAG_DEFINETEXT, id, true );        return defineText( bounds, matrix, false );
    }    /**
     * SWFTagTypes interface
     */     public SWFText tagDefineText2( int id, Rect bounds, Matrix matrix ) throws IOException
    { 
        startTag( TAG_DEFINETEXT2, id, true );        return defineText( bounds, matrix, true );
    }    
    /**
     * SWFTagTypes interface
     */     public SWFActions tagDefineButton( int id, Vector buttonRecords )
        throws IOException
    {
        startTag( TAG_DEFINEBUTTON, id, true );
        
        ButtonRecord.write( out, buttonRecords );
        System.out.println( "BUTTON" );
        return new ActionWriter( this, version );
    }
    
    /**
     * SWFTagTypes interface
     */     public void tagButtonCXForm( int buttonId, ColorTransform transform ) 
        throws IOException
    {        startTag( TAG_DEFINEBUTTONCXFORM, buttonId, false );
        transform.writeWithoutAlpha( out );        completeTag();
    }        
    /**
     * SWFTagTypes interface
     */     public SWFActions tagDefineButton2( int id,                                         boolean trackAsMenu,                                         Vector buttonRecord2s )
        throws IOException
    {        startTag( TAG_DEFINEBUTTON2, id, true ); 
        out.writeUI8( trackAsMenu ? 1 : 0 );

        return new ButtonActionWriter( this, version, buttonRecord2s );    }
    /**
     * SWFTagTypes interface
     */     public void tagExport( String[] names, int[] ids ) throws IOException
    {
        startTag( TAG_EXPORT, true );
        
        int count = ids.length;

        out.writeUI16( count );

        for( int i = 0; i < count; i++ )
        {
            //System.out.println( "Exporting " + ids[i] + " as " + names[i] );
            out.writeUI16  ( ids  [i] );
            out.writeString( names[i], mStringEncoding );
        }        
        
        completeTag();
    }
    
    /**
     * SWFTagTypes interface
     */     public void tagImport( String movieName, String[] names, int[] ids ) 
        throws IOException
    {
        startTag( TAG_IMPORT_ASSETS, true );
        
        int count = ids.length;
        
        out.writeString( movieName, mStringEncoding );
        out.writeUI16  ( count );

        for( int i = 0; i < count; i++ )
        {
            //System.out.println( "Importing " + names[i] + " as " + ids[i] + " from " + movieName );
            out.writeUI16  ( ids  [i] );
            out.writeString( names[i], mStringEncoding );
        }
        
        completeTag();
    }

    /**
     * SWFTagTypes interface
     */ 
    public void tagImportAssets2( String movieName, String[] names, int[] ids ) 
        throws IOException
    {
        startTag( TAG_IMPORT_ASSETS_2, true );
        
        int count = ids.length;
        
        out.writeString( movieName, mStringEncoding );
        out.writeUI8   ( 1 );
        out.writeUI8   ( 0 );
        out.writeUI16  ( count );

        for( int i = 0; i < count; i++ )
        {
            //System.out.println( "Importing " + names[i] + " as " + ids[i] + " from " + movieName );
            out.writeUI16  ( ids  [i] );
            out.writeString( names[i], mStringEncoding );
        }
        
        completeTag();
    }

    
    /**
     * SWFTagTypes interface
     */     public void tagDefineQuickTimeMovie( int id, String filename ) throws IOException
    {
        startTag( TAG_DEFINEQUICKTIMEMOVIE, id, true );        out.writeString( filename, mStringEncoding );        completeTag();
    }    
    /**
     * SWFTagTypes interface
     */     public void tagDefineBitsJPEG2( int id, byte[] data ) throws IOException
    {
        startTag( TAG_DEFINEBITSJPEG2, id, true );        out.write( data );        completeTag();
    }
    /**
     * SWFTagTypes interface
     */     public void tagDefineBitsLossless( int id, int format, int width, int height,                                       Color[] colors, byte[] imageData )        throws IOException    {
        writeBitsLossless( id, format, width, height, colors, imageData, false );    }
        /**
     * SWFTagTypes interface
     */     public void tagDefineBitsLossless2( int id, int format, int width, int height,                                        Color[] colors, byte[] imageData )        throws IOException    {
        writeBitsLossless( id, format, width, height, colors, imageData, true );    }
    
    
    public void writeBitsLossless( int id, int format, int width, int height,                                   Color[] colors, byte[] imageData, boolean hasAlpha )        throws IOException    {        startTag( hasAlpha ? TAG_DEFINEBITSLOSSLESS2 : TAG_DEFINEBITSLOSSLESS,
                  id, true );        out.writeUI8 ( format );
        out.writeUI16( width  );
        out.writeUI16( height );
        
        switch( format )
        {
            case BITMAP_FORMAT_8_BIT:  out.writeUI8 ( colors.length - 1 ); break;
            case BITMAP_FORMAT_16_BIT: out.writeUI16( colors.length - 1 ); break;
            case BITMAP_FORMAT_32_BIT: break;
            default: throw new IOException( "unknown bitmap format: " + format );
        }                
               //--zip up the colors and the bitmap data
        DeflaterOutputStream deflater = new DeflaterOutputStream( bytes );
        OutStream zipOut = new OutStream( deflater );
                        if( format == BITMAP_FORMAT_8_BIT || format == BITMAP_FORMAT_16_BIT )        {
            for( int i = 0; i < colors.length; i++ )
            {
                if( hasAlpha ) colors[i].writeWithAlpha( zipOut );                 else           colors[i].writeRGB( zipOut );
            }        }
        zipOut.write( imageData );                zipOut.flush();        deflater.finish();        
        completeTag();    }
        /**
     * SWFTagTypes interface
     */     public void tagDefineBitsJPEG2( int id, InputStream jpegImage ) throws IOException
    {        startTag( TAG_DEFINEBITSJPEG2, id, true );        
        //--Write stream terminator/header
        out.writeUI8( 0xff );
        out.writeUI8( 0xd9 );
        out.writeUI8( 0xff );
        out.writeUI8( 0xd8 );
        
        int read = 0;
        byte[] bytes = new byte[10000];
        
        while( (read = jpegImage.read( bytes )) >= 0 )
        {
            out.write( bytes, 0, read );
        }
        
        jpegImage.close();        
        completeTag();
    }
        /**
     * SWFTagTypes interface
     */     public SWFShape tagDefineMorphShape( int id, Rect startBounds, Rect endBounds )         throws IOException
    {
        startTag( TAG_DEFINEMORPHSHAPE, id, true );
        startBounds.write( out );
        endBounds  .write( out );
        return new MorphShapeImpl( this );
    }
    
    //-----------------------------------------------------------------------

        
    protected static class ButtonActionWriter extends ActionWriter
    {
        //offsets is a vector of int[]{ offsetPtr, offsetValue }
        protected Vector offsets = new Vector();
        protected int lastPtr;
        protected OutStream tagout;
        
        public ButtonActionWriter( TagWriter tagWriter, 
                                   int flashVersion, 
                                   Vector<ButtonRecord2> buttonRecs )
            throws IOException 
        {
            super( tagWriter, flashVersion );

            tagout = tagWriter.getOutStream();
            
            //--save ptr to first offset location
            lastPtr = (int)tagout.getCount();
            
            tagout.writeUI16( 0 ); //will be calculated later
            
            //--write the button records
            ButtonRecord2.write( tagout, buttonRecs );
        }
        
        public SWFActionBlock start( int conditions ) throws IOException
        {
            //--save ptr to offset location and offset value
            int ptr    = (int)tagout.getCount();
            int offset = ptr - lastPtr;
            
            offsets.addElement( new int[] { lastPtr, offset } );
            
            lastPtr = ptr;
            
            tagout.writeUI16( 0 );  //will be calculated later
            tagout.writeUI16( conditions );            
        
            return super.start( conditions );
        }      
                
        public void done() throws IOException
        {
            //--save last offset
            offsets.addElement( new int[] { lastPtr, 0 } );
            
            tagout.flush();
            byte[] contents = mTagWriter.bytes.toByteArray();
        
            mTagWriter.out = null;
            mTagWriter.bytes = null;
        
            //--fix the offsets
            for( Enumeration e = offsets.elements(); e.hasMoreElements(); )
            {
                int[] offInfo = (int[])e.nextElement();
                int ptr = offInfo[0];
                int off = offInfo[1];
                
                byte[] offbytes = OutStream.uintTo2Bytes( off );
                contents[ ptr     ] = offbytes[0];
                contents[ ptr + 1 ] = offbytes[1];
            }
            
            mTagWriter.mTags.tag( mTagWriter.tagType, true, contents );
        }                
    }
    
    protected SWFText defineText( Rect bounds, Matrix matrix, boolean hasAlpha ) 
        throws IOException 
    {
        bounds.write( out );
        matrix.write( out );

        return new SWFTextImpl( hasAlpha );
    }
    
    protected class SWFTextImpl implements SWFText 
    {
        protected boolean hasAlpha;

        protected int maxGlyphIndex = 0;
        protected int maxAdvance    = 0;
        
        // Text records are stored as
        // Object[] { int[]{ font, height }, int[]{ x }, int[]{ y }, Color }, or
        // Object[] { int[] glyphs, int[] advances }
        protected Vector recs = new Vector();
        protected Object[] currentStyleRecord = null;
        
        
        public SWFTextImpl( boolean hasAlpha )
        {
            this.hasAlpha = hasAlpha;
        }
        
        protected Object[] getCurrentStyle()
        {
            if( currentStyleRecord == null )
            {
                currentStyleRecord = new Object[4];
                recs.addElement( currentStyleRecord );
            }

            return currentStyleRecord;
        }
        
        /**
         * SWFText interface
         */
        public void font( int fontId, int textHeight ) throws IOException
        {
            getCurrentStyle()[0] = new int[] { fontId, textHeight };
        }
    
        /**
         * SWFText interface
         */
        public void color( Color color ) throws IOException
        {
            getCurrentStyle()[3] = color;
        }
    
        /**
         * SWFText interface
         */
        public void setX( int x ) throws IOException
        {
            getCurrentStyle()[1] = new int[] { x };
        }

        /**
         * SWFText interface
         */
        public void setY( int y ) throws IOException
        {
            getCurrentStyle()[2] = new int[] { y };
        }
    
        /**
         * SWFText interface
         */
        public void text( int[] glyphIndices, int[] glyphAdvances ) throws IOException
        {
            currentStyleRecord = null;
            recs.addElement( new Object[] { glyphIndices, glyphAdvances } );
            
            for( int i = 0; i < glyphIndices.length; i++ )
            {
                if( glyphIndices[i] > maxGlyphIndex ) maxGlyphIndex = glyphIndices[i];
                if( glyphAdvances[i] > maxAdvance   ) maxAdvance    = glyphAdvances[i];
            }
        }
    
        /**
         * SWFText interface
         */
        public void done() throws IOException
        {
            int glyphBits   = OutStream.determineUnsignedBitSize( maxGlyphIndex );
            int advanceBits = OutStream.determineSignedBitSize  ( maxAdvance );               
               
            out.writeUI8( glyphBits );
            out.writeUI8( advanceBits );

            for( Enumeration e = recs.elements(); e.hasMoreElements(); )
            {
                Object[] rec = (Object[])e.nextElement();

                if( rec.length == 4 ) //style record
                {
                    boolean hasFont  = rec[0] != null;
                    boolean hasX     = rec[1] != null;
                    boolean hasY     = rec[2] != null;
                    boolean hasColor = rec[3] != null;
                    
                    int flags = 0x80;
                    if( hasFont  ) flags |= TEXT_HAS_FONT;
                    if( hasX     ) flags |= TEXT_HAS_XOFFSET;
                    if( hasY     ) flags |= TEXT_HAS_YOFFSET;
                    if( hasColor ) flags |= TEXT_HAS_COLOR;

                    out.writeUI8( flags );
            
                    if( hasFont )
                    {
                        out.writeUI16( ((int[])rec[0])[0] ); //fontId
                    }
            
                    if( hasColor )
                    {
                        Color color = (Color)rec[3];
                        
                        if( hasAlpha ) color.writeWithAlpha( out );
                        else           color.writeRGB( out );
                    }

                    if( hasX )
                    {
                        int xOffset = ((int[])rec[1])[0];
                        out.writeSI16( (short)xOffset );
                    }

                    if( hasY ) //x & y are in reverse order from flag bits
                    {
                        int yOffset = ((int[])rec[2])[0];
                        out.writeSI16( (short)yOffset );
                    }

                    if( hasFont )
                    {
                        out.writeUI16( ((int[])rec[0])[1] ); //textHeight
                    }                        
                }
                else //glyph record
                {
                    int[] glyphs   = (int[])rec[0];
                    int[] advances = (int[])rec[1];
                    
                    out.writeUI8( glyphs.length );
            
                    for( int i = 0; i < glyphs.length; i++ )
                    {
                        out.writeUBits( glyphBits,   glyphs[i]   );
                        out.writeSBits( advanceBits, advances[i] );
                    }                                
                }
            }

            out.writeUI8( 0 );  //record terminator

            completeTag();
        }        
    }
    
    
    protected class SpriteTags implements SWFTags 
    {
        protected int frameCount = 0;
        
        public void tag( int tagType2, boolean longTag2, 
                         byte[] contents ) throws IOException
        {
            int length = (contents != null ) ? contents.length : 0;
            longTag2 = ( length > 62 ) || longTag2;
                
            int hdr = ( tagType2 << 6 ) + ( longTag2 ? 0x3f : length );

            out.writeUI16( hdr );
                            
            if( longTag2 ) out.writeUI32( length );        
                
            if( contents != null ) out.write( contents );
                
            if( tagType2 == SWFConstants.TAG_SHOWFRAME ) frameCount++;
            
            if( tagType2 == SWFConstants.TAG_END )
            {
                out.flush();
                contents = bytes.toByteArray();
        
                out = null;
                bytes = null;
        
                byte[] fc = OutStream.uintTo2Bytes( frameCount );
                contents[2] = fc[0];
                contents[3] = fc[1];
                
                mTags.tag( tagType, longTag, contents );                  
            }
        }
        
        public void header( int version, long length,
                            int twipsWidth, int twipsHeight,
                            int frameRate, int frameCount ) throws IOException
        {
        }
    }    
    
    protected void startShape( int tagType, int id, Rect outline ) throws IOException
    {
        startTag( tagType, id, true );
        outline.write( out );
    }
    
    /** 
     * Implementation of the SWFShape interface
     */
    protected static class SWFShapeImpl implements SWFShape 
    {
        protected boolean hasAlpha;
        protected boolean hasStyle;
        protected boolean outstandingChanges = true;
        protected boolean initialStyles = false;
        
        protected int fill0Index = -1;
        protected int fill1Index = -1;
        protected int lineIndex  = -1;
        
        protected int[] moveXY;
        protected Vector lineStyles = new Vector();
        protected Vector fillStyles = new Vector();
        
        protected int fillBits;
        protected int lineBits;
        
        protected int    glyphCount = 0;
        protected Vector glyphByteArrays;
        
        protected OutStream out;
        protected TagWriter writer;
        protected ByteArrayOutputStream bout;
        
        /**
         * For shapes (other than glyphs)
         */
        public SWFShapeImpl( TagWriter writer, boolean hasAlpha, boolean hasStyle )
        {
            this.hasAlpha  = hasAlpha;
            this.hasStyle  = hasStyle;
            this.writer    = writer;
            out = writer.getOutStream();
        }

        /**
         * For glyphs
         */
        public SWFShapeImpl( TagWriter writer, int glyphCount ) 
        {
            this( writer, false, false );
            this.glyphCount = glyphCount;
            bout = new ByteArrayOutputStream();
            out  = new OutStream( bout );
            glyphByteArrays = new Vector();
            
            fill1Index = 1;                
            lineIndex  = 0;            
        }

       
        public void done() throws IOException
        {
            if( ! initialStyles )
            {
                writeInitialStyles();
                initialStyles = true;
            }
            
            out.writeUBits( 6, 0 );  //end record            
            out.flushBits();
         
            if( bout != null && glyphCount > 0 ) //capturing bytes internally
            {
                byte[] glyphBytes = bout.toByteArray();
                glyphByteArrays.addElement( glyphBytes );
            }
            
            if( glyphCount > 1 )
            {
                bout = new ByteArrayOutputStream();
                out  = new OutStream( bout );
                glyphCount--;
                
                fill1Index = 1;
                lineIndex  = 0;
                outstandingChanges = true;
                initialStyles = false;
            }
            else
            {
                if( bout != null ) finishFont();
                writer.completeTag();
            }
        }
    
        protected void finishFont() throws IOException
        {
            out = writer.getOutStream();
            
            int glyphCount = glyphByteArrays.size();
            
            //--Write first shape offset
            int offset = glyphCount * 2;
            out.writeUI16( offset );
        
            //--Write subsequent shape offsets
            for( int i = 0; i < glyphCount - 1; i++ )
            {
                offset += ((byte[])glyphByteArrays.elementAt(i)).length;
                out.writeUI16( offset );        
            }

            //--Write shapes        
            for( int i = 0; i < glyphCount; i++ )
            {
                out.write( (byte[])glyphByteArrays.elementAt(i) );
            }                            
        }
        
        public void setFillStyle0( int styleIndex ) throws IOException
        {
            fill0Index = styleIndex;
            outstandingChanges = true;
        }
    
        public void setFillStyle1( int styleIndex ) throws IOException
        {
            fill1Index = styleIndex;
            outstandingChanges = true;
        }
    
        public void setLineStyle( int styleIndex ) throws IOException
        {
            lineIndex = styleIndex;
            outstandingChanges = true;
        }

        public void defineFillStyle( Color color ) throws IOException
        {
            fillStyles.addElement( new FillStyle( color ) );
            outstandingChanges = true;
        }

        public void defineFillStyle( Matrix matrix, int[] ratios,
                                     Color[] colors, boolean radial )
            throws IOException
        {
            fillStyles.addElement( new FillStyle( matrix, ratios, colors, radial ) );
            outstandingChanges = true;
        }

        public void defineFillStyle( int bitmapId, Matrix matrix, boolean clipped, boolean smoothed )
            throws IOException
        {
            fillStyles.addElement( new FillStyle( bitmapId, matrix, clipped, smoothed ) );
            outstandingChanges = true;
        }
    
        public void defineLineStyle( int width, Color color ) throws IOException
        {
            lineStyles.addElement( new LineStyle( width, color ) );
            outstandingChanges = true;            
        }

        public void line( int deltaX, int deltaY ) throws IOException
        {
            if( outstandingChanges ) flushChangeRecords();

            int numBits = OutStream.determineSignedBitSize( deltaX );
            int dyBits  = OutStream.determineSignedBitSize( deltaY );
            
            if( dyBits  > numBits ) numBits = dyBits;            
            if( numBits < 2 ) numBits = 2;
            
            out.writeUBits( 2, 3 );  //11b = line record
            
            out.writeUBits( 4, numBits - 2 );
            
            if( deltaX != 0 && deltaY != 0 ) //general line
            {
                out.writeUBits( 1, 1 );
                out.writeSBits( numBits, deltaX );
                out.writeSBits( numBits, deltaY );
            }
            else //horz or vert line
            {
                out.writeUBits( 1, 0 );
                
                if( deltaY != 0 ) //vert line
                {
                    out.writeUBits( 1, 1 );
                    out.writeSBits( numBits, deltaY );
                }
                else ///horz line
                {
                    out.writeUBits( 1, 0 );
                    out.writeSBits( numBits, deltaX );
                }
            }        
        }
    
        public void curve( int cx, int cy, int dx, int dy ) throws IOException
        {
            if( outstandingChanges ) flushChangeRecords();
            
            int numBits = OutStream.determineSignedBitSize( cx );
            int dyBits  = OutStream.determineSignedBitSize( cy );
            int adxBits = OutStream.determineSignedBitSize( dx );
            int adyBits = OutStream.determineSignedBitSize( dy );
            
            if( dyBits  > numBits ) numBits = dyBits;
            if( adxBits > numBits ) numBits = adxBits;
            if( adyBits > numBits ) numBits = adyBits;
            
            if( numBits < 2 ) numBits = 2;
            
            out.writeUBits( 2, 2 );  //10b = curve record
            
            out.writeUBits( 4, numBits - 2 );
            
            out.writeSBits( numBits, cx );
            out.writeSBits( numBits, cy );
            out.writeSBits( numBits, dx );
            out.writeSBits( numBits, dy );
        }
    
        public void move( int x, int y ) throws IOException
        {
            moveXY = new int[] { x, y };
            outstandingChanges = true;
        }
        
        protected void flushChangeRecords() throws IOException
        {            
            if( ! initialStyles )
            {
                writeInitialStyles();
                initialStyles = true;
            }

            writeChangeRecord();
            
            outstandingChanges = false;            
        }
        
        protected void writeInitialStyles() throws IOException
        {   
            out.flushBits();       
        
            fillBits = OutStream.determineUnsignedBitSize( fillStyles.size() );
            lineBits = OutStream.determineUnsignedBitSize( lineStyles.size() );

            //--For fonts - the fillstyle bits must be one
            if( ! hasStyle )
            {
                fillBits = 1;
            }
            else
            {
                writeStyles( fillStyles );
                writeStyles( lineStyles );        
                out.flushBits(); 
            }
       
                        
            out.writeUBits( 4, fillBits );
            out.writeUBits( 4, lineBits );        
        }
        
        protected void writeChangeRecord() throws IOException
        {
            boolean hasNewStyles = hasStyle &&
                       ( fillStyles.size() > 0 || lineStyles.size() > 0 );
            
            boolean hasMoveTo = ( moveXY != null );
            boolean hasFillStyle0 = fill0Index >= 0;
            boolean hasFillStyle1 = fill1Index >= 0;
            boolean hasLineStyle  = lineIndex >= 0;
            
            if( (! hasStyle) && hasFillStyle0 ) hasFillStyle1 = false;
            
            if( hasNewStyles )
            {
                out.writeUBits( 1, 0 );  //non-edge record
                out.writeUBits( 1, 1 );  //defines new styles
                out.writeUBits( 1, 1 );
                out.writeUBits( 1, 1 );
                out.writeUBits( 1, 1 );
                out.writeUBits( 1, 1 );
                
                //--Clear existing styles
                writeMoveXY( 0, 0 );
                out.writeUBits( fillBits, 0 );
                out.writeUBits( fillBits, 0 );
                out.writeUBits( lineBits, 0 );
                
                if( fill0Index == 0 ) fill0Index = -1;
                if( fill1Index == 0 ) fill1Index = -1;
                if( lineIndex  == 0 ) lineIndex  = -1;
                
                fillBits = OutStream.determineUnsignedBitSize( fillStyles.size() );
                lineBits = OutStream.determineUnsignedBitSize( lineStyles.size() );

                writeStyles( fillStyles );
                writeStyles( lineStyles );
                
                out.writeUBits( 4, fillBits );
                out.writeUBits( 4, lineBits ); 
                
                writeChangeRecord();
                return;
            }               
            
            if( hasFillStyle0 || hasFillStyle1 || hasLineStyle || hasMoveTo )
            {
                out.writeUBits( 1, 0 );  //non-edge record
                out.writeUBits( 1, 0 );
                out.writeUBits( 1, hasLineStyle  ? 1 : 0 );
                out.writeUBits( 1, hasFillStyle1 ? 1 : 0 );
                out.writeUBits( 1, hasFillStyle0 ? 1 : 0 );
                out.writeUBits( 1, hasMoveTo     ? 1 : 0 );
            
                if( hasMoveTo )
                {
                    int moveX = moveXY[0];
                    int moveY = moveXY[1];
                    writeMoveXY( moveX, moveY );
                }
                    
                if( hasFillStyle0 )
                {
                    out.writeUBits( fillBits, fill0Index );
                }
                    
                if( hasFillStyle1 )
                {
                    out.writeUBits( fillBits, fill1Index );
                }
                    
                if( hasLineStyle )
                {
                    out.writeUBits( lineBits, lineIndex );
                }

                moveXY = null;
                fill0Index = -1;
                fill1Index = -1;
                lineIndex  = -1;            
            }
        }
        
        protected void writeMoveXY( int moveX, int moveY ) throws IOException
        {
            int moveBits  = OutStream.determineSignedBitSize( moveX );
            int moveYBits = OutStream.determineSignedBitSize( moveY );
            if( moveYBits > moveBits ) moveBits = moveYBits;              
                    
            out.writeUBits( 5,  moveBits );
            out.writeSBits( moveBits, moveX );
            out.writeSBits( moveBits, moveY );            
        }
        
        protected void writeStyles( Vector styles ) throws IOException
        {    
            int numStyles = (styles != null) ? styles.size() : 0;
            
            if( numStyles < 0xff )
            {
                out.writeUI8( numStyles );
            }
            else
            {
                out.writeUI8( 0xff );
                out.writeUI16( numStyles );
            }
            
            if( styles != null )
            {
                for( Enumeration e = styles.elements(); 
                     e.hasMoreElements(); )
                {
                    Style style = (Style)e.nextElement();
                    style.write( out, hasAlpha );
                }
                
                styles.removeAllElements();
            }
        }
    }
    
    protected static class MorphShapeImpl extends TagWriter.SWFShapeImpl
    {
        protected int edgeOffsetBase;
        protected int edgeOffsetTarget;
        protected int shapeCount;        
        protected int fillBitSize;
        protected int lineBitSize;
        protected int shapeStart;
        
        public MorphShapeImpl( TagWriter writer ) throws IOException 
        {
            super( writer, true, false );
            fill0Index = -1;
            fill1Index = -1;
            lineIndex  = -1;

            shapeCount = 2;
            
            this.out = writer.getOutStream();

            edgeOffsetBase = (int)this.out.getCount();
            
            this.out.writeUI32( 0 );  //edge offset - to be filled in later
        }      
        
        public void done() throws IOException
        {
            if( ! initialStyles )
            {
                writeInitialStyles();
                initialStyles = true;
            }
            
            this.out.writeUBits( 6, 0 );  //end record            
            this.out.flushBits();

            if( shapeCount == 2 )
            {               
                edgeOffsetTarget = (int)this.out.getCount();                
                
                fill0Index = -1;
                fill1Index = -1;
                lineIndex  = -1;
                moveXY     = null;
                outstandingChanges = true;
                initialStyles = false;
                
                shapeCount--;
                return;
            }
            
            this.out.flush();
            byte[] bytes = writer.bytes.toByteArray();
            
            int edgeOffset = edgeOffsetTarget - edgeOffsetBase - 4;
            
            byte[] offsetBytes = OutStream.uintTo4Bytes( edgeOffset );
            
            bytes[ edgeOffsetBase     ] = offsetBytes[0];
            bytes[ edgeOffsetBase + 1 ] = offsetBytes[1];
            bytes[ edgeOffsetBase + 2 ] = offsetBytes[2];
            bytes[ edgeOffsetBase + 3 ] = offsetBytes[3];
            
            writer.out = null;
            writer.bytes = null;
            
            writer.mTags.tag( writer.tagType, writer.longTag, bytes ); 
        }        
        
        protected void writeInitialStyles() throws IOException
        {   
            this.out.flushBits();       
        
            int fillCount = fillStyles.size()/2;
            int lineCount = lineStyles.size()/2;
            
            fillBitSize = OutStream.determineUnsignedBitSize( fillCount );
            lineBitSize = OutStream.determineUnsignedBitSize( lineCount );
                                        
            //--Write style definitions
            if( shapeCount == 2 )
            {                
                if( fillCount < 255 )
                {
                    this.out.writeUI8( fillCount );
                }
                else
                {
                    this.out.writeUI8( 255 );
                    this.out.writeUI16( fillCount );
                }
                
                for( Enumeration e = fillStyles.elements(); e.hasMoreElements(); )
                {
                    FillStyle startStyle = (FillStyle)e.nextElement();
                    FillStyle endStyle   = (FillStyle)e.nextElement();
                    
                    FillStyle.writeMorphFillStyle( this.out, startStyle, endStyle );
                }                
                
                if( lineCount < 255 )
                {
                    this.out.writeUI8( lineCount );
                }
                else
                {
                    this.out.writeUI8( 255 );
                    this.out.writeUI16( lineCount );
                }
                
                for( Enumeration e = lineStyles.elements(); e.hasMoreElements(); )
                {
                    LineStyle startStyle = (LineStyle)e.nextElement();
                    LineStyle endStyle   = (LineStyle)e.nextElement();
                    
                    LineStyle.writeMorphLineStyle( this.out, startStyle, endStyle );
                }                
            }
            
            if( shapeStart == 0 ) shapeStart = (int)this.out.getCount();
            
            this.out.writeUBits( 4, fillBitSize );
            this.out.writeUBits( 4, lineBitSize );        
        }        

        protected void writeChangeRecord() throws IOException
        {
            boolean hasMoveTo = ( moveXY != null );
            boolean hasFillStyle0 = fill0Index >= 0;
            boolean hasFillStyle1 = fill1Index >= 0;
            boolean hasLineStyle  = lineIndex >= 0;             
            
            if( hasFillStyle0 || hasFillStyle1 || hasLineStyle || hasMoveTo )
            {
                this.out.writeUBits( 1, 0 );  //non-edge record
                this.out.writeUBits( 1, 0 );
                this.out.writeUBits( 1, hasLineStyle  ? 1 : 0 );
                this.out.writeUBits( 1, hasFillStyle1 ? 1 : 0 );
                this.out.writeUBits( 1, hasFillStyle0 ? 1 : 0 );
                this.out.writeUBits( 1, hasMoveTo     ? 1 : 0 );

                if( hasMoveTo )
                {
                    int moveX = moveXY[0];
                    int moveY = moveXY[1];
                    int moveBits  = OutStream.determineSignedBitSize( moveX );
                    int moveYBits = OutStream.determineSignedBitSize( moveY );
                    if( moveYBits > moveBits ) moveBits = moveYBits;              
                    
                    this.out.writeUBits( 5,  moveBits );
                    this.out.writeSBits( moveBits, moveX );
                    this.out.writeSBits( moveBits, moveY );
                }
                    
                if( hasFillStyle0 )
                {
                    this.out.writeUBits( fillBitSize, fill0Index );
                }
                    
                if( hasFillStyle1 )
                {
                    this.out.writeUBits( fillBitSize, fill1Index );
                }
                    
                if( hasLineStyle )
                {
                    this.out.writeUBits( lineBitSize, lineIndex );
                }

                moveXY = null;
                fill0Index = -1;
                fill1Index = -1;
                lineIndex  = -1;            
            }
        }        
    }    
    
    protected static class Font2ShapeImpl extends TagWriter.SWFShapeImpl
    {
        protected int    flags;
        protected int    ascent;
        protected int    descent;
        protected int    leading;                 
        protected int[]  codes; 
        protected int[]  advances;
        protected Rect[] bounds;
        protected int[]  kernCodes1;
        protected int[]  kernCodes2;
        protected int[]  kernAdjustments;
                                          
        public Font2ShapeImpl( TagWriter writer, int flags, int glyphCount,
                               int ascent, int descent, int leading,
                               int[] codes, int[] advances, Rect[] bounds,
                               int[] kernCodes1, int[] kernCodes2,
                               int[] kernAdjustments ) 
        {
            super( writer, glyphCount );
     
            this.flags           = flags;
            this.ascent          = ascent;         
            this.descent         = descent;        
            this.leading         = leading;        
            this.codes           = codes;          
            this.advances        = advances;       
            this.bounds          = bounds;         
            this.kernCodes1      = kernCodes1;     
            this.kernCodes2      = kernCodes2;     
            this.kernAdjustments = kernAdjustments;            
        }
        
        protected void finishFont() throws IOException
        {
            this.out = writer.getOutStream();

            int glyphCount = glyphByteArrays.size();
            
            boolean is32 = ( flags & FONT2_32OFFSETS ) != 0;
            int offset = is32 ? (( glyphCount + 1 ) * 4) : (( glyphCount + 1 ) * 2);
            for( int i = 0; i <= glyphCount; i++ )
            {
                if( is32 )
                {
                    this.out.writeUI32( offset );
                }
                else
                {
                    this.out.writeUI16( offset );
                }
                
                if( i < glyphCount )
                {
                    offset += ((byte[])glyphByteArrays.elementAt( i )).length;
                }
            }
        
            for( int i = 0; i < glyphCount; i++ )
            {
                this.out.write( (byte[])glyphByteArrays.elementAt( i ) );            
            }
        
            boolean isWide = ( flags & FONT2_WIDECHARS ) != 0  || glyphCount > 256 ;
            for( int i = 0; i < glyphCount; i++ )
            {
                if( isWide ) this.out.writeUI16( codes[i] );
                else         this.out.writeUI8( codes[i] );
            }        
        
            if( ( flags & FONT2_HAS_LAYOUT ) != 0 )
            {
                this.out.writeSI16( (short)ascent );
                this.out.writeSI16( (short)descent );
                this.out.writeSI16( (short)leading );

                for( int i = 0; i < glyphCount; i++ )
                {
                    this.out.writeSI16( (short)advances[i] );
                }        
                
                for( int i = 0; i < glyphCount; i++ )
                {
                    bounds[i].write( this.out );
                }        
             
                int kerningCount = (kernCodes1 != null ) ? kernCodes1.length : 0;
                this.out.writeUI16( kerningCount );
                
                for( int i = 0; i < kerningCount; i++ )
                {
                    if( isWide )
                    {
                        this.out.writeUI16( kernCodes1[i] );
                        this.out.writeUI16( kernCodes2[i] );
                        this.out.writeSI16( (short)kernAdjustments[i] );                    
                    }
                    else
                    {
                        this.out.writeUI8 ( kernCodes1[i] );
                        this.out.writeUI8 ( kernCodes2[i] );
                        this.out.writeSI16( (short)kernAdjustments[i] );                    
                    }
                }        
            }                           
        }        
    }
    
    
    /**
     * @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagScriptLimits(int, int)
     */
    public void tagScriptLimits(int maxRecursionDepth, int scriptTimeoutSecs)
        throws IOException {
        startTag( TAG_SCRIPTLIMITS, false );
        out.writeUI16( maxRecursionDepth );
        out.writeUI16( scriptTimeoutSecs );
        completeTag(); 
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes#tagTabOrder(int, int)
     */
    public void tagTabOrder(int depth, int tabOrder) throws IOException {
        startTag( TAG_TABORDER, false );
        out.writeUI16( depth );
        out.writeUI16( tabOrder );
        completeTag(); 
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagDefineVideoStream(int, int, int, int, int, int) */
    public void tagDefineVideoStream(int id, int numFrames, int width,
            int height, int flags, int codec) throws IOException {
        
        startTag( TAG_DEFINEVIDEOSTREAM, id, false );
        
        out.writeUI16( numFrames );
        out.writeUI16( width );
        out.writeUI16( height );
        out.writeUI8 ( flags );
        out.writeUI8 ( codec );
       
        completeTag();
    }
    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagVideoFrame(int, int, byte[]) */
    public void tagVideoFrame(int streamId, int seqNum, int frameType, int codec, byte[] videoPacket)
            throws IOException {

        startTag( TAG_VIDEOFRAME, streamId, false );
        
        out.writeUI16( seqNum );
        //out.write    ( (frameType << 4) + codec ); 
        out.write    ( videoPacket );
        
        completeTag();
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes#tagDoABC(int, java.lang.String) */
    public ABC tagDoABC(int flags, String filename) throws IOException {
        startTag( TAG_DOABC, true );
        out.writeUI32( flags );
        out.writeString( filename, "UTF-8" );
        return new ABCWriter( out ) {
            public void done() {
                super.done();
                try {
                    completeTag();
                } catch( IOException ioe ) {
                    throw new RuntimeException( ioe );
                }
            }
        };
    }
    
    /** @see com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes#tagSymbolClass(java.util.Map) */
    public void tagSymbolClass(Map<Integer, String> classes) throws IOException {
        startTag( TAG_SYMBOLCLASS, false );

        out.writeUI16( classes.size() );
        for( Map.Entry<Integer, String> entry : classes.entrySet() ) {
            out.writeUI16  ( entry.getKey() );
            out.writeString( entry.getValue(), "UTF-8" );
        }

        completeTag();
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagFileAttributes(int) */
    public void tagFileAttributes( int flags ) throws IOException {
        startTag( TAG_FILE_ATTRIBUTES, false );
        out.writeUI32( flags );
        completeTag();
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagDefineBinaryData(int, byte[]) */
    public void tagDefineBinaryData(int id, byte[] data) throws IOException {
        startTag( TAG_DEFINE_BINARY_DATA, id, false );
        out.writeUI32( 0 ); //reserved
        out.write( data );
        completeTag();
    }
}
