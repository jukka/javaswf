package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Set a variable
 *
 * @author nickmain
 */
public class SetVariable extends AVM1OperationAggregation {

    public AVM1Operation name;
    public AVM1Operation value;
    
    @Override
    public void aggregate() {
        if( value == null ) value = consumePrevious();
        if( name  == null ) name  = consumePrevious();
    }

    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.setVariable();
    }
}
