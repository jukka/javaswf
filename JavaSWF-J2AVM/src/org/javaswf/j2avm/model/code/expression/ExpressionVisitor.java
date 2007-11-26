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
 * Visitor interface for passing expressions
 *
 * @author nickmain
 */
public interface ExpressionVisitor {

	/**
	 * Visit the "this" value of the current method
	 * 
	 * @param thisType the value type
	 */
	public void visitThis( ObjectType thisType );
	
	/**
	 * Visit an argument of the current method
	 * 
	 * @param argIndex the argument index (zero is first)
	 * @param argType the argument type
	 */
	public void visitArgument( int argIndex, ValueType argType );
	
    /** Visit a Constant Int */
    public void visitConstantInt( int value );
    
    /** Visit a Constant Long */
    public void visitConstantLong( long value );

    /** Visit a Constant Float */
    public void visitConstantFloat( float value );

    /** Visit a Constant Double */
    public void visitConstantDouble( double value );

    /** Visit a Constant String */
    public void visitConstantString( String value );

    /** Visit a Constant Class */
    public void visitConstantClass( JavaType type );

    /** Visit a Constant Null */
    public void visitConstantNull();
    
    /**
     * Visit a conditional expression
     * 
     * @param condition the controlling expression
     * @param ifTrue the value if true
     * @param ifFalse the value if false
     */
    public void visitConditional( Expression condition, Expression ifTrue, Expression ifFalse );
    
    /**
     * Visit an instance field read
     * 
     * @param field the field to read
     * @param instance the instance field
     */
    public void visitInstanceField( FieldDescriptor field, Expression instance );

    /**
     * Visit a static field read
     * 
     * @param field the field to read
     */
    public void visitStaticField( FieldDescriptor field );

    /**
     * Visit a virtual method call
     * 
     * @param method the method to be called
     * @param instance the instance object 
     * @param args the call arguments
     */
    public void visitVirtualCall( MethodDescriptor method, Expression instance, Expression...args );

    /**
     * Visit a special method call (to super or private method)
     * 
     * @param method the method to be called
     * @param instance the instance object 
     * @param args the call arguments
     */
    public void visitSpecialCall( MethodDescriptor method, Expression instance, Expression...args );

    /**
     * Visit a static method call
     * 
     * @param method the method to be called
     * @param args the call arguments
     */
    public void visitStaticCall( MethodDescriptor method, Expression...args );

    /**
     * Visit a primitive conversion operation
     * @param type the type to convert to
     * @param value the value to convert
     */
    public void visitConvert( PrimitiveType type, Expression value );
    
    /**
     * Visit a new object expression
     * 
     * @param type the type to create
     */
    public void visitNew( ObjectType type );
    
    /**
     * Visit a new array expression
     * 
     * @param type the new array type
     * @param dimSizes the sizes of the dimensions to be created
     */
    public void visitNewArray( ArrayType type, Expression[] dimSizes );
    
    /**
     * An array initializer expression
     * @param array the array to initialize
     * @param values the initial values
     */
    public void visitArrayInitializer( Expression array, Expression...values );
    
    /**
     * Visit an SSA value
     * 
     * @param value the statement that defines the value
     */
    public void visitSSAValue( StaticSingleAssignmentStatement value );

    /**
     * Visit a caught exception
     * 
     * @param type the exception type
     */
    public void visitException( ValueType type );
    
    /**
     * Visit a cast 
     * @param type the type to cast to
     * @param object the object operand
     */
    public void visitCast( ObjectOrArrayType type, Expression object );

    /**
     * Visit an instanceof operation 
     * @param type the type to check for
     * @param object the object operand
     */
    public void visitInstanceOf( ObjectOrArrayType type, Expression object );
    
    /**
     * Visit an array length expression
     * @param array the array operand
     */ 
    public void visitArrayLength( Expression array );
    
    /**
     * Visit a negate operation
     * 
     * @param value the operand
     */
    public void visitNegate( Expression value );
    
    /**
     * Visit a binary op
     * 
     * @param op the operation type
     * @param left the left operand
     * @param right the right operand
     */
    public void visitBinaryOp( BinaryOperation op, Expression left, Expression right );

    /**
     * Visit a condition operation
     * 
     * @param condition the condition to evaluate
     * @param left the left operand
     * @param right the right operand
     */
    public void visitCondition( Condition condition, Expression left, Expression right );
    
    /**
     * Visit an array element
     * 
     * @param array the array object
     * @param index the array index
     */
    public void visitElement( Expression array, Expression index );
}
