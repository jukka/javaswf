package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionContainer;

/**
 * Base for statements
 *
 * @author nickmain
 */
public abstract class Statement {

	/*pkg*/ StatementList list;
	/*pkg*/ Statement prev;
	/*pkg*/ Statement next;
	
	/**
	 * The child expressions (if any)
	 */	
	protected final ExpressionContainer expressions;
	
	protected Statement( Expression...children ) {
		expressions = new ExpressionContainer( children );
	}
	
	/** Get a given child */
	protected final Expression child( int index ) {
		return expressions.child( index );
	}
    
	/**
	 * Get the expression container for child expressions
	 */
	public ExpressionContainer children() {
	    return expressions;
	}
	
    /**
     * Move this statement to after another Statement
     * 
     * @param after null to move to the start of the list
     */
    public final void moveAfter( Statement after ) {
    	list.move( this, after );
    }
    
    /**
     * Move this statement to before another Statement
     * 
     * @param before null to move to the end of the list
     */
    public final void moveBefore( Statement before ) {
    	if( before == null ) {
    		list.move( this, list.last );	
    	}
    	else {
    		list.move( this, before.prev );
    	}
    }
    
    /**
     * Remove this statement
     */
    public final void remove() {
    	list.remove( this );
    }
    
    /**
     * Get the previous statement
     */
    public final Statement previous() {
        return prev;
    }

    /**
     * Get the next statement
     */
    public final Statement next() {
        return next;
    }
    
    /**
     * Remove this statement and get a factory for inserting new statements
     * in its place.
     * 
     * @return the factory
     */
    public final Statements replace() {
    	StatementCursor cursor = new StatementCursor( list, prev );
    	remove();
    	return cursor.insert();
    }
    
	/**
	 * Accept a visitor
	 */
	public abstract void accept( StatementVisitor visitor );
    
    /**
     * INTERNAL USE ONLY.
     * Add this item to a list via the given factory.
     */
    protected final void insertVia( Statements statements ) {   	
    	statements.insert_( this );
    }
}
