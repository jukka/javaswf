package babelswf;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;

/**
 * Wrapper around a set of init-clip actions to be translated
 *
 * @author nickmain
 */
public class InitActions {
    
    private final int  spriteId;
    private final AVM1ActionBlock block = new AVM1ActionBlock();
    
    public InitActions( int spriteId ) {
        this.spriteId = spriteId;
    }
    
    public AVM1ActionBlock block() { return block; }
    
    /**
     * Make a code instance for these actions
     * 
     * @param timeline the timeline to add the actions to
     */
    public AVM2Code makeCode( Timeline timeline ) {    
        AVM2Code code = timeline.movieClip()
                                .addInitActions( 
                                     timeline.frameNumber(), 
                                     spriteId, 
                                     BabelSWFRuntime.ADD_INIT_METHOD );

        //place the global object at the foot of the scope stack
        //code.trace( "BEFORE SETTING UP GLOBAL" );
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.getProperty( BabelSWFRuntime.RT_GLOBAL_PROP );
        code.coerceToObject();
        code.pushWith();        
        //code.trace( "AFTER SETTING UP GLOBAL" );
        
        return code;
    }
}
