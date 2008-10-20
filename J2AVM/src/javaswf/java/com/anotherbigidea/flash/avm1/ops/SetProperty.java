package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Set a property of a movieclip
 *
 * @author nickmain
 */
public class SetProperty extends AVM1OperationAggregation {

    public AVM1Operation propertyIndex;
    public AVM1Operation target;
    public AVM1Operation value;
    
    @Override
    public void aggregate() {
        if( value         == null ) value = consumePrevious();
        if( propertyIndex == null ) propertyIndex = consumePrevious();
        if( target        == null ) target = consumePrevious();
    }

    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.setProperty();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitSetProperty( this );        
    }
}
