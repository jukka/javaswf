package babelswf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.io.AVM2ABCBuilder;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.interfaces.SWFTags;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.flash.writers.SWFTagTypesImpl;

/**
 * Helper for interfacing to the runtime classes.
 *
 * @author nickmain
 */
public class BabelSWFRuntime {
    
    /** The name of the runtime */
    public static final AVM2QName AVM1_RUNTIME_CLASS = new AVM2QName( "babelswf.AVM1Runtime" );

    /** The name of the execution context class */
    public static final AVM2QName EXEC_CONTEXT_CLASS = new AVM2QName( "babelswf.AVM1ExecutionContext" );
    
    /** The name of the movieclip base class */
    public static final AVM2QName AVM1_BASE_CLIP_CLASS = new AVM2QName( "babelswf.AVM1MovieClip" );
    
    /** The name of the main timeline class */
    public static final AVM2QName AVM1_MAIN_TIMELINE_CLASS = new AVM2QName( "babelswf.AVM1MainTimeline" );

    /** The name of the sprite class */
    public static final AVM2QName AVM1_SPRITE_CLASS = new AVM2QName( "babelswf.AVM1Sprite" );

    //--Movieclip method names
    public static final AVM2QName ADD_FRAME_METHOD = new AVM2QName( "avm1_addFrame" );        
    public static final AVM2QName ADD_INIT_METHOD  = new AVM2QName( "avm1_addInitActs" );        
    
    //--Execution context method names
    public static final AVM2QName STARTWITH_METHOD   = new AVM2QName( "avm1_startWith" );
    public static final AVM2QName ENDWITH_METHOD     = new AVM2QName( "avm1_endWith" );    
    public static final AVM2QName DUMP_METHOD        = new AVM2QName( "avm1_dump" );    
    
    //--Runtime names
    public static final AVM2QName RT_TRACE_METHOD     = new AVM2QName( "avm1_trace" );        
    public static final AVM2QName RT_GLOBAL_PROP      = new AVM2QName( "avm1_global" );        
    public static final AVM2QName RT_DELMEMBER_METHOD = new AVM2QName( "avm1_deleteMember" );
    public static final AVM2QName RT_GETMEMBER_METHOD = new AVM2QName( "avm1_getMember" );
    public static final AVM2QName RT_SETMEMBER_METHOD = new AVM2QName( "avm1_setMember" );
    public static final AVM2QName RT_CALLFUNC_METHOD  = new AVM2QName( "avm1_callFunction" );
    public static final AVM2QName RT_CALLMETH_METHOD  = new AVM2QName( "avm1_callMethod" );
    public static final AVM2QName RT_NEWACTIVN_METHOD = new AVM2QName( "avm1_newActivation" );
    public static final AVM2QName RT_ENDDRAG_METHOD   = new AVM2QName( "avm1_endDrag" );
    public static final AVM2QName RT_STARTDRAG_METHOD = new AVM2QName( "avm1_startDrag" );
    public static final AVM2QName RT_PLAY_METHOD      = new AVM2QName( "avm1_play" );
    public static final AVM2QName RT_STOP_METHOD      = new AVM2QName( "avm1_stop" );
    public static final AVM2QName RT_NEWFUNC_METHOD   = new AVM2QName( "avm1_newFunction" );
    public static final AVM2QName RT_NEWOBJECT_METHOD = new AVM2QName( "avm1_newObject" );
    public static final AVM2QName RT_NEWMETHOD_METHOD = new AVM2QName( "avm1_newMethod" );
    public static final AVM2QName RT_EXTENDS_METHOD   = new AVM2QName( "avm1_extends" );

    /**
     * Load the runtime classes. 
     */
    public static AVM2ABCFile loadRuntimeClasses( File runtime ) throws IOException {
        final AVM2ABCBuilder builder = new AVM2ABCBuilder();
        
        SWFTagTypes tags = new SWFTagTypesImpl( null ) {
            /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoABC() */
            @Override
            public ABC tagDoABC( int flags, String name ) throws IOException {                
                return builder;
            }
        };
        
        FileInputStream in  = new FileInputStream( runtime  );        
        SWFTags tagparser = new TagParser( tags );        
        SWFReader reader = new SWFReader( tagparser, in );        
        reader.readFile();
        
        return builder.file;
    }

    /**
     * Duplicate and dump the top object
     */
    public static void dumpObject( AVM2Code code ) {
        code.dup();        
        code.getLocal( code.thisValue );
        code.swap();
        code.callPropVoid( DUMP_METHOD, 1 );        
    }
}
