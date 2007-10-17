package org.javaswf.j2avm.model.code.statement;

/**
 * A cursor for navigating StatementLists.  The position of a cursor is defined
 * by being after a particular Statement.  If statements are inserted after the
 * statement (via a different cursor) then the cursor will remain in the same
 * position.
 *
 * @author nickmain
 */
public final class StatementCursor {

	/*pkg*/ final StatementList list;
	private Statement prev;
	
	/*pkg*/ StatementCursor( StatementList list, Statement prev ) {
		this.list = list;
		this.prev = prev;
	}
	
	/**
	 * Visit the next Statement and position the cursor after it.
	 * 
	 * @param visitor the visitor to be accepted by the next statement
	 * @return true if the visit took place, false if there is no next statement
	 */
	public boolean visitNext( StatementVisitor visitor ) {
		return false; //FIXME
	}
	
	/**
	 * Get a factory for inserting code at the current position.  The position
	 * of this cursor is advanced each time a statement is inserted. 
	 */
	public Statements insert() {
		return new Statements( this );
	}
	
	/*pkg*/ void insert( Statement s ) {
		//FIXME
	}
}
