/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;

/**
 * An integer constant
 *
 * @author nickmain
 */
public final class ConstantInt extends Constant {  
	private final int value;

	/*pkg*/ ConstantInt( int value ) {
		super( PrimitiveType.INT );
		this.value = value;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConstantInt( value );
	}
}