package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Get a property froma movieclip
 *
 * @author nickmain
 */
public class GetProperty extends AVM1OperationAggregation implements AVM1ValueProducer {

    public AVM1Operation propertyIndex;
    public AVM1Operation target;
    
    @Override
    public void aggregate() {
        if( propertyIndex == null ) propertyIndex = consumePrevious();
        if( target        == null ) target = consumePrevious();
    }

    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.getProperty();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitGetProperty( this );        
    }
}
