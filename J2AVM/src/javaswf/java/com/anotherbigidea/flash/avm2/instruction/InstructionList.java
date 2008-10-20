package com.anotherbigidea.flash.avm2.instruction;

import java.util.Collections;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.anotherbigidea.flash.avm2.Operation;

/**
 * A list of instructions.
 *
 * @author nickmain
 */
public class InstructionList {

    /** Immutable map of the targets in this list (in offset order) */
    public final SortedMap<Integer,InstructionTarget> targets;
    
    private final SortedMap<Integer,InstructionTarget> targets_internal;

    //--The list:
    private Instruction firstInstruction;
    private Instruction lastInstruction;
    private int instructionCount;
    
    
    public InstructionList( ) {        
        targets_internal = new TreeMap<Integer,InstructionTarget>();
        targets = Collections.unmodifiableSortedMap( targets_internal );
    }

    /**
     * The size of the list - not including InstructionTargets
     */
    public int size() {
       return instructionCount; 
    }
    
    /**
     * Get the first instruction in the list
     * @return null if the list is empty
     */
    public Instruction first() { return firstInstruction; }

    /**
     * Get the last instruction in the list
     * @return null if the list is empty
     */
    public Instruction last() { return lastInstruction; }

    /**
     * Append one or more instructions. Calls append() for each instruction.
     */
    public void appendInstructions( Instruction...instructions ) {
        for (Instruction instruction : instructions) {
            append( instruction );
        }
    }
    
    /**
     * Add a new target to the list.
     * @param label the label
     * @return the new target
     */
    public InstructionTarget appendTarget( int label ) {
        InstructionTarget target = new InstructionTarget( label );
        append( target );
        return target;
    }
    
    /**
     * Insert a target before the given instruction
     * @param labe the label
     * @param next the next instruction
     * @return the new target
     */
    public InstructionTarget addTargetBefore( int label, Instruction next ) {
        InstructionTarget target = new InstructionTarget( label );
        insertBefore( target, next );
        return target;
    }

    /**
     * Insert a target after the given instruction
     * @param label the label
     * @param prev the previous instruction - not null
     * @return the new target
     */
    public InstructionTarget addTargetAfter( int label, Instruction prev ) {
        InstructionTarget target = new InstructionTarget( label );
        insertAfter( target, prev );
        return target;
    }
    
    /**
     * Append an instruction to this list.  The instruction will be owned by
     * this list and removed from any other list it may be part of.
     * If the instruction is already part of this list then nothing will happen.
     */
    public void append( Instruction i ) {
        insertInstruction( i, lastInstruction, null );
    }

    /**
     * Append an instruction to this list.  The instruction will be owned by
     * this list and removed from any other list it may be part of.
     * If the instruction is already part of this list then nothing will happen.
     */
    public void append( Operation op, Object...args ) {
        insertInstruction( new Instruction( op, args ), lastInstruction, null );
    }
    
    /**
     * Insert an instruction before another. The instruction will be owned by
     * this list and removed from any other list it may be part of.
     * If the instruction is already part of this list then nothing will happen.
     * 
     * @param i the instruction to insert
     * @param next the next instruction - must not be null
     */
    public void insertBefore( Instruction i, Instruction next ) {
        insertInstruction(i, next.prevInstruction, next );
    }

    /**
     * Insert an instruction after another. The instruction will be owned by
     * this list and removed from any other list it may be part of.
     * If the instruction is already part of this list then nothing will happen.
     * 
     * @param i the instruction to insert
     * @param prev the previous instruction - must not be null
     */
    public void insertAfter( Instruction i, Instruction prev ) {
        insertInstruction(i, prev, prev.nextInstruction );
    }

    /**
     * Remove an instruction from this list.
     * If the instruction is not part of this list then nothing will happen.
     * If the instruction is an InstructionTarget and it has targetters then
     * it will not be removed.
     */
    public void remove( Instruction i ) {
        if( i == null ) return;

        if( i.instructionList != this ) return; //not in this list
        
        if( i instanceof InstructionTarget ) {
            InstructionTarget it = (InstructionTarget) i;
            if( ! it.targetters.isEmpty() ) return; //cannot remove if there are targetters
            targets_internal.remove( it.label );
            
        } else {
            instructionCount--;            
        }
        
        if( i == firstInstruction ) firstInstruction = i.nextInstruction;
        if( i == lastInstruction  ) lastInstruction  = i.prevInstruction;
        
        if( i.prevInstruction != null ) i.prevInstruction.nextInstruction = i.nextInstruction;
        if( i.nextInstruction != null ) i.nextInstruction.prevInstruction = i.prevInstruction;
        
        i.clear();
    }
    
    /**
     * Insert a new instruction between the two given ones.
     */
    private void insertInstruction( Instruction i, Instruction prev, Instruction next ) {
        if( i == null ) return;

        if( i.instructionList != null ) {
            if( i.instructionList == this ) return; //already in this list            
            if( i instanceof InstructionTarget ) return; //cannot insert a target from another list 
            i.instructionList.remove(i);
        }
        
        i.instructionList = this;
        i.prevInstruction = prev;
        i.nextInstruction = next;

        if( i instanceof InstructionTarget ) {
            InstructionTarget it = (InstructionTarget) i;
            targets_internal.put( it.label, it );
        } else {
            instructionCount++;
        }        
        
        if( prev == null ) firstInstruction = i;
        else prev.nextInstruction = i;
        
        if( next == null ) lastInstruction  = i;
        else next.prevInstruction = i;        
    }
}
