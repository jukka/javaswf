package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.code.instruction.UndefinedExpression;


/**
 * Base for Statement and Expression - objects that can contain expressions.
 *
 * @author nickmain
 */
public class ExpressionContainer {
		
	/*pkg*/ final Expression[] children;
	/*pkg*/ ExpressionContainer parent;
	
	public ExpressionContainer( Expression...children ) {
		this.children = children;
		
		for( int i = 0; i < children.length; i++ ) {
			if( children[i] == null ) children[i] = new UndefinedExpression();
		}
		
		for( Expression e : children ) {
			if( e != null ) e.parent = this;
		}
	}
	
	/**
	 * Get the number of child expressions
	 */
	public int childCount() {
		return children.length;
	}
	
	/**
	 * Whether all the expressions are complete (there are no nulls or 
	 * UndefinedExpressions).
	 */
	public final boolean isComplete() {
		for( Expression e : children ) {
			if( e == null || e instanceof UndefinedExpression ) return false;
		}
		
		return true;
	}
	
    /**
     * Visit all the child expressions in evaluation order.
     */
    public final void visitChildren( ExpressionVisitor visitor ) {
    	for( Expression e : children ) {
    		if( e != null ) e.accept( visitor );
    	}
    }
    
    /**
     * Get a child expression.
     * 
     * @param index the expression index
     * @return may be null
     */
    public final Expression child( int index ) {
    	return children[index];
    }
    
    /**
     * Get the children from the given index
     */
    final Expression[] children( int start ) {
		Expression[] kids = new Expression[ children.length - start ];
		System.arraycopy( children, start, kids, 0, kids.length );		
		return kids;
    }
}
