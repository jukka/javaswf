package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * Base for code items that are constructed from instructions.
 *
 * @author nickmain
 */
public abstract class ConstructedCode {

	/**
	 * Construct this statement.
	 */
	protected abstract void construct( StatementConstructor constructor );

	/**
	 * Whether this item is complete
	 */
	protected abstract boolean isComplete(); 
	
	/**
	 * Add an item to the processing agenda of the given constructor
	 */
	protected final void addToAgenda( StatementConstructor constructor, ConstructedCode cc ) {
		constructor.addToAgenda( cc );
	}
}
