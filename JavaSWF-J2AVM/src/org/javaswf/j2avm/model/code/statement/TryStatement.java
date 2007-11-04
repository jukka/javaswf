package org.javaswf.j2avm.model.code.statement;

import java.util.Set;

/**
 * The start of a try-block
 *
 * @author nickmain
 */
public class TryStatement extends Statement implements LabelTargetter {

	private final LabelStatement endLabel;
	private final CatchStatement catch_;
	
	/*pkg*/ public TryStatement( LabelStatement endLabel, CatchStatement catch_ ) {
		this.endLabel = endLabel;
		this.catch_   = catch_;

		endLabel.targetters.add( this );
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitTry( endLabel, catch_ );
		
	}

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#release() */
	public void release() {
		endLabel.targetters.remove( this );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#targets(java.util.Set) */
	public void targets( Set<LabelStatement> labels ) {
		labels.add( endLabel );
	}
}
