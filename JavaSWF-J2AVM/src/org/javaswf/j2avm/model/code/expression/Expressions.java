package org.javaswf.j2avm.model.code.expression;

import java.beans.MethodDescriptor;

import org.javaswf.j2avm.model.FieldDescriptor;
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
public interface Expressions {

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
     * Visit an array assignment expression
     * 
     * @param array the array object
     * @param index the array index
     * @param value the value to store
     */
    public void visitElementAssignment( Expression array, Expression index, Expression value );
    
    /**
     * Visit a variable assignment expression
     * 
     * @param varIndex the variable to set
     * @param value the valueto store
     */
    public void visitVariableAssignment( int varIndex, Expression value );

    /**
     * Visit an instance field assignment expression
     * 
     * @param field the field to set
     * @param instance the field instance
     * @param value the value to store
     */
    public void visitInstanceField( FieldDescriptor field, Expression instance, Expression value );

    /**
     * Visit a static field assignment
     * 
     * @param field the field to set
     * @param value the value to store
     */
    public void visitStaticFieldAssignment( FieldDescriptor field, Expression value );
    
    /**
     * Visit a conditional expression
     * 
     * @param condition the condition
     * @param ifTrue the value if the condition is true
     * @param ifFalse the value if the condition is false
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
     * @param paramTypes the constructor signature
     * @param args the constructor call arguments 
     */
    public void visitNew( ObjectType type, ValueType[] paramTypes, Expression...args );
    
    /**
     * Visit a new array expression
     * @param type the new array type
     * @param dimSizes the sizes of the dimensions to be created
     * @param initialValues the initial values (optional)
     */
    public void visitNewArray( ArrayType type, Expression[] dimSizes, Expression...initialValues );
    
    /**
     * Visit a variable
     * 
     * @param index the variable value
     * @param type the variable type
     */
    public void visitVariable( int index, ValueType type );
    
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
