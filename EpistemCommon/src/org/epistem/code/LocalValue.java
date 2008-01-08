package org.epistem.code;

import java.util.Collection;
import java.util.HashSet;


/**
 * Represents a local value that can be allocated to a register
 *
 * @author nickmain
 */
public class LocalValue<INSTRUCTION_TYPE> {

    public int allocatedRegister = -1;
    
    /**
     * Instructions that set the value
     */
    public final Collection<INSTRUCTION_TYPE> setters = new HashSet<INSTRUCTION_TYPE>();

    /**
     * Instructions that get the value
     */
    public final Collection<INSTRUCTION_TYPE> getters = new HashSet<INSTRUCTION_TYPE>();
    
    public LocalValue() {}
    
    public LocalValue( int registerIndex ) {
        this.allocatedRegister = registerIndex;        
    }
}
