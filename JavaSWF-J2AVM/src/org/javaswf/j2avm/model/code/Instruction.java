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
 * An individual instruction.
 * 
 * @author nickmain
 */
public abstract class Instruction {

    /* pkg */Instruction prev;

    /* pkg */Instruction next;

    /* pkg */InstructionList list;

    /**
     * Accept the given visitor and call the appropriate instruction method.
     */
    public abstract void accept(Instructions visitor);

    static class Nop extends Instruction {
        Nop() {
        }

        public void accept(Instructions visitor) {
            visitor.nop();
        }
    }

    static class PushInt extends Instruction {
        int value;

        PushInt(int value) {
            this.value = value;
        }

        public void accept(Instructions visitor) {
            visitor.pushInt( value );
        }
    }

    static class PushFloat extends Instruction {
        float value;

        PushFloat(float value) {
            this.value = value;
        }

        public void accept(Instructions visitor) {
            visitor.pushFloat( value );
        }
    }

    static class PushLong extends Instruction {
        long value;

        PushLong(long value) {
            this.value = value;
        }

        public void accept(Instructions visitor) {
            visitor.pushLong( value );
        }
    }

    static class PushDouble extends Instruction {
        double value;

        PushDouble(double value) {
            this.value = value;
        }

        public void accept(Instructions visitor) {
            visitor.pushDouble( value );
        }
    }

    static class PushString extends Instruction {
        String value;

        PushString(String value) {
            this.value = value;
        }

        public void accept(Instructions visitor) {
            visitor.pushString( value );
        }
    }

    static class PushClass extends Instruction {
        JavaType type;

        PushClass(JavaType type) {
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.pushClass( type );
        }
    }

    static class PushNull extends Instruction {
        PushNull() {
        }

        public void accept(Instructions visitor) {
            visitor.pushNull();
        }
    }

    static class PushVar extends Instruction {
        int varIndex;

        ValueType type;

        PushVar(int varIndex, ValueType type) {
            this.varIndex = varIndex;
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.pushVar( varIndex, type );
        }
    }

    static class StoreVar extends Instruction {
        int varIndex;

        ValueType type;

        StoreVar(int varIndex, ValueType type) {
            this.varIndex = varIndex;
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.storeVar( varIndex, type );
        }
    }

    static class PushElement extends Instruction {
        ValueType type;

        PushElement(ValueType type) {
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.pushElement( type );
        }
    }

    static class StoreElement extends Instruction {
        ValueType type;

        StoreElement(ValueType type) {
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.storeElement( type );
        }
    }

    static class Convert extends Instruction {
        PrimitiveType fromType;

        PrimitiveType toType;

        Convert(PrimitiveType fromType, PrimitiveType toType) {
            this.fromType = fromType;
            this.toType = toType;
        }

        public void accept(Instructions visitor) {
            visitor.convert( fromType, toType );
        }
    }

    static class CheckCast extends Instruction {
        ObjectOrArrayType type;

        CheckCast(ObjectOrArrayType type) {
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.checkCast( type );
        }
    }

    static class InstanceOf extends Instruction {
        ObjectOrArrayType type;

        InstanceOf(ObjectOrArrayType type) {
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.instanceOf( type );
        }
    }

    static class MethodReturn extends Instruction {
        JavaType returnType;

        MethodReturn(JavaType returnType) {
            this.returnType = returnType;
        }

        public void accept(Instructions visitor) {
            visitor.methodReturn( returnType );
        }
    }

    static class ThrowException extends Instruction {
        ThrowException() {
        }

        public void accept(Instructions visitor) {
            visitor.throwException();
        }
    }

    static class MonitorEnter extends Instruction {
        MonitorEnter() {
        }

        public void accept(Instructions visitor) {
            visitor.monitorEnter();
        }
    }

    static class MonitorExit extends Instruction {
        MonitorExit() {
        }

        public void accept(Instructions visitor) {
            visitor.monitorExit();
        }
    }

    static class NewObject extends Instruction {
        ObjectType type;

        NewObject(ObjectType type) {
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.newObject( type );
        }
    }

    static class NewArray extends Instruction {
        ArrayType type;

        int dimCount;

        NewArray(ArrayType type, int dimCount) {
            this.type = type;
            this.dimCount = dimCount;
        }

        public void accept(Instructions visitor) {
            visitor.newArray( type, dimCount );
        }
    }

    static class Switch extends Instruction {
        CodeLabel defaultLabel;

        Case[] cases;

        Switch(CodeLabel defaultLabel, Case... cases) {
            this.defaultLabel = defaultLabel;
            this.cases = cases;
        }

        public void accept(Instructions visitor) {
            visitor.switch_( defaultLabel, cases );
        }
    }

    static class PushField extends Instruction {
        FieldDescriptor fieldDesc;

        PushField(FieldDescriptor fieldDesc) {
            this.fieldDesc = fieldDesc;
        }

        public void accept(Instructions visitor) {
            visitor.pushField( fieldDesc );
        }
    }

    static class StoreField extends Instruction {
        FieldDescriptor fieldDesc;

        StoreField(FieldDescriptor fieldDesc) {
            this.fieldDesc = fieldDesc;
        }

        public void accept(Instructions visitor) {
            visitor.storeField( fieldDesc );
        }
    }

    static class PushStaticField extends Instruction {
        FieldDescriptor fieldDesc;

        PushStaticField(FieldDescriptor fieldDesc) {
            this.fieldDesc = fieldDesc;
        }

        public void accept(Instructions visitor) {
            visitor.pushStaticField( fieldDesc );
        }
    }

    static class StoreStaticField extends Instruction {
        FieldDescriptor fieldDesc;

        StoreStaticField(FieldDescriptor fieldDesc) {
            this.fieldDesc = fieldDesc;
        }

        public void accept(Instructions visitor) {
            visitor.storeStaticField( fieldDesc );
        }
    }

    static class InvokeVirtual extends Instruction {
        MethodDescriptor methodDesc;

        InvokeVirtual(MethodDescriptor methodDesc) {
            this.methodDesc = methodDesc;
        }

        public void accept(Instructions visitor) {
            visitor.invokeVirtual( methodDesc );
        }
    }

    static class InvokeSpecial extends Instruction {
        MethodDescriptor methodDesc;

        InvokeSpecial(MethodDescriptor methodDesc) {
            this.methodDesc = methodDesc;
        }

        public void accept(Instructions visitor) {
            visitor.invokeSpecial( methodDesc );
        }
    }

    static class InvokeStatic extends Instruction {
        MethodDescriptor methodDesc;

        InvokeStatic(MethodDescriptor methodDesc) {
            this.methodDesc = methodDesc;
        }

        public void accept(Instructions visitor) {
            visitor.invokeStatic( methodDesc );
        }
    }

    static class Branch extends Instruction {
        BranchType type;

        CodeLabel label;

        Branch(BranchType type, CodeLabel label) {
            this.type = type;
            this.label = label;
        }

        public void accept(Instructions visitor) {
            visitor.branch( type, label );
        }
    }

    static class IncrementVar extends Instruction {
        int varIndex;

        int value;

        IncrementVar(int varIndex, int value) {
            this.varIndex = varIndex;
            this.value = value;
        }

        public void accept(Instructions visitor) {
            visitor.incrementVar( varIndex, value );
        }
    }

    static class ArrayLength extends Instruction {
        ArrayLength() {
        }

        public void accept(Instructions visitor) {
            visitor.arrayLength();
        }
    }

    static class Pop extends Instruction {
        Pop() {
        }

        public void accept(Instructions visitor) {
            visitor.pop();
        }
    }

    static class Swap extends Instruction {
        Swap() {
        }

        public void accept(Instructions visitor) {
            visitor.swap();
        }
    }

    static class Dup extends Instruction {
        int count;

        int skip;

        Dup(int count, int skip) {
            this.count = count;
            this.skip = skip;
        }

        public void accept(Instructions visitor) {
            visitor.dup( count, skip );
        }
    }

    static class AddInt extends Instruction {
        AddInt() {
        }

        public void accept(Instructions visitor) {
            visitor.addInt();
        }
    }

    static class SubInt extends Instruction {
        SubInt() {
        }

        public void accept(Instructions visitor) {
            visitor.subInt();
        }
    }

    static class MultInt extends Instruction {
        MultInt() {
        }

        public void accept(Instructions visitor) {
            visitor.multInt();
        }
    }

    static class DivInt extends Instruction {
        DivInt() {
        }

        public void accept(Instructions visitor) {
            visitor.divInt();
        }
    }

    static class RemInt extends Instruction {
        RemInt() {
        }

        public void accept(Instructions visitor) {
            visitor.remInt();
        }
    }

    static class NegInt extends Instruction {
        NegInt() {
        }

        public void accept(Instructions visitor) {
            visitor.negInt();
        }
    }

    static class AddLong extends Instruction {
        AddLong() {
        }

        public void accept(Instructions visitor) {
            visitor.addLong();
        }
    }

    static class SubLong extends Instruction {
        SubLong() {
        }

        public void accept(Instructions visitor) {
            visitor.subLong();
        }
    }

    static class MultLong extends Instruction {
        MultLong() {
        }

        public void accept(Instructions visitor) {
            visitor.multLong();
        }
    }

    static class DivLong extends Instruction {
        DivLong() {
        }

        public void accept(Instructions visitor) {
            visitor.divLong();
        }
    }

    static class RemLong extends Instruction {
        RemLong() {
        }

        public void accept(Instructions visitor) {
            visitor.remLong();
        }
    }

    static class NegLong extends Instruction {
        NegLong() {
        }

        public void accept(Instructions visitor) {
            visitor.negLong();
        }
    }

    static class AddFloat extends Instruction {
        AddFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.addFloat();
        }
    }

    static class SubFloat extends Instruction {
        SubFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.subFloat();
        }
    }

    static class MultFloat extends Instruction {
        MultFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.multFloat();
        }
    }

    static class DivFloat extends Instruction {
        DivFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.divFloat();
        }
    }

    static class RemFloat extends Instruction {
        RemFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.remFloat();
        }
    }

    static class NegFloat extends Instruction {
        NegFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.negFloat();
        }
    }

    static class AddDouble extends Instruction {
        AddDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.addDouble();
        }
    }

    static class SubDouble extends Instruction {
        SubDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.subDouble();
        }
    }

    static class MultDouble extends Instruction {
        MultDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.multDouble();
        }
    }

    static class DivDouble extends Instruction {
        DivDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.divDouble();
        }
    }

    static class RemDouble extends Instruction {
        RemDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.remDouble();
        }
    }

    static class NegDouble extends Instruction {
        NegDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.negDouble();
        }
    }

    static class ShiftLeftInt extends Instruction {
        ShiftLeftInt() {
        }

        public void accept(Instructions visitor) {
            visitor.shiftLeftInt();
        }
    }

    static class SignedShiftRightInt extends Instruction {
        SignedShiftRightInt() {
        }

        public void accept(Instructions visitor) {
            visitor.signedShiftRightInt();
        }
    }

    static class UnsignedShiftRightInt extends Instruction {
        UnsignedShiftRightInt() {
        }

        public void accept(Instructions visitor) {
            visitor.unsignedShiftRightInt();
        }
    }

    static class ShiftLeftLong extends Instruction {
        ShiftLeftLong() {
        }

        public void accept(Instructions visitor) {
            visitor.shiftLeftLong();
        }
    }

    static class SignedShiftRightLong extends Instruction {
        SignedShiftRightLong() {
        }

        public void accept(Instructions visitor) {
            visitor.signedShiftRightLong();
        }
    }

    static class UnsignedShiftRightLong extends Instruction {
        UnsignedShiftRightLong() {
        }

        public void accept(Instructions visitor) {
            visitor.unsignedShiftRightLong();
        }
    }

    static class AndInt extends Instruction {
        AndInt() {
        }

        public void accept(Instructions visitor) {
            visitor.andInt();
        }
    }

    static class OrInt extends Instruction {
        OrInt() {
        }

        public void accept(Instructions visitor) {
            visitor.orInt();
        }
    }

    static class XorInt extends Instruction {
        XorInt() {
        }

        public void accept(Instructions visitor) {
            visitor.xorInt();
        }
    }

    static class AndLong extends Instruction {
        AndLong() {
        }

        public void accept(Instructions visitor) {
            visitor.andLong();
        }
    }

    static class OrLong extends Instruction {
        OrLong() {
        }

        public void accept(Instructions visitor) {
            visitor.orLong();
        }
    }

    static class XorLong extends Instruction {
        XorLong() {
        }

        public void accept(Instructions visitor) {
            visitor.xorLong();
        }
    }

    static class CompareLong extends Instruction {
        CompareLong() {
        }

        public void accept(Instructions visitor) {
            visitor.compareLong();
        }
    }

    static class CompareFloat extends Instruction {
        boolean nanG;

        CompareFloat(boolean nanG) {
            this.nanG = nanG;
        }

        public void accept(Instructions visitor) {
            visitor.compareFloat( nanG );
        }
    }

    static class CompareDouble extends Instruction {
        boolean nanG;

        CompareDouble(boolean nanG) {
            this.nanG = nanG;
        }

        public void accept(Instructions visitor) {
            visitor.compareDouble( nanG );
        }
    }

}
