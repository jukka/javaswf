package org.javaswf.j2avm.model.code.instruction;

import static org.javaswf.j2avm.model.code.instruction.OperationArgument.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JVM operations
 *
 * @author nickmain
 */
public enum Operation {

    NOP             ( 0x00 ),             
    ACONST_NULL     ( 0x01 ),     
    ICONST_M1       ( 0x02 ),       
    ICONST_0        ( 0x03 ),        
    ICONST_1        ( 0x04 ),        
    ICONST_2        ( 0x05 ),        
    ICONST_3        ( 0x06 ),        
    ICONST_4        ( 0x07 ),        
    ICONST_5        ( 0x08 ),        
    LCONST_0        ( 0x09 ),        
    LCONST_1        ( 0x0a ),        
    FCONST_0        ( 0x0b ),        
    FCONST_1        ( 0x0c ),        
    FCONST_2        ( 0x0d ),        
    DCONST_0        ( 0x0e ),        
    DCONST_1        ( 0x0f ),        
    BIPUSH          ( 0x10, BYTE_VALUE ),          
    SIPUSH          ( 0x11, SHORT_VALUE ),          
    LDC             ( 0x12, CONSTANT_INDEX ),             
    LDC_W           ( 0x13, WIDE_CONSTANT_INDEX ),           
    LDC2_W          ( 0x14, WIDE_CONSTANT_INDEX ),          
    ILOAD           ( 0x15, VAR_INDEX ),           
    LLOAD           ( 0x16, VAR_INDEX ),           
    FLOAD           ( 0x17, VAR_INDEX ),           
    DLOAD           ( 0x18, VAR_INDEX ),           
    ALOAD           ( 0x19, VAR_INDEX ),           
    ILOAD_0         ( 0x1a ),         
    ILOAD_1         ( 0x1b ),         
    ILOAD_2         ( 0x1c ),         
    ILOAD_3         ( 0x1d ),         
    LLOAD_0         ( 0x1e ),         
    LLOAD_1         ( 0x1f ),         
    LLOAD_2         ( 0x20 ),         
    LLOAD_3         ( 0x21 ),         
    FLOAD_0         ( 0x22 ),         
    FLOAD_1         ( 0x23 ),         
    FLOAD_2         ( 0x24 ),         
    FLOAD_3         ( 0x25 ),         
    DLOAD_0         ( 0x26 ),         
    DLOAD_1         ( 0x27 ),         
    DLOAD_2         ( 0x28 ),         
    DLOAD_3         ( 0x29 ),         
    ALOAD_0         ( 0x2a ),         
    ALOAD_1         ( 0x2b ),         
    ALOAD_2         ( 0x2c ),         
    ALOAD_3         ( 0x2d ),         
    IALOAD          ( 0x2e ),          
    LALOAD          ( 0x2f ),          
    FALOAD          ( 0x30 ),          
    DALOAD          ( 0x31 ),          
    AALOAD          ( 0x32 ),          
    BALOAD          ( 0x33 ),          
    CALOAD          ( 0x34 ),          
    SALOAD          ( 0x35 ),  
    ISTORE          ( 0x36, VAR_INDEX ),          
    LSTORE          ( 0x37, VAR_INDEX ),          
    FSTORE          ( 0x38, VAR_INDEX ),          
    DSTORE          ( 0x39, VAR_INDEX ),          
    ASTORE          ( 0x3a, VAR_INDEX ),          
    ISTORE_0        ( 0x3b ),        
    ISTORE_1        ( 0x3c ),        
    ISTORE_2        ( 0x3d ),        
    ISTORE_3        ( 0x3e ),        
    LSTORE_0        ( 0x3f ),        
    LSTORE_1        ( 0x40 ),        
    LSTORE_2        ( 0x41 ),        
    LSTORE_3        ( 0x42 ),        
    FSTORE_0        ( 0x43 ),        
    FSTORE_1        ( 0x44 ),        
    FSTORE_2        ( 0x45 ),        
    FSTORE_3        ( 0x46 ),        
    DSTORE_0        ( 0x47 ),        
    DSTORE_1        ( 0x48 ),        
    DSTORE_2        ( 0x49 ),        
    DSTORE_3        ( 0x4a ),        
    ASTORE_0        ( 0x4b ),        
    ASTORE_1        ( 0x4c ),        
    ASTORE_2        ( 0x4d ),        
    ASTORE_3        ( 0x4e ),        
    IASTORE         ( 0x4f ),         
    LASTORE         ( 0x50 ),         
    FASTORE         ( 0x51 ),         
    DASTORE         ( 0x52 ),         
    AASTORE         ( 0x53 ),         
    BASTORE         ( 0x54 ),         
    CASTORE         ( 0x55 ),         
    SASTORE         ( 0x56 ),         
    POP             ( 0x57 ),             
    POP2            ( 0x58 ),            
    DUP             ( 0x59 ),             
    DUP_X1          ( 0x5a ),          
    DUP_X2          ( 0x5b ),          
    DUP2            ( 0x5c ),            
    DUP2_X1         ( 0x5d ),         
    DUP2_X2         ( 0x5e ),         
    SWAP            ( 0x5f ),            
    IADD            ( 0x60 ),            
    LADD            ( 0x61 ),            
    FADD            ( 0x62 ),            
    DADD            ( 0x63 ),            
    ISUB            ( 0x64 ),            
    LSUB            ( 0x65 ),            
    FSUB            ( 0x66 ),            
    DSUB            ( 0x67 ),            
    IMUL            ( 0x68 ),            
    LMUL            ( 0x69 ),            
    FMUL            ( 0x6a ),            
    DMUL            ( 0x6b ),            
    IDIV            ( 0x6c ),            
    LDIV            ( 0x6d ),            
    FDIV            ( 0x6e ),            
    DDIV            ( 0x6f ),            
    IREM            ( 0x70 ),            
    LREM            ( 0x71 ),            
    FREM            ( 0x72 ),            
    DREM            ( 0x73 ),            
    INEG            ( 0x74 ),            
    LNEG            ( 0x75 ),            
    FNEG            ( 0x76 ),            
    DNEG            ( 0x77 ),            
    ISHL            ( 0x78 ),            
    LSHL            ( 0x79 ),            
    ISHR            ( 0x7a ),            
    LSHR            ( 0x7b ),            
    IUSHR           ( 0x7c ),           
    LUSHR           ( 0x7d ),           
    IAND            ( 0x7e ),            
    LAND            ( 0x7f ),            
    IOR             ( 0x80 ),             
    LOR             ( 0x81 ),             
    IXOR            ( 0x82 ),            
    LXOR            ( 0x83 ),            
    IINC            ( 0x84, VAR_INDEX, BYTE_VALUE ),            
    I2L             ( 0x85 ),             
    I2F             ( 0x86 ),             
    I2D             ( 0x87 ),             
    L2I             ( 0x88 ),             
    L2F             ( 0x89 ),             
    L2D             ( 0x8a ),             
    F2I             ( 0x8b ),             
    F2L             ( 0x8c ),             
    F2D             ( 0x8d ),             
    D2I             ( 0x8e ),             
    D2L             ( 0x8f ),             
    D2F             ( 0x90 ),             
    I2B             ( 0x91 ),             
    I2C             ( 0x92 ),             
    I2S             ( 0x93 ),             
    LCMP            ( 0x94 ),            
    FCMPL           ( 0x95 ),           
    FCMPG           ( 0x96 ),           
    DCMPL           ( 0x97 ),           
    DCMPG           ( 0x98 ),           
    IFEQ            ( 0x99, OFFSET ),                
    IFNE            ( 0x9a, OFFSET ),            
    IFLT            ( 0x9b, OFFSET ),            
    IFGE            ( 0x9c, OFFSET ),            
    IFGT            ( 0x9d, OFFSET ),            
    IFLE            ( 0x9e, OFFSET ),            
    IF_ICMPEQ       ( 0x9f, OFFSET ),       
    IF_ICMPNE       ( 0xa0, OFFSET ),       
    IF_ICMPLT       ( 0xa1, OFFSET ),       
    IF_ICMPGE       ( 0xa2, OFFSET ),       
    IF_ICMPGT       ( 0xa3, OFFSET ),       
    IF_ICMPLE       ( 0xa4, OFFSET ),       
    IF_ACMPEQ       ( 0xa5, OFFSET ),       
    IF_ACMPNE       ( 0xa6, OFFSET ),       
    GOTO            ( 0xa7, OFFSET ),                
    JSR             ( 0xa8, OFFSET ),
    RET             ( 0xa9, VAR_INDEX ),
    TABLESWITCH     ( 0xaa, ALIGN32, OFFSET_TABLE ),    
    LOOKUPSWITCH    ( 0xab, ALIGN32, OFFSET_LOOKUP ),
    IRETURN         ( 0xac ),         
    LRETURN         ( 0xad ),         
    FRETURN         ( 0xae ),         
    DRETURN         ( 0xaf ),         
    ARETURN         ( 0xb0 ),         
    RETURN          ( 0xb1 ),          
    GETSTATIC       ( 0xb2, FIELD_INDEX ),       
    PUTSTATIC       ( 0xb3, FIELD_INDEX ),       
    GETFIELD        ( 0xb4, FIELD_INDEX ),        
    PUTFIELD        ( 0xb5, FIELD_INDEX ),        
    INVOKEVIRTUAL   ( 0xb6, METHOD_INDEX ),   
    INVOKESPECIAL   ( 0xb7, METHOD_INDEX ),   
    INVOKESTATIC    ( 0xb8, METHOD_INDEX ),    
    INVOKEINTERFACE ( 0xb9, METHOD_INDEX, ARGS_SIZE, ZERO_BYTE ),                          
    NEW             ( 0xbb, CLASS_INDEX ),             
    NEWARRAY        ( 0xbc, ARRAY_TYPE ),        
    ANEWARRAY       ( 0xbd, CLASS_INDEX ),       
    ARRAYLENGTH     ( 0xbe ),     
    ATHROW          ( 0xbf ),          
    CHECKCAST       ( 0xc0, CLASS_INDEX ),       
    INSTANCEOF      ( 0xc1, CLASS_INDEX ),      
    MONITORENTER    ( 0xc2 ),    
    MONITOREXIT     ( 0xc3 ),
    WIDE            ( 0xc4 ),            
    MULTIANEWARRAY  ( 0xc5, CLASS_INDEX, DIMENSION_COUNT ),  
    IFNULL          ( 0xc6, OFFSET ),          
    IFNONNULL       ( 0xc7, OFFSET ),       
    GOTO_W          ( 0xc8, OFFSET32 ),          
    JSR_W           ( 0xc9, OFFSET32 );
    
    /** The opcode */
    public final int opcode;
    
    /** The argument types - read-only */
    public final List<OperationArgument> arguments;
    
    private Operation( int opcode, OperationArgument...arguments ) {
        this.opcode = opcode;
        
        List<OperationArgument> args;
        if( arguments.length == 0 ) {
            args = Collections.emptyList();
        } else {
            args = new ArrayList<OperationArgument>();
            args.addAll( Arrays.asList( arguments ));
        }
        
        this.arguments = Collections.unmodifiableList( args );
    }
    
    /**
     * Get an operation from an opcode
     * @return null if the opcode is not valid
     */
    public static Operation fromOpcode( int opcode ) {
        return operations.get( opcode );
    }
    
    /** Map of opcode to Operation */
    private static Map<Integer, Operation> operations;
    static {
        Map<Integer,Operation> ops = new HashMap<Integer, Operation>();
        operations = Collections.unmodifiableMap( ops );
        
        for( Operation op : values() ) {
            ops.put( op.opcode, op );
        }
    }
}
