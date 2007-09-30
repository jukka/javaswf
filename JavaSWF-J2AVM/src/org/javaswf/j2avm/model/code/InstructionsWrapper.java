package org.javaswf.j2avm.model.code;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.code.Instructions.Case;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A wrapper around Instructions that returns self from each method so that
 * calls can be chained together.
 * 
 * @author nickmain
 */
public class InstructionsWrapper {

	private final Instructions instructions;

	/**
	 * @param instructions the Instructions to delegate to
	 */
	public InstructionsWrapper( Instructions instructions ) {
		this.instructions = instructions;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#addDouble() */
	public InstructionsWrapper addDouble() {
		instructions.addDouble();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#addFloat() */
	public InstructionsWrapper addFloat() {
		instructions.addFloat();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#addInt() */
	public InstructionsWrapper addInt() {
		instructions.addInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#addLong() */
	public InstructionsWrapper addLong() {
		instructions.addLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#andInt() */
	public InstructionsWrapper andInt() {
		instructions.andInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#andLong() */
	public InstructionsWrapper andLong() {
		instructions.andLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#arrayLength() */
	public InstructionsWrapper arrayLength() {
		instructions.arrayLength();
		return this;
	}

	/**
	 * @see org.javaswf.j2avm.model.code.Instructions#branch(org.javaswf.j2avm.model.code.BranchType,
	 *      org.javaswf.j2avm.model.code.CodeLabel)
	 */
	public InstructionsWrapper branch( BranchType type, CodeLabel label ) {
		instructions.branch( type, label );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#checkCast(org.javaswf.j2avm.model.types.ObjectOrArrayType) */
	public InstructionsWrapper checkCast( ObjectOrArrayType type ) {
		instructions.checkCast( type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#compareDouble(boolean) */
	public InstructionsWrapper compareDouble( boolean nanG ) {
		instructions.compareDouble( nanG );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#compareFloat(boolean) */
	public InstructionsWrapper compareFloat( boolean nanG ) {
		instructions.compareFloat( nanG );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#compareLong() */
	public InstructionsWrapper compareLong() {
		instructions.compareLong();
		return this;
	}

	/**
	 * @see org.javaswf.j2avm.model.code.Instructions#convert(org.javaswf.j2avm.model.types.PrimitiveType,org.javaswf.j2avm.model.types.PrimitiveType)
	 */
	public InstructionsWrapper convert( PrimitiveType fromType,
			PrimitiveType toType ) {
		instructions.convert( fromType, toType );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divDouble() */
	public InstructionsWrapper divDouble() {
		instructions.divDouble();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divFloat() */
	public InstructionsWrapper divFloat() {
		instructions.divFloat();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divInt() */
	public InstructionsWrapper divInt() {
		instructions.divInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#divLong() */
	public InstructionsWrapper divLong() {
		instructions.divLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#dup(int, int) */
	public InstructionsWrapper dup( int count, int skip ) {
		instructions.dup( count, skip );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#incrementVar(int, int) */
	public InstructionsWrapper incrementVar( int varIndex, int value ) {
		instructions.incrementVar( varIndex, value );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#instanceOf(org.javaswf.j2avm.model.types.ObjectOrArrayType) */
	public InstructionsWrapper instanceOf( ObjectOrArrayType type ) {
		instructions.instanceOf( type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#invokeSpecial(org.javaswf.j2avm.model.MethodDescriptor) */
	public InstructionsWrapper invokeSpecial( MethodDescriptor methodDesc ) {
		instructions.invokeSpecial( methodDesc );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#invokeStatic(org.javaswf.j2avm.model.MethodDescriptor) */
	public InstructionsWrapper invokeStatic( MethodDescriptor methodDesc ) {
		instructions.invokeStatic( methodDesc );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#invokeVirtual(org.javaswf.j2avm.model.MethodDescriptor) */
	public InstructionsWrapper invokeVirtual( MethodDescriptor methodDesc ) {
		instructions.invokeVirtual( methodDesc );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#label(org.javaswf.j2avm.model.code.CodeLabel) */
	public InstructionsWrapper label( CodeLabel label ) {
		instructions.label( label );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#methodReturn(org.javaswf.j2avm.model.types.JavaType) */
	public InstructionsWrapper methodReturn( JavaType returnType ) {
		instructions.methodReturn( returnType );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#monitorEnter() */
	public InstructionsWrapper monitorEnter() {
		instructions.monitorEnter();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#monitorExit() */
	public InstructionsWrapper monitorExit() {
		instructions.monitorExit();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multDouble() */
	public InstructionsWrapper multDouble() {
		instructions.multDouble();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multFloat() */
	public InstructionsWrapper multFloat() {
		instructions.multFloat();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multInt() */
	public InstructionsWrapper multInt() {
		instructions.multInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#multLong() */
	public InstructionsWrapper multLong() {
		instructions.multLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negDouble() */
	public InstructionsWrapper negDouble() {
		instructions.negDouble();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negFloat() */
	public InstructionsWrapper negFloat() {
		instructions.negFloat();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negInt() */
	public InstructionsWrapper negInt() {
		instructions.negInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#negLong() */
	public InstructionsWrapper negLong() {
		instructions.negLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#newArray(org.javaswf.j2avm.model.types.ArrayType) */
	public InstructionsWrapper newArray( ArrayType type ) {
		instructions.newArray( type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#newObject(org.javaswf.j2avm.model.types.ObjectType) */
	public InstructionsWrapper newObject( ObjectType type ) {
		instructions.newObject( type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#nop() */
	public InstructionsWrapper nop() {
		instructions.nop();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#orInt() */
	public InstructionsWrapper orInt() {
		instructions.orInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#orLong() */
	public InstructionsWrapper orLong() {
		instructions.orLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pop(int) */
	public InstructionsWrapper pop( int count ) {
		instructions.pop( count );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushClass(org.javaswf.j2avm.model.types.JavaType) */
	public InstructionsWrapper pushClass( JavaType type ) {
		instructions.pushClass( type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushDouble(double) */
	public InstructionsWrapper pushDouble( double value ) {
		instructions.pushDouble( value );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushElement(org.javaswf.j2avm.model.types.ValueType) */
	public InstructionsWrapper pushElement( ValueType type ) {
		instructions.pushElement( type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushField(org.javaswf.j2avm.model.FieldDescriptor) */
	public InstructionsWrapper pushField( FieldDescriptor fieldDesc ) {
		instructions.pushField( fieldDesc );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushFloat(float) */
	public InstructionsWrapper pushFloat( float value ) {
		instructions.pushFloat( value );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushInt(int) */
	public InstructionsWrapper pushInt( int value ) {
		instructions.pushInt( value );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushLong(long) */
	public InstructionsWrapper pushLong( long value ) {
		instructions.pushLong( value );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushNull() */
	public InstructionsWrapper pushNull() {
		instructions.pushNull();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushStaticField(org.javaswf.j2avm.model.FieldDescriptor) */
	public InstructionsWrapper pushStaticField( FieldDescriptor fieldDesc ) {
		instructions.pushStaticField( fieldDesc );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#pushString(java.lang.String) */
	public InstructionsWrapper pushString( String value ) {
		instructions.pushString( value );
		return this;
	}

	/**
	 * @see org.javaswf.j2avm.model.code.Instructions#pushVar(int,
	 *      org.javaswf.j2avm.model.types.ValueType)
	 */
	public InstructionsWrapper pushVar( int varIndex, ValueType type ) {
		instructions.pushVar( varIndex, type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remDouble() */
	public InstructionsWrapper remDouble() {
		instructions.remDouble();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remFloat() */
	public InstructionsWrapper remFloat() {
		instructions.remFloat();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remInt() */
	public InstructionsWrapper remInt() {
		instructions.remInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#remLong() */
	public InstructionsWrapper remLong() {
		instructions.remLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#shiftLeftInt() */
	public InstructionsWrapper shiftLeftInt() {
		instructions.shiftLeftInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#shiftLeftLong() */
	public InstructionsWrapper shiftLeftLong() {
		instructions.shiftLeftLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#signedShiftRightInt() */
	public InstructionsWrapper signedShiftRightInt() {
		instructions.signedShiftRightInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#signedShiftRightLong() */
	public InstructionsWrapper signedShiftRightLong() {
		instructions.signedShiftRightLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#storeElement(org.javaswf.j2avm.model.types.ValueType) */
	public InstructionsWrapper storeElement( ValueType type ) {
		instructions.storeElement( type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#storeField(org.javaswf.j2avm.model.FieldDescriptor) */
	public InstructionsWrapper storeField( FieldDescriptor fieldDesc ) {
		instructions.storeField( fieldDesc );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#storeStaticField(org.javaswf.j2avm.model.FieldDescriptor) */
	public InstructionsWrapper storeStaticField( FieldDescriptor fieldDesc ) {
		instructions.storeStaticField( fieldDesc );
		return this;
	}

	/**
	 * @see org.javaswf.j2avm.model.code.Instructions#storeVar(int,
	 *      org.javaswf.j2avm.model.types.ValueType)
	 */
	public InstructionsWrapper storeVar( int varIndex, ValueType type ) {
		instructions.storeVar( varIndex, type );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subDouble() */
	public InstructionsWrapper subDouble() {
		instructions.subDouble();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subFloat() */
	public InstructionsWrapper subFloat() {
		instructions.subFloat();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subInt() */
	public InstructionsWrapper subInt() {
		instructions.subInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#subLong() */
	public InstructionsWrapper subLong() {
		instructions.subLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#swap() */
	public InstructionsWrapper swap() {
		instructions.swap();
		return this;
	}

	/**
	 * @see org.javaswf.j2avm.model.code.Instructions#switch_(org.javaswf.j2avm.model.code.CodeLabel,org.javaswf.j2avm.model.code.Instructions.Case[])
	 */
	public InstructionsWrapper switch_( CodeLabel defaultLabel, Case... cases ) {
		instructions.switch_( defaultLabel, cases );
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#throwException() */
	public InstructionsWrapper throwException() {
		instructions.throwException();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#unsignedShiftRightInt() */
	public InstructionsWrapper unsignedShiftRightInt() {
		instructions.unsignedShiftRightInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#unsignedShiftRightLong() */
	public InstructionsWrapper unsignedShiftRightLong() {
		instructions.unsignedShiftRightLong();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#xorInt() */
	public InstructionsWrapper xorInt() {
		instructions.xorInt();
		return this;
	}

	/** @see org.javaswf.j2avm.model.code.Instructions#xorLong() */
	public InstructionsWrapper xorLong() {
		instructions.xorLong();
		return this;
	}

}
