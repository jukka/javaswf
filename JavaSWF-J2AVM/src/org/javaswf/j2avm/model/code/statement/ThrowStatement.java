/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A throw statement
 *
 * @author nickmain
 */
public final class ThrowStatement extends Statement {
	
	ThrowStatement( Expression expression ) {
		super( expression ); 
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitThrow( children[0] );
	}
}