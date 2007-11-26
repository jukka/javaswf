/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * The "this" value
 *
 * @author nickmain
 */
public final class ThisExpression extends Constant {  

	/*pkg*/ ThisExpression( ObjectType type ) {
        super( type );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
 	public void accept( ExpressionVisitor visitor ) {
		visitor.visitThis( (ObjectType) type() );
	}
}