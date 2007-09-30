package org.javaswf.j2avm.model.code;

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
public abstract class Instruction {

    /*pkg*/ Instruction prev;
    /*pkg*/ Instruction next;
    /*pkg*/ InstructionList list;
    /*pkg*/ Frame frameBefore;
    /*pkg*/ Frame frameAfter;
    
    
    /**
     * Accept the given visitor and call the appropriate instruction method.
     */
    public abstract void accept(Instructions visitor);

    /**
     * Get the incoming frame
     * 
     * @return null if frames have not been determined
     */
    public final Frame frameBefore() {
    	return frameBefore;
    }

    /**
     * Get the outgoing frame
     * 
     * @return null if frames have not been determined
     */
    public final Frame frameAfter() {
    	return frameAfter;
    }
    
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
    public Instruction next() {
    	return next;
    }
    
    /**
     * Get the previous instruction
     * 
     * @return null if this is the first instruction
     */
    public Instruction prev() {
    	return prev;
    }
    
    /**
     * Merge an incoming frame with any already determined
     * 
     * @return true if the outgoing frame changed
     */
    public final boolean mergeIncomingFrame( Frame frame ) {
    	
    	//merge in an existing frame
    	if( frameBefore != null ) {
    		if( frameBefore.mergeLocals( frame ) ) {
    	    	execute( frameBefore );    	
    	    	return true;    			 
    		}
    		
    		return false;
    	}
    	
    	//execute for the first time
    	execute( new Frame( frame ) );    	
    	return true;
    }
    
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
    
    /**
     * Execute the instruction to determine the resulting Frame.  Sets the
     * frameBefore and frameAfter values.
     * 
     * @param before the incoming frame
     */
    protected abstract void execute( Frame before );
    
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

		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
		    frameAfter  = before;			
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

		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( PrimitiveType.INT );
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
		
    	@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( PrimitiveType.FLOAT );
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

    	@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( PrimitiveType.LONG );
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

		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( PrimitiveType.DOUBLE );
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

		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( ObjectType.STRING );
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

    	@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( ObjectType.CLASS );
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

    	@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( ObjectType.OBJECT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( before.getLocal( varIndex ) );
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

		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( ); //index
			frameAfter.pop( ); //array			
			frameAfter.push( type ); //element
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.push( type );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = null;
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = null;
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( type );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
		}
		
		@Override
		public boolean flowsToNext() {
			return false;
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( ); //instance
			frameAfter.push( fieldDesc.type );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.push( fieldDesc.type );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.swap();
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.FLOAT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.FLOAT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.FLOAT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.FLOAT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.FLOAT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.FLOAT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.DOUBLE );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.DOUBLE );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.DOUBLE );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.DOUBLE );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.DOUBLE );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.DOUBLE );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.LONG );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
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
    	
		@Override
		protected void execute( Frame before ) {
			frameBefore = before;
			frameAfter  = new Frame( before );
			frameAfter.pop( );
			frameAfter.pop( );
			frameAfter.push( PrimitiveType.INT );
		}
    }
    
    /**
     * Dump the instruction .
     */
    public abstract void dump( IndentingPrintWriter ipw );
}
