package org.javaswf.j2avm.model.code.statement;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A switch statement
 *
 * @author nickmain
 */
public final class SwitchStatement extends BranchStatement {

	private final SortedSet<SwitchCase> cases = new TreeSet<SwitchCase>();
	
	SwitchStatement( Expression value, LabelStatement defaultTarget, SwitchCase...cases ) {
		super( defaultTarget, value );
		
		for( SwitchCase c : cases ) {
			this.cases.add( c );
			c.target.targetters.add( this );
		}
	}

	/** @see org.javaswf.j2avm.model.code.statement.BranchStatement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitSwitch( children[0], target, cases );
	}

	/** @see org.javaswf.j2avm.model.code.statement.BranchStatement#release() */
	@Override
	public void release() {
		super.release();
		
		for( SwitchCase c : cases ) {			
			c.target.targetters.remove( this );
		}
	}

	/** @see org.javaswf.j2avm.model.code.statement.BranchStatement#targets(java.util.Set) */
	@Override
	public void targets( Set<LabelStatement> labels ) {
		super.targets( labels );
		
		for( SwitchCase c : cases ) {			
			labels.add( c.target );
		}
	}
}
