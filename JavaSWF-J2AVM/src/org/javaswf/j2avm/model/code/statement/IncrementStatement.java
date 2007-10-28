/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * An increment statement
 *
 * @author nickmain
 */
public final class IncrementStatement extends Statement {
	private final String varName;
	
	IncrementStatement( Expression expression, String varName ) {
		super( expression );
		this.varName  = varName;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitIncrement( varName, child(0) );
	}
}