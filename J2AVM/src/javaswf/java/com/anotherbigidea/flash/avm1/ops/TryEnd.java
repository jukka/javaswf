package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * The end of try-catch-finally region
 *
 * @author nickmain
 */
public class TryEnd extends AVM1Operation {

    public final Try tryStart;

    public TryEnd( Try tryStart ) {
        this.tryStart = tryStart;
    }
    
    @Override
    public void accept( AVM1OpVisitor visitor ) {
        visitor.visitTryEnd( this );
    }

    @Override
    public void write( SWFActionBlock block ) throws IOException {
        // nada - this is a virtual op
    }
}
