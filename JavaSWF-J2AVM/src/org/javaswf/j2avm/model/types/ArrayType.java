package org.javaswf.j2avm.model.types;

/**
 * An array type.
 * 
 * An array can have an array element type, but this is only useful when
 * a NewArray instruction is only initializing the dimensions in the outer
 * array and not the element array.
 *
 * @author nickmain
 */
public final class ArrayType extends ObjectOrArrayType {

    /** The type of the array elements */
    public final ValueType elementType;
    
    /** The number of dimensions */
    public final int dimensionCount;
    
    /**
     * @param type the element type
     * @param dimCount the number of dimensions
     */
    public ArrayType( ValueType type, int dimCount ) {
        super( makeName( type.name, dimCount ));
        
        elementType    = type;
        dimensionCount = dimCount;
    }

    private static String makeName( String name, int dimCount ) {
        while( dimCount-- > 0 ) name += "[]";
        return name;
    }
}
