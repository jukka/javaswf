/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;

/**
 * A double constant
 *
 * @author nickmain
 */
public final class ConstantDouble extends Constant {  
	private final double value;

	/*pkg*/ ConstantDouble( double value ) {
		super( PrimitiveType.DOUBLE );
		this.value = value;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConstantDouble( value );
	}
}