package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A conditional expression
 *
 * @author nickmain
 */
public class ConditionalExpression extends Expression {

	/*pkg*/ ConditionalExpression( Expression condition, Expression ifTrue, Expression ifFalse ) {
		super( condition, ifTrue, ifFalse );
	}
	
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConditional( child( 0 ), child( 1 ), child( 2 ) );
	}

	@Override
	public ValueType type() {
		return (ValueType) JavaType.common( child( 1 ).type(), child( 2 ).type() );
	}
}
