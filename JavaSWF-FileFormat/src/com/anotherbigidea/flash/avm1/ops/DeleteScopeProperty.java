package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Delete a named property from the current scope stack
 *
 * @author nickmain
 */
public class DeleteScopeProperty extends AVM1OperationAggregation implements AVM1ValueProducer {

    public AVM1Operation name;
    
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( name == null ) name = consumePrevious();
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        block.deleteScopeProperty();
    }
}
