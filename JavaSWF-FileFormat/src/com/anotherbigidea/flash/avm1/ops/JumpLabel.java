package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * A target for branches
 */
public final class JumpLabel extends AVM1Operation {
    public final String label;
    
    /*pkg*/ JumpLabel( String label ) {
        this.label = label;
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write(SWFActionBlock block) throws IOException {
        block.jumpLabel( label );
    }
}