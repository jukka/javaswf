/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * A reference to an SSA value.
 *
 * @author nickmain
 */
public final class SSAValueExpression extends Expression {  
	private String    name; 
	private ValueType type; 

	/*pkg*/ SSAValueExpression( String name, ValueType type ) {
		this.name = name;
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitVariable( name, type );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {	
		return type;
	}
}