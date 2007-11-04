/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import java.util.HashSet;
import java.util.Set;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.SSAValueExpression;

/**
 * The 
 *
 * @author nickmain
 */
public class StaticSingleAssignmentStatement extends Statement {
	
	//the references to this value
	private final Set<SSAValueExpression> references = new HashSet<SSAValueExpression>();
	
	/*pkg*/ StaticSingleAssignmentStatement( Expression expression ) {
		super( expression );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitS( varName, child(0) );		
	}
}