/**
 * 
 */
package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;

/**
 * A temporary pop statement
 *
 * @author nickmain
 */
public final class PopStatement extends IntermediateStatement {
	
    final int popCount; 
    
    private Expression prevEx1;
    
    PopStatement( int popCount ) {
        this.popCount = popCount;
    }
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitExpression( ExpressionBuilder.constantString( "pop " + popCount ) );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#construct(org.javaswf.j2avm.model.code.instruction.StatementConstructor) */
	@Override
	protected void construct( StatementConstructor constructor ) {
		consumePrevious( constructor );
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#consumeExpression(org.javaswf.j2avm.model.code.expression.Expression) */
	@Override
	protected void consumeExpression( Expression e ) {

		//got the 2nd expression - replace self
		if( prevEx1 != null ) {
			replace().expression( e )
			         .expression( prevEx1 );
			
			return;
		}
		
		if( popCount == 2 && ! e.type().is64Bit() ) {		
			prevEx1 = e;
			return;
		}
		
		replace().expression( e );
	}
}