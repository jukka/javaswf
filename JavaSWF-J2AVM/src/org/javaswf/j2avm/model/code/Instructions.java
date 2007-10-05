package org.javaswf.j2avm.model.code;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Interface for receiving abstracted JVM instructions.
 *
 * @author nickmain
 */
public interface Instructions {

    /**
     * A switch case
     */
    public static final class Case {
        public final int       value;
        public final CodeLabel label;
        
        public Case( int value, CodeLabel label ) {
            this.value = value;
            this.label = label;
        }
    }
    
    /**
     * A branch or exception handler target
     * 
     * @param label the label
     */
    public void label( CodeLabel label );
    
    /** No operation */
    public void nop();
    
    /** Push a constant value */
    public void pushInt( int value );

    /** Push a constant value */
    public void pushFloat( float value );

    /** Push a constant value */
    public void pushLong( long value );

    /** Push a constant value */
    public void pushDouble( double value );

    /** Push a constant value */
    public void pushString( String value );

    /** Push a constant class */
    public void pushClass( JavaType type );
    
    /** Push the null value */
    public void pushNull();
    
    /**
     * Push a local variable value
     * 
     * @param varIndex the variable index
     * @param type the variable type
     */
    public void pushVar( int varIndex, ValueType type );
    
    /**
     * Pop a value and store in a local variable
     * 
     * @param varIndex the variable index
     * @param type the variable type
     */
    public void storeVar( int varIndex, ValueType type );
    
    /**
     * Push an element of an array
     * 
     * @param type the value type
     */
    public void pushElement( ValueType type );

    /**
     * Pop and store an element of an array
     * 
     * @param type the value type
     */
    public void storeElement( ValueType type );

    /**
     * Convert from one primitive type to another
     * 
     * @param fromType the existing type 
     * @param toType the desired type 
     */
    public void convert( PrimitiveType fromType, PrimitiveType toType );

    /**
     * Check that a cast can succeed. 
     * 
     * @param type the target type
     */
    public void checkCast( ObjectOrArrayType type );
    
    /**
     * InstanceOf operator 
     * 
     * @param type the target type
     */
    public void instanceOf( ObjectOrArrayType type );
    
    /**
     * Return from method
     * 
     * @param returnType the return type
     */
    public void methodReturn( JavaType returnType );
        
    /**
     * Throw an exception
     */
    public void throwException();

    /**
     * Acquire an object monitor
     */
    public void monitorEnter();
    
    /**
     * Release an object monitor
     */
    public void monitorExit();

    /**
     * Create a new object
     * 
     * @param type the class to instantiate
     */
    public void newObject( ObjectType type );

    /**
     * Create a new array.  All dimensions are initialized.
     * 
     * @param type the array type to create
     */
    public void newArray( ArrayType type );
        
    /**
     * A switch operation
     * @param defaultLabel the default case
     * @param cases the cases in order of ascending value
     */
    public void switch_( CodeLabel defaultLabel, Case...cases );    
    
    /**
     * Push an instance field value
     * 
     * @param fieldDesc the field reference
     */
    public void pushField( FieldDescriptor fieldDesc );

    /**
     * Pop value and store in an instance field
     * 
     * @param fieldDesc the field reference
     */
    public void storeField( FieldDescriptor fieldDesc );

    /**
     * Push a static field value
     * 
     * @param fieldDesc the field reference
     */
    public void pushStaticField( FieldDescriptor fieldDesc );

    /**
     * Pop value and store in a static field
     * 
     * @param fieldDesc the field reference
     */
    public void storeStaticField( FieldDescriptor fieldDesc );

    /**
     * Invoke a virtual or interface method
     * 
     * @param methodDesc the method reference
     */
    public void invokeVirtual( MethodDescriptor methodDesc );

    /**
     * Invoke a constructor, super or private method
     * 
     * @param methodDesc the method reference
     */
    public void invokeSpecial( MethodDescriptor methodDesc );

    /**
     * Invoke a static method
     * 
     * @param methodDesc the method reference
     */
    public void invokeStatic( MethodDescriptor methodDesc );

    /**
     * A branch
     * 
     * @param type the type of branch condition
     * @param label the branch target
     */
    public void branch( BranchType type, CodeLabel label );
        
    /**
     * Increment a local int var
     * 
     * @param varIndex the variable index
     * @param value the increment value
     */
    public void incrementVar( int varIndex, int value );

    /**
     * Pop the one or two top stack values.
     * 
     * Unlike the raw JVM pop instructions this treats 64-bit values (long
     * and double) as single stack slots.
     * 
     * @param count 1 or 2
     */
    public void pop( int count );

    /**
     * Swap the top two stack values
     */
    public void swap();
    
    /**
     * Duplicate the top "count" stack values and insert them below after
     * skipping over "skip" values.
     *      
     * Unlike the raw JVM dup instructions this treats 64-bit values (long
     * and double) as single stack slots.
     *      
     * @param count the number of values to duplicate - one or two
     * @param skip the number of values to skip over - zero, one or two
     */
    public void dup( int count, int skip );

    /**
     * A unary operation
     * 
     * @param type the type of operation
     * @param resultType the result type
     */
    public void unaryOp( UnaryOpType type, PrimitiveType resultType );
    
    /**
     * A binary operation
     * 
     * @param type the type of binary operation
     * @param resultType the result type
     */
    public void binaryOp( BinOpType type, PrimitiveType resultType );
}
