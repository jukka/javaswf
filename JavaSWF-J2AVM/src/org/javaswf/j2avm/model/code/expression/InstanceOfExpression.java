/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * An instanceof operation
 *
 * @author nickmain
 */
public final class InstanceOfExpression extends Expression {  
	private ObjectOrArrayType type;

	/*pkg*/ InstanceOfExpression( ObjectOrArrayType type, Expression object ) {
		super( object );
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitInstanceOf( type, child(0) );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return PrimitiveType.BOOLEAN;
	}
}