/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * An expression statement
 *
 * @author nickmain
 */
public final class ExpressionStatement extends Statement {
	
	ExpressionStatement( Expression expression ) {
		super( expression ); 
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitExpression( child(0) );		
	}
}