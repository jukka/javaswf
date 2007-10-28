/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * An expression visitor that prints a textual representation.
 *
 * @author nickmain
 */
public final class ExpressionPrinter implements ExpressionVisitor {

	private final IndentingPrintWriter ipw;
	
	/**
	 * @param ipw the target printer
	 */
	public ExpressionPrinter( IndentingPrintWriter ipw ) {
		this.ipw = ipw;
	}
	
	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitArgument(int, org.javaswf.j2avm.model.types.ValueType) */
	public void visitArgument( int argIndex, ValueType argType ) {
		ipw.print( "arg#" + argIndex );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitArrayInitializer(org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression[]) */
	public void visitArrayInitializer( Expression array, Expression... values ) {
		array.accept( this );
		ipw.print( "{" );
		
		for( int i = 0; i < values.length; i++ ) {
			if( i == 0 ) ipw.print( " " );
			else         ipw.print( ", " );
			
			values[i].accept( this );
		}
		
		if( values.length > 0 ) ipw.print( " " );
		ipw.print( "}" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitArrayLength(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitArrayLength( Expression array ) {
		array.accept( this );
		ipw.print( ".length" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitBinaryOp(org.javaswf.j2avm.model.code.expression.BinaryOperation, org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitBinaryOp( BinaryOperation op, Expression left, Expression right ) {		
		printOpArg( left );
		ipw.print( " " );
		ipw.print( op.text );
		ipw.print( " " );
		printOpArg( right );
	}

	//print an operation arg - parenthesizing as appropriate
	private void printOpArg( Expression e ) {
		if( e instanceof BinaryOpExpression 
		 || e instanceof ConditionExpression
		 || e instanceof ConvertExpression
		 || e instanceof CastExpression
		 || e instanceof InstanceOfExpression
		 || e instanceof ConditionalExpression ) {
			ipw.print( "(" );
			e.accept( this );
			ipw.print( ")" );			
		}
		else {
			e.accept( this );
		}		
	}
	
	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitCast(org.javaswf.j2avm.model.types.ObjectOrArrayType, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitCast( ObjectOrArrayType type, Expression object ) {
		ipw.print( "(" );
		ipw.print( type.name );
		ipw.print( ") " );
		object.accept( this );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitCondition(org.javaswf.j2avm.model.code.expression.Condition, org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitCondition( Condition condition, Expression left, Expression right ) {
		printOpArg( left );
		ipw.print( " " );
		ipw.print( condition.text );
		ipw.print( " " );
		printOpArg( right );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConditional(org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitConditional( Expression condition, Expression ifTrue, Expression ifFalse ) {
		printOpArg( condition );
		ipw.print( " ? " );
		printOpArg( ifTrue );
		ipw.print( " : " );
		printOpArg( ifFalse );		
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConstantClass(org.javaswf.j2avm.model.types.JavaType) */
	public void visitConstantClass( JavaType type ) {
		ipw.print( type.name );
		ipw.print( ".class" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConstantDouble(double) */
	public void visitConstantDouble( double value ) {
		ipw.print( value );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConstantFloat(float) */
	public void visitConstantFloat( float value ) {
		ipw.print( value );
		ipw.print( "f" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConstantInt(int) */
	public void visitConstantInt( int value ) {
		ipw.print( value );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConstantLong(long) */
	public void visitConstantLong( long value ) {
		ipw.print( value );
		ipw.print( "L" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConstantNull() */
	public void visitConstantNull() {
		ipw.print( "null" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConstantString(java.lang.String) */
	public void visitConstantString( String value ) {
		ipw.writeDoubleQuotedString( value );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitConvert(org.javaswf.j2avm.model.types.PrimitiveType, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitConvert( PrimitiveType type, Expression value ) {
		ipw.print( "(" );
		ipw.print( type.name );
		ipw.print( ") " );
		printOpArg( value );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitElement(org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitElement( Expression array, Expression index ) {
		array.accept( this );
		ipw.print( "[" );
		index.accept( this );
		ipw.print( "]" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitInstanceField(org.javaswf.j2avm.model.FieldDescriptor, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitInstanceField( FieldDescriptor field, Expression instance ) {
		instance.accept( this );
		ipw.print( "." );
		ipw.print( field.name );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitInstanceOf(org.javaswf.j2avm.model.types.ObjectOrArrayType, org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitInstanceOf( ObjectOrArrayType type, Expression object ) {
		printOpArg( object );
		ipw.print( " instanceof " );
		ipw.print( type.name );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitNegate(org.javaswf.j2avm.model.code.expression.Expression) */
	public void visitNegate( Expression value ) {
		ipw.print( "-" );
		printOpArg( value );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitNew(org.javaswf.j2avm.model.types.ObjectType, org.javaswf.j2avm.model.types.ValueType[], org.javaswf.j2avm.model.code.expression.Expression[]) */
	public void visitNew( ObjectType type, ValueType[] paramTypes, Expression... args ) {
		ipw.print( "new " );
		ipw.print( type.name );
		
		if( args.length == 0 ) {
			ipw.print( "()" );			
		}
		else {
			ipw.print( "( " );
			for( int i = 0; i < args.length; i++ ) {
				if( i > 0 ) ipw.print( ", " );
				args[i].accept( this );
			}		
			ipw.print( " )" );
		}
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitNewArray(org.javaswf.j2avm.model.types.ArrayType, org.javaswf.j2avm.model.code.expression.Expression[]) */
	public void visitNewArray( ArrayType type, Expression[] dimSizes ) {
		
		int uninitializedDimCount = type.dimensionCount - dimSizes.length;
		
		ipw.print( "new " );
		ipw.print( type.elementType.name );
		
		for( int i = 0; i < uninitializedDimCount; i++ ) {
			ipw.print( "[]" );
		}

		for( int i = 0; i < dimSizes.length; i++ ) {
			ipw.print( "[" );
			dimSizes[i].accept( this );
			ipw.print( "]" );			
		}
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitSpecialCall(org.javaswf.j2avm.model.MethodDescriptor, org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression[]) */
	public void visitSpecialCall( MethodDescriptor method, Expression instance, Expression... args ) {
		instance.accept( this );
		ipw.print( ".<" );			
		ipw.print( method.owner.name );		
		ipw.print( ">::" );			
		printMethod( method, args );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitStaticCall(org.javaswf.j2avm.model.MethodDescriptor, org.javaswf.j2avm.model.code.expression.Expression[]) */
	public void visitStaticCall( MethodDescriptor method, Expression... args ) {
		ipw.print( method.owner.name );		
		ipw.print( "." );			
		printMethod( method, args );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitStaticField(org.javaswf.j2avm.model.FieldDescriptor) */
	public void visitStaticField( FieldDescriptor field ) {
		ipw.print( field.owner.name );
		ipw.print( "." );
		ipw.print( field.name );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitThis(org.javaswf.j2avm.model.types.ObjectType) */
	public void visitThis( ObjectType thisType ) {
		ipw.print( "this" );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitVariable(java.lang.String, org.javaswf.j2avm.model.types.ValueType) */
	public void visitVariable( String name, ValueType type ) {
		ipw.print( name );
	}

	/** @see org.javaswf.j2avm.model.code.expression.ExpressionVisitor#visitVirtualCall(org.javaswf.j2avm.model.MethodDescriptor, org.javaswf.j2avm.model.code.expression.Expression, org.javaswf.j2avm.model.code.expression.Expression[]) */
	public void visitVirtualCall( MethodDescriptor method, Expression instance, Expression... args ) {
		instance.accept( this );
		ipw.print( "." );
		printMethod( method, args );
	}
	
	private void printMethod( MethodDescriptor method, Expression...args ) {
		ipw.print( method.signature.name );			

		ipw.print( "(" );
		for( int i = 0; i < args.length; i++ ) {
			if( i > 0 ) ipw.print( ", " );
			else ipw.print( " " );
			
			args[i].accept( this );
		}		
		if( args.length > 0 ) ipw.print( " " );			
		ipw.print( ")" );
	}
}
