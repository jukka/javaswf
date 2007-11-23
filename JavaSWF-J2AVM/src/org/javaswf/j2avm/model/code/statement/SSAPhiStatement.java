/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.SSAValueExpression;

/**
 * A Single Static Assignment "Phi" statement
 *
 * @author nickmain
 */
public final class SSAPhiStatement extends StaticSingleAssignmentStatement {
	
	private final SSAValueExpression[] values;
	
	/*pkg*/ SSAPhiStatement( SSAValueExpression... values ) {
		super( values ); 
		this.values = values;
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitPhi( name(), values );		
	}
}