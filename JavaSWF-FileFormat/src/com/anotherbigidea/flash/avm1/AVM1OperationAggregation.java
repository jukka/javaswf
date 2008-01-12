package com.anotherbigidea.flash.avm1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.anotherbigidea.flash.avm1.ops.StackValue;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.writers.ActionTextWriter;

/**
 * An operation that can aggregate other operations. The aggregated operations
 * are ordered in terms of execution order.
 *
 * @author nickmain
 */
public abstract class AVM1OperationAggregation extends AVM1Operation 
                                               implements Iterable<AVM1Operation> {

    /**
     * The aggregated ops
     */
    protected List<AVM1Operation> ops = new ArrayList<AVM1Operation>();
    
    /**
     * The count of aggregated operations
     */
    public final int count() {
        return ops.size();
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    public final Iterator<AVM1Operation> iterator() {
        return ops.iterator();
    }
    
    /**
     * Cause this operation to aggregate the preceding operations it requires.
     */
    public abstract void aggregate();
    
    /**
     * Consume the previous operation and prepend it to the op list
     */
    protected final AVM1Operation consumePrevious() {
        if( prev == null ) throw new RuntimeException( "cannot consume null previous op" );
        
        AVM1Operation op = prev;
        
        if( op instanceof AVM1OperationAggregation ) {
            ((AVM1OperationAggregation) op).aggregate();
        }
        
        if( !(op instanceof AVM1ValueProducer )) {
            op = new StackValue();
        }
        else {
            op.remove();
        }
        
        return prependOp( op );
    }
    
    /**
     * Prepend an op to the list of consumed ops.
     * @return the op
     */
    protected final AVM1Operation prependOp( AVM1Operation op ) {
        ops.add( 0, op );
        return op;
    }
    
    /**
     * Write the operation
     */
    protected abstract void writeOp( SWFActionBlock block ) throws IOException;

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write(SWFActionBlock block) throws IOException {
        for( AVM1Operation op : ops ) {
            op.write( block );
        }
        
        writeOp( block );
    }
    
    /**
     * Print the operation
     */
    public void print( ActionTextWriter writer ) throws IOException {
        writeOp( writer );

        if( ! ops.isEmpty() ) {
            writer.indent();
            
            for( AVM1Operation op : ops ) {
                op.print( writer );
            }
            
            writer.unindent();
        }        
    }

    /** Visit all the aggregated ops */
    public final void visitAggregated( AVM1OpVisitor visitor ) {
        for( AVM1Operation op : ops ) {
            op.accept( visitor );
        }                
    }
}
