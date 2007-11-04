package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A try-catch statement
 * 
 * @author nickmain
 */
public final class TryCatch extends Statement {

	/*pkg*/ final StatementBlock tryBlock   = new StatementBlock( this );
	/*pkg*/ final StatementBlock catchBlock = new StatementBlock( this );
	
	/** The type of exception to catch */
	public final ObjectType exceptionType;
	
    TryCatch( ObjectType exceptionType ) {
    	this.exceptionType = exceptionType;
    }

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitTryCatch( exceptionType, tryBlock, catchBlock );
	}
}
