package org.javaswf.j2avm.model.code;

import java.util.HashSet;
import java.util.Set;

import org.epistem.io.IndentingPrintWriter;

/**
 * A label that can be the target of branches or exception handlers
 *
 * @author nickmain
 */
public class CodeLabel extends Instruction {

    /**
     * Set of things that target this label
     */
    public final Set<LabelTargetter> targetters = new HashSet<LabelTargetter>();
    
	/**
	 * The string form of the label
	 */
	final String label;
	
	public CodeLabel( String label ) {
		this.label = label;
	}
	
	public CodeLabel( ) { 
		label = "label" + hashCode();
	}
		
	/**
	 * Label the given instruction with this label.  This label is inserted
	 * into the target list just before the instruction.
	 * 
	 * @param insn the instruction to label
	 */
	public void labelInstruction( Instruction insn ) {
		if( list != null ) throw new IllegalStateException( "Cannot use a label already in a list" );
		
		insn.list.insert( this, insn.prev, insn );
	}
	
	/**
	 * Get a cursor positioned immediately after this label.
	 */
	public InstructionCursor cursor() {
		return new InstructionCursor( list, next );
	}
	
	/**
	 * Get the incoming arcs (from other instructions) to this label.
	 */
	public Set<Instruction> incomingArcs() {
	    Set<Instruction> incoming = new HashSet<Instruction>();
	    if( prev != null && prev.flowsToNext() ) incoming.add( prev );
	    
	    for( LabelTargetter targetter : targetters ) {
	        if( targetter instanceof Instruction ) {
	            incoming.add( (Instruction) targetter );
	        }
	    }
	    
	    return incoming;	    
	}
	
    /**
     * @see org.javaswf.j2avm.model.code.Instruction#accept(org.javaswf.j2avm.model.code.Instructions)
     */
    @Override
    public void accept(Instructions visitor) {
        visitor.label( this );        
    }

	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
	@Override
	public void dump(IndentingPrintWriter ipw) {
		ipw.println( "--- " + label + " ---" );		
	}

	/** @see java.lang.Object#toString() */
	@Override
	public String toString() {
		return label;
	}

	/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
	@Override
	protected Frame compute() {
		return frame;
	}
}
