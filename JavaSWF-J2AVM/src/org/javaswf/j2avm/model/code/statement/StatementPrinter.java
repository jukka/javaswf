package org.javaswf.j2avm.model.code.statement;

import java.util.Set;
import java.util.SortedSet;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.code.expression.CaughtExceptionExpression;
import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionPrinter;
import org.javaswf.j2avm.model.code.expression.SSAValueExpression;

/**
 * A statement visitor that prints textual representations.
 *
 * @author nickmain
 */
public final class StatementPrinter implements StatementVisitor {

	private final IndentingPrintWriter ipw;
	private final ExpressionPrinter    ep;
	
	public StatementPrinter( IndentingPrintWriter ipw ) {
		this.ipw = ipw;
		this.ep  = new ExpressionPrinter( ipw );
	}


	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitConditionalBranch(org.javaswf.j2avm.model.code.statement.LabelStatement, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitConditionalBranch( LabelStatement target, Expression condition ) {
		ipw.print( "if( " );
		condition.accept( ep );
		ipw.print( " ) goto " );
		ipw.println( target.name );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitUnconditionalBranch(org.javaswf.j2avm.model.code.statement.LabelStatement) */
	public void visitUnconditionalBranch( LabelStatement target ) {
		ipw.print( "goto " );
		ipw.println( target.name );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitElementAssignment(org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitElementAssignment( Expression array, Expression index, Expression value ) {
		ep.visitElement( array, index );
		ipw.print( " = " );
		value.accept( ep );
		ipw.println();
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitExpression(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitExpression( Expression expression ) {
		expression.accept( ep );
		ipw.println();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitFieldAssignment(org.javaswf.j2avm.model.FieldDescriptor, org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitFieldAssignment( FieldDescriptor field, Expression instance, Expression value ) {
		ep.visitInstanceField( field, instance );
		ipw.print( " = " );
		value.accept( ep );
		ipw.println();
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitLabel(org.javaswf.j2avm.model.code.statement.LabelStatement) */
	public void visitLabel( LabelStatement label ) {
		ipw.print( "---- " );
		ipw.print( label.name );
		ipw.println( " ----" );
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitMonitorEnter(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitMonitorEnter( Expression object ) {
		ipw.print( "enter-monitor: " );
		object.accept( ep );
		ipw.println();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitMonitorExit(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitMonitorExit( Expression object ) {
		ipw.print( "exit-monitor: " );
		object.accept( ep );
		ipw.println();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitReturn(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitReturn( Expression expression ) {
		ipw.print( "return " );
		if( expression != null ) expression.accept( ep );
		ipw.println();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitStaticFieldAssignment(org.javaswf.j2avm.model.FieldDescriptor, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitStaticFieldAssignment( FieldDescriptor field, Expression value ) {
		ep.visitStaticField( field );
		ipw.print( " = " );
		value.accept( ep );
		ipw.println();				
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitSwitch(Expression, LabelStatement, SortedSet) */
	public void visitSwitch( Expression value, LabelStatement defaultTarget, SortedSet<SwitchCase> cases ) {
		ipw.print( "switch( " );
		value.accept( ep );
		ipw.println( " ) {" );
		ipw.indent();
		
		for( SwitchCase switchCase : cases ) {
			ipw.print  ( "case " );
			ipw.print  ( switchCase.caseValue );
			ipw.print  ( ": goto " );
			ipw.println( switchCase.label );			
		}
		
		ipw.print  ( "default: goto " );
		ipw.println( defaultTarget );
		
		ipw.unindent();		
		ipw.println( "}" );
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitThrow(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitThrow( Expression exception ) {
		ipw.print( "throw " );
		exception.accept( ep );
		ipw.println();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitBlockEnd() */
	public void visitBlockEnd() {
		ipw.unindent();
		ipw.print( "}" );
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitBlockStart() */
	public void visitBlockStart() {
		ipw.println( "{" );
		ipw.indent();		
	}


	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitSSAValue(String, Expression, Set) */
	public void visitSSAValue( String valueName, Expression value, Set<SSAValueExpression> references  ) {
		ipw.print( valueName );
		ipw.print( " {" );
        ipw.print( references.size() );
        ipw.print( "} = " );
		value.accept( ep );
		ipw.println();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitTry(org.javaswf.j2avm.model.code.expression.CaughtExceptionExpression, org.javaswf.j2avm.model.code.statement.LabelStatement, org.javaswf.j2avm.model.code.statement.LabelStatement) */
	public void visitTry( CaughtExceptionExpression exception, LabelStatement endLabel, LabelStatement handlerLabel ) {
		ipw.println( "try-until " + endLabel + " : catch( " + exception.type() + " ) --> " + handlerLabel );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitPhi(java.lang.String, org.javaswf.j2avm.model.code.expression.SSAValueExpression[]) */
	public void visitPhi( String valueName, SSAValueExpression... values ) {
		ipw.print( "phi {" );
		
		for( int i = 0; i < values.length; i++ ) {
			if( i > 0 ) ipw.print( "," );
			ipw.print( " " );
			values[i].accept( ep );
		}
		
		ipw.println( " }" );
	}	
}
 
