package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A pseudo-statement representing a stack altering operation.
 *
 * @author nickmain
 */
public final class StackOperationStatement extends IntermediateStatement {

	final int popCount; //0-2
	final int dupCount; //0-2
	final int skipCount; //0-2
	
	StackOperationStatement( int popCount, int dupCount, int skipCount ) {
		this.skipCount = skipCount;
		this.popCount  = popCount;
		this.dupCount  = dupCount;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		String description = "swap";
		if     ( popCount > 0 ) description = "pop " + popCount;
		else if( dupCount > 0 ) description = "dup " + dupCount + " skip " + skipCount;
		
		visitor.visitExpression( ExpressionBuilder.variable( description, ObjectType.OBJECT ) );		
	}
}
