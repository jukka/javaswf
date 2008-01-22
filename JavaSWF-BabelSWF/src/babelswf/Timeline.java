package babelswf;

import java.util.ArrayList;
import java.util.List;

import com.anotherbigidea.flash.avm1.AVM1BlockBuilder;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Base for sprite and main timeline wrappers
 *
 * @author nickmain
 */
public class Timeline {
    
    private final AVM2MovieClip clip;
    private int frameNumber;
    
    private FrameActions frameActions;  //current frame actions
    private List<InitActions> frameInitActions = new ArrayList<InitActions>(); //init actions in current frame
    
    public Timeline( AVM2MovieClip clip ) {
        this.clip = clip;
    }
    
    /** Get the current frame number */
    public final int frameNumber() { return frameNumber; }
    
    /** Access the AVM2 mc */
    public final AVM2MovieClip movieClip() { return clip; }
    
    /**
     * Perform any final processing before the timeline is written
     */
    public void finish() {
        System.err.println( "finish " + clip.avm2Class.name );
        clip.finish();
    }
    
    /**
     * Get the class name for the timeline
     */
    public final String className() {
        return clip.avm2Class.name.toQualString();
    }
    
    /**
     * Start the frame actions for the current frame
     * 
     * @return the block for the frame code
     */
    public final SWFActionBlock frameActions() {        
        frameActions = new FrameActions( this, frameNumber );
        return new AVM1BlockBuilder( frameActions.block() );
    }
    
    /**
     * Start a set of init actions within the current frame
     * 
     * @return the block for the init actions
     */
    public final SWFActionBlock initActions( int spriteId ) {
        
        InitActions acts = new InitActions( spriteId );
        frameInitActions.add( acts );
        
        return new AVM1BlockBuilder( acts.block() );
    }
    
    /**
     * Finish the current frame
     */
    public final void finishFrame() {
        //add the init actions for the frame
        if( ! frameInitActions.isEmpty() ) {
            if( frameActions == null ) {
                frameActions = new FrameActions( this, frameNumber );
                frameActions.block().complete();
            }
            
            for( InitActions acts : frameInitActions ) {
                frameActions.addInitActions( acts );
            }
            frameInitActions.clear();
        }
        
        if( frameActions != null ) {
            frameActions.translate();
            frameActions = null;
        }
        
        frameNumber++;
    }
}
