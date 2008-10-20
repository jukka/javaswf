package com.anotherbigidea.flash.avm2;

import java.io.IOException;

import org.epistem.code.LocalValue;
import org.epistem.io.InStream;
import org.epistem.io.OutStream;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Name;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.io.ConstantPool;

/**
 * Argument types for AVM2 instructions.
 *
 * @author nickmain
 */
public enum ArgType {

    CLASS_INDEX,
    INT_INDEX,
    UINT_INDEX,
    STRING_INDEX,
    DOUBLE_INDEX,
    NAMESPACE_INDEX,
    CATCH_INDEX,
    METHOD_INDEX,
    
    REGISTER,
    TARGET_REGISTER,
    INDEX_REGISTER,
    
    ARG_COUNT,
    KEY_VALUE_COUNT,
    SLOT_ID,
    DISP_ID,
    METHOD_ID,
    NAME_INDEX,
    SCOPE_INDEX,
    BYTE           ( byte.class ),
    SHORT,
    LINE_NUMBER,
    DEFAULT_OFFSET,
    SWITCH_OFFSETS ( int[].class ),
    OFFSET;    
    
    
    /** The type that holds the value */
    public final Class<?> valueType;
    
    private ArgType( Class<?> valueType ) {
        this.valueType = valueType;
    }

    private ArgType() {
        this.valueType = int.class;
    }

    /**
     * Whether this is an address offset.
     */
    public boolean isOffset() {
        switch( this ) {
            case OFFSET:
            case DEFAULT_OFFSET:
            case SWITCH_OFFSETS:
                return true;
            
            default: return false;
        }
    }
    
    /**
     * Get the offset adjustment for the arg type - either zero (for switch
     * offsets or 4 for branch offsets).
     */
    public int offsetAdjustment() {
        switch( this ) {
            case OFFSET: return 4;
            
            case DEFAULT_OFFSET:
            case SWITCH_OFFSETS: return 0;
            
            default: return 0;
        }        
    }
    
    /**
     * Parse the type
     */
    public final Object parse( InStream in ) throws IOException {
        switch( this ) {
            case CLASS_INDEX:
            case INT_INDEX:
            case UINT_INDEX:
            case NAMESPACE_INDEX:
            case STRING_INDEX:
            case CATCH_INDEX:
            case METHOD_INDEX:
            case LINE_NUMBER:
            case NAME_INDEX:
            case ARG_COUNT:
            case KEY_VALUE_COUNT:    
            case REGISTER:
            case TARGET_REGISTER:
            case INDEX_REGISTER:
            case METHOD_ID:
            case DOUBLE_INDEX: 
                return in.readVU30();
            
            case DISP_ID:
            case SLOT_ID: 
                return in.readVU30() - 1;
         
            case SCOPE_INDEX: return in.readUI8();
            case BYTE:        return in.readSI8();
            case SHORT:       return in.readVU30();
            
            case DEFAULT_OFFSET:
            case OFFSET:
                return in.readSI24();
            
            case SWITCH_OFFSETS: {
                int caseCount = in.readVU30() + 1;
                
                int[] offsets = new int[ caseCount ];
                
                for (int i = 0; i < offsets.length; i++) {
                    offsets[i] = in.readSI24();
                }
                
                return offsets;
            }
            
            default: return null;
        }
    }
    
    /**
     * Write a value of this type
     * @param out the output stream
     * @param value the value - must be of the appropriate type
     */
    public final void write( OutStream out, Object value ) throws IOException {
        switch( this ) {
            case CLASS_INDEX:
            case INT_INDEX:
            case UINT_INDEX:
            case NAMESPACE_INDEX:
            case STRING_INDEX:
            case CATCH_INDEX:
            case METHOD_INDEX:
            case LINE_NUMBER:
            case NAME_INDEX:
            case ARG_COUNT:
            case KEY_VALUE_COUNT:    
            case METHOD_ID:
            case DOUBLE_INDEX:
            case SHORT:    
                if( !( value instanceof Number)) throw new IOException( "Op arg must be a number" );
                out.writeVU30( ((Number) value).intValue() );
                return;

            case REGISTER:
            case TARGET_REGISTER:
            case INDEX_REGISTER:
                if( value instanceof LocalValue ) {
                    value = ((LocalValue<?>) value).allocatedRegister;
                }
                if( !( value instanceof Number)) throw new IOException( "Op arg must be a number" );
                out.writeVU30( ((Number) value).intValue() );
                return;
                
            case DISP_ID:
            case SLOT_ID: 
                if( !( value instanceof Number)) throw new IOException( "Op arg must be a number" );
                out.writeVU30( ((Number) value).intValue() + 1 );
                return;
         
            case SCOPE_INDEX: 
                if( !( value instanceof Number)) throw new IOException( "Op arg must be a number" );
                out.writeUI8( ((Number) value).intValue() );
                return;
            
            case BYTE:
                if( !( value instanceof Number)) throw new IOException( "Op arg must be a number" );
                out.write( ((Number) value).intValue() );
                return;
            
            case DEFAULT_OFFSET:
            case OFFSET:
                if( !( value instanceof Number)) throw new IOException( "Op arg must be a number" );
                out.writeSI24( ((Number) value).intValue() );
                return;
            
            case SWITCH_OFFSETS: {
                if( !(value instanceof int[]) )  throw new IOException( "Switch offsets must be an int array" );
                int[] offsets = (int[]) value;
                
                out.writeVU30( offsets.length - 1 );
                
                for (int i = 0; i < offsets.length; i++) {
                    out.writeSI24( offsets[i] );
                }
            }
        }
    }
    
    /**
     * If this argument type implies extra values to be popped from the stack
     * then determine the count.
     * 
     * @param value the argument value
     * @return the pop count
     */
    public int extraStackPops( Object value ) {
        switch( this ) {
            case NAME_INDEX: {
                AVM2Name name = (AVM2Name) value;
                return ( name.kind.hasRuntimeName      ? 1 : 0 ) 
                     + ( name.kind.hasRuntimeNamespace ? 1 : 0 );
            }

            case ARG_COUNT:       return (Integer) value;                
            case KEY_VALUE_COUNT: return 2 * (Integer) value;                
            default:              return 0;
        }
    }
    
    /**
     * Get the index of the value
     */
    public Object index( AVM2ABCFile.WriteContext context, Object value ) {
        
        ConstantPool pool = context.pool; 
        
        switch( this ) {
            case CLASS_INDEX: {
                AVM2QName className = (AVM2QName) value;
                return context.getFile().classes.get( className ).index;
            }
            case INT_INDEX: {
                return pool.intIndex( ((Number) value).intValue() );
            }
            case UINT_INDEX: {
                return pool.uintIndex( ((Number) value).longValue() );
            }
            case NAMESPACE_INDEX: {
                AVM2Namespace ns = (AVM2Namespace) value;
                return pool.namespaceIndex( ns.kind, ns.name );
            }
            case STRING_INDEX: {
                return pool.stringIndex( value.toString() );
            }
            case DOUBLE_INDEX: {
                return pool.doubleIndex( ((Number) value).doubleValue() );
            }
            case NAME_INDEX: {
                return ((AVM2Name) value).indexIn( pool );
            }
            case SWITCH_OFFSETS:
            case DEFAULT_OFFSET:
            case CATCH_INDEX:
            case METHOD_INDEX:
            case LINE_NUMBER:
            case ARG_COUNT:
            case KEY_VALUE_COUNT:    
            case REGISTER:
            case TARGET_REGISTER:
            case INDEX_REGISTER:
            case METHOD_ID:
            case SHORT:    
            case DISP_ID:
            case SLOT_ID: 
            case SCOPE_INDEX: 
            case BYTE:
            case OFFSET:
            default: return value;
        }
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context, Object value ) {
        switch( this ) {
            case INT_INDEX: {
                context.pool.intIndex( (Integer) value );
                return;
            }
            
            case UINT_INDEX: {
                context.pool.uintIndex( (Long) value );
                return;
            }
            
            case NAMESPACE_INDEX: {
                AVM2Namespace namespace = (AVM2Namespace) value;
                context.pool.namespaceIndex( namespace.kind, namespace.name );
                return;
            }
                
            case STRING_INDEX: {
                context.pool.stringIndex( (String) value );
                return;
            }
                        
            case NAME_INDEX: {
                AVM2Name name = (AVM2Name) value;
                name.initPool( context );
                return;
            }
                
            case DOUBLE_INDEX: {
                context.pool.doubleIndex( (Double) value );
                return;
            }

            case CLASS_INDEX: 
            case LINE_NUMBER:
            case METHOD_INDEX:
            case CATCH_INDEX:
            case ARG_COUNT:
            case KEY_VALUE_COUNT:    
            case REGISTER:
            case TARGET_REGISTER:
            case INDEX_REGISTER:
            case METHOD_ID:
            case SHORT:    
            case DISP_ID:
            case SLOT_ID: 
            case SCOPE_INDEX: 
            case BYTE:
            case DEFAULT_OFFSET:
            case OFFSET:
            case SWITCH_OFFSETS:            
            default: return;
        }
    }
    
    /**
     * Get the number of bytes of preamble in the binary argument before the
     * offset data.  This is only relevant for SWITCH_OFFSETS where there
     * is a size value written before the actual offset values.
     * 
     * @param value the argument value
     */
    public final int preambleBeforeOffsets( Object value ) {
        
        if( this == SWITCH_OFFSETS ) {
            if( !(value instanceof int[])) throw new RuntimeException( "Switch offsets must be an int array" );
            int[] offsets = (int[]) value;
            
            return OutStream.sizeOfVU( offsets.length - 1 );            
        }
        
        return 0;
    }
}
