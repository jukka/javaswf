/**
 * 
 */
package org.javaswf.j2avm.model.code.statement;

import java.util.HashSet;
import java.util.Set;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.SSAValueExpression;

/**
 * The definition of an SSA value
 *
 * @author nickmain
 */
public class StaticSingleAssignmentStatement extends Statement {
	
	//the references to this value
	private final Set<SSAValueExpression> references = new HashSet<SSAValueExpression>();
	
	private String name;
	
	protected StaticSingleAssignmentStatement( Expression expression ) {
		super( expression );
	}

	//for phi statements:
	protected StaticSingleAssignmentStatement( SSAValueExpression... values ) {
		super( values );
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitSSAValue( name, child(0) );		
	}
	
	/**
	 * Get the value expression
	 */
	public Expression expression() {
		return child( 0 );
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#addedToList() */
	@Override
	protected void addedToList() {
		name = "$" + (list.ssaValueIndex++);
		
		list.ssaValues.put( name, this );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#removingFromList() */
	@Override
	protected void removingFromList() {
		list.ssaValues.remove( name );
	}

	/**
	 * Add a reference to this value
	 */
	public void addReference( SSAValueExpression ref ) {
		references.add( ref );
	}

	/**
	 * Drop a reference to this value
	 */
	public void dropReference( SSAValueExpression ref ) {
		references.remove( ref );
	}
	
	/**
	 * Get the value name
	 */
	public String name() {
		return name;
	}
}