package com.anotherbigidea.flash.avm1;

import java.io.IOException;
import java.util.*;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm1.ops.JumpLabel;
import com.anotherbigidea.flash.avm1.ops.PushRegister;
import com.anotherbigidea.flash.avm1.ops.StoreInRegister;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.writers.ActionTextWriter;

/**
 * A block of AVM1 Actions
 *
 * @author nickmain
 */
public class AVM1ActionBlock {

    /**
     * The operation that owns this block - null for a top-level block
     */
    public final AVM1Operation owner;
    
    private AVM1Operation first;
    private AVM1Operation last;
    private int count;
    private int registerCount = 0;

    //--the labels in this block (or any sub-block)
    private final Map<String, JumpLabel> labels;
    private final Map<String, Collection<AVM1Operation>> labelReferences; //reference counts
    
    public AVM1ActionBlock() {
        owner  = null;
        labels = new HashMap<String, JumpLabel>();        
        labelReferences = new HashMap<String, Collection<AVM1Operation>>();
    }
    
    public AVM1ActionBlock( AVM1Operation owner ) {
        this.owner = owner;
        labels = null;
        labelReferences = null;
    }
    
    /**
     * Count of registers used in this block - as preset or determined by
     * added operations
     */
    public int registerCount() {
        return registerCount;
    }
    
    /**
     * Preset the register count
     */
    public void setRegisterCount( int count ) {
        registerCount = count;
    }
    
    /**
     * Find a label in this block, any sub-block or outer-block.
     *  
     * @return null if the label does not exist
     */
    public JumpLabel findLabel( String label ) {
        if( labels != null ) {
            return labels.get( label );
        }
        
        if( owner != null ) {
            return owner.block.findLabel( label );
        }
        
        return null;
    }

    /**
     * Find a label in this block, any sub-block or outer-block, and get the
     * references to it.  This does not guarantee that the label exists - just
     * that there are references to it.
     *  
     * @return empty collection if the label does not exist
     */
    public Collection<AVM1Operation> labelRefs( String label ) {
        if( labels != null ) {
            return labelReferences.get( label );
        }
        
        if( owner != null ) {
            return owner.block.labelRefs( label );
        }
        
        return Collections.emptySet();
    }

    /**
     * Remove a label - just the label, not any references
     */
    private void dropLabel( JumpLabel label ) {
        if( labels != null ) {
            labels.remove( label.label );
        }
        
        else if( owner != null ) {
            owner.block.dropLabel( label );
        }
    }

    /**
     * Add a label 
     */
    private void addLabel( JumpLabel label ) {
        if( labels != null ) {
            labels.put( label.label, label );
        }
        
        else if( owner != null ) {
            owner.block.addLabel( label );
        }
    }
    
    /**
     * Add a reference to a label
     */
    private void referenceLabel( String label, AVM1Operation op ) {
        if( labelReferences != null ) {
            Collection<AVM1Operation> refs = labelReferences.get( label );
            if( refs == null ) {
                refs = new HashSet<AVM1Operation>();
                labelReferences.put( label, refs );
            }
            
            refs.add( op );
        }
        
        else if( owner != null ) {
            owner.block.referenceLabel( label, op );
        }
    }
    
    /**
     * Remove a reference to a label
     */
    private void dereferenceLabel( String label, AVM1Operation op ) {
        if( labelReferences != null ) {
            Collection<AVM1Operation> refs = labelReferences.get( label );
            if( refs != null ) {
                refs.remove( op );
                if( refs.isEmpty() ) labelReferences.remove( label );
            }
        }
        
        else if( owner != null ) {
            owner.block.dereferenceLabel( label, op );
        }
    }
    
    /**
     * Called when all operations have been added to the block.
     */
    public void complete() {
        if( owner == null ) { //only outer block
        
            //remove extraneous labels (only if outer block)        
            Set<JumpLabel> extraneousLabels = new HashSet<JumpLabel>();
            for( String label : labels.keySet() ) {
                Collection<AVM1Operation> refs = labelReferences.get( label );
                if( refs == null || refs.isEmpty()) {
                    extraneousLabels.add( labels.get( label ) );
                }
            }
            
            for( JumpLabel label : extraneousLabels ) {
                label.remove();
            }
            
            aggregateAll();
        }
    }
    
    /**
     * Aggregate all operations
     */
    public void aggregateAll() {
        for( AVM1Operation op = first; op != null; op = op.next() ) {
            if( op instanceof AVM1OperationAggregation ) {
                AVM1OperationAggregation agg = (AVM1OperationAggregation) op;                
                agg.aggregate();
            }
            
            if( op instanceof AVM1BlockContainer ) {
                AVM1BlockContainer container = (AVM1BlockContainer) op;
                AVM1ActionBlock[] blocks = container.subBlocks();
                
                for( AVM1ActionBlock block : blocks ) {
                    block.aggregateAll();
                }
            }
        }
    }

    /**
     * Print all operations
     */
    public void print( ActionTextWriter actwriter ) throws IOException {
        for( AVM1Operation op = first; op != null; op = op.next() ) {
            op.print( actwriter );
        }
    }
    
    /**
     * Print all operations
     */
    public void print( IndentingPrintWriter ipw ) throws IOException {
        print( new ActionTextWriter( ipw ));
    }
    
    /**
     * Get the operation count (operations that have not been aggregated)
     */
    public final int count() {
        return count;
    }
    
    /**
     * Get the first operation in the list
     * 
     * @return null if there are no operations
     */
    public final AVM1Operation first() {
        return first;
    }

    /**
     * Get the last operation in the list
     * 
     * @return null if there are no operations
     */
    public final AVM1Operation last() {
        return last;
    }

    /**
     * Remove an operation from this block
     */
    /*pkg*/ void remove( AVM1Operation op ) {
        
        if( op instanceof JumpLabel ) {
            dropLabel( (JumpLabel) op );
        }
        else {
            String label = op.labelReference();
            if( label != null ) {
                dereferenceLabel( label, op );
            }
        }
        
        if( op.block != this ) return;
        count--;
        
        AVM1Operation prev = op.prev;
        AVM1Operation next = op.next;

        if( op == first ) first = next;
        if( op == last  ) last  = prev;
        
        if( prev != null ) prev.next = next;
        if( next != null ) next.prev = prev;
        
        op.prev  = null;
        op.next  = null;
        op.block = null;
    }
    
    /**
     * Append an operation to this block
     */
    /*pkg*/ void append( AVM1Operation op ) {
        //System.out.println( "Appending " + op.getClass().getSimpleName() );

        if( op instanceof JumpLabel ) {
            addLabel( (JumpLabel) op );
        }
        else {
            String label = op.labelReference();
            if( label != null ) {
                referenceLabel( label, op );
            }
        }

        if( op instanceof StoreInRegister ) {
            registerCount = Math.max( registerCount, ((StoreInRegister) op).registerNumber + 1 );
        }
        else if( op instanceof PushRegister ) {
            registerCount = Math.max( registerCount, ((PushRegister) op).registerNumber + 1 );
        }
        
        count++;
        op.block = this;
        
        if( first == null ) first = op;
        
        if( last != null ) {
            last.next = op;
            op.prev = last;
        }
        
        last = op;
    }
    
    /**
     * Write this block
     */
    public void write( SWFActionBlock block ) throws IOException {
        AVM1Operation op = first;
        
        while( op != null ) {
            op.write( block );
            op = op.next();
        }
        
        block.end();
    }
    
    /**
     * Visit all the operations in this list, in order
     */
    public void accept( AVM1OpVisitor visitor ) {
        for( AVM1Operation op = first(); op != null; op = op.next() ) {
            System.err.println( "About to visit " + op.getClass().getSimpleName() );
            op.accept( visitor );
        }
    }   
}
