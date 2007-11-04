package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionContainer;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;

/**
 * A pseudo-statement for holding unassembled expressions.
 *
 * @author nickmain
 */
public final class UnassembledExpressionStatement extends IntermediateStatement {

	UnassembledExpressionStatement( Expression expression ) {
		super( expression );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitExpression( child( 0 ) );		
	}

	/**
	 * Get the wrapped expression
	 */
	public final Expression wrappedExpression() {
	    return child( 0 );
	}
	
	/**
	 * Overridden to get the wrapped expression
	 * 
	 * @see org.javaswf.j2avm.model.code.statement.Statement#children()
	 */
    @Override
    protected final ExpressionContainer children() {
        return child( 0 );
    }
}
