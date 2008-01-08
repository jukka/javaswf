package com.anotherbigidea.flash.avm2.instruction;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an allocated register and can be used in place of an actual
 * register number.  The register will be allocated by the code analyzer.       
 *
 * @author nickmain
 */
public class RegisterAllocation {

    /**
     * A node in the interference graph.  RegisterAllocations in this set
     * represent values that are live at the same time as this value.
     */
    private Set<RegisterAllocation> collisions = new HashSet<RegisterAllocation>();
        
    /**
     * Set of instructions that reference this allocation
     */
    /*pkg*/ Set<Instruction> references = new HashSet<Instruction>();
    
}
