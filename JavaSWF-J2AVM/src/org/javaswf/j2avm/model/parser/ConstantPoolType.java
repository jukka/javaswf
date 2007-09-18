package org.javaswf.j2avm.model.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Types of Constant Pool entry.
 *
 * @author nickmain
 */
public enum ConstantPoolType {
    
    CPool_Utf8               ( 1 ),
    CPool_Integer            ( 3 ),
    CPool_Float              ( 4 ),
    CPool_Long               ( 5 ),
    CPool_Double             ( 6 ),
    CPool_Class              ( 7 ),
    CPool_String             ( 8 ),
    CPool_Fieldref           ( 9 ),
    CPool_Methodref          ( 10 ),
    CPool_InterfaceMethodref ( 11 ),
    CPool_NameAndType        ( 12 );
    
    /** The binary value */
    public final int value;
    
    private ConstantPoolType( int value ) {
        this.value = value;
    }
    
    /** Map of value to instance */
    public static Map<Integer, ConstantPoolType> valueToType;
    static {
        Map<Integer,ConstantPoolType> map = new HashMap<Integer, ConstantPoolType>();
        valueToType = Collections.unmodifiableMap( map );
        
        for( ConstantPoolType t : values() ) {
            map.put( t.value, t );
        }
    }
}
