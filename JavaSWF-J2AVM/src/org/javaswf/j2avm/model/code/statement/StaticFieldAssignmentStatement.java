/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A static field assignment statement
 *
 * @author nickmain
 */
public final class StaticFieldAssignmentStatement extends Statement {
	
	private FieldDescriptor field;
	
	/*pkg*/ StaticFieldAssignmentStatement( FieldDescriptor field, Expression value ) {
		super( value );
		this.field = field;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitStaticFieldAssignment( field, child(0) );		
	}
}