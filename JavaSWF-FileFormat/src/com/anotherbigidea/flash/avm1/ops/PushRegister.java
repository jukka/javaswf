package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Push value from a local register
 *
 * @author nickmain
 */
public class PushRegister extends AVM1Operation implements AVM1ValueProducer {

    public final int registerNumber;
    
    public PushRegister( int registerNumber ) {
        this.registerNumber = registerNumber;
    }
    
    @Override
    public void write( SWFActionBlock block ) throws IOException {
        block.pushRegister( registerNumber );
    }
}
