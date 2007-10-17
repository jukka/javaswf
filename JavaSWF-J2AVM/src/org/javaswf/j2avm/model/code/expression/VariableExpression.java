/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * A variable value
 *
 * @author nickmain
 */
public final class VariableExpression extends Expression {  
	private int index; 
	private ValueType type; 

	/*pkg*/ public VariableExpression( int index, ValueType type ) {
		this.index = index;
		this.type  = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitVariable( index, type );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {	
		return type;
	}
}