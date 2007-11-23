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
     * Remove this statement
     */
    public final void remove() {
    	list.remove( this );
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
	
    /*pkg*/ final void addToListed_internal() {
    	this.statement = this;
    	super.addToList();
    }

    /*pkg*/ final void removedFromList_internal() {
    	super.removeFromList();
    }
    
	/**
	 * Called when this statement has been added to a list
	 */
    @Override
	protected void addedToList() {}
	
	/**
	 * Called when this statement is being removed from a list (before the
	 * list reference and prev/next have been nulled)
	 */
    @Override
	protected void removingFromList() {}
}
