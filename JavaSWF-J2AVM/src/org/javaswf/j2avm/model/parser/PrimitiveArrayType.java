package org.javaswf.j2avm.model.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.javaswf.j2avm.model.types.PrimitiveType;

/**
 * The type of a primitive array
 *
 * @author nickmain
 */
public enum PrimitiveArrayType {

    T_BOOLEAN ( 4 , PrimitiveType.BOOLEAN ),
    T_CHAR    ( 5 , PrimitiveType.CHAR ),
    T_FLOAT   ( 6 , PrimitiveType.FLOAT ),
    T_DOUBLE  ( 7 , PrimitiveType.DOUBLE ),
    T_BYTE    ( 8 , PrimitiveType.BYTE ),
    T_SHORT   ( 9 , PrimitiveType.SHORT ),
    T_INT     ( 10, PrimitiveType.INT ),
    T_LONG    ( 11, PrimitiveType.LONG );
    
    /** The binary value */
    public final int value;
    
    /** The corresponding primitive type */
    public final PrimitiveType primitiveType;
    
    private PrimitiveArrayType( int value, PrimitiveType primitiveType ) {
        this.value = value;
        this.primitiveType = primitiveType;
    }
    
    /** Map of value to instance */
    public static Map<Integer, PrimitiveArrayType> valueToType;
    static {
        Map<Integer,PrimitiveArrayType> map = new HashMap<Integer, PrimitiveArrayType>();
        valueToType = Collections.unmodifiableMap( map );
        
        for( PrimitiveArrayType t : values() ) {
            map.put( t.value, t );
        }
    }
}
