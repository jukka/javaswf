package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Return from a function or method
 *
 * @author nickmain
 */
public class ReturnValue extends AVM1OperationAggregation {

    public  AVM1Operation value;

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( value == null ) value = consumePrevious();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        block.returnValue();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitReturnValue( this );        
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#getFollowOnInstructions(com.anotherbigidea.flash.avm1.AVM1ActionBlock) */
    @Override
    public Collection<AVM1Operation> getFollowOnInstructions( AVM1ActionBlock block ) {
        return Collections.emptySet();
    }
}
