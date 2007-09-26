package org.javaswf.j2avm.model.code;

import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Base for instruction list walkers and rewriters.
 * 
 *
 * @author nickmain
 */
public abstract class InstructionListWalker implements Instructions {

	/**
	 * Walking the given method from the beginning
	 */
	public final void walk( ClassModel classModel, MethodModel methodModel ) {
		this.classModel  = classModel;
		this.methodModel = methodModel;
		
		CodeAttribute code = methodModel.attribute( CodeAttribute.class );
		if( code == null ) return;
		
		this.list = code.instructions;
		
		current = list.first();
		next    = current.next();
		
		while( current != null ) {
			current.accept( this );
			
			while( next != null && next.list == null ) {
				next = next.next;
			}
			
			if( next == null ) break;
			current = next;
			next    = current.next();
		}
	}
	
	private InstructionList list;	
	private Instruction current;
	private Instruction next;
	private ClassModel classModel;
	private MethodModel methodModel;
	
	/**
	 * Get the current class
	 */
	protected final ClassModel classModel() {
		return classModel;
	}
	
	/**
	 * Get the current method
	 */
	protected final MethodModel methodModel() {
		return methodModel;
	}
	
	/**
	 * Get the instruction currently being visited
	 */
	protected final Instruction current() {
		return current;
	}
	
    /**
     * Remove the instruction currently being visited - only do this after
     * inserting any new instructions.
     */
    protected final void remove() {
        current.remove();
    }
    
    /**
     * Get an interface for inserting instructions before the one being visited.
     */
    protected final Instructions insertBefore() {
        return list.cursorBefore( current );
    }

    /**
     * Get an interface for inserting instructions after the one being visited.
     */
    protected final Instructions insertAfter() {
        return list.cursorAfter( current );
    }

    //=======================================================================
    
	/** @see org.javaswf.j2avm.model.code.Instructions#addDouble() */
	public void addDouble() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#addFloat() */
	public void addFloat() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#addInt() */
	public void addInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#addLong() */
	public void addLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#andInt() */
	public void andInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#andLong() */
	public void andLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#arrayLength() */
	public void arrayLength() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#branch(org.javaswf.j2avm.model.code.BranchType, org.javaswf.j2avm.model.code.CodeLabel) */
	public void branch( BranchType type, CodeLabel label ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#checkCast(org.javaswf.j2avm.model.types.ObjectOrArrayType) */
	public void checkCast( ObjectOrArrayType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#compareDouble(boolean) */
	public void compareDouble( boolean nanG ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#compareFloat(boolean) */
	public void compareFloat( boolean nanG ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#compareLong() */
	public void compareLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#convert(org.javaswf.j2avm.model.types.PrimitiveType, org.javaswf.j2avm.model.types.PrimitiveType) */
	public void convert( PrimitiveType fromType, PrimitiveType toType ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divDouble() */
	public void divDouble() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divFloat() */
	public void divFloat() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divInt() */
	public void divInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divLong() */
	public void divLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#dup(int, int) */
	public void dup( int count, int skip ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#incrementVar(int, int) */
	public void incrementVar( int varIndex, int value ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#instanceOf(org.javaswf.j2avm.model.types.ObjectOrArrayType) */
	public void instanceOf( ObjectOrArrayType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#invokeSpecial(org.javaswf.j2avm.model.MethodDescriptor) */
	public void invokeSpecial( MethodDescriptor methodDesc ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#invokeStatic(org.javaswf.j2avm.model.MethodDescriptor) */
	public void invokeStatic( MethodDescriptor methodDesc ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#invokeVirtual(org.javaswf.j2avm.model.MethodDescriptor) */
	public void invokeVirtual( MethodDescriptor methodDesc ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#label(org.javaswf.j2avm.model.code.CodeLabel) */
	public void label( CodeLabel label ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#methodReturn(org.javaswf.j2avm.model.types.JavaType) */
	public void methodReturn( JavaType returnType ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#monitorEnter() */
	public void monitorEnter() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#monitorExit() */
	public void monitorExit() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multDouble() */
	public void multDouble() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multFloat() */
	public void multFloat() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multInt() */
	public void multInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multLong() */
	public void multLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negDouble() */
	public void negDouble() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negFloat() */
	public void negFloat() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negInt() */
	public void negInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negLong() */
	public void negLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#newArray(org.javaswf.j2avm.model.types.ArrayType) */
	public void newArray( ArrayType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#newObject(org.javaswf.j2avm.model.types.ObjectType) */
	public void newObject( ObjectType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#nop() */
	public void nop() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#orInt() */
	public void orInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#orLong() */
	public void orLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pop(int) */
	public void pop( int count ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushClass(org.javaswf.j2avm.model.types.JavaType) */
	public void pushClass( JavaType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushDouble(double) */
	public void pushDouble( double value ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushElement(org.javaswf.j2avm.model.types.ValueType) */
	public void pushElement( ValueType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushField(org.javaswf.j2avm.model.FieldDescriptor) */
	public void pushField( FieldDescriptor fieldDesc ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushFloat(float) */
	public void pushFloat( float value ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushInt(int) */
	public void pushInt( int value ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushLong(long) */
	public void pushLong( long value ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushNull() */
	public void pushNull() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushStaticField(org.javaswf.j2avm.model.FieldDescriptor) */
	public void pushStaticField( FieldDescriptor fieldDesc ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushString(java.lang.String) */
	public void pushString( String value ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushVar(int, org.javaswf.j2avm.model.types.ValueType) */
	public void pushVar( int varIndex, ValueType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remDouble() */
	public void remDouble() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remFloat() */
	public void remFloat() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remInt() */
	public void remInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remLong() */
	public void remLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#shiftLeftInt() */
	public void shiftLeftInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#shiftLeftLong() */
	public void shiftLeftLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#signedShiftRightInt() */
	public void signedShiftRightInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#signedShiftRightLong() */
	public void signedShiftRightLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#storeElement(org.javaswf.j2avm.model.types.ValueType) */
	public void storeElement( ValueType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#storeField(org.javaswf.j2avm.model.FieldDescriptor) */
	public void storeField( FieldDescriptor fieldDesc ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#storeStaticField(org.javaswf.j2avm.model.FieldDescriptor) */
	public void storeStaticField( FieldDescriptor fieldDesc ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#storeVar(int, org.javaswf.j2avm.model.types.ValueType) */
	public void storeVar( int varIndex, ValueType type ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subDouble() */
	public void subDouble() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subFloat() */
	public void subFloat() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subInt() */
	public void subInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subLong() */
	public void subLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#swap() */
	public void swap() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#switch_(org.javaswf.j2avm.model.code.CodeLabel, org.javaswf.j2avm.model.code.Instructions.Case[]) */
	public void switch_( CodeLabel defaultLabel, Case... cases ) {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#throwException() */
	public void throwException() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#unsignedShiftRightInt() */
	public void unsignedShiftRightInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#unsignedShiftRightLong() */
	public void unsignedShiftRightLong() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#xorInt() */
	public void xorInt() {
		// to be overridden
		
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#xorLong() */
	public void xorLong() {
		// to be overridden
		
	}    
}
