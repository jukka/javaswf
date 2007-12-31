package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * An unconditional branch
 *
 * @author nickmain
 */
public class Jump extends AVM1Operation {

    public final String jumpLabel;
    
    public Jump( String jumpLabel ) {
        this.jumpLabel = jumpLabel;
    }
    
    @Override
    public void write(SWFActionBlock block) throws IOException {
        block.jump( jumpLabel );
    }
}
