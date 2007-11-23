/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import java.util.Iterator;

import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A new array expression
 *
 * @author nickmain
 */
public final class NewArrayExpression extends Expression {
	private ArrayType type; 
	
	/*pkg*/ NewArrayExpression( ArrayType type, Expression...dimSizes ) {
		super( dimSizes );
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		Expression[] children = new Expression[ childCount() ];
		Iterator<Expression> cc = iterator();
		for( int i = 0; i < children.length; i++ ) {
			children[i] = cc.next();
		}
		visitor.visitNewArray( type, children );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return type;
	}
}