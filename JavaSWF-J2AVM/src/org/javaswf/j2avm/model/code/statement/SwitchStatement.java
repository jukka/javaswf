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
	
	SwitchStatement( Expression value, Object defaultTarget, SwitchCase...cases ) {
		super( defaultTarget, value );		

		for( SwitchCase c : cases ) {
			this.cases.add( c );
		}
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.BranchStatement#addedToList() */
	@Override
	protected void addedToList() {
		super.addedToList();

		for( SwitchCase c : cases ) {
			LabelStatement label = list.labelForName( c.targetName );
			label.targetters.add( this );
			c.label = label;
		}
	}

	/** @see org.javaswf.j2avm.model.code.statement.BranchStatement#removingFromList() */
	@Override
	protected void removingFromList() {
		super.removingFromList();

		for( SwitchCase c : cases ) {
			c.label.targetters.remove( this );
			c.label = null;
		}	
	}

	/** @see org.javaswf.j2avm.model.code.statement.BranchStatement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitSwitch( child(0), target, cases );
	}


	/** @see org.javaswf.j2avm.model.code.statement.BranchStatement#targets(java.util.Set) */
	@Override
	public void targets( Set<LabelStatement> labels ) {
		super.targets( labels );
		
		for( SwitchCase c : cases ) {			
			labels.add( c.label );
		}
	}
}
