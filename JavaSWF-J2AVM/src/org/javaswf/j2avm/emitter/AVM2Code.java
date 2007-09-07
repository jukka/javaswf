package org.javaswf.j2avm.emitter;

import com.anotherbigidea.flash.avm2.instruction.InstructionList;

/**
 * A wrapper around an AVM2 instruction list in order to capture AVM2 bytecode
 * idioms and manage stack and scope limits.
 *
 * @author nickmain
 */
public class AVM2Code {

    /** The target instruction list */
    public final InstructionList instructions;
    
    /**
     * @param instructions the instruction list to target
     */
    public AVM2Code( InstructionList instructions ) {
        this.instructions = instructions;
    }
    
    
}
