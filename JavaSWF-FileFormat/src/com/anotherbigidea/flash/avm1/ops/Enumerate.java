package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Enumerate the property names for an object
 *
 * @author nickmain
 */
public class Enumerate extends AVM1OperationAggregation implements AVM1ValueProducer {

    public final boolean expectsVarName; //true if stack value is var name rather than object
    
    public AVM1Operation object;

    public Enumerate(  boolean expectsVarName ) {
        this.expectsVarName = expectsVarName;
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( object == null ) object = consumePrevious();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        if( expectsVarName ) block.enumerate();
        else                 block.enumerateObject();        
    } 
}
