/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * An array initializer
 *
 * @author nickmain
 */
public final class ArrayInitializerExpression extends Expression {
	
	/*pkg*/ ArrayInitializerExpression( Expression array, Expression...dimSizes ) {
		super( merge( array, dimSizes ) );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		Expression[] vals = new Expression[ children.length - 1 ];
		System.arraycopy( children, 1, vals, 0, vals.length );	
		
		visitor.visitArrayInitializer( children[0], vals );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return children[0].type();
	}
}