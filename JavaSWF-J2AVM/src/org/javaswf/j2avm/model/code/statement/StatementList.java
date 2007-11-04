package org.javaswf.j2avm.model.code.statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A list of statements
 *
 * @author nickmain
 */
public final class StatementList {
	
	protected Statement first;
	protected Statement last;
	private int count;
	
	private final Map<String, StaticValue> values = new HashMap<String, StaticValue>();
	/*pkg*/ final Map<String, LabelStatement> labels = new HashMap<String, LabelStatement>();
		
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
	 * Get an iterator over all the labels in the list
	 */
	public final Iterator<LabelStatement> labels() {
		return labels.values().iterator();
	}
	
	/**
	 * Move a statement.
	 * @param s the statement to be moved
	 * @param after the statement to place s after
	 */
	final void move( Statement s, Statement after ) {
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
		
		s.addedToList();
	}

	/** Remove a statement */
	final void remove( Statement s ) {
		if( s.list != this ) return; //not in this list
		
		s.removingFromList();
		
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
	
	/**
	 * Add an exception handler
	 * 
	 * @param tryStart label before start of covered statements
	 * @param tryEnd label after covered statements
	 * @param exType the type of exception to catch
	 * @param handler the start of the handler code
	 * @return the new try-catch statement
	 */
	public final TryCatch addExceptionHandler( Object tryStart, Object tryEnd,
			                                   ObjectType exType, Object handler ) {
		
		TryCatch tryCatch = new TryCatch( exType );
		StatementBlock tryBlock = tryCatch.tryBlock;
		
		LabelStatement start = labelForName( tryStart );
		LabelStatement end   = labelForName( tryEnd );
		LabelStatement hand  = labelForName( handler );		
		
		Statement next ;		
		for( Statement s = start.next; s != end; s = next ) {
			next = s.next;
			remove( s );
			tryBlock.insert( s, tryBlock.last );
		}
		
		insert( tryCatch, start );
		
		//FIXME: 
		
		return tryCatch;
	}

	
	/** Get the label with the given name - throw up if not found */
	/*pkg*/ final LabelStatement labelForName( Object name ) {
		LabelStatement label = labels.get( name.toString() );
		
		if( label == null ) throw new IllegalArgumentException( "Label name not found: " + name );
		
		return label;
	}
}
