/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A cast operation
 *
 * @author nickmain
 */
public final class CastExpression extends Expression { 
	private ObjectOrArrayType type; 
	
	/*pkg*/ CastExpression( ObjectOrArrayType type, Expression object ) {
		super( object );
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitCast( type, children[0] );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return type;
	}
}