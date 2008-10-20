package org.epistem.code;

import java.util.Collection;

/**
 * An adaptor for a particular instruction type
 * 
 * @author nickmain
 */
public interface InstructionAdaptor<INSTRUCTION_TYPE,FRAME_TYPE> {

    /**
     * Compute the resulting frame for an instruction
     *  
     * @param instruction the instruction
     * @param incomingFrames the incoming frames
     * @return the outgoing frame
     */
    public FRAME_TYPE computeFrame( INSTRUCTION_TYPE instruction, Collection<FRAME_TYPE> incomingFrames );
    
    
    /**
     * Get the set of instructions that can follow the given one
     * 
     * @param instruction the instruction in question
     * @return not null
     */
    public Collection<INSTRUCTION_TYPE> getFollowOnInstructions( INSTRUCTION_TYPE instruction );
    
    /**
     * Get the locals referenced by the instruction
     * 
     * @param instruction the instruction in question
     * @param locals the collection to add the locals to
     */
    public void gatherReferencedLocals( INSTRUCTION_TYPE instruction, Collection<LocalValue<INSTRUCTION_TYPE>> locals );
    
    /**
     * Notification of the last instruction in the lifespan of a local value
     * 
     * @param instruction the last instruction where the local value is alive
     * @param local the local value
     */
    public void endOfLocalLifespan( INSTRUCTION_TYPE instruction, LocalValue<INSTRUCTION_TYPE> local );
}
