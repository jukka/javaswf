package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Create and initialize a new object
 *
 * @author nickmain
 */
public class InitObject extends AVM1OperationAggregation implements AVM1ValueProducer {

    public AVM1Operation count;
    public Map<AVM1Operation, AVM1Operation> members;
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( count == null ) count = consumePrevious();
        
        if( members == null ) {
            if( !( count instanceof ConstantOp )) throw new RuntimeException( "member count is not a constant" );
            int memCount = count.intValue();
            
            members = new HashMap<AVM1Operation, AVM1Operation>();
            
            for( int i = 0; i < memCount; i++ ) {
                AVM1Operation value = consumePrevious();
                AVM1Operation name  = consumePrevious();
                
                members.put( name, value );
            }
        }
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.initObject();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitInitObject( this );        
    }
}
