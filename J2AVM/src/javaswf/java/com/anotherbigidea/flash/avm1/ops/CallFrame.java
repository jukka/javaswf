package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Call a frame
 *
 * @author nickmain
 */
public class CallFrame extends AVM1OperationAggregation {

    public AVM1Operation frame;
    
    @Override
    public void aggregate() {
        if( frame == null ) frame = consumePrevious();
    }

    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.call();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitCallFrame( this );        
    }
}
