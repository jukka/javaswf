package org.javaswf.j2avm.model.parser;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.Signature;
import org.javaswf.j2avm.model.types.ValueType;


/**
 * Types of operation argument
 *
 * @author nickmain
 */
public enum OperationArgument {

    BYTE_VALUE,
    SHORT_VALUE,
    CONSTANT_INDEX,
    WIDE_CONSTANT_INDEX,
    VAR_INDEX,
    OFFSET,
    OFFSET32,
    ALIGN32,
    OFFSET_TABLE,
    OFFSET_LOOKUP,
    FIELD_INDEX,
    METHOD_INDEX,
    CLASS_INDEX,
    ARRAY_TYPE,
    DIMENSION_COUNT,
    ARGS_SIZE,
    ZERO_BYTE;
    
    /**
     * Parse an argument of this type.
     * 
     * @param offset the offset of the current instruction
     * @param isWide whether the instruction is wide
     * @param cpool the constant pool
     * @param in the class file binary source
     * @return the argument
     */
    public Object parse( int offset, boolean isWide, 
                         ConstantPool cpool, DataInput in ) 
        throws IOException {
        
        switch( this ) {
                    
            case CONSTANT_INDEX:  
            case WIDE_CONSTANT_INDEX: { 
                int index = (this == CONSTANT_INDEX) ? 
                                in.readUnsignedByte() :
                                in.readUnsignedShort();
                                
                return cpool.getConstant( index );
            }
                
            case VAR_INDEX: return isWide ? in.readUnsignedShort() : in.readUnsignedByte();
                
            case OFFSET:   return offset + (int) in.readShort();
            case OFFSET32: return offset + in.readInt();
            
            case ALIGN32: { 
                offset++; 
                while( offset % 4 != 0 ) { //skip to next dword boundary
                    in.readByte();  
                    offset++;
                }
                return null;                
            }
                
            case OFFSET_TABLE: {
                int defaultAddr = offset + in.readInt();
                int lowIndex    = in.readInt();
                int highIndex   = in.readInt();
                
                int[] targets = new int[ highIndex - lowIndex + 1 ];
                int[] cases   = new int[ targets.length ]; 
                for (int i = 0; i < targets.length; i++) {
                    targets[i] = offset + in.readInt();
                    cases  [i] = lowIndex++;
                }
                
                return new SwitchOffsets( defaultAddr, cases, targets );
            }
            
            case OFFSET_LOOKUP: {
                int defaultAddr = offset + in.readInt();
                int numPairs    = in.readInt();
                
                int[] cases   = new int[ numPairs ];
                int[] targets = new int[ numPairs ];
                for (int i = 0; i < targets.length; i++) {
                    cases[i]   = in.readInt();
                    targets[i] = offset + in.readInt();
                }
                
                return new SwitchOffsets( defaultAddr, cases, targets );
            }
            
            case FIELD_INDEX: {
                int index = in.readUnsignedShort();
                
                ConstantPool.FieldRefEntry fieldRef = 
                    (ConstantPool.FieldRefEntry) cpool.getEntry(index);
                
                ConstantPool.NameAndTypeEntry nameType = 
                    (ConstantPool.NameAndTypeEntry)
                        cpool.getEntry( fieldRef.nameAndTypeIndex );
                
                String fieldClass = cpool.getClassName( fieldRef.classIndex );
                String fieldType  = cpool.getTypeName ( nameType.typeIndex );
                String fieldName  = cpool.getUTF8Value( nameType.nameIndex );
                
                return new FieldDescriptor( new ObjectType( fieldClass ), 
                		                    fieldName,
                		                    ValueType.fromName( fieldType ));
            }
            
            case METHOD_INDEX: {
                int index = in.readUnsignedShort();
                
                ConstantPool.MethodRefEntry methodRef =
                    (ConstantPool.MethodRefEntry) cpool.getEntry( index );
                
                ConstantPool.NameAndTypeEntry nameType = 
                    (ConstantPool.NameAndTypeEntry)
                        cpool.getEntry( methodRef.nameAndTypeIndex );

                String methodClass = cpool.getClassName( methodRef.classIndex );
                String methodSig   = cpool.getUTF8Value( nameType.typeIndex );
                String methodName  = cpool.getUTF8Value( nameType.nameIndex );
                
                String[] types = ConstantPool.readSignature( methodSig );
                JavaType retType = JavaType.fromName( types[0] );
                
                ValueType[] paramTypes = new ValueType[ types.length - 1 ];
                for (int i = 0; i < paramTypes.length; i++) {
                    paramTypes[i] = ValueType.fromName( types[i+1] );
                }
                
                Signature sig = new Signature( methodName, paramTypes );
                
                return new MethodDescriptor( new ObjectType( methodClass ), 
                		                     sig, retType );
            }
                
            case CLASS_INDEX: {
                int index = in.readUnsignedShort();
                String className = cpool.getClassName( index );
                return JavaType.fromName( className );
            }
                
            case ARRAY_TYPE: {
                int tag = in.readUnsignedByte();
                PrimitiveArrayType atype = PrimitiveArrayType.valueToType.get( tag );                
                return new ArrayType( atype.primitiveType, 1 );
            }
                
            case DIMENSION_COUNT: return in.readUnsignedByte();                
            case BYTE_VALUE:      return (int) (isWide ? in.readShort() : in.readByte());
            case SHORT_VALUE:     return (int) in.readShort();
            case ARGS_SIZE:       return in.readUnsignedByte();
            case ZERO_BYTE:       return (int) in.readByte(); 
        }
        
        throw new IOException( "Unhandled OperationArgument " + name() );
    }
    
    /**
     * Dump an argument.
     * 
     * @param arg the value to dump
     */
    public void dump( IndentingPrintWriter out, Object arg ) {
        switch( this ) {
        
            case CONSTANT_INDEX: 
            case WIDE_CONSTANT_INDEX: {
                if( arg instanceof String ) {
                    out.print( " " );
                    out.writeDoubleQuotedString( (String) arg );
                    return;
                }
                
                out.write( " " + arg );
                return;
            }
                
            case VAR_INDEX:       out.write( " " + arg ); return;
            case OFFSET:          out.write( " " + arg ); return;
            case OFFSET32:        out.write( " " + arg ); return;
            case ALIGN32:         return; 
            
            case OFFSET_TABLE:             
            case OFFSET_LOOKUP: {
                out.print( " " );
                ((SwitchOffsets) arg).dump( out );
                return;
            }
            
            case FIELD_INDEX: {
                out.print( " " + ((FieldDescriptor) arg));
                return;
            } 
            
            case METHOD_INDEX: {
                out.print( " " + ((MethodDescriptor) arg));
                return;
            } 
                
            case CLASS_INDEX:     out.write( " " + arg ); return;               
            case ARRAY_TYPE:      out.write( " " + arg ); return;                
            case DIMENSION_COUNT: out.write( " [" + arg + "]" ); return;              
            case BYTE_VALUE:      out.write( " " + arg ); return;
            case SHORT_VALUE:     out.write( " " + arg ); return;
            case ARGS_SIZE:       return;
            case ZERO_BYTE:       return; 
        } 
    }
}
