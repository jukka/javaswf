package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * A "with" block
 *
 * @author nickmain
 */
public class With extends AVM1OperationAggregation {

    public final AVM1ActionBlock block = new AVM1ActionBlock( this );
    
    public AVM1Operation object;

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( object == null ) object = consumePrevious();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        SWFActionBlock withBlock = block.startWith();        
        this.block.write( withBlock );
    }
}
