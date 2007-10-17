/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A string constant
 *
 * @author nickmain
 */
public final class ConstantString extends Constant {  
	private final String value;

	/*pkg*/ ConstantString( String value ) {
		super( ObjectType.STRING );
		this.value = value;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConstantString( value );
	}
}