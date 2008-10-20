package com.anotherbigidea.flash.avm2;

import java.util.HashMap;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Name;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.io.ConstantPool;

/**
 * Types of constant-pool values
 *
 * @author nickmain
 */
public enum ValueKind {

    CONSTANT_Utf8      ( 0x01 ){ public Object getPoolValue( ConstantPool cpool, int index ) { return cpool.stringAt(index); }},
    CONSTANT_Int       ( 0x03 ){ public Object getPoolValue( ConstantPool cpool, int index ) { return cpool.intAt(index); }},
    CONSTANT_UInt      ( 0x04 ){ public Object getPoolValue( ConstantPool cpool, int index ) { return cpool.uintAt(index); }},
    CONSTANT_Double    ( 0x06 ){ public Object getPoolValue( ConstantPool cpool, int index ) { return cpool.doubleAt(index); }},
    CONSTANT_Namespace ( 0x08 ){ public Object getPoolValue( ConstantPool cpool, int index ) { return cpool.namespaceAt(index); }},
    CONSTANT_Multiname ( 0x09 ){ public Object getPoolValue( ConstantPool cpool, int index ) { return cpool.nameAt(index); }},
    CONSTANT_False     ( 0x0A ){ public Object getPoolValue( ConstantPool cpool, int index ) { return Boolean.FALSE; }},
    CONSTANT_True      ( 0x0B ){ public Object getPoolValue( ConstantPool cpool, int index ) { return Boolean.TRUE; }},
    CONSTANT_Null      ( 0x0C ){ public Object getPoolValue( ConstantPool cpool, int index ) { return null; }};
    
    public final int value;
    private ValueKind( int value ) { this.value = value; }
    
    private static Map<Integer, ValueKind> val2enum =
        new HashMap<Integer, ValueKind>();

    static {
        for (ValueKind nk : values()) {
            val2enum.put( nk.value, nk );
        }        
    }
     
    /**
     * Find an enum instance
     */
    public static ValueKind fromValue( int value ) {        
        ValueKind nk = val2enum.get( value );        
        if( nk == null ) throw new RuntimeException( "Unrecognized value kind " + value );        
        return nk;
    }
    
    /** Get the value of this kind from the given constant pool at the given index */
    public abstract Object getPoolValue( ConstantPool cpool, int index );
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out, Object value ) {
        out.print( name() + " " );
        
        switch( this ) {
            case CONSTANT_Utf8:      out.writeDoubleQuotedString( (String) value ); break;     
            case CONSTANT_Int:       out.print( value ); break;
            case CONSTANT_UInt:      out.print( value ); break;
            case CONSTANT_Double:    out.print( value ); break;
            case CONSTANT_Namespace: ((AVM2Namespace) value).dump( out ); break;
            case CONSTANT_Multiname: ((AVM2Name     ) value).dump( out ); break;
            case CONSTANT_False:
            case CONSTANT_True:
            case CONSTANT_Null:
            default: return;
        }
    }
    
    /** Initialize a constant pool */
    public void initPool( AVM2ABCFile.WriteContext context, Object value ) {
        
        ConstantPool pool = context.pool;
        
        switch( this ) {
            case CONSTANT_Utf8:      pool.stringIndex( (String) value ); break;     
            case CONSTANT_Int:       pool.intIndex( (Integer) value ); break;
            case CONSTANT_UInt:      pool.uintIndex( (Long) value ); break;
            case CONSTANT_Double:    pool.doubleIndex( (Double) value ); break;
            case CONSTANT_Namespace: {
                AVM2Namespace ns = (AVM2Namespace) value;
                pool.namespaceIndex( ns.kind, ns.name );
                break;
            }
            case CONSTANT_Multiname: ((AVM2Name) value).initPool( context ); break;
            case CONSTANT_False:
            case CONSTANT_True:
            case CONSTANT_Null:
            default: return;
        }
    }
    
    /** Get the index of the value in the constant pool */
    public int poolIndex( AVM2ABCFile.WriteContext context, Object value ) {
        
        ConstantPool pool = context.pool;
        
        switch( this ) {
            case CONSTANT_Utf8:      return pool.stringIndex( (String) value );     
            case CONSTANT_Int:       return pool.intIndex( (Integer) value );
            case CONSTANT_UInt:      return pool.uintIndex( (Long) value );
            case CONSTANT_Double:    return pool.doubleIndex( (Double) value );
            case CONSTANT_Namespace: {
                AVM2Namespace ns = (AVM2Namespace) value;
                return pool.namespaceIndex( ns.kind, ns.name ).poolIndex;
            }
            case CONSTANT_Multiname: return ((AVM2Name) value).indexIn( pool );
            case CONSTANT_False:
            case CONSTANT_True:
            case CONSTANT_Null:
            default: return 0;
        }
    }
}
