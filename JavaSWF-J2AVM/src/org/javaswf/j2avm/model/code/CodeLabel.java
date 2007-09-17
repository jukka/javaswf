package org.javaswf.j2avm.model.code;

/**
 * A label that can be the target of branches or exception handlers
 *
 * @author nickmain
 */
public class CodeLabel extends Instruction {

    /**
     * @see org.javaswf.j2avm.model.code.Instruction#accept(org.javaswf.j2avm.model.code.Instructions)
     */
    @Override
    public void accept(Instructions visitor) {
        visitor.label( this );        
    }
}
