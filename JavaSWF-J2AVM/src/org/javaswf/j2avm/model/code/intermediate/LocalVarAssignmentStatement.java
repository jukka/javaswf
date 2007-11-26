/**
 * 
 */
package org.javaswf.j2avm.model.code.intermediate;

import java.util.Collections;
import java.util.Set;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.SSAValueExpression;
import org.javaswf.j2avm.model.code.statement.StatementVisitor;

/**
 * A temporary local var assignment
 *
 * @author nickmain
 */
public final class LocalVarAssignmentStatement extends IntermediateStatement {
	
    private final int varIndex; 
    
    LocalVarAssignmentStatement( int varIndex, Expression value ) {
        super( value );
        this.varIndex = varIndex;
    }
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
	    Set<SSAValueExpression> references = Collections.emptySet();
		visitor.visitSSAValue( "<local-" + varIndex + ">", child( 0 ), references );		
	}
}