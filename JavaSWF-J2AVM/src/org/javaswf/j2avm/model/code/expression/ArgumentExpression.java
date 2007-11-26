/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * An argument value
 *
 * @author nickmain
 */
public final class ArgumentExpression extends Constant {  

    private final int index;
    
	/*pkg*/ ArgumentExpression( ValueType type, int index ) {
	    super( type );
		this.index = index;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
 	public void accept( ExpressionVisitor visitor ) {
		visitor.visitArgument( index, type() );
	}
}