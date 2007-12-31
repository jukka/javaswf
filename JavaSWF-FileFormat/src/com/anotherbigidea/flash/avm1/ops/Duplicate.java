package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Duplicate the top stack value
 *
 * @author nickmain
 */
public class Duplicate extends AVM1Operation implements AVM1ValueProducer {

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write( SWFActionBlock block ) throws IOException {
        block.duplicate();
    }
}