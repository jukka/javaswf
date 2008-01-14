package babelswf;

import static com.anotherbigidea.flash.SWFConstants.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import com.anotherbigidea.flash.avm1.AVM1BlockBuilder;
import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
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
	private final AVM2MovieClip mainClip;
    private final AVM2MovieClip thisClip;
	
	//map of symbol id to movieclip
	private final Map<Integer, AVM2MovieClip> clips = new HashMap<Integer, AVM2MovieClip>();
	
	private final AVM2ABCFile abc;

    private final List<Tag> tags = new ArrayList<Tag>();
    
    private final TagParser parser = new TagParser( this );
    private int frameNumber = 0;
    
    private FrameActions frameActions;  //current frame actions
    private List<InitActions> frameInitActions = new ArrayList<InitActions>(); //init actions in current frame

    private static class Tag {
        final int    tagType;
        final byte[] data;
        
        Tag( int tagType, byte[] data ) {
            this.tagType = tagType;
            this.data    = data;
        }

        Tag( int tagType ) {
            this( tagType, new byte[0] );
        }

        void write( SWFTagTypes tagTypes ) throws IOException {
            tagTypes.tag( tagType, false, data );
        }
    }
    
	/**
	 * @param context a description of the context
	 * @param tags the target to pass tags to
	 */
    public AVM1ActionInterceptor( String context, SWFTagTypes tags ) throws IOException {
        super( tags );
        
        //start off with the runtime classes and add custom code to that
        abc = BabelSWFRuntime.loadRuntimeClasses();
        
        this.context = context;        
        this.mainClip = new AVM2MovieClip( 
                                abc, 
                                context.replace( '.', '_' ) + ".MainTimeline",
                                context,
                                true,
                                BabelSWFRuntime.AVM1_BASE_CLIP_CLASS,
                                BabelSWFRuntime.AVM1_MAIN_TIMELINE_CLASS
                            );
        
        thisClip = mainClip;
        
        //set up the _global value  -- now done in class init scripts
//        AVM2Code cons = mainClip.constructor();
//        cons.getGlobalScope();
//        cons.dup();
//        cons.setProperty( "_global" );
    }
    
    private void log( String message ) {
    	log.info( context + "[" + frameNumber + "]: " + message );
    }
    
	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tag(int, boolean, byte[]) */
	@Override
	public void tag( int tagType, boolean longTag, byte[] contents ) throws IOException {
		
		//--intercept and parse certain tags
		switch( tagType ) {
			case TAG_DOACTION:
			case TAG_DOINITACTION:
			case TAG_SHOWFRAME:
			case TAG_DEFINESPRITE:
			case TAG_FILE_ATTRIBUTES:
			case TAG_PLACEOBJECT2:
			case TAG_SCRIPTLIMITS:
			case TAG_SYMBOLCLASS:
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
		}
		
		//--save other tags as blobs
		tags.add( new Tag( tagType, contents ) );
	}

	/**
	 * Finish the SWF
	 */
	private void finish() throws IOException {
	    mainClip.finish();
	    
	    //finish all the other clips
	    for( AVM2MovieClip clip : clips.values() ) {
	        clip.finish();
	    }
	    
		//--emit the ABC tag
		ABC abcFile = super.tagDoABC( DO_ABC_LAZY_INITIALIZE_FLAG, "javaswf" );
		abc.write( abcFile );
		
		//--set the class for the main timeline and clips
		Map<Integer,String> classes = new TreeMap<Integer, String>();
		classes.put( 0, mainClip.avm2Class.name.toQualString() );
		for( Integer clipId : clips.keySet()) {
		    AVM2MovieClip clip = clips.get( clipId );
	        classes.put( clipId, clip.avm2Class.name.toQualString() );
		}
		
		super.tagSymbolClass( classes );
		
		//--flush all the tags
		for( Tag tag : tags ) {
			tag.write( super.mTagtypes );
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
		super.tagEnd();
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagShowFrame() */
	@Override
	public void tagShowFrame() throws IOException {
	    
	    //add the init actions for the frame
	    if( ! frameInitActions.isEmpty() ) {
	        if( frameActions == null ) {
	            frameActions = new FrameActions(  thisClip, frameNumber );
	            frameActions.block().complete();
	        }
	        
	        for( InitActions acts : frameInitActions ) {
	            frameActions.addInitActions( acts );
	        }
	        frameInitActions.clear();
	    }
	    
	    if( frameActions != null ) {
	        frameActions.translate();
	    }
	    
		frameNumber++;
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

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagSymbolClass(java.util.Map) */
	@Override
	public void tagSymbolClass( Map<Integer, String> classes ) throws IOException {
		// TODO handle symbol class

	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDefineSprite(int) */
	@Override
	public SWFTagTypes tagDefineSprite( int id ) throws IOException {
		log( "tag DefineSprite - id=" + id );
		
		//create a clip class for the sprite
        AVM2MovieClip clip = 
            new AVM2MovieClip( abc, 
                               context.replace( '.', '_' ) + ".MovieClip_" + id,
                               id,
                               false,
                               BabelSWFRuntime.AVM1_BASE_CLIP_CLASS,
                               BabelSWFRuntime.AVM1_SPRITE_CLASS                   
                             );
        
        clips.put( id, clip );
		
		// TODO intercept sprite actions
		return null;
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoInitAction(int) */
	@Override
	public SWFActions tagDoInitAction( int spriteId ) throws IOException {
		log( "tag DoInitAction - id=" + spriteId );
		
		AVM2MovieClip clip = clips.get( spriteId );
        InitActions initActions = new InitActions( clip, spriteId );
        frameInitActions.add( initActions );
        
        final AVM1BlockBuilder builder = new AVM1BlockBuilder( initActions.block() );
        
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
                
                return builder;
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
				
		frameActions = new FrameActions( thisClip, frameNumber );
		
		final AVM1BlockBuilder builder = new AVM1BlockBuilder( frameActions.block() );
		
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
				
				return builder;
			}
		};
	}
}
