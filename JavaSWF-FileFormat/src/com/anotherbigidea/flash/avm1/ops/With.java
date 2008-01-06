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
public class With extends AVM1OperationAggregation {

    /**
     * Label of the instruction after the end of the with-block
     */
    public String endLabel();
    
    public AVM1Operation object;

    /**
     * @param endLabel the label just after the end of the block
     */
    public With( String endLabel ) {
        this.endLabel = endLabel;
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( object == null ) object = consumePrevious();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#labelReferences() */
    @Override
    public String[] labelReferences() {
        return new String[] { endLabel };
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
