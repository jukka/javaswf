package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1BlockContainer;
import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.writers.ActionTextWriter;

/**
 * A "with" block
 *
 * @author nickmain
 */
public class With extends AVM1OperationAggregation implements AVM1BlockContainer {

    public final AVM1ActionBlock block = new AVM1ActionBlock( this );
    
    public AVM1Operation object;

    /** @see com.anotherbigidea.flash.avm1.AVM1BlockContainer#subBlocks() */
    public AVM1ActionBlock[] subBlocks() {
        return new AVM1ActionBlock[] { block };
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( object == null ) object = consumePrevious();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        SWFActionBlock withBlock = block.startWith();        
        this.block.write( withBlock );
    }
    
    /**
     * Print the operation
     */
    public void print( ActionTextWriter writer ) throws IOException {
        
        if( object != null ) {
            writer.println( "-- object for with :" );
            writer.indent();
            object.print( writer );
            writer.unindent();
        }
        
        writer.startWith();
        block.print( writer );
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitWith( this );        
    }
}
