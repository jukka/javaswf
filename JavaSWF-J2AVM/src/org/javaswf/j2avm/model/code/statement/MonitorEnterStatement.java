/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * A monitor-enter statement
 *
 * @author nickmain
 */
public final class MonitorEnterStatement extends Statement {
	
	MonitorEnterStatement( Expression expression ) {
		super( expression );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitMonitorEnter( children[0] );
	}
}