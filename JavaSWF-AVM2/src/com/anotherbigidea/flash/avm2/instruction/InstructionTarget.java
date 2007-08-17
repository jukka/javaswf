package com.anotherbigidea.flash.avm2.instruction;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.epistem.io.OutStream;

import com.anotherbigidea.flash.avm2.ABC.Instructions;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile.WriteContext;

/**
 * A pseudo-instruction that denotes the target of a branch or exception handler.
 *
 * @author nickmain
 */
public class InstructionTarget extends Instruction implements Comparable<Instruction> {

    /** The target label */
    public final int label;
    
    /** The set of objects that target this instruction */
    public final Set<InstructionTargetter> targetters;
    
    /*pkg*/ InstructionTarget( int label ) {
        super(null);
        this.label = label;
        
        targetters_internal = new HashSet<InstructionTargetter>();
        targetters = Collections.unmodifiableSet( targetters_internal );
    }

    /*pkg*/ final Set<InstructionTargetter> targetters_internal;

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + label;
        return result;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( getClass() != obj.getClass() )
            return false;
        final InstructionTarget other = (InstructionTarget) obj;
        if( label != other.label )
            return false;
        return true;
    }

    /** @see java.lang.Comparable#compareTo(java.lang.Object) */
    public int compareTo(Instruction o) {
        if( o == null ) return -1;        
        if( ! (o instanceof InstructionTarget)) return -1;
        
        return label - ((InstructionTarget) o).label;
    }

    /** @see com.anotherbigidea.flash.avm2.instruction.Instruction#initPool(com.anotherbigidea.flash.avm2.model.AVM2ABCFile.WriteContext) */
    @Override
    public void initPool(WriteContext context) {
        //nada
    }

    /** @see com.anotherbigidea.flash.avm2.instruction.Instruction#write(com.anotherbigidea.flash.avm2.ABC.Instructions, com.anotherbigidea.flash.avm2.model.AVM2ABCFile.WriteContext) */
    @Override
    public void write(Instructions instrs, WriteContext context) {
        write( instrs );
    }

    /** @see com.anotherbigidea.flash.avm2.instruction.Instruction#write(com.anotherbigidea.flash.avm2.ABC.Instructions) */
    @Override
    public void write(Instructions instrs) {
        instrs.target( label );
    }

    /** @see com.anotherbigidea.flash.avm2.instruction.Instruction#write(org.epistem.io.OutStream) */
    @Override
    public void write(OutStream out) throws IOException {
        //nada
    }
}
