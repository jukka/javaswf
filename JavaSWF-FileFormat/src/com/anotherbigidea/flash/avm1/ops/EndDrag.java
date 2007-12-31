package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * End the current drag
 *
 * @author nickmain
 */
public class EndDrag extends AVM1Operation {

    @Override
    public void write(SWFActionBlock block) throws IOException {
        block.endDrag();
    }
}
