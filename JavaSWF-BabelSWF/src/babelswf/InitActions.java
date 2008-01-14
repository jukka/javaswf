package babelswf;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;

/**
 * Wrapper around a set of init-clip actions to be translated
 *
 * @author nickmain
 */
public class InitActions {
    
    public  final AVM2MovieClip clip;
    private final AVM1ActionBlock block = new AVM1ActionBlock();
    
    public final int symbolId;
    
    public InitActions( AVM2MovieClip clip, int symbolId ) {
        this.clip = clip;
        this.symbolId = symbolId;
    }
    
    public AVM1ActionBlock block() { return block; }
}
