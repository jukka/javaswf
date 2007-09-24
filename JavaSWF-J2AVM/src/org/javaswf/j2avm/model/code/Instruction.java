package org.javaswf.j2avm.model.code;

import java.util.HashSet;
import java.util.Set;

import org.epistem.io.IndentingPrintWriter;
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

    /*pkg*/ Instruction prev;
    /*pkg*/ Instruction next;
    /*pkg*/ InstructionList list;
    
    /**
     * Accept the given visitor and call the appropriate instruction method.
     */
    public abstract void accept(Instructions visitor);

    /**
     * Remove this instruction from the list
     */
    public final void remove() {
    	if( list == null ) return;
    	list.remove( this );
    }
    
    static class Nop extends Instruction {
        Nop() {
        }

        public void accept(Instructions visitor) {
            visitor.nop();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "nop" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushInt " + value );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushFloat " + value );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushLong " + value );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushDouble " + value );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.print( "pushString " );
    		ipw.writeDoubleQuotedString( value  );
    		ipw.println();
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushClass " + type );		
    	}
    }

    static class PushNull extends Instruction {
        PushNull() {
        }

        public void accept(Instructions visitor) {
            visitor.pushNull();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushNull" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushVar " + varIndex + " (" + type + ")" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "storeVar " + varIndex + " (" + type + ")" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushElement (" + type + ")" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "storeElement (" + type + ")" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "convert " + fromType + " --> " + toType );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "checkCast " + type );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "instanceOf " + type );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "methodReturn (" + returnType + ")" );		
    	}
    }

    static class ThrowException extends Instruction {
        ThrowException() {
        }

        public void accept(Instructions visitor) {
            visitor.throwException();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "throwException" );		
    	}
    }

    static class MonitorEnter extends Instruction {
        MonitorEnter() {
        }

        public void accept(Instructions visitor) {
            visitor.monitorEnter();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "monitorEnter" );		
    	}
    }

    static class MonitorExit extends Instruction {
        MonitorExit() {
        }

        public void accept(Instructions visitor) {
            visitor.monitorExit();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "monitorExit" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "newObject " + type );		
    	}
    }

    static class NewArray extends Instruction {
        ArrayType type;

        NewArray( ArrayType type ) {
            this.type = type;
        }

        public void accept(Instructions visitor) {
            visitor.newArray( type );
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "newArray (" + type.dimensionCount + ")" + type );		
    	}
    }

    static class Switch extends Instruction implements LabelTargetter {
        CodeLabel defaultLabel;

        Case[] cases;

        Switch(CodeLabel defaultLabel, Case... cases) {
            this.defaultLabel = defaultLabel;
            this.cases = cases;
            
            defaultLabel.targetters.add( this );
            for( Case c : cases ) {
                c.label.targetters.add( this );
            }
        }

        public void accept(Instructions visitor) {
            visitor.switch_( defaultLabel, cases );
        }
        
        /** @see org.javaswf.j2avm.model.code.LabelTargetter#targets() */
        public Set<CodeLabel> targets() {
            Set<CodeLabel> targets = new HashSet<CodeLabel>();
            targets.add( defaultLabel );
            for( Case c : cases ) {
                targets.add( c.label );                
            }
            return targets;
        }
        
        /** @see org.javaswf.j2avm.model.code.LabelTargetter#release() */
        public void release() {
            defaultLabel.targetters.remove( this );
            for( Case c : cases ) {
                c.label.targetters.remove( this );
            }          
        }

        /** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "switch" );
    		ipw.indent();
    		for (Case c : cases) {
				ipw.println( "case " + c.value + " --> " + c.label );
			}
			ipw.println( "default --> " + defaultLabel );
    		ipw.unindent();
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushField " + fieldDesc );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "storeField " + fieldDesc );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pushStaticField " + fieldDesc );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "storeStaticField " + fieldDesc );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "invokeVirtual " + methodDesc );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "invokeSpecial " + methodDesc );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "invokeStatic " + methodDesc );		
    	}
    }

    static class Branch extends Instruction implements LabelTargetter {
        BranchType type;

        CodeLabel label;

        Branch(BranchType type, CodeLabel label) {
            this.type = type;
            this.label = label;
        }

        public void accept(Instructions visitor) {
            visitor.branch( type, label );
        }
        
        /** @see org.javaswf.j2avm.model.code.LabelTargetter#targets() */
        public Set<CodeLabel> targets() {
            Set<CodeLabel> targets = new HashSet<CodeLabel>();
            targets.add( label );
            return targets;
        }
        
    	/** @see org.javaswf.j2avm.model.code.LabelTargetter#release() */
        public void release() {
            label.targetters.remove( this );            
        }

        /** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "branch " + type.name().toLowerCase() + " --> " + label );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "incrementVar " + varIndex + " by " + value );		
    	}
    }

    static class ArrayLength extends Instruction {
        ArrayLength() {
        }

        public void accept(Instructions visitor) {
            visitor.arrayLength();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "arrayLength" );		
    	}
    }

    static class Pop extends Instruction {
    	int count;
    	
        Pop( int count ) {
        	this.count = count;
        }

        public void accept(Instructions visitor) {
            visitor.pop( count );
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "pop " + count );		
    	}
    }

    static class Swap extends Instruction {
        Swap() {
        }

        public void accept(Instructions visitor) {
            visitor.swap();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "swap" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.print( "dup " + count );		
    		if( skip > 0 ) ipw.print( " skip " + skip );
    		ipw.println();
    	}
    }

    static class AddInt extends Instruction {
        AddInt() {
        }

        public void accept(Instructions visitor) {
            visitor.addInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "addInt" );		
    	}
    }

    static class SubInt extends Instruction {
        SubInt() {
        }

        public void accept(Instructions visitor) {
            visitor.subInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "subInt" );		
    	}
    }

    static class MultInt extends Instruction {
        MultInt() {
        }

        public void accept(Instructions visitor) {
            visitor.multInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "multInt" );		
    	}
    }

    static class DivInt extends Instruction {
        DivInt() {
        }

        public void accept(Instructions visitor) {
            visitor.divInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "divInt" );		
    	}
    }

    static class RemInt extends Instruction {
        RemInt() {
        }

        public void accept(Instructions visitor) {
            visitor.remInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "remInt" );		
    	}
    }

    static class NegInt extends Instruction {
        NegInt() {
        }

        public void accept(Instructions visitor) {
            visitor.negInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "negInt" );		
    	}
   }

    static class AddLong extends Instruction {
        AddLong() {
        }

        public void accept(Instructions visitor) {
            visitor.addLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "addLong" );		
    	}
    }

    static class SubLong extends Instruction {
        SubLong() {
        }

        public void accept(Instructions visitor) {
            visitor.subLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "subLong" );		
    	}
    }

    static class MultLong extends Instruction {
        MultLong() {
        }

        public void accept(Instructions visitor) {
            visitor.multLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "multLong" );		
    	}
    }

    static class DivLong extends Instruction {
        DivLong() {
        }

        public void accept(Instructions visitor) {
            visitor.divLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "divLong" );		
    	}
    }

    static class RemLong extends Instruction {
        RemLong() {
        }

        public void accept(Instructions visitor) {
            visitor.remLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "remLong" );		
    	}
    }

    static class NegLong extends Instruction {
        NegLong() {
        }

        public void accept(Instructions visitor) {
            visitor.negLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "negLong" );		
    	}
    }

    static class AddFloat extends Instruction {
        AddFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.addFloat();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "addFloat" );		
    	}
    }

    static class SubFloat extends Instruction {
        SubFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.subFloat();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "subFloat" );		
    	}
    }

    static class MultFloat extends Instruction {
        MultFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.multFloat();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "multFloat" );		
    	}
    }

    static class DivFloat extends Instruction {
        DivFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.divFloat();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "divFloat" );		
    	}
    }

    static class RemFloat extends Instruction {
        RemFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.remFloat();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "remFloat" );		
    	}
    }

    static class NegFloat extends Instruction {
        NegFloat() {
        }

        public void accept(Instructions visitor) {
            visitor.negFloat();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "negFloat" );		
    	}
    }

    static class AddDouble extends Instruction {
        AddDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.addDouble();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "addDouble" );		
    	}
    }

    static class SubDouble extends Instruction {
        SubDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.subDouble();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "subDouble" );		
    	}
    }

    static class MultDouble extends Instruction {
        MultDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.multDouble();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "multDouble" );		
    	}
    }

    static class DivDouble extends Instruction {
        DivDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.divDouble();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "divDouble" );		
    	}
    }

    static class RemDouble extends Instruction {
        RemDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.remDouble();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "remDouble" );		
    	}
    }

    static class NegDouble extends Instruction {
        NegDouble() {
        }

        public void accept(Instructions visitor) {
            visitor.negDouble();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "negDouble" );		
    	}
    }

    static class ShiftLeftInt extends Instruction {
        ShiftLeftInt() {
        }

        public void accept(Instructions visitor) {
            visitor.shiftLeftInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "shiftLeftInt" );		
    	}
    }

    static class SignedShiftRightInt extends Instruction {
        SignedShiftRightInt() {
        }

        public void accept(Instructions visitor) {
            visitor.signedShiftRightInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "signedShiftRightInt" );		
    	}
    }

    static class UnsignedShiftRightInt extends Instruction {
        UnsignedShiftRightInt() {
        }

        public void accept(Instructions visitor) {
            visitor.unsignedShiftRightInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "unsignedShiftRightInt" );		
    	}
    }

    static class ShiftLeftLong extends Instruction {
        ShiftLeftLong() {
        }

        public void accept(Instructions visitor) {
            visitor.shiftLeftLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "shiftLeftLong" );		
    	}
    }

    static class SignedShiftRightLong extends Instruction {
        SignedShiftRightLong() {
        }

        public void accept(Instructions visitor) {
            visitor.signedShiftRightLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "signedShiftRightLong" );		
    	}
    }

    static class UnsignedShiftRightLong extends Instruction {
        UnsignedShiftRightLong() {
        }

        public void accept(Instructions visitor) {
            visitor.unsignedShiftRightLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "unsignedShiftRightLong" );		
    	}
    }

    static class AndInt extends Instruction {
        AndInt() {
        }

        public void accept(Instructions visitor) {
            visitor.andInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "andInt" );		
    	}
    }

    static class OrInt extends Instruction {
        OrInt() {
        }

        public void accept(Instructions visitor) {
            visitor.orInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "orInt" );		
    	}
    }

    static class XorInt extends Instruction {
        XorInt() {
        }

        public void accept(Instructions visitor) {
            visitor.xorInt();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "xorInt" );		
    	}
    }

    static class AndLong extends Instruction {
        AndLong() {
        }

        public void accept(Instructions visitor) {
            visitor.andLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "andLong" );		
    	}
    }

    static class OrLong extends Instruction {
        OrLong() {
        }

        public void accept(Instructions visitor) {
            visitor.orLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "orLong" );		
    	}
    }

    static class XorLong extends Instruction {
        XorLong() {
        }

        public void accept(Instructions visitor) {
            visitor.xorLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "xorLong" );		
    	}
    }

    static class CompareLong extends Instruction {
        CompareLong() {
        }

        public void accept(Instructions visitor) {
            visitor.compareLong();
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "compareLong" );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "compareFloat " + (nanG ? "nanG" : "nanL" ) );		
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
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( "compareDouble " + (nanG ? "nanG" : "nanL" ) );		
    	}
    }

    
    /**
     * Dump the instruction .
     */
    public abstract void dump( IndentingPrintWriter ipw );
}
