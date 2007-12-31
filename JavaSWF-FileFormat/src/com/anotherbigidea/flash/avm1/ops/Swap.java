package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Swap the top stack values
 *
 * @author nickmain
 */
public class Swap extends AVM1Operation {

    @Override
    public void write( SWFActionBlock block ) throws IOException {
        block.swap();
    }
}
