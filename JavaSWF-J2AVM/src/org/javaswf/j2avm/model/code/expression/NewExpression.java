/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A new object expression
 *
 * @author nickmain
 */
public final class NewExpression extends Expression {  
	private ObjectType type; 
	private ValueType[] paramTypes; 
	
	/*pkg*/ NewExpression( ObjectType type, ValueType[] paramTypes, Expression...args ) {
		super( args );
		this.type = type;
		this.paramTypes = paramTypes;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitNew( type, paramTypes, children );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return type;
	}
}