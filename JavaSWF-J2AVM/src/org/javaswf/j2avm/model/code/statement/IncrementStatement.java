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
	private final int varIndex;
	
	IncrementStatement( Expression expression, int varIndex ) {
		super( expression );
		this.varIndex  = varIndex;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitIncrement( varIndex, children[0] );
	}
}