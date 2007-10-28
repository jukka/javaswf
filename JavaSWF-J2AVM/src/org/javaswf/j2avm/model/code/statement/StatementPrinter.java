package org.javaswf.j2avm.model.code.statement;

import java.util.SortedSet;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionPrinter;

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

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitBranch(org.javaswf.j2avm.model.code.statement.LabelStatement, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitBranch( LabelStatement target, Expression condition ) {
		ipw.print( "if( " );
		condition.accept( ep );
		ipw.print( " ) goto " );
		ipw.println( target.name );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitBranch(org.javaswf.j2avm.model.code.statement.LabelStatement) */
	public void visitBranch( LabelStatement target ) {
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

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitIncrement(java.lang.String, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitIncrement( String varName, Expression increment ) {
		ipw.print( varName );
		ipw.print( " += " );
		increment.accept( ep );
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

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitSwitch(org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.statement.LabelStatement, java.util.SortedSet) */
	public void visitSwitch( Expression value, LabelStatement defaultTarget, SortedSet<SwitchCase> cases ) {
		ipw.print( "switch( " );
		value.accept( ep );
		ipw.println( " ) {" );
		ipw.indent();
		
		for( SwitchCase switchCase : cases ) {
			ipw.print  ( "case " );
			ipw.print  ( switchCase.caseValue );
			ipw.print  ( ": goto " );
			ipw.println( switchCase.target.name );			
		}
		
		ipw.print  ( "default: goto " );
		ipw.println( defaultTarget.name );
		
		ipw.println( "}" );
		ipw.unindent();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitThrow(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitThrow( Expression exception ) {
		ipw.print( "throw " );
		exception.accept( ep );
		ipw.println();		
	}

	/** @see org.javaswf.j2avm.model.code.statement.StatementVisitor#visitVariableAssignment(java.lang.String, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitVariableAssignment( String varName, Expression value ) {
		ipw.print( varName );
		ipw.print( " = " );
		value.accept( ep );
		ipw.println();		
	}	
}
