package org.javaswf.j2avm.model.code;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.code.Instruction.*;

import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Represents a point in an InstructionList between two instructions and allows
 * instructions to be inserted.
 * 
 * Calling any of the Instructions interface methods will insert that
 * instruction at the current point and leave this cursor just after the new
 * instruction.
 * 
 * @author nickmain
 */
public class InstructionCursor implements Instructions {

    private final InstructionList list;

    private Instruction prev;

    private Instruction next;

    /**
     * Visit the the next instruction and then advance beyond it.
     * 
     * @return true if the visit took place, false if cursor was at the end of
     *         the list 
     */
    public boolean visitNext( Instructions visitor ) {
        
        if( next == null ) return false;
        next.accept( visitor );

        prev = next;
        next = (prev != null) ? prev.next : null;
        
        return true;
    }

    /**
     * Get the next instruction
     * 
     * @return null if the cursor is at the end of the list
     */
    public Instruction next() {
        return next;
    }
    
    /**
     * Get the previous instruction
     * 
     * @return null if the cursor is at the start of the list
     */
    public Instruction prev() {
        return prev;
    }
    
    /**
     * Get the next instruction and position the cursor after it.
     * @return null if there is no following instruction
     */
    public Instruction forward() {
    	if( next == null ) return null;
    
    	prev = next;
    	next = next.next;
    	return prev;
    }
    
    /*pkg*/ InstructionCursor( InstructionList list, Instruction prev,
                               Instruction next) {
        this.list = list;
        this.prev = prev;
        this.next = next;
    }

    private void add(Instruction insn) {
        list.insert( insn, prev, next );
        prev = insn;
        next = insn.next;
    }

    /** @see org.javaswf.j2avm.model.code.Instructions#label(org.javaswf.j2avm.model.code.CodeLabel) */
    public void label(CodeLabel label) {
        add( label );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#nop()
     */
    public void nop() {
        add( new Nop() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushInt(int)
     */
    public void pushInt(int value) {
        add( new PushInt( value ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushFloat(float)
     */
    public void pushFloat(float value) {
        add( new PushFloat( value ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushLong(long)
     */
    public void pushLong(long value) {
        add( new PushLong( value ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushDouble(double)
     */
    public void pushDouble(double value) {
        add( new PushDouble( value ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushString(java.lang.String)
     */
    public void pushString(String value) {
        add( new PushString( value ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushClass(org.javaswf.j2avm.model.types.JavaType)
     */
    public void pushClass(JavaType type) {
        add( new PushClass( type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushNull()
     */
    public void pushNull() {
        add( new PushNull() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushVar(int,
     *      org.javaswf.j2avm.model.types.ValueType)
     */
    public void pushVar(int varIndex, ValueType type) {
        add( new PushVar( varIndex, type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#storeVar(int,
     *      org.javaswf.j2avm.model.types.ValueType)
     */
    public void storeVar(int varIndex, ValueType type) {
        add( new StoreVar( varIndex, type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushElement(org.javaswf.j2avm.model.types.ValueType)
     */
    public void pushElement(ValueType type) {
        add( new PushElement( type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#storeElement(org.javaswf.j2avm.model.types.ValueType)
     */
    public void storeElement(ValueType type) {
        add( new StoreElement( type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#convert(org.javaswf.j2avm.model.types.PrimitiveType,
     *      org.javaswf.j2avm.model.types.PrimitiveType)
     */
    public void convert(PrimitiveType fromType, PrimitiveType toType) {
        add( new Convert( fromType, toType ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#checkCast(org.javaswf.j2avm.model.types.ObjectOrArrayType)
     */
    public void checkCast(ObjectOrArrayType type) {
        add( new CheckCast( type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#instanceOf(org.javaswf.j2avm.model.types.ObjectOrArrayType)
     */
    public void instanceOf(ObjectOrArrayType type) {
        add( new InstanceOf( type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#methodReturn(org.javaswf.j2avm.model.types.JavaType)
     */
    public void methodReturn(JavaType returnType) {
        add( new MethodReturn( returnType ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#throwException()
     */
    public void throwException() {
        add( new ThrowException() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#monitorEnter()
     */
    public void monitorEnter() {
        add( new MonitorEnter() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#monitorExit()
     */
    public void monitorExit() {
        add( new MonitorExit() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#newObject(org.javaswf.j2avm.model.types.ObjectType)
     */
    public void newObject(ObjectType type) {
        add( new NewObject( type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#newArray(ArrayType)
     */
    public void newArray(ArrayType type) {
        add( new NewArray( type ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#switch_(org.javaswf.j2avm.model.code.CodeLabel,
     *      org.javaswf.j2avm.model.code.Instructions.Case[])
     */
    public void switch_(CodeLabel defaultLabel, Case... cases) {
        add( new Switch( defaultLabel, cases ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushField(org.javaswf.j2avm.model.FieldDescriptor)
     */
    public void pushField(FieldDescriptor fieldDesc) {
        add( new PushField( fieldDesc ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#storeField(org.javaswf.j2avm.model.FieldDescriptor)
     */
    public void storeField(FieldDescriptor fieldDesc) {
        add( new StoreField( fieldDesc ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pushStaticField(org.javaswf.j2avm.model.FieldDescriptor)
     */
    public void pushStaticField(FieldDescriptor fieldDesc) {
        add( new PushStaticField( fieldDesc ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#storeStaticField(org.javaswf.j2avm.model.FieldDescriptor)
     */
    public void storeStaticField(FieldDescriptor fieldDesc) {
        add( new StoreStaticField( fieldDesc ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#invokeVirtual(org.javaswf.j2avm.model.MethodDescriptor)
     */
    public void invokeVirtual(MethodDescriptor methodDesc) {
        add( new InvokeVirtual( methodDesc ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#invokeSpecial(org.javaswf.j2avm.model.MethodDescriptor)
     */
    public void invokeSpecial(MethodDescriptor methodDesc) {
        add( new InvokeSpecial( methodDesc ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#invokeStatic(org.javaswf.j2avm.model.MethodDescriptor)
     */
    public void invokeStatic(MethodDescriptor methodDesc) {
        add( new InvokeStatic( methodDesc ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#branch(org.javaswf.j2avm.model.code.BranchType,
     *      org.javaswf.j2avm.model.code.CodeLabel)
     */
    public void branch(BranchType type, CodeLabel label) {
        add( new Branch( type, label ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#incrementVar(int, int)
     */
    public void incrementVar(int varIndex, int value) {
        add( new IncrementVar( varIndex, value ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#arrayLength()
     */
    public void arrayLength() {
        add( new ArrayLength() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#pop(int)
     */
    public void pop( int count ) {
        add( new Pop( count ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#swap()
     */
    public void swap() {
        add( new Swap() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#dup(int, int)
     */
    public void dup(int count, int skip) {
        add( new Dup( count, skip ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#addInt()
     */
    public void addInt() {
        add( new AddInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#subInt()
     */
    public void subInt() {
        add( new SubInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#multInt()
     */
    public void multInt() {
        add( new MultInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#divInt()
     */
    public void divInt() {
        add( new DivInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#remInt()
     */
    public void remInt() {
        add( new RemInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#negInt()
     */
    public void negInt() {
        add( new NegInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#addLong()
     */
    public void addLong() {
        add( new AddLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#subLong()
     */
    public void subLong() {
        add( new SubLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#multLong()
     */
    public void multLong() {
        add( new MultLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#divLong()
     */
    public void divLong() {
        add( new DivLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#remLong()
     */
    public void remLong() {
        add( new RemLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#negLong()
     */
    public void negLong() {
        add( new NegLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#addFloat()
     */
    public void addFloat() {
        add( new AddFloat() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#subFloat()
     */
    public void subFloat() {
        add( new SubFloat() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#multFloat()
     */
    public void multFloat() {
        add( new MultFloat() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#divFloat()
     */
    public void divFloat() {
        add( new DivFloat() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#remFloat()
     */
    public void remFloat() {
        add( new RemFloat() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#negFloat()
     */
    public void negFloat() {
        add( new NegFloat() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#addDouble()
     */
    public void addDouble() {
        add( new AddDouble() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#subDouble()
     */
    public void subDouble() {
        add( new SubDouble() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#multDouble()
     */
    public void multDouble() {
        add( new MultDouble() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#divDouble()
     */
    public void divDouble() {
        add( new DivDouble() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#remDouble()
     */
    public void remDouble() {
        add( new RemDouble() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#negDouble()
     */
    public void negDouble() {
        add( new NegDouble() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#shiftLeftInt()
     */
    public void shiftLeftInt() {
        add( new ShiftLeftInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#signedShiftRightInt()
     */
    public void signedShiftRightInt() {
        add( new SignedShiftRightInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#unsignedShiftRightInt()
     */
    public void unsignedShiftRightInt() {
        add( new UnsignedShiftRightInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#shiftLeftLong()
     */
    public void shiftLeftLong() {
        add( new ShiftLeftLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#signedShiftRightLong()
     */
    public void signedShiftRightLong() {
        add( new SignedShiftRightLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#unsignedShiftRightLong()
     */
    public void unsignedShiftRightLong() {
        add( new UnsignedShiftRightLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#andInt()
     */
    public void andInt() {
        add( new AndInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#orInt()
     */
    public void orInt() {
        add( new OrInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#xorInt()
     */
    public void xorInt() {
        add( new XorInt() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#andLong()
     */
    public void andLong() {
        add( new AndLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#orLong()
     */
    public void orLong() {
        add( new OrLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#xorLong()
     */
    public void xorLong() {
        add( new XorLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#compareLong()
     */
    public void compareLong() {
        add( new CompareLong() );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#compareFloat(boolean)
     */
    public void compareFloat(boolean nanG) {
        add( new CompareFloat( nanG ) );
    }

    /**
     * @see org.javaswf.j2avm.model.code.Instructions#compareDouble(boolean)
     */
    public void compareDouble(boolean nanG) {
        add( new CompareDouble( nanG ) );
    }
}
