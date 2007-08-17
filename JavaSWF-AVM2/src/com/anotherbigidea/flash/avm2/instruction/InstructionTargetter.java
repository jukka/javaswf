package com.anotherbigidea.flash.avm2.instruction;

import java.util.Set;

/**
 * Implemented by instructions and other objects that target instructions.
 *
 * @author nickmain
 */
public interface InstructionTargetter {

    /**
     * The instructions targetted by this object.
     * 
     * @return immutable set
     */
    public Set<InstructionTarget> targets();
    
}
