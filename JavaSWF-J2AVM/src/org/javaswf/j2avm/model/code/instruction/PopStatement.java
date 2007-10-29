/**
 * 
 */
package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.statement.Statement;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;
import org.javaswf.j2avm.model.code.statement.Statements;

/**
 * A temporary pop statement
 *
 * @author nickmain
 */
public final class PopStatement extends Statement {
	
    final int popCount; 
    
    PopStatement( int popCount ) {
        super( (Expression) null );
        
        this.popCount = popCount;
    }
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitExpression( child(0) );		
	}
	
	/** Append to a statement list */
    /*pkg*/ final void append( Statements statements ) {
        insertVia( statements );
    }
}