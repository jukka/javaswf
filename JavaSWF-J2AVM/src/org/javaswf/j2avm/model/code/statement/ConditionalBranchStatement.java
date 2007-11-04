package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A conditional branch statement
 *
 * @author nickmain
 */
public final class ConditionalBranchStatement extends BranchStatement {

	ConditionalBranchStatement( Object targetName, Expression expression ) {
		super( targetName, expression );
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitConditionalBranch( target, child( 0 ) );
	}
	
	/**
	 * Get the condition
	 */
	/*pkg*/ Expression getCondition() {
		return child( 0 );
	}
	
}
