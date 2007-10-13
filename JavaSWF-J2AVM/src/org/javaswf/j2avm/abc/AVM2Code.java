package org.javaswf.j2avm.abc;

import com.anotherbigidea.flash.avm2.Operation;
import static com.anotherbigidea.flash.avm2.Operation.*;
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
    
    /**
     * Append an instruction to the list
     */
    public void append( Operation op, Object...args ) {
        instructions.append( op, args );
    }
    
    /**
     * Append a branch label to the list
     */
    public void appendLabel( int label ) {
        instructions.appendTarget( label );
    }
    
    /**
     * Load a local variable onto the stack
     * 
     * @param varIndex the local var index
     */
    public void loadLocalVar( int varIndex ) {
        
        Operation op;
        
        switch( varIndex ) {
            case 0: op = OP_getlocal0; break;
            case 1: op = OP_getlocal1; break;
            case 2: op = OP_getlocal2; break;
            case 3: op = OP_getlocal3; break;
            
            default:
                instructions.append( OP_getlocal, varIndex );
                return;
        }
        
        instructions.append( op );
    }
    
    /**
     * Pop the stack and store into a local variable
     * 
     * @param varIndex the local var index
     */
    public void storeLocalVar( int varIndex ) {
        
        Operation op;
        
        switch( varIndex ) {
            case 0: op = OP_setlocal0; break;
            case 1: op = OP_setlocal1; break;
            case 2: op = OP_setlocal2; break;
            case 3: op = OP_setlocal3; break;
            
            default:
                instructions.append( OP_setlocal, varIndex );
                return;
        }
        
        instructions.append( op );
    }
    
    /**
     * Set up the initial scope for a method
     */
    public void setupInitialScope() {
        instructions.append( OP_getlocal0 );
        instructions.append( OP_pushscope );
    }
}
