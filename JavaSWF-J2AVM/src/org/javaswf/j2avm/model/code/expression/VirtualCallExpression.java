/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.MethodDescriptor;

/**
 * A virtual method call
 *
 * @author nickmain
 */
public final class VirtualCallExpression extends MethodCallExpression {  
 
	/*pkg*/ VirtualCallExpression( MethodDescriptor method, Expression instance, Expression...args ) {
		super( method, instance, args );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitVirtualCall( method, child(0), children( 1 ) );
	}
}