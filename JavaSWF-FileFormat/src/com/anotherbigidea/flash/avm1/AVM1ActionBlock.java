package com.anotherbigidea.flash.avm1;

/**
 * A block of AVM1 Actions
 *
 * @author nickmain
 */
public class AVM1ActionBlock {

    private AVM1Operation first;
    private AVM1Operation last;
    private int count;
    
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
        count++;
        op.block = this;
        
        if( first == null ) first = op;
        
        if( last != null ) {
            last.next = op;
            op.prev = last;
        }
        
        last = op;
    }
}
