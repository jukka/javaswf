package org.javaswf.j2avm.model.code.statement;

import org.javaswf.j2avm.model.code.expression.CaughtExceptionExpression;
import org.javaswf.j2avm.model.code.expression.ConditionalExpression;
import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.expression.ExpressionContainer;
import org.javaswf.j2avm.model.code.instruction.ConstructedCode;
import org.javaswf.j2avm.model.code.instruction.StatementConstructor;
import org.javaswf.j2avm.model.code.instruction.UnassembledExpressionStatement;

/**
 * Base for statements
 *
 * @author nickmain
 */
public abstract class Statement extends ConstructedCode {

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

    /**
     * Get the wrapped expressions
     */
    protected ExpressionContainer children() {
    	return expressions;
    }
    
	/** @see org.javaswf.j2avm.model.code.instruction.ConstructedCode#construct(org.javaswf.j2avm.model.code.instruction.StatementConstructor) */
	@Override
	protected void construct( StatementConstructor constructor ) {        
        while( ! children().isComplete() ) {            
        	if( ! consumePrevious( constructor ) ) return;
        }		
	}

	/** @see org.javaswf.j2avm.model.code.instruction.ConstructedCode#isComplete() */
	@Override
	protected boolean isComplete() {		 
		return children().isComplete();
	}
	
	/**
	 * Attempt to consume the previous expression.  If the expression exists
	 * then call consumeExpression and return true.  Otherwise
	 * append this statement to the agenda and return false.
	 */
	protected final boolean consumePrevious( StatementConstructor constructor ) {

		Expression prevEx = getPrevExpression();
    	if( prevEx == null ) {
            addToAgenda( constructor, this );
    		return false;
    	}
		
        //consume the expression
        prev.remove();
    	consumeExpression( prevEx );
    	return true;
	}
	
	/**
	 * Consume an expression
	 */
	protected void consumeExpression( Expression e ) {
		children().setLastUndefinedChild( e );
	}
	
	/**
	 * Get the previous expression - throw up if none
	 */
	protected final Expression getPrevExpression() {
        //is the previous expression complete ?
        if( prev == null ) throw new RuntimeException( "Incomplete statement and no prior expression" );
        
        if( prev instanceof UnassembledExpressionStatement ) {
            return ((UnassembledExpressionStatement) prev).wrappedExpression();
        }
        else {        	
        	if( prev instanceof LabelStatement ) {
        		LabelStatement label = (LabelStatement) prev;
        		
        		//for exception handlers - expression is the caught exception
        		TryCatch handler = label.exceptionHandler();
        		if( handler != null ) {
        			CaughtExceptionExpression e = ExpressionBuilder.exception( handler.exceptionType );
        			list.insert( new ExpressionStatement( e ), label );
        			
        			return e;
        		}
        		
        		//detect a conditional expression
        		ConditionalExpression condEx = findConditional();
        		if( condEx != null ) return condEx;
        	}
        	
            //FIXME:
            return null;
        }		
	}
	
	/**
	 * Find a conditional expression
	 * 
	 * @return null if none
	 */
	private ConditionalExpression findConditional() {
		Statement[] ss = matchPrevTypes( 
				             ConditionalBranchStatement.class,
	                         UnassembledExpressionStatement.class,
	                         UnconditionalBranchStatement.class,
	                         LabelStatement.class,
	                         UnassembledExpressionStatement.class,
	                         LabelStatement.class );
		
		if( ss == null ) return null;
		
		ConditionalBranchStatement     cond = (ConditionalBranchStatement)     ss[0];
		UnassembledExpressionStatement e1   = (UnassembledExpressionStatement) ss[1];
        UnconditionalBranchStatement   ub   = (UnconditionalBranchStatement)   ss[2];
        LabelStatement                 l1   = (LabelStatement)                 ss[3];
		UnassembledExpressionStatement e2   = (UnassembledExpressionStatement) ss[4];
        LabelStatement                 l2   = (LabelStatement)                 ss[5];

		//make sure the branches go to the right places and that there aren't
        //other targetters
        if( cond.target != l1 || ub.target != l2 
         || l1.targetters.size() > 1 || l2.targetters.size() > 1 ) return null;
        
        ConditionalExpression ex = 
        	ExpressionBuilder.conditional( cond.getCondition(), 
                                           e2.wrappedExpression(), 
                                           e1.wrappedExpression() );
        
        //rewrite the statements
        cond.remove();
        e1.remove();
        ub.remove();
        l1.remove();
        e2.remove();
        l2.replace().expression( ex );
        
        return ex;        	
	}
	

	/**
	 * Match the previous statements against their types
	 * 
	 * @param types the types to match
	 * @return null if no match, otherwise the statements
	 */
	@SuppressWarnings("unchecked")
	private Statement[] matchPrevTypes( Class...types ) {
		Statement[] ss = new Statement[ types.length ];		
		Statement s = this;
		
		for( int i = ss.length - 1; i >= 0; i-- ) {
			s = s.prev;
			if( s == null ) return null;
			if(! types[i].isAssignableFrom( s.getClass() )) return null;
			ss[i] = s;
		}
		
		return ss;
	}
	
	/**
	 * Called when this statement has been added to a list
	 */
	protected abstract void addedToList();
	
	/**
	 * Called when this statement is being removed from a list (before the
	 * list reference and prev/next have been nulled)
	 */
	protected abstract void removingFromList();
}
