package org.javaswf.j2avm.model.code.statement;


/**
 * An unconditional branch statement
 *
 * @author nickmain
 */
public final class UnconditionalBranchStatement extends BranchStatement {

	UnconditionalBranchStatement( Object targetName ) {
		super( targetName );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitUnconditionalBranch( target );		
	}
}
