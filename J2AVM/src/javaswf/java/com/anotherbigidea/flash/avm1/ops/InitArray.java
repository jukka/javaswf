package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Create and initialize a new array
 *
 * @author nickmain
 */
public class InitArray extends AVM1OperationAggregation implements AVM1ValueProducer {

    public AVM1Operation length;
    public AVM1Operation[] elements; //in reverse order
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( length == null ) length = consumePrevious();
        
        if( elements == null ) {
            if( !( length instanceof ConstantOp )) throw new RuntimeException( "array length is not a constant" );
            int argCount = length.intValue();
            elements = new AVM1Operation[ argCount ];
            
            for( int i = 0; i < elements.length; i++ ) {
                elements[i] = consumePrevious();
            }
        }
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.initArray();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitInitArray( this );        
    }
    
    /** Visit all the aggregated elements */
    public final void visitElements( AVM1OpVisitor visitor ) {
        for( AVM1Operation op : elements ) {
            op.accept( visitor );
        }                
    }
}
