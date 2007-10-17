package org.javaswf.j2avm.model.code.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A list of statements and associated exception handlers.
 *
 * @author nickmain
 */
public class StatementList {

	/*pkg*/ Statement first;
	private Statement last;
	private int count;
	private final List<TryCatch> handlers = new ArrayList<TryCatch>();
	private final Map<String, LabelStatement> labels = new HashMap<String, LabelStatement>();
	
	/**
	 * Get a cursor positioned before the first statement in the list
	 */
	public StatementCursor cursorAtStart() {
		return new StatementCursor( this, null );
	}

	/**
	 * Get a cursor positioned after the last statement in the list
	 */
	public StatementCursor cursorAtEnd() {
		return new StatementCursor( this, last );
	}

	/**
	 * Get the statement count
	 */
	public int count() {
		return count;
	}

	/** Insert a statement after another */
	/*pkg*/ void insert( Statement s, Statement after ) {
		if( s.list == this ) return; //already in this list
		s.list = this;
		s.prev = after;
		s.next = ( after != null ) ? after.next : null;
		
		if( s.prev != null ) after.next = s;
		else first = s;
		
		if( s.next != null ) s.next.prev = s;
		else last = s;
		
		count++;
		
		if( s instanceof LabelStatement ) {
			LabelStatement label = (LabelStatement) s;
			if( labels.containsKey( label.name ) ) throw new IllegalArgumentException( "duplicate label name " + label.name );
			labels.put( label.name, label );
		}
	}

	/** Remove a statement */
	/*pkg*/ void remove( Statement s ) {
		if( s.list != this ) return; //not in this list		
		s.list = null;
		
		if( s.prev != null ) s.prev.next = s.next;
		else first = s.next;
		
		if( s.next != null ) s.next.prev = s.prev;
		else last = s.prev;
		
		s.next = null;
		s.prev = null;
		count--;

		if( s instanceof LabelStatement ) {
			LabelStatement label = (LabelStatement) s;
			labels.remove( label.name );
		}
	}
	
	/** Get the label with the given name - throw up if not found */
	/*pkg*/ LabelStatement labelForName( String name ) {
		LabelStatement label = labels.get( name );
		
		if( label == null ) throw new IllegalArgumentException( "Label name not found: " + name );
		
		return label;
	}
}
