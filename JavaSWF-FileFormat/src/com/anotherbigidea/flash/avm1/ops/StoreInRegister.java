package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import org.epistem.code.LocalValue;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Store top stack value (without popping) in a local register
 *
 * @author nickmain
 */
public class StoreInRegister extends AVM1OperationAggregation implements AVM1ValueProducer {

    public final int registerNumber;    
    public AVM1Operation value;

    /**
     * The abstracted value that this register contains
     */
    public LocalValue<AVM1Operation> localValue;
    
    public StoreInRegister( int registerNumber ) {
        this.registerNumber = registerNumber;
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( value == null ) value = consumePrevious();
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        block.storeInRegister( ( localValue  != null ) ? localValue.hashCode() : registerNumber );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitStoreInRegister( this );        
    }
}
