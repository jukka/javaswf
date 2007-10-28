/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A method return statement
 *
 * @author nickmain
 */
public final class ReturnStatement extends Statement {

	/**
	 * Void return
	 */
	ReturnStatement() {
	}
	
	/**
	 * Non-void return
	 */
	ReturnStatement( Expression expression ) {
		super( expression );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitReturn((expressions.childCount() == 1) ? child(0) : null );	
	}
}