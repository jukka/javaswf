/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A null constant
 *
 * @author nickmain
 */
public final class ConstantNull extends Constant {  

	/*pkg*/ ConstantNull() {
		super( ObjectType.OBJECT );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
 	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConstantNull();
	}
}