package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
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

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#labelReferences() */
    @Override
    public String[] labelReferences() {
        return new String[] { jumpLabel };
    }    

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitJump( this );        
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#getFollowOnInstructions(com.anotherbigidea.flash.avm1.AVM1ActionBlock) */
    @Override
    public Collection<AVM1Operation> getFollowOnInstructions( AVM1ActionBlock block ) {
        return Collections.singleton( (AVM1Operation) block.findLabel( jumpLabel ) );
    }
}
