package org.javaswf.j2avm.model.code.expression;

/**
 * Base for Statement and Expression - objects that can contain expressions.
 *
 * @author nickmain
 */
public abstract class ExpressionContainer {
	
	protected final Expression[] children; //in order of evaluation
	
	protected ExpressionContainer( Expression...children ) {
		this.children = children;
		
		for( Expression e : children ) {
			e.parent = this;
		}
	}
	
    /**
     * Visit all the child expressions in evaluation order.
     */
    public final void visitChildren( ExpressionVisitor visitor ) {
    	for( Expression e : children ) {
    		e.accept( visitor );
    	}
    }
}
