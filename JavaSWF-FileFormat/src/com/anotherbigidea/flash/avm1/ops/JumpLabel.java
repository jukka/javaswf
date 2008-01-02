package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.writers.ActionTextWriter;

/**
 * A target for branches
 */
public final class JumpLabel extends AVM1Operation {
    public final String label;
    
    public JumpLabel( String label ) {
        this.label = label;
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write(SWFActionBlock block) throws IOException {
        block.jumpLabel( label );
    }
    
    /**
     * Print the operation
     */
    public void print( ActionTextWriter writer ) throws IOException {
        writer.jumpLabelOnOwnLine( label );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitJumpLabel( this );        
    }
}