package babelswf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm2.model.AVM2Code;

/**
 * Wrapper around a set of frame actions to be translated
 *
 * @author nickmain
 */
public class FrameActions  {

    private final Timeline timeline;
    private final int frameNumber;
    private final AVM1ActionBlock block = new AVM1ActionBlock();
    
    private final List<InitActions> initActList = new ArrayList<InitActions>();
    
    public FrameActions( Timeline timeline, int frameNumber ) {
        this.timeline    = timeline;
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
            AVM2Code initCode = acts.makeCode( timeline );
            OperationVisitor initVis = new OperationVisitor( timeline, initCode );
            acts.block().accept( initVis );
            initCode.returnVoid();
            initCode.analyze();            
        }
        
        AVM2Code code = timeline.movieClip().addFrame( frameNumber, BabelSWFRuntime.ADD_FRAME_METHOD );
        
        //place the global object at the foot of the scope stack
        //code.trace( "BEFORE SETTING UP GLOBAL" );
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.getProperty( BabelSWFRuntime.RT_GLOBAL_PROP );
        code.coerceToObject();
        code.pushWith();        
        //code.trace( "AFTER SETTING UP GLOBAL" );
        
        code.setupDynamicScope();

        try {
            block.print( IndentingPrintWriter.SYSOUT );
            IndentingPrintWriter.SYSOUT.flush();
        } catch( IOException ioe ) {
            //nada
        }
        
        OperationVisitor visitor = new OperationVisitor( timeline, code );
        block.accept( visitor );
        code.returnVoid();
        code.analyze();
    }    
}
