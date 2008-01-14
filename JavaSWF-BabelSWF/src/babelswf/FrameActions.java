package babelswf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2Method;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;

/**
 * Wrapper around a set of frame actions to be translated
 *
 * @author nickmain
 */
public class FrameActions  {

    private final AVM2MovieClip clip;
    private final int frameNumber;
    private final AVM1ActionBlock block = new AVM1ActionBlock();
    
    private final List<InitActions> initActList = new ArrayList<InitActions>();
    
    public FrameActions( AVM2MovieClip clip, int frameNumber ) {
        this.clip = clip;
        this.frameNumber = frameNumber;
    }
    
    public AVM1ActionBlock block() { return block; }
    
    /**
     * Add init actions to the frame
     */
    public void addInitActions( InitActions initActs ) {
        initActList.add( initActs );
    }
    
    /**
     * Translate the frame code
     */
    public void translate() {
        for( InitActions acts : initActList ) {
            AVM2Code initCode = clip.addInitActions( frameNumber, acts.symbolId, BabelSWFRuntime.ADD_INIT_METHOD );
            OperationVisitor initVis = new OperationVisitor( clip.avm2Class, initCode );
            acts.block().accept( initVis );
            initCode.returnVoid();
            initCode.analyze();            
        }
        
        AVM2Code code = clip.addFrame( frameNumber, BabelSWFRuntime.ADD_FRAME_METHOD );
        
        try {
            block.print( IndentingPrintWriter.SYSOUT );
            IndentingPrintWriter.SYSOUT.flush();
        } catch( IOException ioe ) {
            //nada
        }
        
        OperationVisitor visitor = new OperationVisitor( clip.avm2Class, code );
        block.accept( visitor );
        code.returnVoid();
        code.analyze();
    }
    
}
