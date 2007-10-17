/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;

/**
 * A long constant
 *
 * @author nickmain
 */
public final class ConstantLong extends Constant {  
	private final long value;

	/*pkg*/ ConstantLong( long value ) {
		super( PrimitiveType.LONG );
		this.value = value;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConstantLong( value );
	}
}