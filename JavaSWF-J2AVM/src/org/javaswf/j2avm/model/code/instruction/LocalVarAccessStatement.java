/**
 * 
 */
package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;
import org.javaswf.j2avm.model.code.statement.Statements;
import org.javaswf.j2avm.model.code.statement.StaticSingleAssignmentStatement;

/**
 * A temporary statement that pushes a local var value on the stack
 *
 * @author nickmain
 */
public final class LocalVarAccessStatement extends StaticSingleAssignmentStatement {
	
    private final int varIndex; 
    
    LocalVarAccessStatement( int varIndex ) {
    	super( ExpressionBuilder.constantString( "<local-" + varIndex + ">" ) );
        this.varIndex = varIndex;
    }
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitSSAValue( "<local-" + varIndex + ">", child( 0 ), references );		
	}
	
	/** Append to a statement list */
	/*pkg*/ final void append( Statements statements ) {
		insertVia( statements );
	}
}