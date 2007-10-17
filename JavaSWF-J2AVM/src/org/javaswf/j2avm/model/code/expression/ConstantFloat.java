/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;

/**
 * A float constant
 *
 * @author nickmain
 */
public final class ConstantFloat extends Constant {  
	private final float value;

	/*pkg*/ ConstantFloat( float value ) {
		super( PrimitiveType.FLOAT );
		this.value = value;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConstantFloat( value );
	}
}