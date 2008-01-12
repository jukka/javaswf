package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Denotes the end of a With block
 *
 * @author nickmain
 */
public class WithEnd extends AVM1Operation {

    /**
     * The start of the with block
     */
    public final With withStart;
    
    public WithEnd( With withStart ) {
        this.withStart = withStart;
    }
    
    @Override
    public void accept( AVM1OpVisitor visitor ) {
        visitor.visitWithEnd( this );
    }

    @Override
    public void write( SWFActionBlock block ) throws IOException {
        // nada - this is a virtual op
    }
}
