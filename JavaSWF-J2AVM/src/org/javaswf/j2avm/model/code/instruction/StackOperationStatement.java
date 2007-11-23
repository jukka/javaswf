package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;

/**
 * A pseudo-statement representing a stack altering operation.
 *
 * @author nickmain
 */
public final class StackOperationStatement extends IntermediateStatement {

	final int dupCount; //0-2
	final int skipCount; //0-2
	
	StackOperationStatement( int dupCount, int skipCount ) {
		this.skipCount = skipCount;
		this.dupCount  = dupCount;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		String description = "swap";
		if( dupCount > 0 ) description = "dup " + dupCount + " skip " + skipCount;
		
		visitor.visitExpression( ExpressionBuilder.constantString( description ) );		
	}
}
