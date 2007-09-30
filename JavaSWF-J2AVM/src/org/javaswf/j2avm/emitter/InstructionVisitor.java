package org.javaswf.j2avm.emitter;

import static org.javaswf.j2avm.emitter.EmitterUtils.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.javaswf.j2avm.abc.TranslatedABC;
import org.javaswf.j2avm.abc.TranslatedClass;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.code.BranchType;
import org.javaswf.j2avm.model.code.CodeLabel;
import org.javaswf.j2avm.model.code.Instruction;
import org.javaswf.j2avm.model.code.InstructionListWalker;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;
import org.javaswf.j2avm.model.types.VoidType;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * Visitor for instructions for a single method
 *
 * @author nickmain
 */
public class InstructionVisitor extends InstructionListWalker {

    private final AVM2Code        avm2Code;
    private final TranslatedABC   abc;
    private final TranslatedClass avm2Class;

    /**
     * @param avm2code the target AVM2 method body
     * @param abc the target ABC file
     * @param avm2Class the target AVM2 class
     */
    InstructionVisitor( AVM2Code avm2code, TranslatedABC abc, TranslatedClass avm2Class ) {
        this.avm2Code  = avm2code;
        this.abc       = abc;
        this.avm2Class = avm2Class;
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


    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitInsn(int)
     */
    @Override
    public void visitInsn(int opcode) {
        switch( opcode ) {
                            
                
                
            case Opcodes.I2D: avm2Code.append( Operation.OP_convert_d ); return;
                
            case Opcodes.F2D: //fall-thru
            case Opcodes.L2D:
                //floats and longs are already doubles - do nothing
                return;
            
            case Opcodes.IADD: avm2Code.append( Operation.OP_add_i ); return;
            case Opcodes.IMUL: avm2Code.append( Operation.OP_multiply_i ); return;
            
            case Opcodes.I2L: avm2Code.append( Operation.OP_convert_d ); return; //longs are represented by doubles
            
            case Opcodes.D2L: //TODO: how to truncate a double ? 
            case Opcodes.F2L: //TODO: 

            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }
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

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#addInt() */
	@Override
	public void addInt() {
		avm2Code.append( Operation.OP_add_i );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#multInt() */
	@Override
	public void multInt() {
		avm2Code.append( Operation.OP_multiply_i );
	}

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#subInt() */
	@Override
	public void subInt() {
		avm2Code.append( Operation.OP_subtract_i );
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

	/** @see org.javaswf.j2avm.model.code.InstructionListWalker#arrayLength() */
	@Override
	public void arrayLength() {
        avm2Code.append( Operation.OP_getproperty, nameForField( "length" )); 
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

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitMethodInsn(int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        
        int argCount = getArgCount( desc );        
        
        switch( opcode ) {
            case Opcodes.INVOKEINTERFACE: //fall-thru
            case Opcodes.INVOKEVIRTUAL: {
                                
                Operation op = isVoidMethod( desc ) ?
                                   Operation.OP_callpropvoid :
                                   Operation.OP_callproperty;
                
                avm2Code.append( op, nameForMethod( name ), argCount );
                                
                return;
            }
                
            case Opcodes.INVOKESPECIAL: {
                if( "<init>".equals( name ) ) { //constructor call
                    
                    String className = externalName( owner );
                    if( className.equals( javaClass.superclass.clazz.getName())) {                    
                        avm2Code.append( Operation.OP_constructsuper, argCount );
                    }
                    else {
                        //call to a constructor that is not the superclass.
                        
                        //TODO: for now skip this and just pop the object
                        avm2Code.append( Operation.OP_pop );                        
                    }
                                        
                } else { //super call or private call
                    
                    Method m = getMethod( javaClass, owner, name, desc ); 
                    if( Modifier.isPrivate( m.getModifiers() ) ) { //private

                        Operation op = isVoidMethod( desc ) ?
                                           Operation.OP_callpropvoid :
                                           Operation.OP_callproperty;
                         
                        avm2Code.append( op, nameForPrivateMethod( name ), argCount );
                        
                    }
                    else { //super call
                        
                    }
                    
                    //TODO:
                }
                return;
            }

            //TODO:
            case Opcodes.INVOKESTATIC:
            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }
        
    }

    /** @see org.javaswf.j2avm.model.code.InstructionListWalker#newObject(org.javaswf.j2avm.model.types.ObjectType) */
	@Override
	public void newObject( ObjectType type ) {
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