/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A primitive conversion operation
 *
 * @author nickmain
 */
public final class ConvertExpression extends Expression {  
	private PrimitiveType type;

	/*pkg*/ ConvertExpression( PrimitiveType type, Expression value ) {
		super( value );
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConvert( type, child(0) );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return type;
	}
}