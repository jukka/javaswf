/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * An array element assignment statement
 *
 * @author nickmain
 */
public final class ElementAssignmentStatement extends Statement {
	
	/*pkg*/ ElementAssignmentStatement( Expression array, Expression index, Expression value ) {
		super( array, index, value );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitElementAssignment( child(0), child(1), child(2) );		
	}
}