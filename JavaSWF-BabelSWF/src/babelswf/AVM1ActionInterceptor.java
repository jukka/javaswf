package babelswf;

import static com.anotherbigidea.flash.SWFConstants.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import com.anotherbigidea.flash.avm1.AVM1BlockBuilder;
import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
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

	private static final Logger log = AVM1BytecodeTranslator.log;
	
	private final String context;
	private final AVM2MovieClip mainClip;
	
	private final AVM2ABCFile abc = new AVM2ABCFile();

    private final List<Tag> tags = new ArrayList<Tag>();
    
    private final TagParser parser = new TagParser( this );
    private int frameNumber = 0;

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
    public AVM1ActionInterceptor( String context, SWFTagTypes tags ) {
        super( tags );
        this.context = context;        
        this.mainClip = new AVM2MovieClip( abc, context.replace( '.', '_' ) + ".MainTimeline");        
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
	    
		//--emit the ABC tag
		ABC abcFile = super.tagDoABC( DO_ABC_LAZY_INITIALIZE_FLAG, "javaswf" );
		abc.write( abcFile );
		
		//--set the class for the main timeline
		Map<Integer,String> classes = new HashMap<Integer, String>();
		classes.put( 0, mainClip.avm2Class.name.toQualString() );
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
		super.tagScriptLimits( 1000, 300 ); //TODO: figure out better values		
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
		
		// TODO intercept sprite actions
		return null;
	}

	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoInitAction(int) */
	@Override
	public SWFActions tagDoInitAction( int spriteId ) throws IOException {
		log( "tag DoInitAction - id=" + spriteId );
		
		// TODO intercept init actions
		return null;
	}


	/** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagPlaceObject2(boolean, int, int, int, com.anotherbigidea.flash.structs.Matrix, com.anotherbigidea.flash.structs.AlphaTransform, int, java.lang.String, int) */
	@Override
	public SWFActions tagPlaceObject2( boolean isMove, int clipDepth,
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
				
		FrameActions frameActions = new FrameActions( mainClip, frameNumber );
		
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
