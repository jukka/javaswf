package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * Base for expressions
 *
 * @author nickmain
 */
public abstract class Expression {

    /**
     * Get the type of this expression
     * 
     * @return null if this is a void method call
     */
    public abstract ValueType type();
    
    /**
     * Accept a visitor.
     */
    public abstract void accept( Expressions visitor );
    
    /**
     * Visit all the child expressions in evaluation order.
     */
    public abstract void visitChildren( Expressions visitor );
    
}
