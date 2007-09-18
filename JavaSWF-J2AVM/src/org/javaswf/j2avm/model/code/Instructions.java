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
     * The array length operator
     */
    public void arrayLength();

    /**
     * Pop the top stack value.
     * 
     * This abstracts away longs and doubles and treats them as single-size
     * values.
     */
    public void pop();

    /**
     * Swap the top two stack values
     */
    public void swap();
    
    /**
     * Duplicate the top "count" stack values and insert them below after
     * skipping over "skip" values.
     * 
     * This abstracts away longs and doubles and treats them as single-size
     * values.
     * 
     * @param count the number of values to duplicate - one or two
     * @param skip the number of values to skip over - zero, one or two
     */
    public void dup( int count, int skip );

    /** Int addition */
    public void addInt();

    /** Int subtraction */
    public void subInt();

    /** Int multiplication */
    public void multInt();

    /** Int division */
    public void divInt();

    /** Int remainder */
    public void remInt();

    /** Int negation */
    public void negInt();
        
    /** Long addition */
    public void addLong();

    /** Long subtraction */
    public void subLong();

    /** Long multiplication */
    public void multLong();

    /** Long division */
    public void divLong();

    /** Long remainder */
    public void remLong();
    
    /** Long negation */
    public void negLong();
        
    /** Float addition */
    public void addFloat();

    /** Float subtraction */
    public void subFloat();

    /** Float multiplication */
    public void multFloat();

    /** Float division */
    public void divFloat();

    /** Float remainder */
    public void remFloat();
    
    /** Float negation */
    public void negFloat();
        
    /** Double addition */
    public void addDouble();

    /** Double subtraction */
    public void subDouble();

    /** Double multiplication */
    public void multDouble();

    /** Double division */
    public void divDouble();

    /** Double remainder */
    public void remDouble();
    
    /** Double negation */
    public void negDouble();
        
    /** Int shift left */
    public void shiftLeftInt();
    
    /** Int signed shift right */
    public void signedShiftRightInt();
    
    /** Int unsigned shift right */
    public void unsignedShiftRightInt();
    
    /** Long shift left */
    public void shiftLeftLong();
    
    /** Long signed shift right */
    public void signedShiftRightLong();
    
    /** Long unsigned shift right */
    public void unsignedShiftRightLong();
    
    /** Int bitwise AND */
    public void andInt();
    
    /** Int bitwise OR */
    public void orInt();
    
    /** Int bitwise XOR */
    public void xorInt();

    /** Long bitwise AND */
    public void andLong();
    
    /** Long bitwise OR */
    public void orLong();
    
    /** Long bitwise XOR */
    public void xorLong();

    /** Long comparison */
    public void compareLong();

    /**
     * Float comparison
     * @param nanG true if NaN pushes 1, -1 otherwise
     */
    public void compareFloat( boolean nanG );

    /**
     * Double comparison
     * @param nanG true if NaN pushes 1, -1 otherwise
     */
    public void compareDouble( boolean nanG );
}
