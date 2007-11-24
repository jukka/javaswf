package org.javaswf.j2avm.model.code.statement;

import java.util.Set;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * Base for branch statements
 *
 * @author nickmain
 */
public abstract class BranchStatement extends Statement implements LabelTargetter {

	protected final Object targetName;
	protected LabelStatement target;
	
	BranchStatement( Object targetName ) {
		this.targetName = targetName;		
	}

	BranchStatement( Object targetName, Expression expression ) {
		super( expression );
		this.targetName = targetName;		
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#addedToList() */
	@Override
	protected void addedToList() {
		target = list.labelForName( targetName );
		target.targetters.add( this );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#removingFromList() */
	@Override
	protected void removingFromList() {
		target.targetters.remove( this );
		target = null;		
	}

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#targets(java.util.Set) */
	public void targets( Set<LabelStatement> labels ) {
		labels.add( target );		
	}	
}
