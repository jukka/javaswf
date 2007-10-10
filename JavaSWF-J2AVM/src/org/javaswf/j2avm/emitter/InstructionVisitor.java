package org.javaswf.j2avm.emitter;

import static org.javaswf.j2avm.emitter.EmitterUtils.nameForField;
import static org.javaswf.j2avm.emitter.EmitterUtils.nameForMethod;
import static org.javaswf.j2avm.emitter.EmitterUtils.nameForPrivateMethod;
import static org.javaswf.j2avm.emitter.EmitterUtils.qnameForJavaType;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.abc.ClassTranslation;
import org.javaswf.j2avm.abc.TargetABC;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.code.BinOpType;
import org.javaswf.j2avm.model.code.BranchType;
import org.javaswf.j2avm.model.code.CodeLabel;
import org.javaswf.j2avm.model.code.Instruction;
import org.javaswf.j2avm.model.code.InstructionListWalker;
import org.javaswf.j2avm.model.code.UnaryOpType;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.Signature;
import org.javaswf.j2avm.model.types.ValueType;
import org.javaswf.j2avm.model.types.VoidType;

import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * Visitor for instructions for a single method
 *
 * @author nickmain
 */
public class InstructionVisitor extends InstructionListWalker {

    private final AVM2Code           avm2Code;
    private final TargetABC      abc;
    private final ClassTranslation    avm2Class;
    private final TranslationContext context;
    private final MethodModel        methodModel;
    private final ClassModel         classModel;
    
    private boolean superConstructorCalled = false;
    
    /**
     * @param avm2code the target AVM2 method body
     * @param abc the target ABC file
     * @param avm2Class the target AVM2 class
     */
    InstructionVisitor( AVM2Code avm2code, TargetABC abc, 
    		            ClassTranslation avm2Class, TranslationContext context,
    		            ClassModel classModel, MethodModel methodModel ) {
        this.avm2Code  = avm2code;
        this.abc       = abc;
        this.avm2Class = avm2Class;
        this.context   = context;
        
        this.classModel  = classModel;
        this.methodModel = methodModel;        
    }

    /** @see org.javaswf.j2avm.model.code.InstructionListWalker#pushField(org.javaswf.j2avm.model.FieldDescriptor) */
	@Override
	public void pushField( FieldDescriptor fieldDesc ) {
        avm2Code.append( Operation.OP_getproperty, nameForField( fieldDesc.name ));
        avm2Code.append( Operation.OP_coerce,  qnameForJavaType( fieldDesc.type, abc ));
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#storeField(org.javaswf.j2avm.model.FieldDescriptor) */
	@Override
	public void storeField( FieldDescriptor fieldDesc ) {
        avm2Code.append( Operation.OP_setproperty, nameForField( fieldDesc.name ));
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#incrementVar(int, int) */
	@Override
	public void incrementVar( int varIndex, int value ) {
        if( value == 1 ) {
            avm2Code.append( Operation.OP_inclocal_i, varIndex ); 
            return;
        }
        
        //simulate the increment by an addition
        avm2Code.loadLocalVar( varIndex );                
        pushInt( value );
        avm2Code.append( Operation.OP_add_i );
        avm2Code.storeLocalVar( varIndex );    
        
        //TODO: account for potential stack size increase
	}
    
    /** @see org.javaswf.j2avm.model.code.InstructionListWalker#convert(org.javaswf.j2avm.model.types.PrimitiveType, org.javaswf.j2avm.model.types.PrimitiveType) */
	@Override
	public void convert( PrimitiveType fromType, PrimitiveType toType ) {
		
		if( fromType == PrimitiveType.INT && toType == PrimitiveType.DOUBLE ) {
			avm2Code.append( Operation.OP_convert_d );
		}
		else if( fromType == PrimitiveType.FLOAT && toType == PrimitiveType.DOUBLE ) {
			//floats are represented by doubles - this is a no-op
		}
		else  throw new RuntimeException( "unimplemented conversion from " + fromType + " to " + toType );
		
		//TODO: implement other type conversions
	}

	
	
	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#binaryOp(org.javaswf.j2avm.model.code.BinOpType, org.javaswf.j2avm.model.types.PrimitiveType) */
	@Override
	public void binaryOp( BinOpType type, PrimitiveType resultType ) {
		switch( type ) {
			case ADD:
				if( resultType.isIntType ) {
					avm2Code.append( Operation.OP_add_i );
					return;
				}		
				//TODO:
				break;
				
			case MULTIPLY:
				if( resultType.isIntType ) {
					avm2Code.append( Operation.OP_multiply_i );
					return;
				}		
				//TODO:
				break;

			case SUBTRACT:
				if( resultType.isIntType ) {
					avm2Code.append( Operation.OP_subtract_i );
					return;
				}		
				//TODO:
				break;
				
			//TODO:
			case DIVIDE:
			case REMAINDER:
			case SHIFT_LEFT:
			case SHIFT_RIGHT_SIGNED:
			case SHIFT_RIGHT_UNSIGNED:
			case AND:
			case OR:
			case XOR:
			case COMPARE:
			case COMPARE_G:
			case COMPARE_L: 
		
			default: break;
		}
		
		throw new RuntimeException( "Unhandled Binary Op" );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#unaryOp(org.javaswf.j2avm.model.code.UnaryOpType, org.javaswf.j2avm.model.types.PrimitiveType) */
	@Override
	public void unaryOp( UnaryOpType type, PrimitiveType resultType ) {
		switch( type ) {
			case ARRAY_LENGTH:
				avm2Code.append( Operation.OP_getproperty, nameForField( "length" )); 
				return;
				
			//TODO:	
			case NEGATE: 
			default: break;
		}
		
		throw new RuntimeException( "Unhandled Unary Op" );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#dup(int, int) */
	@Override
	public void dup( int count, int skip ) {
		if( count == 1 && skip == 0 ) {
			avm2Code.append( Operation.OP_dup );
		}
		
		//TODO: implement dup scenarios
		else throw new RuntimeException( "unimplemented dup" );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#pop(int) */
	@Override
	public void pop( int count ) {
		while( count-- > 0 ) {
			avm2Code.append( Operation.OP_pop );
		}
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#swap() */
	@Override
	public void swap() {
		avm2Code.append( Operation.OP_swap );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#methodReturn(org.javaswf.j2avm.model.types.JavaType) */
	@Override
	public void methodReturn( JavaType returnType ) {
		if( returnType == VoidType.VOID ) {
			avm2Code.append( Operation.OP_returnvoid );
		}
		else {
			avm2Code.append( Operation.OP_returnvalue );
		}      
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#pushInt(int) */
	@Override
	public void pushInt( int value ) {
		
		if( value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE ) {
	        avm2Code.append( Operation.OP_pushbyte, value );
		}
		else if( value <= Short.MAX_VALUE && value >= Short.MIN_VALUE ) {
	        avm2Code.append( Operation.OP_pushshort, value );
		}
		else {
	        avm2Code.append( Operation.OP_pushint, value );
		}
    }
	
	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#pushDouble(double) */
	@Override
	public void pushDouble( double value ) {
		avm2Code.append( Operation.OP_pushdouble, value );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#pushFloat(float) */
	@Override
	public void pushFloat( float value ) {
		avm2Code.append( Operation.OP_pushdouble, value );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#pushNull() */
	@Override
	public void pushNull() {
		avm2Code.append( Operation.OP_pushnull );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#pushString(java.lang.String) */
	@Override
	public void pushString( String value ) {
		avm2Code.append( Operation.OP_pushstring, value );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#nop() */
	@Override
	public void nop() {
		avm2Code.append( Operation.OP_nop );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#branch(org.javaswf.j2avm.model.code.BranchType, org.javaswf.j2avm.model.code.CodeLabel) */
	@Override
	public void branch( BranchType type, CodeLabel label ) {
		
        //handle compare-to-zero and compare-to-null preparation
        switch( type ) {
	        case IF_EQUAL_TO_ZERO:            //fall-thru 
	        case IF_NOT_EQUAL_TO_ZERO:        //fall-thru 
	        case IF_LESS_THAN_ZERO:           //fall-thru
	        case IF_GREATER_THAN_ZERO:        //fall-thru 
	        case IF_GREATER_OR_EQUAL_TO_ZERO: //fall-thru
	        case IF_LESS_OR_EQUAL_TO_ZERO: 
                avm2Code.append( Operation.OP_pushint, 0 );
                //TODO: account for potential stack size increase
                
	        case IF_NULL:     //fall-thru 
	        case IF_NOT_NULL: 
                avm2Code.append( Operation.OP_pushnull );
                //TODO: account for potential stack size increase

            default: break;
        }        
		
        Operation operation;
		switch( type ) {
	        case UNCONDITIONAL:               operation = Operation.OP_jump; break;
	        case IF_EQUAL_TO_ZERO:            //fall-thru
	        case IF_EQUAL:                    operation = Operation.OP_ifeq; break;
	        case IF_NOT_EQUAL_TO_ZERO:        //fall-thru
	        case IF_NOT_EQUAL:                operation = Operation.OP_ifne; break;
	        case IF_LESS_THAN_ZERO:           //fall-thru
	        case IF_LESS_THAN:                operation = Operation.OP_iflt; break;
	        case IF_GREATER_THAN_ZERO:        //fall-thru
	        case IF_GREATER_THAN:             operation = Operation.OP_ifgt; break;
	        case IF_GREATER_OR_EQUAL_TO_ZERO: //fall-thru
	        case IF_GREATER_OR_EQUAL:         operation = Operation.OP_ifge; break;
	        case IF_LESS_OR_EQUAL_TO_ZERO:    //fall-thru
	        case IF_LESS_OR_EQUAL:            operation = Operation.OP_ifle; break;
	        case IF_SAME_OBJECT:              //fall-thru
	        case IF_NULL:                     operation = Operation.OP_ifstricteq; break;
	        case IF_NOT_SAME_OBJECT:          //fall-thru
	        case IF_NOT_NULL:                 operation = Operation.OP_ifstrictne; break;
	        default: return;
		}
		
        avm2Code.append( operation, label.hashCode() );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#label(org.javaswf.j2avm.model.code.CodeLabel) */
	@Override
	public void label( CodeLabel label ) {

		avm2Code.appendLabel( label.hashCode() );

        //if the previous instruction was abrupt - such as a branch - then we need
        //to insert a label so that the AVM2 verifier does not think that this
        //code is unreachable (see "label" operation in AVM2 spec).
		Instruction last = current().prev();
        if( last != null && ! last.flowsToNext() ) {
            avm2Code.append( Operation.OP_label );
        }
	}

    /** @see org.javaswf.j2avm.model.code.InstructionListWalker#invokeSpecial(org.javaswf.j2avm.model.MethodDescriptor) */
	@Override
	public void invokeSpecial( MethodDescriptor methodDesc ) {
        int argCount = methodDesc.signature.paramTypes.length;
        Signature sig = methodDesc.signature;
        
        /* Constructor calls are either to the super constructor or to 
         * another constructor of this same class. 
         * Calls to constructors of other objects should have been refactored
         * into newObject operations previously.
         * Since AVM2 does not support constructor overloading there cannot
         * be a call to another constructor of this same class and a prior
         * translation step is required to refactor the constructor structure.
         */
        if( MethodModel.CONSTRUCTOR_NAME.equals( sig.name ) ) { //constructor call            
        	//call to the super-constructor
            avm2Code.append( Operation.OP_constructsuper, argCount );
                                                
        } else { //super call or private call
            
        	MethodModel m = context.methodFor( methodDesc );
            if( m.flags.contains( MethodFlag.MethodIsPrivate )) { //private

                Operation op = (m.returnType == VoidType.VOID) ?
                                   Operation.OP_callpropvoid :
                                   Operation.OP_callproperty;
                 
                avm2Code.append( op, nameForPrivateMethod( methodDesc.signature.name ), argCount );
                
            }
            else { //super call
                Operation op = (m.returnType == VoidType.VOID) ?
                        Operation.OP_callsupervoid :
                        Operation.OP_callsuper;

                //TODO: 
                
                avm2Code.append( op, nameForPrivateMethod( methodDesc.signature.name ), argCount );
            }
            
            //TODO:
        }
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#invokeStatic(org.javaswf.j2avm.model.MethodDescriptor) */
	@Override
	public void invokeStatic( MethodDescriptor methodDesc ) {
		//TODO: implement static methods
		throw new RuntimeException( "Static method invocation is yet to be implemented" );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#invokeVirtual(org.javaswf.j2avm.model.MethodDescriptor) */
	@Override
	public void invokeVirtual( MethodDescriptor methodDesc ) {
        int argCount = methodDesc.signature.paramTypes.length;
        
        Operation op = 
        	(context.methodFor( methodDesc ).returnType == VoidType.VOID) ?
                Operation.OP_callpropvoid :
                Operation.OP_callproperty;
        
        avm2Code.append( op, nameForMethod( methodDesc.signature.name ), argCount );        
	}

    /** @see org.javaswf.j2avm.model.code.InstructionListWalker#newObject(ObjectType, ValueType...) */
	@Override
	public void newObject( ObjectType type, ValueType...paramTypes ) {
		//TODO: this needs to be coordinated with the constructor call
        AVM2QName typeName = qnameForJavaType( type, abc );

        avm2Code.append( Operation.OP_findpropstrict, typeName );
        avm2Code.append( Operation.OP_constructprop , typeName, 0 );
        avm2Code.append( Operation.OP_coerce        , typeName );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#pushVar(int, org.javaswf.j2avm.model.types.ValueType) */
	@Override
	public void pushVar( int varIndex, ValueType type ) {
		avm2Code.loadLocalVar( varIndex );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#storeVar(int, org.javaswf.j2avm.model.types.ValueType) */
	@Override
	public void storeVar( int varIndex, ValueType type ) {
		avm2Code.storeLocalVar( varIndex );
	}
}