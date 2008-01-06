package babelswf;

import java.io.FileInputStream;
import java.io.IOException;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
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
    
    /** The SWF containing the runtime classes */
    public static final String RUNTIME_SWF = "runtime/runtime.swf";
    
    /** The name of the base movie clip */
    public static final AVM2QName BASE_MOVIECLIP = new AVM2QName( "babelswf.BaseMovieClip" );

    //--Runtime method names
    public static final AVM2QName TRACE_METHOD     = new AVM2QName( "babelswf_trace" );
    public static final AVM2QName FRAMES_POST_CALL = new AVM2QName( "babelswf_framesAdded" );
    
    /**
     * Load the runtime classes. 
     */
    public static AVM2ABCFile loadRuntimeClasses() throws IOException {
        final AVM2ABCBuilder builder = new AVM2ABCBuilder();
        
        SWFTagTypes tags = new SWFTagTypesImpl( null ) {
            /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoABC() */
            @Override
            public ABC tagDoABC( int flags, String name ) throws IOException {                
                return builder;
            }
        };
        
        FileInputStream in  = new FileInputStream( RUNTIME_SWF );        
        SWFTags tagparser = new TagParser( tags );        
        SWFReader reader = new SWFReader( tagparser, in );        
        reader.readFile();
        
        return builder.file;
    }
}
