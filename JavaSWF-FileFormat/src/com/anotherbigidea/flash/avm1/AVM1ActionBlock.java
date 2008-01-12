package com.anotherbigidea.flash.avm1;

import java.io.IOException;
import java.util.*;

import org.epistem.code.LocalValue;
import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm1.ops.*;
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
    private final Map<String, JumpLabel> labels = new HashMap<String, JumpLabel>();
    private final Map<String, Collection<AVM1Operation>> labelReferences = 
        new HashMap<String, Collection<AVM1Operation>>();
    
    private final Set<StoreInRegister> registerStores = new HashSet<StoreInRegister>();
    private final Set<Try>             tryBlocks      = new HashSet<Try>();
    
    public AVM1ActionBlock() {
        this( null );
    }
    
    public AVM1ActionBlock( AVM1Operation owner ) {
        this.owner = owner;
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
        labels.remove( label.label );
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
        Collection<AVM1Operation> refs = labelReferences.get( label );
        if( refs == null ) {
            refs = new HashSet<AVM1Operation>();
            labelReferences.put( label, refs );
        }
        
        refs.add( op );
    }
    
    /**
     * Remove a reference to a label
     */
    private void dereferenceLabel( String label, AVM1Operation op ) {
        Collection<AVM1Operation> refs = labelReferences.get( label );
        if( refs != null ) {
            refs.remove( op );
            if( refs.isEmpty() ) labelReferences.remove( label );
        }
    }
    
    /**
     * Called when all operations have been added to the block.
     */
    public void complete() {
        //only do this for outer block
        if( owner == null ) complete_internal();
    }
    
    private void complete_internal() {
        //remove extraneous labels        
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
        
        determineLocalValues();        
        aggregateAll();
    }
    
    /**
     * Wire up the initial "fake" register stores for a function body
     */
    private void wireUpInitialRegisterStores() {
        if( owner != null && owner instanceof Function ) {
            Function func = (Function) owner;
            
            for( int i = 0; i < func.paramRegisters.length; i++ ) {
                StoreInRegister store = func.paramRegisters[i];
                if( store == null ) continue;
                
                registerStores.add( store );
                ((AVM1Operation) store).next = first;                    
            }
            
            StoreInRegister[] specialRegs = {
                    func.thisRegister,
                    func.argumentsRegister,
                    func.superRegister,
                    func.rootRegister,
                    func.parentRegister,
                    func.globalRegister 
            };
            
            for( int i = 0; i < specialRegs.length; i++ ) {
                StoreInRegister store = specialRegs[i];
                if( store == null ) continue;
                
                registerStores.add( store );
                ((AVM1Operation) store).next = first;                    
            }
        }
    }
    
    /**
     * Determine the local values for the registers.  A local value represents
     * a value stored in a register that has a defined lifespan.  More than
     * one local value may use the same register, if the lifespans do not
     * overlap.
     * 
     * (Should be done before aggregation)
     */
    private void determineLocalValues() {
        
        wireUpInitialRegisterStores();
        
        //iterate over the register stores - for each, follow the control-flow
        // to find all the register pushes
        for( StoreInRegister store : registerStores ) {
            LocalValue<AVM1Operation> local = new LocalValue<AVM1Operation>( store.registerNumber );
            local.setters.add( store );
            store.localValue = local;
            
            //find all uses
            LinkedList<AVM1Operation> queue = new LinkedList<AVM1Operation>();
            Set<AVM1Operation> visited = new HashSet<AVM1Operation>();
            queue.addAll( store.getFollowOnInstructions( this ) );
            
            System.err.println( "---------------------" );
            
            while( ! queue.isEmpty() ) {
                
                for( AVM1Operation op : queue ) {
                    if( op == null ) System.err.print( "null " );
                    else System.err.print( op.getClass().getSimpleName() + " " );                    
                }
                System.err.println();
                
                AVM1Operation op = queue.remove();
                if( visited.contains( op ) ) continue;
                visited.add( op );
                
                if( op instanceof StoreInRegister ) {
                    StoreInRegister s = (StoreInRegister) op;
                    
                    //another store for this register is the end of the
                    //local value's lifespan
                    if( s.registerNumber == store.registerNumber ) continue;
                }
                
                if( op instanceof PushRegister ) {
                    PushRegister p = (PushRegister) op;
                    
                    //push of this value
                    if( p.registerNumber == store.registerNumber ) {
                        
                        //if this push is already associated with a value
                        //from an alternate store then we need to change
                        //all the setters and getters of that value to use this
                        //one
                        if( p.localValue != null ) {
                            LocalValue<AVM1Operation> otherLocal = p.localValue;
                            
                            for( AVM1Operation setter : otherLocal.setters ) {
                                StoreInRegister otherStore = (StoreInRegister) setter;
                                otherStore.localValue = local;
                                local.setters.add( otherStore );
                            }
                            
                            for( AVM1Operation getter : otherLocal.getters ) {
                                PushRegister otherPush =  (PushRegister) getter;
                                otherPush.localValue = local;
                                local.getters.add( otherPush );                                
                            }
                        }
                        
                        p.localValue = local;
                        local.getters.add( p );
                    }
                }

                //visit downstream operations
                queue.addAll( op.getFollowOnInstructions( this ) );

                //make sure that the visit goes into catch and finally blocks
                if( op instanceof Try ) {
                    Try _try = (Try) op;
                    if( _try.tryCatch   != null ) queue.add( _try.tryCatch.next() );
                    if( _try.tryFinally != null ) queue.add( _try.tryFinally.next() );
                }
            }
        }
    }
    
    /**
     * Aggregate all operations
     */
    public void aggregateAll() {
        for( AVM1Operation op = first; op != null; op = op.next() ) {
            if( op instanceof Function ) {
                ((Function) op).body.complete_internal();
            }
            
            if( op instanceof AVM1OperationAggregation ) {
                AVM1OperationAggregation agg = (AVM1OperationAggregation) op;                
                agg.aggregate();
            }
            
            if( op instanceof Function ) {
                Function func = (Function) op;
                AVM1ActionBlock block = func.body;
                block.complete();
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
            String[] labels = op.labelReferences();
            for( String label : labels ) {
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
            String[] labels = op.labelReferences();
            for( String label : labels ) {
                referenceLabel( label, op );
            }
        }

        if( op instanceof StoreInRegister ) {
            registerCount = Math.max( registerCount, ((StoreInRegister) op).registerNumber + 1 );
            registerStores.add( (StoreInRegister) op );
        }
        else if( op instanceof PushRegister ) {
            registerCount = Math.max( registerCount, ((PushRegister) op).registerNumber + 1 );
        }
        else if( op instanceof Try ) {
            tryBlocks.add( (Try) op );
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
