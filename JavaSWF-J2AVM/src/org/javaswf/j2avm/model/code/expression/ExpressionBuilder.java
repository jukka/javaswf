package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Builder for expressions.  Static methods to allow static import.
 *
 * @author nickmain
 */
public class ExpressionBuilder {

	/**
	 * An array length operation.
	 * 
	 * @param array the array
	 */
	public static ArrayLengthExpression arrayLength( Expression array ) {
		return new ArrayLengthExpression( array );
	}

	/**
	 * A virtual method call
	 * 
	 * @param method the method
	 * @param instance the instance object
	 * @param args the method args
	 */
	public static VirtualCallExpression virtualCall( MethodDescriptor method, 
			                                         Expression instance, 
			                                         Expression...args ) {
		return new VirtualCallExpression( method, instance, args );
	}
	
	/**
	 * A special method call
	 * 
	 * @param method the method
	 * @param instance the instance object
	 * @param args the method args
	 */
	public static SpecialCallExpression specialCall( MethodDescriptor method, 
			                                         Expression instance, 
			                                         Expression...args ) {
		return new SpecialCallExpression( method, instance, args );
	}

	/**
	 * A static method call
	 * 
	 * @param method the method
	 * @param args the method args
	 */
	public static StaticCallExpression staticCall( MethodDescriptor method, 
			                                       Expression...args ) {
		return new StaticCallExpression( method, args );
	}

	/**
	 * An integer constant
	 * 
	 * @param value the constant value
	 */
	public static ConstantInt constantInt( int value ) {
		return new ConstantInt( value );
	}

	/**
	 * A long constant
	 * 
	 * @param value the constant value
	 */
	public static ConstantLong constantLong( long value ) {
		return new ConstantLong( value );
	}

	/**
	 * A float constant
	 * 
	 * @param value the constant value
	 */
	public static ConstantFloat constantFloat( float value ) {
		return new ConstantFloat( value );
	}

	/**
	 * A double constant
	 * 
	 * @param value the constant value
	 */
	public static ConstantDouble constantDouble( double value ) {
		return new ConstantDouble( value );
	}

	/**
	 * A string constant
	 * 
	 * @param value the constant value
	 */
	public static ConstantString constantString( String value ) {
		return new ConstantString( value );
	}

	/**
	 * A class constant
	 * 
	 * @param type the class
	 */
	public static ConstantClass constantClass( JavaType type ) {
		return new ConstantClass( type );
	}

	/**
	 * A null constant
	 */
	public static ConstantNull constantNull() {
		return new ConstantNull();
	}

	/**
	 * A variable value
	 * 
	 * @param index the variable index
	 * @param type the variable type
	 */
	public static VariableExpression variable( int index, ValueType type ) {
		return new VariableExpression( index, type );
	}
	
	/**
	 * A binary operation
	 * 
	 * @param op the operation
	 * @param left the left operand
	 * @param right the right operand
	 */
	public static BinaryOpExpression binaryOp( BinaryOperation op,
			                                   Expression left,
			                                   Expression right ) {
		return new BinaryOpExpression( op, left, right );
	}
	
	/**
	 * A cast operation
	 * 
	 * @param type the type to cast to
	 * @param object the object to cast
	 */
	public static CastExpression cast( ObjectOrArrayType type, Expression object ) {
		return new CastExpression( type, object );
	}
	
	/**
	 * A primitive conversion operation
	 * 
	 * @param type the type to convert to
	 * @param value the value to convert
	 */
	public static ConvertExpression convert( PrimitiveType type, Expression value ) {
		return new ConvertExpression( type, value );
	}
	
	/**
	 * An instanceof operation
	 * 
	 * @param type the type to check for
	 * @param object the value to check
	 */
	public static InstanceOfExpression instanceOf( ObjectOrArrayType type, Expression object ) {
		return new InstanceOfExpression( type, object );
	}
	
	/**
	 * A negate operation
	 * 
	 * @param value the value to negate
	 */
	public static NegateExpression negate( Expression value ) {
		return new NegateExpression( value );
	}
}
