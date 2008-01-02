package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
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

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#labelReference() */
    @Override
    public String labelReference() {
        return jumpLabel;
    }    

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitJump( this );        
    }
}
