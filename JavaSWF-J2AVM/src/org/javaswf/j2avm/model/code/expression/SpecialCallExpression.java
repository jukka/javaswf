/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

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
		Expression[] args = new Expression[ children.length - 1 ];
		System.arraycopy( children, 1, args, 0, args.length );		
		visitor.visitSpecialCall( method, children[0], args );
	}
}