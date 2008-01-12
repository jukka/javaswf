package babelswf;

import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2MovieClip;

/**
 * Wrapper around a set of init-clip actions to be translated
 *
 * @author nickmain
 */
public class InitActions {

    
    private final AVM2MovieClip clip;
    
    public InitActions( AVM2MovieClip clip ) {
        this.clip = clip;
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
     * Translate the init code
     */
    private void translate( AVM1ActionBlock block ) {        
        AVM2Code code = clip.initializer();
        
        try {
            block.print( IndentingPrintWriter.SYSOUT );
            IndentingPrintWriter.SYSOUT.flush();
        } catch( IOException ioe ) {
            //nada
        }
        
        OperationVisitor visitor = new OperationVisitor( clip.avm2Class, code );
        block.accept( visitor );
    }
}
