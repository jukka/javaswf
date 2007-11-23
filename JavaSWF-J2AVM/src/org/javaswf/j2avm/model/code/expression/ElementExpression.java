/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * An array element
 *
 * @author nickmain
 */
public final class ElementExpression extends Expression { 

	/*pkg*/ ElementExpression( Expression array, Expression index ) {
		super( array, index );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitElement( child(0), child(1) );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		ValueType aType = child(0).type();
		if( !( aType instanceof ArrayType ) ) {
			throw new IllegalStateException( "Array expression is not an array type" );
		}
		
		return ((ArrayType) aType).firstDimType();
	}	
}