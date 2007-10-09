package org.javaswf.j2avm.model.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    	
        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
            return frame.popStore( frame.stack[0], varIndex );
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

        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
            return frame.pop( 3 );
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
    	
        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
            return frame.popPush( 1, new Value( this, toType, list.nextValueName() ));
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
        ValueType[] paramTypes; //null if this instruction has not been aggregated

        NewObject(ObjectType type, ValueType... paramTypes) {
            this.type = type;
            this.paramTypes = paramTypes;
        }

        public void accept(Instructions visitor) {
            visitor.newObject( type, paramTypes );
        }
        
    	/** @see org.javaswf.j2avm.model.code.Instruction#dump(org.epistem.io.IndentingPrintWriter) */
    	@Override
    	public void dump(IndentingPrintWriter ipw) {
    		String params;
    		
    		if( paramTypes != null ) {
    			StringBuilder buff = new StringBuilder();
    			buff.append( "(" );
    			
    			boolean first = true;
    			for( ValueType vt : paramTypes ) {
    				if( first ) first = false;
    				else buff.append( "," );
    				
    				buff.append( vt.name );
    			}
    			
    			buff.append( ")" );
    			params = buff.toString();
    		}
    		else {
    			params = "";
    		}
    		
    		ipw.println( "newObject " + type + params );		
    	}
    	
		/** @see org.javaswf.j2avm.model.code.Instruction#compute() */
		@Override
		protected Frame compute() {
			int paramCount = ( paramTypes == null ) ? 0 : paramTypes.length;
			
			return frame.popPush( paramCount, new Value( this, type, "new_" + list.nextValueName() ) );
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
        protected Frame compute() {
            return frame.popPush( type.dimensionCount, new Value( this, type, list.nextValueName() ) );
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
        
        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
            return frame.pop( 2 );
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
    	
        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
            return frame.pop( 1 );
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
    	
        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
            int popCount = 1 + methodDesc.signature.paramTypes.length;
            
            if( methodDesc.type == VoidType.VOID ) {
                return frame.pop( popCount );
            }
            
            return frame.popPush( 
                popCount, 
                new Value( this, (ValueType) methodDesc.type, list.nextValueName() ) );
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
    	
        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
        	
            int popCount = 1 + methodDesc.signature.paramTypes.length;
          
            if( methodDesc.type == VoidType.VOID ) {
                return frame.pop( popCount );
            }
            
            return frame.popPush( 
                popCount, 
                new Value( this, (ValueType) methodDesc.type, list.nextValueName()));
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
    	
        /** @see org.javaswf.j2avm.model.code.Instruction#compute() */
        @Override
        protected Frame compute() {
            int popCount = methodDesc.signature.paramTypes.length;
            
            if( methodDesc.type == VoidType.VOID ) {
                return frame.pop( popCount );
            }
            
            return frame.popPush( 
                popCount, 
                new Value( this, (ValueType) methodDesc.type, list.nextValueName() ) );
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
        protected Frame compute() {

			switch( type ) {
				case UNCONDITIONAL:
					return frame;
					
				case IF_EQUAL_TO_ZERO:
				case IF_NOT_EQUAL_TO_ZERO:
				case IF_LESS_THAN_ZERO:
				case IF_GREATER_THAN_ZERO:
				case IF_GREATER_OR_EQUAL_TO_ZERO:
				case IF_LESS_OR_EQUAL_TO_ZERO:
				case IF_NULL:
				case IF_NOT_NULL:
					return frame.pop( 1 );
					
				case IF_EQUAL:
				case IF_NOT_EQUAL:
				case IF_LESS_THAN:
				case IF_GREATER_THAN:
				case IF_GREATER_OR_EQUAL:
				case IF_LESS_OR_EQUAL:
				case IF_SAME_OBJECT:
				case IF_NOT_SAME_OBJECT:
					return frame.pop( 2 );
			    
				default: throw new RuntimeException();
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
        protected Frame compute() {
            return frame; //is this right - is it really the same value ?
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
        protected Frame compute() {
		    ValueType top = frame.stack[0].getValue().type();
		    
		    //if the list is not yet normalized and the top type is 64-bit then
		    //normalize this instruction 
		    if( count == 2 && ! list.hasBeenNormalized && PrimitiveType.is64Bit( top )) {
		        count = 1;
		    }
		    
            return frame.pop( count );
        }
		
		@Override
		/*pkg*/ boolean mayInvolve64BitSlots() {
	    	return count == 2;
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
        protected Frame compute() {
            Slot dup1  = frame.stack[0];
            Slot dup2  = null;
            Slot skip1 = null;
            Slot skip2 = null;

            int stackIndex = 1;

            //if the list is not yet normalized and the top type is 64-bit then
            //normalize the dup count 
            if( count == 2 && ! list.hasBeenNormalized && PrimitiveType.is64Bit( dup1.getValue().type() )) {
                count = 1;
            }
            else if( count == 2 ) {
                dup2 = frame.stack[ stackIndex++ ];
            }
            
            if( skip > 0 ) skip1 = frame.stack[ stackIndex++ ];
            
            //if the list is not yet normalized and the skip type is 64-bit then
            //normalize the skip count 
            if( skip == 2 && ! list.hasBeenNormalized && PrimitiveType.is64Bit( skip1.getValue().type() )) {
                skip = 1;
            }
            else if( skip == 2 ) {
                skip2 = frame.stack[ stackIndex ];
            }            
            
            int popCount = 1 + ((dup2  != null)? 1 :0) 
                             + ((skip1 != null)? 1 :0)
                             + ((skip2 != null)? 1 :0);
            
            List<Slot> slots = new ArrayList<Slot>();
            slots.add( dup1 );
            if( dup2  != null ) slots.add( dup2  );
            if( skip1 != null ) slots.add( skip1 );
            if( skip2 != null ) slots.add( skip2 );
            slots.add( dup1 );
            if( dup2 != null ) slots.add( dup2 );
            
            return frame.popPush( popCount, slots.toArray( new Slot[ slots.size() ]));
        }
		
		@Override
		/*pkg*/ boolean mayInvolve64BitSlots() {
	    	return count == 2 || skip == 2;
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
