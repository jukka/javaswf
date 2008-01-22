package babelswf;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;

/**
 * Wrapper around the main timeline of the SWF being translated
 * 
 * @author nickmain
 */
public final class SWFTimeline extends Timeline {

    //map of symbol id to movieclip
    private final Map<Integer, SpriteTimeline> sprites = new HashMap<Integer, SpriteTimeline>();
    
    private final AVM2ABCFile abc;
    
    public SWFTimeline( String context, AVM2ABCFile abc ) {
        super( new AVM2MovieClip( 
                       abc, 
                       context.replace( '.', '_' ) + ".MainTimeline",
                       context,
                       true,
                       BabelSWFRuntime.AVM1_BASE_CLIP_CLASS,
                       BabelSWFRuntime.AVM1_MAIN_TIMELINE_CLASS
                   ));
        
        this.abc = abc;
    }

    /** @see babelswf.Timeline#finish() */
    @Override
    public void finish() {
        //finish all sprites
        for( SpriteTimeline sprite : sprites.values() ) {
            sprite.finish();
        }
        
        super.finish();
    }

    /**
     * Get the map of symbolId to class name
     */
    public Map<Integer,String> symbolMappings() {
        Map<Integer,String> classes = new TreeMap<Integer, String>();

        classes.put( 0, movieClip().avm2Class.name.toQualString() );

        for( Integer clipId : sprites.keySet()) {
            SpriteTimeline sprite = sprites.get( clipId );
            classes.put( clipId, sprite.className() );
        }

        return classes;
    }
    
    /**
     * Add a sprite definition to the main timeline 
     */
    public SpriteTimeline addSprite( String context, int id ) {
        //create a clip class for the sprite
        SpriteTimeline sprite = new SpriteTimeline( context, id, abc ); 
        sprites.put( id, sprite );
        
        return sprite;
    }
}
