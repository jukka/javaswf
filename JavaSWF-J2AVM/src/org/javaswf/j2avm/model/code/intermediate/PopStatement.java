/**
 * 
 */
package org.javaswf.j2avm.model.code.intermediate;

import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;

/**
 * A temporary pop statement
 *
 * @author nickmain
 */
public final class PopStatement extends IntermediateStatement {
	
    final int popCount; 
    
    PopStatement( int popCount ) {
        this.popCount = popCount;
    }
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitExpression( ExpressionBuilder.constantString( "pop " + popCount ) );		
	}

}