package org.epistem.code;

import java.util.HashSet;
import java.util.Set;

/**
 * A node in a local-value interference graph
 *
 * @author nickmain
 */
public class InterferenceNode<INSTRUCTION_TYPE> {

    /**
     * The local value
     */
    public final LocalValue<INSTRUCTION_TYPE> local;
    
    /**
     * The other locals that overlap this one
     */
    public final Set<InterferenceNode<INSTRUCTION_TYPE>> concurrentValues = new HashSet<InterferenceNode<INSTRUCTION_TYPE>>();
    
    public InterferenceNode( LocalValue<INSTRUCTION_TYPE> local ) {
        this.local = local;
    }
}
