package org.javaswf.j2avm.model.code.statement;

import java.util.HashMap;
import java.util.Map;

/**
 * A list of statements
 *
 * @author nickmain
 */
public final class StatementList {
	
	protected Statement first;
	protected Statement last;
	private int count;
	
	/*pkg*/ int ssaValueIndex = 1;
	
	/*pkg*/ final Map<String, LabelStatement> labels = new HashMap<String, LabelStatement>();
	/*pkg*/ final Map<String, StaticSingleAssignmentStatement> ssaValues = 
		new HashMap<String, StaticSingleAssignmentStatement>();
			
	/**
	 * Get a cursor positioned before the first statement in the list
	 */
	public final StatementCursor cursorAtStart() {
		return new StatementCursor( this, null );
	}

	/**
	 * Get a cursor positioned after the last statement in the list
	 */
	public final StatementCursor cursorAtEnd() {
		return new StatementCursor( this, last );
	}

	/**
	 * Get the statement count
	 */
	public final int count() {
		return count;
	}
	
	/**
	 * Get an iterable over all the labels in the list
	 */
	public final Iterable<LabelStatement> labels() {
		return labels.values();
	}
	
	/**
	 * Move a statement.
	 * @param s the statement to be moved
	 * @param after the statement to place s after
	 */
	final void move( Statement s, Statement after ) {
	    if( s.prev == after || s == after ) return;
	    
		pluck( s );
		place( s, after );
	}
	
	/** Pluck a statement out of the list */
	private void pluck( Statement s ) {
		if( s.prev != null ) s.prev.next = s.next;
		else first = s.next;
		
		if( s.next != null ) s.next.prev = s.prev;
		else last = s.prev;
		
		s.next = null;
		s.prev = null;
	}
	
	/** Place a statement in the list */
	private void place( Statement s, Statement after ) {
		s.prev = after;
		s.next = ( after != null ) ? after.next : first;
		
		if( s.prev != null ) after.next = s;
		else first = s;
		
		if( s.next != null ) s.next.prev = s;
		else last = s;	
	}
	
	/** Insert a statement after another */
	final void insert( Statement s, Statement after ) {
		if( s.list == this ) return; //already in this list
		s.list = this;
		place( s, after );
		count++;
		
		s.addToListed_internal();
	}

	/** Remove a statement */
	final void remove( Statement s ) {
		if( s.list != this ) return; //not in this list
		
		s.removedFromList_internal();
		
		s.list = null;
		pluck( s );
		count--;
	}

	/**
	 * Get the label with the given name, or create and add Statement to the end.
	 * 
	 * @param name the label name
	 */
	public final LabelStatement label( Object name ) {
		LabelStatement label = labels.get( name.toString() );
		
		if( label == null ) {
			label = new LabelStatement( name );
			insert( label, last );
		}
		
		return label;
	}
	
	/**
	 * Label the given statement with the given name.  If the label exists
	 * then move it to just before the statement.
	 * 
	 * @param name the label name
	 * @return the new or existing label
	 */
	public final LabelStatement label( Statement s, Object name ) {
		
	    LabelStatement label = labels.get( name.toString() );
		
		if( label == null ) {
			label = new LabelStatement( name );
			insert( label, s.prev );
		}
		else {
			move( label, s.prev );
		}

		return label;
	}
	
	/** Get the SSA value with the given name - throw up if not found */
	/*pkg*/ final StaticSingleAssignmentStatement valueForName( Object name ) {
		StaticSingleAssignmentStatement value = ssaValues.get( name.toString() );
		
		if( value == null ) throw new IllegalArgumentException( "Value name not found: " + name );
		
		return value;
	}
	
	/** Get the label with the given name - throw up if not found */
	/*pkg*/ final LabelStatement labelForName( Object name ) {
		LabelStatement label = labels.get( name.toString() );
		
		if( label == null ) throw new IllegalArgumentException( "Label name not found: " + name );
		
		return label;
	}
}
