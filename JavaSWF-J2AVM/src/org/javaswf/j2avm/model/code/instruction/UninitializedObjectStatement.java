package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A pseudo-statement for holding an uninitialized new object (before the
 * constructor has been called).
 *
 * @author nickmain
 */
public final class UninitializedObjectStatement extends IntermediateStatement {

	final ObjectType type;
	
	UninitializedObjectStatement( ObjectType type ) {
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitExpression( ExpressionBuilder.variable( "UNINITIALIZED-<" + type + ">", type ) );		
	}
}
