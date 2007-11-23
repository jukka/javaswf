package org.javaswf.j2avm.model.code.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.javaswf.j2avm.model.code.statement.Statement;

/**
 * Base for Statement and Expression - objects that can contain expressions.
 *
 * @author nickmain
 */
public abstract class ExpressionContainer implements Iterable<Expression> {
		
	private final List<Expression> children = new ArrayList<Expression>();
	protected ExpressionContainer parent;
	protected Statement statement;  //the statement at the root of the expression tree
	
	/**
	 * @param ee the child expressions
	 */
	protected ExpressionContainer( Expression...ee ) {
		for( Expression e : ee ) {
			children.add( e );
    		e.parent = this;
		}
	}
	
	/** @see java.lang.Iterable#iterator() */
	public final Iterator<Expression> iterator() {
		return children.iterator();
	}

	/**
	 * Get the number of child expressions
	 */
	public final int childCount() {
		return children.size();
	}
	
    /**
     * Visit all the child expressions in evaluation order.
     */
    public final void visitChildren( ExpressionVisitor visitor ) {
    	for( Expression e : children ) {
    		if( e != null ) e.accept( visitor );
    	}
    }
    
    /**
     * Get a child expression.
     * 
     * @param index the expression index
     * @return may be null
     */
    public final Expression child( int index ) {
    	return children.get( index );
    }
    
    protected final void addToList() {
    	addedToList();

    	//retransmit to children
    	for( Expression e : children ) {
    		e.statement = this.statement;
    		e.addToList();    		
    	}    	
    }

    protected final void removeFromList() {    	
    	//retransmit to children
    	for( Expression e : children ) {
    		e.removeFromList();
    		e.statement = null;
    	}

    	removingFromList();
    }

    
    /**
     * This method is called after this container has been added to a 
     * statement list to allow any setup to be performed.
     */
    protected abstract void addedToList();
    
    /**
     * This method is called just before this container is removed from a
     * statement list to allow any cleanup to be performed. 
     */
    protected abstract void removingFromList();
    
    
	public final void release() {
		//FIXME: DELETE ME
    }

}
