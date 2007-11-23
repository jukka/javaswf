/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A condition expression
 *
 * @author nickmain
 */
public final class ConditionExpression extends Expression {
	private Condition condition; 
	
	/*pkg*/ ConditionExpression( Condition condition, Expression left, Expression right ) {
		super( left, right );
		this.condition = condition;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitCondition( condition, child(0), child(1));		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return PrimitiveType.BOOLEAN;
	}
}