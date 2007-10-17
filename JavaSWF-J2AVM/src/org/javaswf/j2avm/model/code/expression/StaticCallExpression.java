/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.MethodDescriptor;

/**
 * A static method call
 *
 * @author nickmain
 */
class StaticCallExpression extends MethodCallExpression {  
 
	/*pkg*/ StaticCallExpression( MethodDescriptor method, Expression...args ) {
		super( method, args );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitStaticCall( method, children );
	}
}