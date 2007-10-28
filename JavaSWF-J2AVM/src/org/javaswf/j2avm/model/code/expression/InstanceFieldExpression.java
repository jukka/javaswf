/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * An instance field read
*
* @author nickmain
*/
public final class InstanceFieldExpression extends Expression { 
	private FieldDescriptor field;
	
	/*pkg*/ InstanceFieldExpression( FieldDescriptor field, Expression instance ) {
		super( instance );
		this.field = field;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitInstanceField( field, children[0] );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return field.type;
	}
}