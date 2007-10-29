package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.code.instruction.UndefinedExpression;


/**
 * Base for Statement and Expression - objects that can contain expressions.
 *
 * @author nickmain
 */
public class ExpressionContainer {
		
	/*pkg*/ final Expression[] children;
	/*pkg*/ ExpressionContainer parent;
	
	//count of valid (not undefined) expressions
	private int count;
	
	public ExpressionContainer( Expression...ee ) {
		this.children = new Expression[ ee.length ];
		
		for( int i = 0; i < ee.length; i++ ) {		    
		    set( i, ee[i] );
		}
	}
	
	private void set( int index, Expression e ) {
	    if( e == null ) {
            e = new UndefinedExpression();	        
	    }
	    else {
	        count++;
	    }
	    
	    e.parent = this;
	    children[ index ] = e;
	}
	
	/**
	 * Get the number of child expressions
	 */
	public int childCount() {
		return children.length;
	}
	
	/**
	 * The count of valid expressions (not null or undefined)
	 */
	public int validCount() {
	    return count;
	}
	
	/**
	 * Whether all the expressions are complete (there are no nulls or 
	 * undefined children) and their children are all complete.
	 */
	public final boolean isComplete() {
		if( count != children.length ) return false;
		for( Expression e : children ) {
            if( ! e.isComplete() ) return false;
        }
		
		return true;
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
    	return children[index];
    }
    
    /**
     * Set the last child that is undefined in the expression tree to the 
     * given expression.
     */
    public final void setLastUndefinedChild( Expression e ) {
        if( setLastUndefinedChild_( e ) ) return;
        
        throw new RuntimeException( "There were no undefined child expressions" );
    }
    
    final boolean setLastUndefinedChild_( Expression e ) {
        for( int i = children.length - 1; i >= 0; i-- ) {
            if( children[i] instanceof UndefinedExpression ) {
                set( i, e );
                return true;
            }
            else {
                if( children[i].setLastUndefinedChild_( e ) ) return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get the children from the given index
     */
    final Expression[] children( int start ) {
		Expression[] kids = new Expression[ children.length - start ];
		System.arraycopy( children, start, kids, 0, kids.length );		
		return kids;
    }
}
