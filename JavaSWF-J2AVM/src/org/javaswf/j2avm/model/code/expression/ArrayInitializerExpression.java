/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import java.util.Iterator;

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
		Expression[] vals = new Expression[ childCount() - 1 ];
		Iterator<Expression> children = iterator();
		for( int i = 0; i < vals.length; i++ ) {
			vals[i] = children.next();
		}
		
		visitor.visitArrayInitializer( child(0), vals );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return child(0).type();
	}
}