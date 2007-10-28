/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ValueType;
import org.javaswf.j2avm.model.types.VoidType;

/**
 * A conditional expression
 *
 * @author nickmain
 */
public final class ConditionalExpression extends Expression {  
	
	/*pkg*/ ConditionalExpression( Expression condition, Expression ifTrue, Expression ifFalse ) {
		super( condition, ifTrue, ifFalse );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConditional( children[0], children[1], children[2] );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {	
		JavaType common = JavaType.common( children[1].type(), children[2].type() );
		
		if( common == null || common == VoidType.VOID ) {
			throw new IllegalStateException( "Alternatives in a conditional expression are not type-compatible" );
		}
		
		return (ValueType) common;
	}	
}