/**
 * 
 */
package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

public final class NextFrame extends AVM1Operation {        
    public NextFrame() {}

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write(SWFActionBlock block) throws IOException {
        block.nextFrame();
    }
}