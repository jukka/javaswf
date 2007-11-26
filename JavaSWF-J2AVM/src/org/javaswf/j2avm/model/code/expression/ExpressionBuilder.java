package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.code.statement.StaticSingleAssignmentStatement;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Builder for expressions.  Static methods to allow static import.
 *
 * @author nickmain
 */
public final class ExpressionBuilder {

	private ExpressionBuilder() {} //prevent instantiation

	   
    /**
     * An argument value
     * 
     * @param type the parameter type
     * @param index the parameter index (zero based)
     */
    public static ArgumentExpression argument( ValueType type, int index ) {
        return new ArgumentExpression( type, index );
    }

    
    /**
     * A reference to "this"
     * 
     * @param type the instance type
     */
    public static ThisExpression this_( ObjectType type ) {
        return new ThisExpression( type );
    }
	
	
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
	public static VirtualCallExpression call( MethodDescriptor method, 
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
	 * A conditional expression
	 * 
	 * @param condition the controlling condition
	 * @param ifTrue the value if true
	 * @param ifFalse the value if false
	 */
	public static ConditionalExpression conditional( Expression condition, 
			                                         Expression ifTrue,
			                                         Expression ifFalse ) {
		return new ConditionalExpression( condition, ifTrue, ifFalse );
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
	 * An SSA value reference
	 * 
	 * @param value the value to reference
	 */
	public static SSAValueExpression value( StaticSingleAssignmentStatement value ) {
		return new SSAValueExpression( value );
	}

	/**
	 * A caught exception
	 * 
	 * @param type the exception type
	 */
	public static CaughtExceptionExpression exception( ObjectType type ) {
		return new CaughtExceptionExpression( type );
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
	
	/**
	 * A static field read
	 * 
	 * @param field the field to access
	 */
	public static StaticFieldExpression staticField( FieldDescriptor field ) {
		return new StaticFieldExpression( field );
	}
	
	/**
	 * An instance field read
	 * 
	 * @param field the field to access
	 * @param instance the instance object
	 */
	public static InstanceFieldExpression field( FieldDescriptor field, Expression instance ) {
		return new InstanceFieldExpression( field, instance );
	}
	
	/**
	 * An array element read.
	 * 
	 * @param array the array object
	 * @param index the array index
	 */
	public static ElementExpression element( Expression array, Expression index ) {
		return new ElementExpression( array, index );
	}
	
	/**
	 * A condition expression
	 * 
	 * @param condition the condition to evaluate
	 * @param left the left operand
	 * @param right the right operand
	 */
	public static ConditionExpression condition( Condition condition, 
			                                     Expression left,
			                                     Expression right ) {
		return new ConditionExpression( condition, left, right );
	}
	
	/**
	 * A new object expression
	 * 
	 * @param type the type to instantiate
	 */
	public static NewExpression newObject( ObjectType type ) {
		return new NewExpression( type );
	}
	
	/**
	 * A new array
	 * 
	 * @param type the array type
	 * @param dimSizes the dimension sizes
	 */
	public static NewArrayExpression newArray( ArrayType type,
			                                   Expression...dimSizes ) {
		return new NewArrayExpression( type, dimSizes );
	}
	
	/**
	 * An array initializer expression
	 * 
	 * @param array the array to initialize
	 * @param values the initial values
	 */
	public static ArrayInitializerExpression initialize( Expression array, Expression...values ) {
		return new ArrayInitializerExpression( array, values );
	}
}
