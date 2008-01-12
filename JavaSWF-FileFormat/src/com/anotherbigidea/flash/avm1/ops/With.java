package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

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
     * The end of the with-block
     */
    public WithEnd withEnd;
    
    public AVM1Operation object;
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( object == null ) object = consumePrevious();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp( SWFActionBlock block ) throws IOException {
        SWFActionBlock withBlock = block.startWith();    
        for( AVM1Operation op = next(); op != null; op = op.next() ) {
            if( op == withEnd ) {
                withBlock.end();
                op.write( block );
                break;
            }
            
            op.write( withBlock );
        }        
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

        for( AVM1Operation op = next(); op != null; op = op.next() ) {
            if( op == withEnd ) break;

            op.print( writer );
        }
        
        writer.end();
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitWith( this );        
    }
}
