package org.javaswf.j2avm.model.code.statement;

import java.util.SortedSet;
import java.util.TreeSet;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.code.expression.Expression;

/**
 * Factory for creating statements.
 * Statements are inserted into the underlying StatementList and the associated 
 * StatementCursor is advanced.
 *
 * @author nickmain
 */
public abstract class Statements {
	
	/**
	 * Listener interface for receiving insertion events
	 */
	public interface Listener {		
		public void statementInserted( Statement s );
	}
	
	private Listener listener;
	
	/**
	 * Set the listener to receive insertion events
	 * @param listener may be null to stop events
	 */
	public void setListener( Listener listener ) {
		this.listener = listener;
	}

	/*pkg*/ void insert_( Statement s ) {		
		insert( s );
		if( listener != null ) listener.statementInserted( s );
	}
	
	/**
	 * Override to insert the given statement
	 */
	protected abstract void insert( Statement s );
	
	/**
	 * Get or make a LabelStatement for the given name
	 */
	protected abstract LabelStatement labelForName( Object name );
	
    /**
     * Insert a label.
     * 
     * @param name a unique name for the label
     */
    public final Statements label( Object name )  {
    	insert_( new LabelStatement( name ) );
    	return this;
    }
    
    /**
     * Insert an unconditional branch
     * 
     * @param name the target label name
     */
    public final Statements branch( Object name ) {
    	LabelStatement label = labelForName( name );
    	BranchStatement branch = new UnconditionalBranchStatement( label );
    	insert_( branch );
    	return this;
    }
    
    /**
     * Insert a conditional branch
     * 
     * @param name the target label name
     * @param condition the condition for the branch
     */
    public final Statements conditionalBranch( Object name, Expression condition ) {
    	LabelStatement label = labelForName( name );
    	BranchStatement branch = new ConditionalBranchStatement( label, condition );
    	insert_( branch );    	
    	return this;
    }
    
    /**
     * Insert an expression evaluation statement
     * 
     * @param expression the expression to evaluate
     */
    public final Statements expression( Expression expression ) {    	
    	insert_( new ExpressionStatement( expression ) );
    	return this;
    }
    
    /**
     * Assign to a variable
     * 
     * @param varName the variable to assign
     * @param value the value to assign
     */
    public final Statements assign( String varName, Expression value ) {
    	insert_( new StaticSingleAssignmentStatement( varName, value ) );
    	return this;
    }
    
    /**
     * Assign to an array element
     * 
     * @param array the array object
     * @param index the element index
     * @param value the value to assign
     */
    public final Statements assign( Expression array, Expression index, Expression value ) {
    	insert_( new ElementAssignmentStatement( array, index, value )  );
    	return this;
    }
    
    /**
     * Assign to a static field
     * 
     * @param field the field
     * @param value the value to assigns
     */
    public final Statements assign( FieldDescriptor field, Expression value ) {
    	insert_( new StaticFieldAssignmentStatement( field, value ) );
    	return this;
    }
    
    /**
     * Assign to a field
     * 
     * @param field    the field
     * @param instance the instance object
     * @param value    the value to assign
     */
    public final Statements assign( FieldDescriptor field, Expression instance, Expression value ) {
    	insert_( new FieldAssignmentStatement( field, instance, value ) );
    	return this;
    }
    
    
    /**
     * Insert a void method return
     */
    public final Statements voidReturn() {
    	insert_( new ReturnStatement() );
    	return this;
    }

    /**
     * Insert a method return
     * 
     * @param expression the return value
     */
    public final Statements returnValue( Expression expression ) {
    	insert_( new ReturnStatement( expression ) );
    	return this;
    }

    
    /**
     * Insert a throw statement
     * 
     * @param exception the exception to throw
     */
    public final Statements throwException( Expression exception ) {
    	insert_( new ThrowStatement( exception ) );
    	return this;    	
    }
    
    /**
     * Insert a switch statement
     * 
     * @param value the value to match on
     * @return a factory for the cases
     */
    public final Cases switchBranch( Expression value ) {    	
    	return new Cases( value );
    }
    
    /**
     * Insert the start of a synchronized block
     * 
     * @param object the object to sync on
     */
    public final Statements monitorEnter( Expression object ) {
    	insert_( new MonitorEnterStatement( object ) );
    	return this;    	
    }
    
    /**
     * Insert the end of a synchronized block
     * 
     * @param object the object to sync on
     */
    public final Statements monitorExit( Expression object ) {
    	insert_( new MonitorExitStatement( object ) );
    	return this;    	
    }    
    
    /**
     * Insert an increment statement
     * 
     * @param varName the variable to increment
     * @param increment the increment value
     */
    public final Statements increment( String varName, Expression increment ) {
    	insert_( new IncrementStatement( increment, varName ) );
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
    	public final Cases switchCase( int caseValue, Object target ) {
    		LabelStatement label = labelForName( target );
    		cases.add( new SwitchCase( caseValue, label ) );
    		return this;
    	}

    	/**
    	 * Add the default case and insert the switch statement into the 
    	 * statement list.
    	 * 
    	 * @param target the label name for the default target
    	 */
    	public final Statements defaultCase( Object target ) {
    		LabelStatement label = labelForName( target );
    		insert_( new SwitchStatement( 
    			value, label, 
    		    cases.toArray( new SwitchCase[ cases.size() ] )));
    		return Statements.this;
    	}
    }
}
