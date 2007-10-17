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
	
	ReturnStatement( Expression expression ) {
		super( (expression == null) ?
				new Expression[0] :
				new Expression[] { expression } );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitReturn((children.length == 1) ? children[0] : null );	
	}
}