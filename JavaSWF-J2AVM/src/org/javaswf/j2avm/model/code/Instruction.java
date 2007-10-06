package org.javaswf.j2avm.model.code;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.Collections;
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
import org.javaswf.j2avm.model.types.VoidType;

/**
 * An individual instruction.
 * 
 * @author nickmain
 */
public abstract class Instruction implements ValueGenerator {

    /*pkg*/ Instruction prev;
    /*pkg*/ Instruction next;
    /*pkg*/ InstructionList list;
    
    /**
     * The incoming frame - may be null.
     */
    protected Frame frame;
    
    /**
     * Accept the given visitor and call the appropriate instruction method.
     */
    public abstract void accept(Instructions visitor);

    /**
     * Get the incoming frame
     * 
     * @return may be null
     */    
    public final Frame frame() { return frame; }
    
    /**
     * Remove this instruction from the list
     */
    public final void remove() {
    	if( list == null ) return;
    	list.remove( this );
    }
    
    /**
     * Whether execution can flow from this instruction to the next, without
     * branching. 
     */
    public boolean flowsToNext() {
    	return true;
    }
    
    /**
     * Get the next instruction
     * 
     * @return null if this is the last instruction
     */
    public final Instruction next() {
    	return next;
    }
    
    /**
     * Get the subsequent instructions that may be reached by normal execution.
     */
    public Collection<Instruction> subsequents() {
    	if( next == null ) return Collections.emptySet();    	
    	return Collections.singleton( next );
    }
    
    /**
     * Get the previous instruction
     * 
     * @return null if this is the first instruction
     */
    public final Instruction prev() {
    	return prev;
    }
    
    /**
     * Perform the operation, given the incoming frame and return the
     * computed frame.
     * 
     * @param frame the incoming frame
     */
    public final Frame compute( Frame frame ) {
    	this.frame = frame;
    	return compute();
    }

    protected abstract Frame compute();
    
    /**
     * Whether the instruction may involve semantics that differ when 64 bit
     * types are on the stack - that is pop(2) and dup with a count or skip of
     * 2.
     */
    /*pkg*/ boolean mayInvolve64BitSlots() {
    	return false;
    }
    
    /**
     * Normalize the instruction so that it treats 64 bits types as single
     * stack slots rather than 2.
     */
    /*pkg*/ void normalize() {}
    
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame;
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, PrimitiveType.INT, list.nextValueName() ));
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
		
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, PrimitiveType.FLOAT, list.nextValueName() ));
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, PrimitiveType.LONG, list.nextValueName() ));
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, PrimitiveType.DOUBLE, list.nextValueName() ));
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, ObjectType.STRING, list.nextValueName() ));
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, ObjectType.CLASS, list.nextValueName() ));
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, ObjectType.OBJECT, list.nextValueName() ));
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( frame.locals[ varIndex ] );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.setLocal( varIndex, type );
			frameAfter.pop( );
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

		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.popPush( 2, new Value( this, type, list.nextValueName() ));
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

		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( ); //index
			frameAfter.pop( ); //array			
			frameAfter.pop( ); //value
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( ); //from
			frameAfter.push( toType );
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.popPush( 1, new Value( this, type, list.nextValueName() ));
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.popPush( 1, new Value( this, PrimitiveType.BOOLEAN, list.nextValueName() ));
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame; //since there are no subsequents
		}
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#subsequents() */
		@Override
		public Collection<Instruction> subsequents() {
			return Collections.emptySet();
		}
		
		@Override
		public boolean flowsToNext() {
			return false;
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame;  //since there are no subsequents
		}
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#subsequents() */
		@Override
		public Collection<Instruction> subsequents() {
			return Collections.emptySet();
		}
		
		@Override
		public boolean flowsToNext() {
			return false;
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.pop( 1 );
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.pop( 1 );
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, type, "new_" + list.nextValueName() ) );
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
    	    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			
			for( int i = 0; i < type.dimensionCount; i++ ) {
				frameAfter.pop( );				
			}

			frameAfter.push( type );
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
        
        /** @see org.javaswf.j2avm.model.code.LabelTargetter#targets(Set) */
        public void targets( Set<CodeLabel> targets ) {
            targets.add( defaultLabel );
            for( Case c : cases ) {
                targets.add( c.label );                
            }
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.pop( 1 );
		}
		
		@Override
		public boolean flowsToNext() {
			return false;
		}
		    	
		/** @see org.javaswf.j2avm.model.code.Instruction#subsequents() */
		@Override
		public Collection<Instruction> subsequents() {

			Collection<Instruction> subs = new HashSet<Instruction>();
			subs.add( defaultLabel );
			
			for( Case c : cases ) {
				subs.add( c.label );	
			}
			
			return subs;
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.popPush( 1, new Value( this, fieldDesc.type, list.nextValueName()) );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( ); //value
			frameAfter.pop( ); //instance
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.push( new Value( this, fieldDesc.type, list.nextValueName()) );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			
			int count = methodDesc.signature.paramTypes.length;
			for( int i = 0; i < count; i++ ) {
				frameAfter.pop( );
			}
			frameAfter.pop( ); //instance
			
			if( methodDesc.type != VoidType.VOID ) {
				frameAfter.push( (ValueType) methodDesc.type );
			}
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			
			int count = methodDesc.signature.paramTypes.length;
			for( int i = 0; i < count; i++ ) {
				frameAfter.pop( );
			}
			frameAfter.pop( ); //instance
			
			if( methodDesc.type != VoidType.VOID ) {
				frameAfter.push( (ValueType) methodDesc.type );
			}
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			
			int count = methodDesc.signature.paramTypes.length;
			for( int i = 0; i < count; i++ ) {
				frameAfter.pop( );
			}
			
			if( methodDesc.type != VoidType.VOID ) {
				frameAfter.push( (ValueType) methodDesc.type );
			}
		}
    }

    static class Branch extends Instruction implements LabelTargetter {
        BranchType type;

        CodeLabel label;

        Branch(BranchType type, CodeLabel label) {
            this.type = type;
            this.label = label;
            label.targetters.add( this );
        }

        public void accept(Instructions visitor) {
            visitor.branch( type, label );
        }
        
        /** @see org.javaswf.j2avm.model.code.LabelTargetter#targets(Set) */
        public void targets( Set<CodeLabel> targets ) {
            targets.add( label );
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#subsequents() */
		@Override
		public Collection<Instruction> subsequents() {
			if( type == BranchType.UNCONDITIONAL || next == null ) {
				return Collections.singleton( (Instruction) label );
			}
			
			Collection<Instruction> subs = new HashSet<Instruction>();
			subs.add( next );
			subs.add( label );
			
			return subs;
		}

		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );

			switch( type ) {
				case UNCONDITIONAL:
					return;
					
				case IF_EQUAL_TO_ZERO:
				case IF_NOT_EQUAL_TO_ZERO:
				case IF_LESS_THAN_ZERO:
				case IF_GREATER_THAN_ZERO:
				case IF_GREATER_OR_EQUAL_TO_ZERO:
				case IF_LESS_OR_EQUAL_TO_ZERO:
				case IF_NULL:
				case IF_NOT_NULL:
					frameAfter.pop( );
					return;
					
				case IF_EQUAL:
				case IF_NOT_EQUAL:
				case IF_LESS_THAN:
				case IF_GREATER_THAN:
				case IF_GREATER_OR_EQUAL:
				case IF_LESS_OR_EQUAL:
				case IF_SAME_OBJECT:
				case IF_NOT_SAME_OBJECT:
					frameAfter.pop( );
					frameAfter.pop( );
					return;
			    
				default: break;
			}
		}

		@Override
		public boolean flowsToNext() {
			return type != BranchType.UNCONDITIONAL;
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.setLocal( varIndex, PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			
			//64 bit type requires a double pop
			if( count == 1 && list.hasBeenNormalized ) {
				ValueType vt = before.peek( 0 );
				if( vt == PrimitiveType.LONG || vt == PrimitiveType.DOUBLE ) {
					frameAfter.pop( 2 );
					return;
				}				
			}
			
			frameAfter.pop( count );
		}
		
		@Override
		/*pkg*/ boolean mayInvolve64BitSlots() {
	    	return count == 2;
	    }

		/** @see org.javaswf.j2avm.model.code.Instruction#normalize() */
		@Override
		void normalize() {
			//if popping two slots and the stack top is a 64 bit type then
			//normalize to popping 1
			if( count == 2 ) {
				ValueType vt = frameBefore.peek( 0 );
				if( vt == PrimitiveType.LONG || vt == PrimitiveType.DOUBLE ) {
					count = 1;
				}
			}
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
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.swap();
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			
			int c = count;
			int s = skip;
			
			//if normalized and item to be skipped or duped is a 64 bit type
			//then need to use 2 slots
			if( list.hasBeenNormalized ) { 
				if( count == 1 ) {
					ValueType vt = frameBefore.peek( 0 );
					if( vt == PrimitiveType.LONG || vt == PrimitiveType.DOUBLE ) {
						c = 2;
					}
				}
				
				if( skip == 1 ) {
					ValueType vt = frameBefore.peek( c );
					if( vt == PrimitiveType.LONG || vt == PrimitiveType.DOUBLE ) {
						s = 2;
					}
				}
			}			
			
			frameAfter.dup( c, s );
		}
		
		@Override
		/*pkg*/ boolean mayInvolve64BitSlots() {
	    	return count == 2 || skip == 2;
	    }
		
		/** @see org.javaswf.j2avm.model.code.Instruction#normalize() */
		@Override
		void normalize() {

			//if skipping two slots and the item to be skipped is a 64 bit type then
			//normalize to skipping 1
			if( skip == 2 ) {
				ValueType vt = frameBefore.peek( count );
				if( vt == PrimitiveType.LONG || vt == PrimitiveType.DOUBLE ) {
					skip = 1;
				}
			}

			//if duping two slots and the stack top is a 64 bit type then
			//normalize to duping 1
			if( count == 2 ) {
				ValueType vt = frameBefore.peek( 0 );
				if( vt == PrimitiveType.LONG || vt == PrimitiveType.DOUBLE ) {
					count = 1;
				}
			}
		}
    }

    static class BinaryOp extends Instruction {
    	
    	private final BinOpType type; 
    	private final PrimitiveType resultType;
    	
        BinaryOp( BinOpType type, PrimitiveType resultType ) {
        	this.type       = type;
        	this.resultType = resultType;
        }

        public void accept(Instructions visitor) {
            visitor.binaryOp( type, resultType );
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( type.name().toLowerCase() + " " + resultType.name );		
    	}
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.popPush( 2, new Value( this, resultType, list.nextValueName()) );
		}
    }

    static class UnaryOp extends Instruction {
    	
    	private final UnaryOpType type;
    	private final PrimitiveType resultType;
    	
        UnaryOp( UnaryOpType type, PrimitiveType resultType ) {
        	this.resultType = resultType;
        	this.type       = type;
        }

        public void accept(Instructions visitor) {
            visitor.unaryOp( type, resultType );
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		ipw.println( type.name().toLowerCase() + " " + resultType.name );		
    	}
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			return frame.popPush( 1, new Value( this, resultType, list.nextValueName()) );
		}
    }

    /**
     * Dump the instruction .
     */
    public abstract void dump( IndentingPrintWriter ipw );
}
