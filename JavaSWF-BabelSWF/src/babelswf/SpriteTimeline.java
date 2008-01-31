package babelswf;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;

/**
 * Wrapper around a sprite timeline being translated
 *
 * @author nickmain
 */
public final class SpriteTimeline extends Timeline {

    public final int id; 
    
    public SpriteTimeline( String context, int id, AVM2ABCFile abc ) {
        super( new AVM2MovieClip( abc, 
                       context.replace( '.', '_' ) + ".MovieClip_" + id,
                       id,
                       false,
                       BabelSWFRuntime.AVM1_BASE_CLIP_CLASS,
                       BabelSWFRuntime.AVM1_SPRITE_CLASS                   
                   ));
        
        this.id = id;
    }
    
}
