package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Sets up the array of implemented interfaces for a class.
 *
 * @author nickmain
 */
public class Implements extends AVM1OperationAggregation {

    public AVM1Operation constructor;
    public AVM1Operation interfaceCount;
    public AVM1Operation[] interfaces;
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( constructor    == null ) constructor = consumePrevious();
        if( interfaceCount == null ) interfaceCount = consumePrevious();
        
        if( interfaces == null ) {
            if( ! ( interfaceCount instanceof ConstantOp )) throw new RuntimeException( "interface count is not a constant" );
            
            int count = interfaceCount.intValue();
            interfaces = new AVM1Operation[ count ];
            
            for( int i = 0; i < interfaces.length; i++ ) {
                interfaces[i] = consumePrevious();
            }
        }
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        block._implements();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitImplements( this );        
    }
}
