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
	 * Get the previous statement.
	 * 
	 * @return null if the cursor is at the start of the list or the list is 
	 *         empty
	 */
	public Statement prev() {
		return prev;
	}
	
	/**
	 * Jump to just after the given label
	 * @param label a LabelStatement or label name
	 */
	public void jumpTo( Object label ) {
		if( label instanceof LabelStatement ) {
			prev = (LabelStatement) label;
			return;
		}
		
		prev = list.labelForName( label );		
	}
	
	/**
	 * Get the next statement and advance beyond it
	 * @return null if there are no more statements
	 */
	public Statement next() {
        Statement next = getNext();
        
        if( next == null ) return null;
        
        prev = next;
        return next;
	}
	
	//get the next statement, do not advance
	private Statement getNext() {
        Statement next = null;
        
        if( prev == null ) {
            next = list.first;
        }
        else {
            next = prev.next;
        }
        
        return next;	    
	}
	
	/**
	 * Visit the next Statement and position the cursor after it.
	 * 
	 * @param visitor the visitor to be accepted by the next statement
	 * @return true if the visit took place, false if there is no next statement
	 */
	public boolean visitNext( StatementVisitor visitor ) {
		Statement next = next();
		
		if( next == null ) return false;
		
		next.accept( visitor );
		prev = next;
		return true;
	}
	
	/**
	 * Get a factory for inserting code at the current position.  The position
	 * of this cursor is advanced each time a statement is inserted. 
	 */
	public Statements insert() {
		return new Statements() {
			@Override protected void insert( Statement s ) {
				StatementCursor.this.insert( s  );		
			}
			@Override protected LabelStatement labelForName( Object name ) {
				return list.label( name );
			}		
		};
	}
	
	/*pkg*/ void insert( Statement s ) {
		list.insert( s, prev );
		prev = s;						
	}
}
