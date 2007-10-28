/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A static field read
 *
 * @author nickmain
 */
public final class StaticFieldExpression extends Expression { 
	private FieldDescriptor field;
	
	/*pkg*/ StaticFieldExpression( FieldDescriptor field ) {
		this.field = field;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitStaticField( field );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {
		return field.type;
	}
}