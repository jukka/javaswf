/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.code.statement.StaticSingleAssignmentStatement;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A reference to an SSA value.
 *
 * @author nickmain
 */
public final class SSAValueExpression extends Expression {  
	private final StaticSingleAssignmentStatement value; 

	/*pkg*/ SSAValueExpression( StaticSingleAssignmentStatement value ) {
		this.value = value;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitSSAValue( value );
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {	
		return value.expression().type();
	}
	
	/** @see org.javaswf.j2avm.model.code.expression.ExpressionContainer#addedToList() */
	@Override
	protected void addedToList() {
		value.addReference( this );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionContainer#removingFromList() */
	@Override
	protected void removingFromList() {
		value.dropReference( this );
	}
} 