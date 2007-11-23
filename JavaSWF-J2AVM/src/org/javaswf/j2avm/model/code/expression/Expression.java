package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * Base for expressions
 *
 * @author nickmain
 */
public abstract class Expression extends ExpressionContainer {

	protected Expression( Expression...children ) {
		super( children );
	}
		
    /**
     * Get the type of this expression
     * 
     * @return null if this is a void method call
     */
    public abstract ValueType type();
    
    /**
     * Accept a visitor.
     */
    public abstract void accept( ExpressionVisitor visitor );
    	
	static Expression[] merge( Expression e, Expression[] ee ) {
		Expression[] merged = new Expression[ ee.length + 1 ];
		merged[0] = e;
		System.arraycopy( ee, 0, merged, 1, ee.length );
		return merged;
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionContainer#addedToList() */
	@Override
	protected void addedToList() {
		//empty
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionContainer#removingFromList() */
	@Override
	protected void removingFromList() {
		//empty		
	}	
}

