/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A caught exception
 *
 * @author nickmain
 */
public final class CaughtExceptionExpression extends Expression {  
 
	private ObjectType type; 

	/*pkg*/ CaughtExceptionExpression( ObjectType type ) {
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitException( type );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {	
		return type;
	}
}