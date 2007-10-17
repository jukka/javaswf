package org.javaswf.j2avm.model.code.statement;

import java.util.SortedSet;
import java.util.TreeSet;

import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * Factory for creating statements.
 * Statements are inserted into the underlying StatementList and the associated 
 * StatementCursor is advanced.
 *
 * @author nickmain
 */
public class Statements {

	private final StatementCursor cursor;
	
	/*pkg*/ Statements( StatementCursor cursor ) {
		this.cursor = cursor;
	}
	
    /**
     * Insert a label.
     * 
     * @param name a unique name for the label
     */
    public Statements label( String name )  {
    	cursor.insert( new LabelStatement( name ) );
    	return this;
    }
    
    /**
     * Insert an unconditional branch
     * 
     * @param name the target label name
     */
    public Statements branch( String name ) {
    	LabelStatement label = cursor.list.labelForName( name );
    	BranchStatement branch = new BranchStatement( label );
    	cursor.insert( branch );
    	return this;
    }
    
    /**
     * Insert a condition branch
     * 
     * @param name the target label name
     * @param condition the condition for the branch
     */
    public Statements branch( String name, Expression condition ) {
    	LabelStatement label = cursor.list.labelForName( name );
    	BranchStatement branch = new BranchStatement( label, condition );
    	cursor.insert( branch );    	
    	return this;
    }
    
    /**
     * Insert an expression evaluation statement
     * 
     * @param expression the expression to evaluate
     */
    public Statements expression( Expression expression ) {    	
    	cursor.insert( new ExpressionStatement( expression ) );
    	return this;
    }
    
    /**
     * Insert a void method return
     */
    public Statements voidReturn() {
    	cursor.insert( new ReturnStatement( null ) );
    	return this;
    }

    /**
     * Insert a method return
     * 
     * @param expression the return value - null if the method is Statements
     */
    public Statements returnValue( Expression expression ) {
    	cursor.insert( new ReturnStatement( expression ) );
    	return this;
    }

    
    /**
     * Insert a throw statement
     * 
     * @param exception the exception to throw
     */
    public Statements throwException( Expression exception ) {
    	cursor.insert( new ThrowStatement( exception ) );
    	return this;    	
    }
    
    /**
     * Insert a switch statement
     * 
     * @param value the value to match on
     * @return a factory for the cases
     */
    public Cases switchBranch( Expression value ) {    	
    	return new Cases( value );
    }
    
    /**
     * Insert the start of a synchronized block
     * 
     * @param object the object to sync on
     */
    public Statements monitorEnter( Expression object ) {
    	cursor.insert( new MonitorEnterStatement( object ) );
    	return this;    	
    }
    
    /**
     * Insert the end of a synchronized block
     * 
     * @param object the object to sync on
     */
    public Statements monitorExit( Expression object ) {
    	cursor.insert( new MonitorExitStatement( object ) );
    	return this;    	
    }    
    
    /**
     * Insert an increment statement
     * 
     * @param varIndex the variable to increment
     * @param increment the increment value
     */
    public Statements increment( int varIndex, Expression increment ) {
    	cursor.insert( new IncrementStatement( increment, varIndex ) );
		return this;    	
	}   
    
    /**
     * Factory for switch cases.  defaultCase() is called after all the
     * value cases and completes the switch statement.
     */
    public class Cases {
    	
    	private final Expression value;
    	private final SortedSet<SwitchCase> cases = new TreeSet<SwitchCase>(); 
    	
    	Cases( Expression value ) {
    		this.value = value;
    	}
    	
    	/**
    	 * Add a case to the switch
    	 * 
    	 * @param caseValue the case value
    	 * @param target the target label name
    	 */
    	public Cases switchCase( int caseValue, String target ) {
    		LabelStatement label = cursor.list.labelForName( target );
    		cases.add( new SwitchCase( caseValue, label ) );
    		return this;
    	}

    	/**
    	 * Add the default case and insert the switch statement into the 
    	 * statement list.
    	 * 
    	 * @param target the label name for the default target
    	 */
    	public Statements defaultCase( String target ) {
    		LabelStatement label = cursor.list.labelForName( target );
    		cursor.insert( new SwitchStatement( 
    			value, label, 
    		    cases.toArray( new SwitchCase[ cases.size() ] )));
    		return Statements.this;
    	}
    }
}
