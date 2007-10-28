/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A field assignment statement
 *
 * @author nickmain
 */
public final class FieldAssignmentStatement extends Statement {
	
	private FieldDescriptor field;
	
	/*pkg*/ FieldAssignmentStatement( FieldDescriptor field, Expression instance, Expression value ) {
		super( instance, value );
		this.field = field;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitFieldAssignment( field, child(0), child(1) );		
	}
}