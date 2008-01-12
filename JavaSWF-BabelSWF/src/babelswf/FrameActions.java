package babelswf;

import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;

/**
 * Wrapper around a set of frame actions to be translated
 *
 * @author nickmain
 */
public class FrameActions  {

    private final AVM2MovieClip clip;
    private final int frameNumber;
    
    public FrameActions( AVM2MovieClip clip, int frameNumber ) {
        this.clip = clip;
        this.frameNumber = frameNumber;
    }
    
    public AVM1ActionBlock block() {
        return new AVM1ActionBlock() {
            /** @see com.anotherbigidea.flash.avm1.AVM1ActionBlock#complete() */
            @Override
            public void complete() {
                super.complete();
                translate( this );
            }            
        };
    }
    
    /**
     * Translate the frame code
     */
    private void translate( AVM1ActionBlock block ) {        
        AVM2Code code = clip.addFrame( frameNumber );
        
        try {
            block.print( IndentingPrintWriter.SYSOUT );
            IndentingPrintWriter.SYSOUT.flush();
        } catch( IOException ioe ) {
            //nada
        }
        
        OperationVisitor visitor = new OperationVisitor( clip.avm2Class, code );
        block.accept( visitor );
        code.returnVoid();
        code.calcMaxes();
    }
    
}
