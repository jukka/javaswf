/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import java.util.Iterator;

import org.javaswf.j2avm.model.MethodDescriptor;

/**
 * A super or private method call
 *
 * @author nickmain
 */
public final class SpecialCallExpression extends MethodCallExpression {  
 
	/*pkg*/ SpecialCallExpression( MethodDescriptor method, Expression instance, Expression...args ) {
		super( method, instance, args );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		Expression[] args = new Expression[ childCount() - 1 ];
		Iterator<Expression> children = iterator();
		for( int i = 0; i < args.length; i++ ) {
			args[i] = children.next();
		}

		visitor.visitSpecialCall( method, child(0), args );
	}
}