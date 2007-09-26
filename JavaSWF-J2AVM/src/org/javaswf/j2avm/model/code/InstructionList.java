package org.javaswf.j2avm.model.code;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class InstructionList implements Iterable<Instruction> {

    /**
     * Get the number of instructions in the list
     */
    public int size() {
        return count;
    }

    /**
     * Remove the given instruction from the list
     */
    public void remove( Instruction insn ) {
        Instruction next = insn.next;
        Instruction prev = insn.prev;
        
        if( first == insn ) first = next;
        if( last  == insn ) last  = prev;
        
        if( next != null ) next.prev = prev;
        if( prev != null ) prev.next = next;
        
        count--;
        
        insn.list = null;
        
        if( insn instanceof LabelTargetter ) {
            ((LabelTargetter) insn).release();
        }
    }
  
    /**
     * Get a cursor that is positioned at the start of the list
     */
    public InstructionCursor cursorAtStart() {
        return new InstructionCursor( this, first );
    }
    
    /**
     * Get a cursor that is positioned at the end of the list
     */
    public InstructionCursor cursorAtEnd() {
        return new InstructionCursor( this, null );
    }
    
    /**
     * Get a cursor that is positioned before the given instruction
     */
    public InstructionCursor cursorBefore( Instruction insn ) {
     	if( insn.list != this ) {
     		throw new IllegalArgumentException( "Instruction is not part of this list" );
     	}
    	
        return new InstructionCursor( this, insn );
    }
    
    /**
     * Get a cursor that is positioned after the given instruction
     */
    public InstructionCursor cursorAfter( Instruction insn ) {
     	if( insn.list != this ) {
     		throw new IllegalArgumentException( "Instruction is not part of this list" );
     	}
    	
        return new InstructionCursor( this, insn.next );
    }
    
    /**
     * Label the given instruction by inserting a CodeLabel before it.
     * 
     * @param label the label text - this should be unique within the list
     * @param insn the instruction to label
     * @return the new label (or the given instruction if it is a label)
     */
    public CodeLabel label( Instruction insn, String label ) {
     	if( insn.list != this ) {
     		throw new IllegalArgumentException( "Instruction is not part of this list" );
     	}

     	if( insn instanceof CodeLabel ) return (CodeLabel) insn;
     	
     	CodeLabel cl = new CodeLabel( label );
    	insert( cl, insn.prev, insn );
    	return cl;
    }
    
    /**
     * Get an iterator over the instructions
     */
    public Iterator<Instruction> iterator() {
		return new Iterator<Instruction>(){
			
			private Instruction next = first;
			private Instruction curr = null;
			
			/** @see java.util.Iterator#hasNext() */
			public boolean hasNext() {
				return next != null ;
			}

			/** @see java.util.Iterator#next() */
			public Instruction next() {
				if( next == null ) throw new NoSuchElementException();
				curr = next;
				next = curr.next;
				return curr;
			}

			/** @see java.util.Iterator#remove() */
			public void remove() {
				if( curr == null ) throw new IllegalStateException();
				curr.remove();
				curr = null;
			}
		};
	}

    /**
     * Get the first instruction in the list.
     * 
     * @return null if the list is empty
     */
    public Instruction first() { return first; }
    
    /**
     * Get the last instruction in the list.
     * 
     * @return null if the list is empty
     */
    public Instruction last() { return last; }
    
    /**
     * Whether this list is empty
     */
    public boolean isEmpty() { return count == 0; }
    
	private Instruction first;
    private Instruction last;
    private int count;
    
    /**
     * If true then this list has been normalized such that 64 bit types
     * are assumed to be single slots (rather than the double slots that the
     * JVM normally uses).
     */
    /*pkg*/ boolean hasBeenNormalized;
    
    /*pkg*/ void insert( Instruction newInsn, 
                         Instruction prevInsn, Instruction nextInsn ) {
        newInsn.next = nextInsn;
        newInsn.prev = prevInsn;
        newInsn.list = this;
        
        if( prevInsn == null ) {
            first = newInsn;
        } else {
            prevInsn.next = newInsn;
        }
        
        if( nextInsn == null ) {
            last = newInsn;
        } else {
            nextInsn.prev = newInsn;
        }
        
        count++;
    }    
}
