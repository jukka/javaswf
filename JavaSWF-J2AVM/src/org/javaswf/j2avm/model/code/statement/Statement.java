package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionContainer;

/**
 * Base for statements
 *
 * @author nickmain
 */
public abstract class Statement extends ExpressionContainer {

	/*pkg*/ StatementList list;
	/*pkg*/ Statement prev;
	/*pkg*/ Statement next;
	
	protected Statement( Expression...children ) {
		super( children );
	}
	
	/**
	 * Accept a visitor
	 */
	public abstract void accept( StatementVisitor visitor );	
}
