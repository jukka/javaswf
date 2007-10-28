/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A variable assignment statement
 *
 * @author nickmain
 */
public final class VariableAssignmentStatement extends Statement {
	
	private String varName;
	
	/*pkg*/ VariableAssignmentStatement( String varName, Expression expression ) {
		super( expression );
		this.varName = varName;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitVariableAssignment( varName, child(0) );		
	}
}