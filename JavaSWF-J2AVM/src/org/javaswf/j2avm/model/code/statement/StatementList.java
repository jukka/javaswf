package org.javaswf.j2avm.model.code.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A list of statements and associated exception handlers.
 *
 * @author nickmain
 */
public final class StatementList {

	private final List<TryCatch> handlers = new ArrayList<TryCatch>();
	private final Map<String, LabelStatement> labels = new HashMap<String, LabelStatement>();
	
	protected Statement first;
	protected Statement last;
	private int count;
	
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
	public Iterator<LabelStatement> labels() {
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
		s.next = ( after != null ) ? after.next : null;
		
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
		
		statementAdded( s );
	}

	/** Remove a statement */
	final void remove( Statement s ) {
		if( s.list != this ) return; //not in this list
		
		s.list = null;
		pluck( s );
		count--;
		
		statementRemoved( s );
	}

	/**
	 * Get the label with the given name, or create and add Statement to the end.
	 * 
	 * @param name the label name
	 */
	public LabelStatement label( Object name ) {
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
	public LabelStatement label( Statement s, Object name ) {
		
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
	 * Whether there are any exception handlers
	 */
	public boolean hasExceptionHandlers() {
		return ! handlers.isEmpty();
	}
	
	/**
	 * Get an iterator over the exception handlers
	 */
	public Iterator<TryCatch> handlers() {
		return handlers.iterator();
	}
	
	/**
	 * Add a new exception handler
	 * 
	 * @param start the start of the try-block
	 * @param end the end of the try-block
	 * @param exceptionType the exception type to catch
	 * @param handler the start of the handler code
	 */
	public void addHandler( LabelStatement start, LabelStatement end,
			                ObjectType exceptionType,
			                LabelStatement handler ) {
		handlers.add( new TryCatch( start, end, handler, exceptionType ));
	}
	
	private void statementAdded( Statement s ) {
		if( s instanceof LabelStatement ) {
			LabelStatement label = (LabelStatement) s;
			String name = label.toString();
			if( labels.containsKey( name ) ) throw new IllegalArgumentException( "duplicate label name " + label.name );
			labels.put( name, label );
		}
	}
	
	private void statementRemoved( Statement s ) {
		if( s instanceof LabelStatement ) {
			LabelStatement label = (LabelStatement) s;
			labels.remove( label.toString() );
		}
	}
	
	/** Get the label with the given name - throw up if not found */
	/*pkg*/ LabelStatement labelForName( Object name ) {
		LabelStatement label = labels.get( name.toString() );
		
		if( label == null ) throw new IllegalArgumentException( "Label name not found: " + name );
		
		return label;
	}
}
