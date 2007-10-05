package org.javaswf.j2avm.model.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.javaswf.j2avm.model.code.BinOpType.*;
import static org.javaswf.j2avm.model.code.UnaryOpType.*;
import org.epistem.io.CountingDataInput;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.code.BranchType;
import org.javaswf.j2avm.model.code.CodeLabel;
import org.javaswf.j2avm.model.code.Instruction;
import org.javaswf.j2avm.model.code.InstructionList;
import org.javaswf.j2avm.model.code.Instructions;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;
import org.javaswf.j2avm.model.types.VoidType;

/**
 * Handles conversion from low-level JVM operations to abstracted instructions
 *  
 * @author dmain
 */
public class OperationConvertor {
	
	//map from Operation to the method that handles it
	private final static Map<Operation, Method> handlers =
		new EnumMap<Operation, Method>( Operation.class );
	static {
		Method[] meths = OperationConvertor.class.getMethods();
		for( Method m : meths ) {
			if( m.getName().startsWith( "handle_" ) ) {
				String opName = m.getName().substring( 7 );
				Operation op = Operation.valueOf( opName );
				handlers.put( op, m );
			}
		}
	}
	
	public final Map<Integer, Instruction> offsets = new HashMap<Integer, Instruction>();
	private final Instructions instructions;
	private final InstructionList list;
	private final ConstantPool cpool;
	private final CountingDataInput in; 
	private final int codeSize;
	private int offset;
	
	/**
	 * @param instructions the instructions to write to
	 */
	public OperationConvertor( InstructionList list, ConstantPool cpool,
			                   byte[] bytecode ) {
		this.instructions = list.cursorAtStart();		
		this.cpool        = cpool;
		this.codeSize     = bytecode.length;
		this.list         = list;
		
		in = new CountingDataInput( 
                new DataInputStream( 
                    new ByteArrayInputStream( bytecode )));
	}
	
	/**
	 * Perform the conversion
	 */
	public void convert() throws IOException {
        while( in.count < codeSize ) {        	
            parse();            
            
            //associate offset with instruction
            Instruction insn = list.last();
                        
            //label already at offset
            Instruction labelInsn = offsets.get( offset ); 
            if( labelInsn != null && labelInsn instanceof CodeLabel ) {
            	CodeLabel label = (CodeLabel) labelInsn;            	
            	label.labelInstruction( insn );
            }
            else {
                offsets.put( offset, insn );            	
            }            
        }
	}
	
    /**
     * Parse an instruction
     */
    private void parse() throws IOException {
        offset = in.count;        
        parse( false );
    }
    
    /**
     * Parse an instruction - recursive for WIDE instructions
     */
    private void parse( boolean isWide ) throws IOException {
        
        int opcode = in.readUnsignedByte();
        Operation op = Operation.fromOpcode( opcode );
        if( op == null ) throw new IOException( "Unknown opcode 0x" + Integer.toHexString( opcode ) );
        
        //wide instructions
        if( op == Operation.WIDE ) {
        	parse( true );
        	return;
        }
        
        List<OperationArgument> argTypes = op.arguments;
        
        Object[] args = new Object[ argTypes.size() ];
        for (int i = 0; i < args.length; i++) {
            args[i] = argTypes.get(i).parse( offset, isWide, cpool, in );
        }

        Method meth = handlers.get( op );
        try {
        	meth.invoke( this, args );
        } catch( Exception ex ) {
        	throw new RuntimeException( ex );
        }
    }
	
    /**
     * Get the label at the given offset.
     */
    public CodeLabel labelAtOffset( int offset ) {
    	Instruction insn = offsets.get( offset );
    	
    	//make a label before the offset has been reached yet
    	if( insn == null ) {
    		CodeLabel label = new CodeLabel( "" + offset );
    		offsets.put( offset, label );
    		return label;
    	}
    	
    	if( insn instanceof CodeLabel ) return (CodeLabel) insn;
    	
    	//label an existing instruction
    	CodeLabel label = list.label( insn, "" + offset );
    	if( insn != label ) offsets.put( offset, label );
    	
    	return label;
    }
    
    public void handle_NOP             ( ) { instructions.nop(); }             
    public void handle_ACONST_NULL     ( ) { instructions.pushNull(); }     
    public void handle_ICONST_M1       ( ) { instructions.pushInt( -1 ); }       
    public void handle_ICONST_0        ( ) { instructions.pushInt( 0 ); }        
    public void handle_ICONST_1        ( ) { instructions.pushInt( 1 ); }        
    public void handle_ICONST_2        ( ) { instructions.pushInt( 2 ); }        
    public void handle_ICONST_3        ( ) { instructions.pushInt( 3 ); }        
    public void handle_ICONST_4        ( ) { instructions.pushInt( 4 ); }        
    public void handle_ICONST_5        ( ) { instructions.pushInt( 5 ); }        
    public void handle_LCONST_0        ( ) { instructions.pushLong( 0L ); }        
    public void handle_LCONST_1        ( ) { instructions.pushLong( 1L ); }        
    public void handle_FCONST_0        ( ) { instructions.pushFloat( 0f ); }        
    public void handle_FCONST_1        ( ) { instructions.pushFloat( 1f ); }        
    public void handle_FCONST_2        ( ) { instructions.pushFloat( 2f ); }        
    public void handle_DCONST_0        ( ) { instructions.pushDouble( 0.0 ); }        
    public void handle_DCONST_1        ( ) { instructions.pushDouble( 1.0 ); }        
    public void handle_BIPUSH          ( int value ) { instructions.pushInt( value ); }          
    public void handle_SIPUSH          ( int value ) { instructions.pushInt( value ); }          
    public void handle_LDC             ( Object value ) { pushConst( value ); }             
    public void handle_LDC_W           ( Object value ) { pushConst( value ); }           
    public void handle_LDC2_W          ( Object value ) { pushConst( value ); }          
    public void handle_ILOAD           ( int index ) { instructions.pushVar( index, PrimitiveType.INT ); }           
    public void handle_LLOAD           ( int index ) { instructions.pushVar( index, PrimitiveType.LONG ); }           
    public void handle_FLOAD           ( int index ) { instructions.pushVar( index, PrimitiveType.FLOAT ); }           
    public void handle_DLOAD           ( int index ) { instructions.pushVar( index, PrimitiveType.DOUBLE ); }           
    public void handle_ALOAD           ( int index ) { instructions.pushVar( index, ObjectType.OBJECT ); }           
    public void handle_ILOAD_0         ( ) { instructions.pushVar( 0, PrimitiveType.INT ); }         
    public void handle_ILOAD_1         ( ) { instructions.pushVar( 1, PrimitiveType.INT ); }         
    public void handle_ILOAD_2         ( ) { instructions.pushVar( 2, PrimitiveType.INT ); }         
    public void handle_ILOAD_3         ( ) { instructions.pushVar( 3, PrimitiveType.INT ); }         
    public void handle_LLOAD_0         ( ) { instructions.pushVar( 0, PrimitiveType.LONG ); }         
    public void handle_LLOAD_1         ( ) { instructions.pushVar( 1, PrimitiveType.LONG ); }         
    public void handle_LLOAD_2         ( ) { instructions.pushVar( 2, PrimitiveType.LONG ); }         
    public void handle_LLOAD_3         ( ) { instructions.pushVar( 3, PrimitiveType.LONG ); }         
    public void handle_FLOAD_0         ( ) { instructions.pushVar( 0, PrimitiveType.FLOAT ); }         
    public void handle_FLOAD_1         ( ) { instructions.pushVar( 1, PrimitiveType.FLOAT ); }         
    public void handle_FLOAD_2         ( ) { instructions.pushVar( 2, PrimitiveType.FLOAT ); }         
    public void handle_FLOAD_3         ( ) { instructions.pushVar( 3, PrimitiveType.FLOAT ); }         
    public void handle_DLOAD_0         ( ) { instructions.pushVar( 0, PrimitiveType.DOUBLE ); }         
    public void handle_DLOAD_1         ( ) { instructions.pushVar( 1, PrimitiveType.DOUBLE ); }         
    public void handle_DLOAD_2         ( ) { instructions.pushVar( 2, PrimitiveType.DOUBLE ); }         
    public void handle_DLOAD_3         ( ) { instructions.pushVar( 3, PrimitiveType.DOUBLE ); }         
    public void handle_ALOAD_0         ( ) { instructions.pushVar( 0, ObjectType.OBJECT ); }         
    public void handle_ALOAD_1         ( ) { instructions.pushVar( 1, ObjectType.OBJECT ); }         
    public void handle_ALOAD_2         ( ) { instructions.pushVar( 2, ObjectType.OBJECT ); }         
    public void handle_ALOAD_3         ( ) { instructions.pushVar( 3, ObjectType.OBJECT ); }         
    public void handle_IALOAD          ( ) { instructions.pushElement( PrimitiveType.INT ); }          
    public void handle_LALOAD          ( ) { instructions.pushElement( PrimitiveType.LONG ); }          
    public void handle_FALOAD          ( ) { instructions.pushElement( PrimitiveType.FLOAT ); }          
    public void handle_DALOAD          ( ) { instructions.pushElement( PrimitiveType.DOUBLE ); }          
    public void handle_AALOAD          ( ) { instructions.pushElement( ObjectType.OBJECT ); }          
    public void handle_BALOAD          ( ) { instructions.pushElement( PrimitiveType.BYTE ); }          
    public void handle_CALOAD          ( ) { instructions.pushElement( PrimitiveType.CHAR ); }          
    public void handle_SALOAD          ( ) { instructions.pushElement( PrimitiveType.SHORT ); }  
    public void handle_ISTORE          ( int index ) { instructions.storeVar( index , PrimitiveType.INT ); }          
    public void handle_LSTORE          ( int index ) { instructions.storeVar( index , PrimitiveType.LONG ); }          
    public void handle_FSTORE          ( int index ) { instructions.storeVar( index , PrimitiveType.FLOAT ); }          
    public void handle_DSTORE          ( int index ) { instructions.storeVar( index , PrimitiveType.DOUBLE ); }          
    public void handle_ASTORE          ( int index ) { instructions.storeVar( index , ObjectType.OBJECT ); }              
    public void handle_ISTORE_0        ( ) { instructions.storeVar( 0, PrimitiveType.INT ); }         
    public void handle_ISTORE_1        ( ) { instructions.storeVar( 1, PrimitiveType.INT ); }         
    public void handle_ISTORE_2        ( ) { instructions.storeVar( 2, PrimitiveType.INT ); }         
    public void handle_ISTORE_3        ( ) { instructions.storeVar( 3, PrimitiveType.INT ); }         
    public void handle_LSTORE_0        ( ) { instructions.storeVar( 0, PrimitiveType.LONG ); }         
    public void handle_LSTORE_1        ( ) { instructions.storeVar( 1, PrimitiveType.LONG ); }         
    public void handle_LSTORE_2        ( ) { instructions.storeVar( 2, PrimitiveType.LONG ); }         
    public void handle_LSTORE_3        ( ) { instructions.storeVar( 3, PrimitiveType.LONG ); }         
    public void handle_FSTORE_0        ( ) { instructions.storeVar( 0, PrimitiveType.FLOAT ); }         
    public void handle_FSTORE_1        ( ) { instructions.storeVar( 1, PrimitiveType.FLOAT ); }         
    public void handle_FSTORE_2        ( ) { instructions.storeVar( 2, PrimitiveType.FLOAT ); }         
    public void handle_FSTORE_3        ( ) { instructions.storeVar( 3, PrimitiveType.FLOAT ); }         
    public void handle_DSTORE_0        ( ) { instructions.storeVar( 0, PrimitiveType.DOUBLE ); }        
    public void handle_DSTORE_1        ( ) { instructions.storeVar( 1, PrimitiveType.DOUBLE ); }        
    public void handle_DSTORE_2        ( ) { instructions.storeVar( 2, PrimitiveType.DOUBLE ); }        
    public void handle_DSTORE_3        ( ) { instructions.storeVar( 3, PrimitiveType.DOUBLE ); }        
    public void handle_ASTORE_0        ( ) { instructions.storeVar( 0, ObjectType.OBJECT ); }         
    public void handle_ASTORE_1        ( ) { instructions.storeVar( 1, ObjectType.OBJECT ); }         
    public void handle_ASTORE_2        ( ) { instructions.storeVar( 2, ObjectType.OBJECT ); }         
    public void handle_ASTORE_3        ( ) { instructions.storeVar( 3, ObjectType.OBJECT ); }     
    public void handle_IASTORE         ( ) { instructions.storeElement( PrimitiveType.INT ); }         
    public void handle_LASTORE         ( ) { instructions.storeElement( PrimitiveType.LONG ); }         
    public void handle_FASTORE         ( ) { instructions.storeElement( PrimitiveType.FLOAT ); }         
    public void handle_DASTORE         ( ) { instructions.storeElement( PrimitiveType.DOUBLE ); }         
    public void handle_AASTORE         ( ) { instructions.storeElement( ObjectType.OBJECT ); }         
    public void handle_BASTORE         ( ) { instructions.storeElement( PrimitiveType.BYTE ); }         
    public void handle_CASTORE         ( ) { instructions.storeElement( PrimitiveType.CHAR ); }         
    public void handle_SASTORE         ( ) { instructions.storeElement( PrimitiveType.SHORT ); }         
    public void handle_POP             ( ) { instructions.pop( 1 ); }
    public void handle_POP2            ( ) { instructions.pop( 2 ); }         
    public void handle_DUP             ( ) { instructions.dup( 1, 0 ); }
    public void handle_DUP_X1          ( ) { instructions.dup( 1, 1 ); }
    public void handle_DUP_X2          ( ) { instructions.dup( 1, 2 ); }
    public void handle_DUP2            ( ) { instructions.dup( 2, 0 ); }
    public void handle_DUP2_X1         ( ) { instructions.dup( 2, 1 ); }
    public void handle_DUP2_X2         ( ) { instructions.dup( 2, 2 ); }         
    public void handle_SWAP            ( ) { instructions.swap(); }
    public void handle_IADD            ( ) { instructions.binaryOp( ADD, PrimitiveType.INT ); }
    public void handle_LADD            ( ) { instructions.binaryOp( ADD, PrimitiveType.LONG ); }
    public void handle_FADD            ( ) { instructions.binaryOp( ADD, PrimitiveType.FLOAT ); }
    public void handle_DADD            ( ) { instructions.binaryOp( ADD, PrimitiveType.DOUBLE ); }
    public void handle_ISUB            ( ) { instructions.binaryOp( SUBTRACT, PrimitiveType.INT ); }
    public void handle_LSUB            ( ) { instructions.binaryOp( SUBTRACT, PrimitiveType.LONG ); }
    public void handle_FSUB            ( ) { instructions.binaryOp( SUBTRACT, PrimitiveType.FLOAT ); }
    public void handle_DSUB            ( ) { instructions.binaryOp( SUBTRACT, PrimitiveType.DOUBLE ); }            
    public void handle_IMUL            ( ) { instructions.binaryOp( MULTIPLY, PrimitiveType.INT ); }            
    public void handle_LMUL            ( ) { instructions.binaryOp( MULTIPLY, PrimitiveType.LONG ); }            
    public void handle_FMUL            ( ) { instructions.binaryOp( MULTIPLY, PrimitiveType.FLOAT ); }            
    public void handle_DMUL            ( ) { instructions.binaryOp( MULTIPLY, PrimitiveType.DOUBLE ); }            
    public void handle_IDIV            ( ) { instructions.binaryOp( DIVIDE, PrimitiveType.INT ); }            
    public void handle_LDIV            ( ) { instructions.binaryOp( DIVIDE, PrimitiveType.LONG ); }            
    public void handle_FDIV            ( ) { instructions.binaryOp( DIVIDE, PrimitiveType.FLOAT ); }            
    public void handle_DDIV            ( ) { instructions.binaryOp( DIVIDE, PrimitiveType.DOUBLE ); }            
    public void handle_IREM            ( ) { instructions.binaryOp( REMAINDER, PrimitiveType.INT ); }            
    public void handle_LREM            ( ) { instructions.binaryOp( REMAINDER, PrimitiveType.LONG ); }            
    public void handle_FREM            ( ) { instructions.binaryOp( REMAINDER, PrimitiveType.FLOAT ); }            
    public void handle_DREM            ( ) { instructions.binaryOp( REMAINDER, PrimitiveType.DOUBLE ); }            
    public void handle_INEG            ( ) { instructions.unaryOp( NEGATE, PrimitiveType.INT ); }
    public void handle_LNEG            ( ) { instructions.unaryOp( NEGATE, PrimitiveType.LONG ); }            
    public void handle_FNEG            ( ) { instructions.unaryOp( NEGATE, PrimitiveType.FLOAT ); }            
    public void handle_DNEG            ( ) { instructions.unaryOp( NEGATE, PrimitiveType.DOUBLE ); }            
    public void handle_ISHL            ( ) { instructions.binaryOp( SHIFT_LEFT, PrimitiveType.INT ); }            
    public void handle_LSHL            ( ) { instructions.binaryOp( SHIFT_LEFT, PrimitiveType.LONG ); }            
    public void handle_ISHR            ( ) { instructions.binaryOp( SHIFT_RIGHT_SIGNED, PrimitiveType.INT ); }            
    public void handle_LSHR            ( ) { instructions.binaryOp( SHIFT_RIGHT_SIGNED, PrimitiveType.LONG ); }            
    public void handle_IUSHR           ( ) { instructions.binaryOp( SHIFT_RIGHT_UNSIGNED, PrimitiveType.INT ); }           
    public void handle_LUSHR           ( ) { instructions.binaryOp( SHIFT_RIGHT_UNSIGNED, PrimitiveType.LONG ); }           
    public void handle_IAND            ( ) { instructions.binaryOp( AND, PrimitiveType.INT ); }            
    public void handle_LAND            ( ) { instructions.binaryOp( AND, PrimitiveType.LONG ); }            
    public void handle_IOR             ( ) { instructions.binaryOp( OR, PrimitiveType.INT ); }             
    public void handle_LOR             ( ) { instructions.binaryOp( OR, PrimitiveType.LONG ); }             
    public void handle_IXOR            ( ) { instructions.binaryOp( XOR, PrimitiveType.INT ); }            
    public void handle_LXOR            ( ) { instructions.binaryOp( XOR, PrimitiveType.LONG ); }            
    public void handle_IINC            ( int index, int value ) { instructions.incrementVar( index, value ); }                
    public void handle_I2L             ( ) { instructions.convert( PrimitiveType.INT, PrimitiveType.LONG ); }             
    public void handle_I2F             ( ) { instructions.convert( PrimitiveType.INT, PrimitiveType.FLOAT ); }             
    public void handle_I2D             ( ) { instructions.convert( PrimitiveType.INT, PrimitiveType.DOUBLE ); }             
    public void handle_L2I             ( ) { instructions.convert( PrimitiveType.LONG, PrimitiveType.INT ); }             
    public void handle_L2F             ( ) { instructions.convert( PrimitiveType.LONG, PrimitiveType.FLOAT ); }             
    public void handle_L2D             ( ) { instructions.convert( PrimitiveType.LONG, PrimitiveType.DOUBLE ); }             
    public void handle_F2I             ( ) { instructions.convert( PrimitiveType.FLOAT, PrimitiveType.INT ); }             
    public void handle_F2L             ( ) { instructions.convert( PrimitiveType.FLOAT, PrimitiveType.LONG ); }             
    public void handle_F2D             ( ) { instructions.convert( PrimitiveType.FLOAT, PrimitiveType.DOUBLE ); }             
    public void handle_D2I             ( ) { instructions.convert( PrimitiveType.DOUBLE, PrimitiveType.INT ); }             
    public void handle_D2L             ( ) { instructions.convert( PrimitiveType.DOUBLE, PrimitiveType.LONG ); }             
    public void handle_D2F             ( ) { instructions.convert( PrimitiveType.DOUBLE, PrimitiveType.FLOAT ); }             
    public void handle_I2B             ( ) { instructions.convert( PrimitiveType.INT, PrimitiveType.BYTE ); }             
    public void handle_I2C             ( ) { instructions.convert( PrimitiveType.INT, PrimitiveType.CHAR ); }             
    public void handle_I2S             ( ) { instructions.convert( PrimitiveType.INT, PrimitiveType.SHORT ); }             
    public void handle_LCMP            ( ) { instructions.binaryOp( COMPARE,   PrimitiveType.INT ); }            
    public void handle_FCMPL           ( ) { instructions.binaryOp( COMPARE_L, PrimitiveType.INT ); }           
    public void handle_FCMPG           ( ) { instructions.binaryOp( COMPARE_G, PrimitiveType.INT ); }           
    public void handle_DCMPL           ( ) { instructions.binaryOp( COMPARE_L, PrimitiveType.INT ); }           
    public void handle_DCMPG           ( ) { instructions.binaryOp( COMPARE_G, PrimitiveType.INT ); }           
    public void handle_IFEQ            ( int offset ) { instructions.branch( BranchType.IF_EQUAL_TO_ZERO,            labelAtOffset( offset ) ); }            
    public void handle_IFNE            ( int offset ) { instructions.branch( BranchType.IF_NOT_EQUAL_TO_ZERO,        labelAtOffset( offset ) ); }            
    public void handle_IFLT            ( int offset ) { instructions.branch( BranchType.IF_LESS_OR_EQUAL_TO_ZERO,    labelAtOffset( offset ) ); }            
    public void handle_IFGE            ( int offset ) { instructions.branch( BranchType.IF_GREATER_OR_EQUAL_TO_ZERO, labelAtOffset( offset ) ); }            
    public void handle_IFGT            ( int offset ) { instructions.branch( BranchType.IF_GREATER_THAN_ZERO,        labelAtOffset( offset ) ); }            
    public void handle_IFLE            ( int offset ) { instructions.branch( BranchType.IF_LESS_OR_EQUAL_TO_ZERO,    labelAtOffset( offset ) ); }            
    public void handle_IF_ICMPEQ       ( int offset ) { instructions.branch( BranchType.IF_EQUAL,            labelAtOffset( offset ) ); }       
    public void handle_IF_ICMPNE       ( int offset ) { instructions.branch( BranchType.IF_NOT_EQUAL,        labelAtOffset( offset ) ); }       
    public void handle_IF_ICMPLT       ( int offset ) { instructions.branch( BranchType.IF_LESS_THAN,        labelAtOffset( offset ) ); }       
    public void handle_IF_ICMPGE       ( int offset ) { instructions.branch( BranchType.IF_GREATER_OR_EQUAL, labelAtOffset( offset ) ); }       
    public void handle_IF_ICMPGT       ( int offset ) { instructions.branch( BranchType.IF_GREATER_THAN,     labelAtOffset( offset ) ); }       
    public void handle_IF_ICMPLE       ( int offset ) { instructions.branch( BranchType.IF_LESS_OR_EQUAL,    labelAtOffset( offset ) ); }       
    public void handle_IF_ACMPEQ       ( int offset ) { instructions.branch( BranchType.IF_SAME_OBJECT,     labelAtOffset( offset ) ); }       
    public void handle_IF_ACMPNE       ( int offset ) { instructions.branch( BranchType.IF_NOT_SAME_OBJECT, labelAtOffset( offset ) ); }       
    public void handle_GOTO            ( int offset ) { instructions.branch( BranchType.UNCONDITIONAL, labelAtOffset( offset ) ); }            
    public void handle_JSR             ( int offset ) { throw new RuntimeException( "Invalid operation - JSR" ); }
    public void handle_RET             ( int index )  { throw new RuntimeException( "Invalid operation - RET" ); }    
    public void handle_TABLESWITCH     ( Object dummy, SwitchOffsets offsets ) { handleSwitch( offsets ); }     
    public void handle_LOOKUPSWITCH    ( Object dummy, SwitchOffsets offsets ) { handleSwitch( offsets ); }    
    public void handle_IRETURN         ( ) { instructions.methodReturn( PrimitiveType.INT ); }         
    public void handle_LRETURN         ( ) { instructions.methodReturn( PrimitiveType.LONG ); }         
    public void handle_FRETURN         ( ) { instructions.methodReturn( PrimitiveType.FLOAT ); }         
    public void handle_DRETURN         ( ) { instructions.methodReturn( PrimitiveType.DOUBLE ); }         
    public void handle_ARETURN         ( ) { instructions.methodReturn( ObjectType.OBJECT ); }         
    public void handle_RETURN          ( ) { instructions.methodReturn( VoidType.VOID ); }          
    public void handle_GETSTATIC       ( FieldDescriptor fieldDesc ) { instructions.pushStaticField( fieldDesc ); }       
    public void handle_PUTSTATIC       ( FieldDescriptor fieldDesc ) { instructions.storeStaticField( fieldDesc ); }       
    public void handle_GETFIELD        ( FieldDescriptor fieldDesc ) { instructions.pushField( fieldDesc ); }        
    public void handle_PUTFIELD        ( FieldDescriptor fieldDesc ) { instructions.storeField( fieldDesc ); }        
    public void handle_INVOKEVIRTUAL   ( MethodDescriptor methodDesc ) { instructions.invokeVirtual( methodDesc ); }   
    public void handle_INVOKESPECIAL   ( MethodDescriptor methodDesc ) { instructions.invokeSpecial( methodDesc ); }   
    public void handle_INVOKESTATIC    ( MethodDescriptor methodDesc ) { instructions.invokeStatic( methodDesc ); }    
    public void handle_INVOKEINTERFACE ( MethodDescriptor methodDesc, int argCount, Object dummy ) { instructions.invokeVirtual( methodDesc ); }                          
    public void handle_NEW             ( ObjectType type ) { instructions.newObject( type ); }             
    public void handle_NEWARRAY        ( ArrayType type ) { instructions.newArray( type ); }        
    public void handle_ANEWARRAY       ( ValueType type ) { instructions.newArray( new ArrayType( type, 1 )); }       
    public void handle_ARRAYLENGTH     ( ) { instructions.unaryOp( ARRAY_LENGTH, PrimitiveType.INT ); }     
    public void handle_ATHROW          ( ) { instructions.throwException(); }          
    public void handle_CHECKCAST       ( ObjectOrArrayType type ) { instructions.checkCast( type ); }       
    public void handle_INSTANCEOF      ( ObjectOrArrayType type ) { instructions.instanceOf( type ); }      
    public void handle_MONITORENTER    ( ) { instructions.monitorEnter(); }    
    public void handle_MONITOREXIT     ( ) { instructions.monitorExit(); }
    public void handle_WIDE            ( ) { throw new RuntimeException(); }            
    public void handle_IFNULL          ( int offset ) { instructions.branch( BranchType.IF_NULL,       labelAtOffset( offset ) ); }          
    public void handle_IFNONNULL       ( int offset ) { instructions.branch( BranchType.IF_NOT_NULL,   labelAtOffset( offset ) ); }       
    public void handle_GOTO_W          ( int offset ) { instructions.branch( BranchType.UNCONDITIONAL, labelAtOffset( offset ) ); }          
    public void handle_JSR_W           ( int offset ) { throw new RuntimeException( "Invalid operation - JSR" ); }
    
    public void handle_MULTIANEWARRAY  ( ArrayType type, int dimCount ) { 
    	if( type.dimensionCount == dimCount ) {
    		instructions.newArray( type );
    		return;
    	}
    	
    	//make an element type that reflects the uninitialized dimensions of
    	//the array
    	int uninitializedDimCount = type.dimensionCount - dimCount;    	
    	ArrayType elementType = new ArrayType( type.elementType, uninitializedDimCount );
    	
    	instructions.newArray( new ArrayType( elementType, dimCount ) );
    }  
    
    private void handleSwitch( SwitchOffsets offsets ) {
    	
    	Instructions.Case[] cases = new Instructions.Case[ offsets.cases.length ];
    	for( int i = 0; i < cases.length; i++ ) {    		
    		cases[i] = new Instructions.Case( offsets.cases[i],
    	                                      labelAtOffset( offsets.offsets[i] ));
		}
    	
    	instructions.switch_( labelAtOffset( offsets.defaultOffset ), cases );
    }
    
    private void pushConst( Object value ) {
    	if( value instanceof Integer ) {
    		instructions.pushInt( ((Integer) value).intValue() );
    	}
    	else if( value instanceof Long ) {
    		instructions.pushLong( ((Long) value).longValue() );    		
    	}
    	else if( value instanceof Float ) {
    		instructions.pushFloat( ((Float) value).floatValue() );    		
    	}
    	else if( value instanceof Double ) {
    		instructions.pushDouble( ((Double) value).doubleValue() );    		
    	}
    	else if( value instanceof String ) {
    		instructions.pushString( (String) value );    		
    	}
    	else if( value instanceof JavaType ) {
    		instructions.pushClass( (JavaType) value );
    	}
    }
    
    
}
