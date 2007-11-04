package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.statement.Statement;
import org.javaswf.j2avm.model.code.statement.Statements;

/**
 * A pseudo-statement used before statements are fully assembled.
 *
 * @author nickmain
 */
public abstract class IntermediateStatement extends Statement {

	IntermediateStatement( Expression... children ) {
		super( children );
	}

	/** Append to a statement list */
	/*pkg*/ final void append( Statements statements ) {
		insertVia( statements );
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#isComplete() */
	@Override
	protected boolean isComplete() {
		return false;  //an intermediate statement is never complete  
	}
}
