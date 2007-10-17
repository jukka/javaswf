/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A binary operation
 *
 * @author nickmain
 */
public final class BinaryOpExpression extends Expression {  
	private BinaryOperation op; 
	
	/*pkg*/ BinaryOpExpression( BinaryOperation op, Expression left, Expression right ) {
		super( left, right );
		this.op = op;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitBinaryOp( op, children[0], children[1] );
		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		ValueType leftType = children[0].type();
		if( leftType instanceof PrimitiveType ) {
			return op.type( (PrimitiveType) leftType ) ;
		}
		
		throw new IllegalStateException( "Binary op left operand is not primitive" );
	}
}