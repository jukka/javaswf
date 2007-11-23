package org.javaswf.j2avm.model.code.statement;

import java.util.Set;

import org.javaswf.j2avm.model.code.expression.CaughtExceptionExpression;

/**
 * The start of a try-block
 *
 * @author nickmain
 */
public class TryStatement extends Statement implements LabelTargetter {

	private final CaughtExceptionExpression exception;
	private final Object endName; //name of end label
	private final Object handlerName; //name of handler label
	
	private LabelStatement endLabel;
	private LabelStatement handlerLabel;
	
	/*pkg*/ public TryStatement( CaughtExceptionExpression exception, Object endName, Object handlerName ) {
		this.exception   = exception;
		this.endName     = endName;
		this.handlerName = handlerName;
	}
	
	/** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitTry( exception, endLabel, handlerLabel );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#targets(java.util.Set) */
	public void targets( Set<LabelStatement> labels ) {
		labels.add( endLabel );
		labels.add( handlerLabel );
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#addedToList() */
	@Override
	protected void addedToList() {
		endLabel     = list.labelForName( endName );
		handlerLabel = list.labelForName( handlerName );

		endLabel    .targetters.add( this );
		handlerLabel.targetters.add( this );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.Statement#removingFromList() */
	@Override
	protected void removingFromList() {
		endLabel    .targetters.remove( this );
		handlerLabel.targetters.remove( this );		
	}
}


