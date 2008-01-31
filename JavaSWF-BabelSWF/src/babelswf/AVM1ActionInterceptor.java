package babelswf;

import static com.anotherbigidea.flash.SWFConstants.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import org.epistem.io.InStream;

import com.anotherbigidea.flash.avm1.AVM1BlockBuilder;
import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.flash.structs.AlphaTransform;
import com.anotherbigidea.flash.structs.ButtonRecord;
import com.anotherbigidea.flash.structs.ButtonRecord2;
import com.anotherbigidea.flash.structs.Matrix;
import com.anotherbigidea.flash.writers.SWFTagTypesImpl;

/**
 * Passes through SWF tags and intercepts those containing AVM1 actions. The
 * actions are passed to the bytecode-translator.
 *
 * @author nickmain
 */
public class AVM1ActionInterceptor extends SWFTagTypesImpl {

	private static final Logger log = AVMTranslator.log;
	
	private final String context;
	private final SWFTimeline mainClip;
    private final Timeline    thisClip;
		
	private final AVM2ABCFile abc;

    private final List<Tag> tags = new ArrayList<Tag>();
    
    private final TagParser parser = new TagParser( this );

    private static class Tag {
        final int    tagType;
        final byte[] data;
        final AVM1ActionInterceptor sprite;
        
        Tag( int tagType, byte[] data ) {
            this.tagType = tagType;
            this.data    = data;
            this.sprite  = null;
        } 

        Tag( AVM1ActionInterceptor sprite ) {
            this.tagType = 0;
            this.data    = null;
            this.sprite  = sprite;            
        }
        
        Tag( int tagType ) {
            this( tagType, new byte[0] );
        }

        void write( SWFTagTypes tagTypes ) throws IOException {
            if( sprite != null ) {
                SWFTagTypes spriteTags = 
                    tagTypes.tagDefineSprite( ((SpriteTimeline) sprite.thisClip).id );
                
                sprite.flushTags( spriteTags );
                spriteTags.tagEnd();
                return;
            }
                        
            tagTypes.tag( tagType, false, data );
        }
    }
    
	/**
	 * @param context a description of the context
	 * @param tags the target to pass tags to
	 * @param runtime the runtime SWF to embed - null for none
	 */
    public AVM1ActionInterceptor( String context, SWFTagTypes tags, File runtime ) throws IOException {
        super( tags );
        
        //start off with the runtime classes and add custom code to that
        if( runtime != null ) {
            abc = BabelSWFRuntime.loadRuntimeClasses( runtime );           
        }
        else {
            abc = new AVM2ABCFile(); //runtime is assumed to be loaded separately
        }
        
        this.context  = context;        
        this.mainClip = new SWFTimeline( context, abc );
        
        thisClip = mainClip;        
    }

    /**
     * @param context a description of the context
     * @param tags the target to pass tags to
     * @param runtime the runtime SWF to embed - null for none
     */
    private AVM1ActionInterceptor( SWFTimeline mainClip, AVM2ABCFile abc,
                                   Timeline clip, String context ) throws IOException {
        super( null );
        
        this.abc      = abc;
        this.mainClip = mainClip;
        this.context  = context;        
        thisClip      = clip;        
    }

    
    private void log( String message ) {
    	log.info( context + "[" + thisClip.frameNumber() + "]: " + message );
    }
    
	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tag(int, boolean, byte[]) */
	@Override
	public void tag( int tagType, boolean longTag, byte[] contents ) throws IOException {
		
		//--intercept and parse certain tags
		switch( tagType ) {
			case TAG_DOACTION:
			case TAG_DOINITACTION:
			case TAG_SHOWFRAME:
			case TAG_FILE_ATTRIBUTES:
			//case TAG_PLACEOBJECT2:
			case TAG_SCRIPTLIMITS:
			//case TAG_SYMBOLCLASS:
			case TAG_DEFINEBUTTON:
			case TAG_DEFINEBUTTON2:
			case TAG_END:
				parser.tag( tagType, longTag, contents );
				return;

			case TAG_SETBACKGROUNDCOLOR:
			    super.tag( tagType, longTag, contents );
			    return;
				
            case TAG_PLACEOBJECT3:
                //FIXME
                throw new RuntimeException( "TAG_PLACEOBJECT3 is not yet implemented" );

		
            case TAG_DEFINESPRITE:
                handleSprite( contents );
                return;
		}
		
		//--save other tags as blobs
		tags.add( new Tag( tagType, contents ) );
	}

	/**
	 * Handle a sprite timeline
	 */
	private void handleSprite( byte[] contents ) throws IOException {
	    InStream  in = new InStream( contents );
        int id = in.readUI16();
        in.readUI16(); //frame count
	    
        log( "tag DefineSprite - id=" + id );
        
        SpriteTimeline spriteTime = mainClip.addSprite( context, id );
        
        AVM1ActionInterceptor interceptor =     
            new AVM1ActionInterceptor( mainClip, abc, spriteTime, context );
        
        tags.add( new Tag( interceptor ) );
	    
	    SWFReader reader = new SWFReader( interceptor, in );
	    reader.readTags();
	}
	
	/**
	 * Finish the SWF
	 */
	private void finish() throws IOException {
	    thisClip.finish();
	    
	    if( thisClip == mainClip ) {    	    
    		//--emit the ABC tag
    		ABC abcFile = super.tagDoABC( DO_ABC_LAZY_INITIALIZE_FLAG, "javaswf" );
    		abc.write( abcFile );
            super.tagSymbolClass( mainClip.symbolMappings() );          
	    }
	    
        //--flush all the tags
	    if( super.mTagtypes != null ) {
            flushTags( super.mTagtypes );
	    }

        if( thisClip == mainClip ) {            
            //--set the class for the main timeline and clips           
        }

        super.tagEnd();
	}
	
	private void flushTags( SWFTagTypes tagTypes ) throws IOException {
        //--flush all the tags
        for( Tag tag : tags ) {
            tag.write( tagTypes );
        }	    
	}
	
	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#header(int, long, int, int, int, int) */
	@Override
	public void header( int version, long length, int twipsWidth,
                        int twipsHeight, int frameRate, int frameCount ) throws IOException {
		// Change version to 9 and force recalculation of the file size
		super.header( FLASH_9, -1, twipsWidth, twipsHeight, frameRate, frameCount );
		super.tagFileAttributes( FILE_ATTRIBUTES_ALLOW_AS3 
		                       | FILE_ATTRIBUTES_HAS_METADATA 
		                       | FILE_ATTRIBUTES_USE_NETWORK );
		super.tagScriptLimits( 1000, 60 ); //TODO: figure out better values		
	}
	
	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagEnd() */
	@Override
	public void tagEnd() throws IOException {
		finish();
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagShowFrame() */
	@Override
	public void tagShowFrame() throws IOException {	    
	    thisClip.finishFrame();
		tags.add( new Tag( TAG_SHOWFRAME ) );		
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDefineButton(int, java.util.Vector) */
	@Override
	public SWFActions tagDefineButton( int id, Vector<ButtonRecord> buttonRecords ) throws IOException {
		log( "tag DefineButton - id=" + id );
		
		// TODO intercept button actions
		return null;
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDefineButton2(int, boolean, java.util.Vector) */
	@Override
	public SWFActions tagDefineButton2( int id, boolean trackAsMenu, Vector<ButtonRecord2> buttonRecord2s ) throws IOException {
		log( "tag DefineButton2 - id=" + id );
		
		// TODO intercept button actions
		return null;
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagFileAttributes(int) */
	@Override
	public void tagFileAttributes( int flags ) throws IOException {
		//suppress - this is emitted with the header
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoInitAction(int) */
	@Override
	public SWFActions tagDoInitAction( int spriteId ) throws IOException {
		log( "tag DoInitAction - id=" + spriteId );
		
        final SWFActionBlock block = thisClip.initActions( spriteId );
        
        return new SWFActions() {
            /** @see com.anotherbigidea.flash.interfaces.SWFActions#done() */
            public void done() throws IOException {
                //nada
            }

            /** @see com.anotherbigidea.flash.interfaces.SWFActions#start(int, int) */
            public SWFActionBlock start( int flags, int keycode ) throws IOException {
                throw new UnsupportedOperationException( "IMPLEMENT KEYCODE ACTIONS" );
            }

            /** @see com.anotherbigidea.flash.interfaces.SWFActions#start(int) */
            public SWFActionBlock start( int flags ) throws IOException {
                if( flags != 0 ) {
                    throw new UnsupportedOperationException( "IMPLEMENT ACTION FLAGS" );
                }
                
                return block;
            }
        };
	}


	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagPlaceObject2(boolean, int, int, int, com.anotherbigidea.flash.structs.Matrix, com.anotherbigidea.flash.structs.AlphaTransform, int, java.lang.String, int) */
	@Override
	public SWFActions tagPlaceObject2( 
	           boolean isMove, int clipDepth,
			   int depth, int charId, Matrix matrix, AlphaTransform cxform,
			   int ratio, String name, int clipActionFlags ) throws IOException {
		log( "tag PlaceObject2 - id=" + charId );
		
		// TODO intercept intance actions
		return null;
	}



	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagScriptLimits(int, int) */
	@Override
	public void tagScriptLimits( int maxRecursionDepth, int scriptTimeoutSecs )
			throws IOException {
		// suppress - this is emitted elsewhere
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoAction() */
	@Override
	public SWFActions tagDoAction() throws IOException {
		log( "tag DoAction" );
				
		final SWFActionBlock block = thisClip.frameActions();
		
		return new SWFActions() {
			/** @see com.anotherbigidea.flash.interfaces.SWFActions#done() */
			public void done() throws IOException {
				//nada
			}

			/** @see com.anotherbigidea.flash.interfaces.SWFActions#start(int, int) */
			public SWFActionBlock start( int flags, int keycode ) throws IOException {
				throw new UnsupportedOperationException( "IMPLEMENT KEYCODE ACTIONS" );
			}

			/** @see com.anotherbigidea.flash.interfaces.SWFActions#start(int) */
			public SWFActionBlock start( int flags ) throws IOException {
				if( flags != 0 ) {
					throw new UnsupportedOperationException( "IMPLEMENT ACTION FLAGS" );
				}
				
				return block;
			}
		};
	}
}
