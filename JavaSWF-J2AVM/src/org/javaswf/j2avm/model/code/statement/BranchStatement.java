package org.javaswf.j2avm.model.code.statement;

import java.util.Set;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A branch statement
 *
 * @author nickmain
 */
public class BranchStatement extends Statement implements LabelTargetter {

	protected final LabelStatement target;
	
	BranchStatement( LabelStatement target, Expression expression ) {
		super( (expression == null) ? 
				   new Expression[0] : 
				   new Expression[] { expression });
		
		this.target = target;
		
		target.targetters.add( this );
	}
	
	BranchStatement( LabelStatement target ) {
		this( target, null );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		if( children.length == 1 ) visitor.visitBranch( target, children[0] );
		else                       visitor.visitBranch( target );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#release() */
	public void release() {
		target.targetters.remove( this );
	}

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#targets(java.util.Set) */
	public void targets( Set<LabelStatement> labels ) {
		labels.add( target );		
	}	
}
