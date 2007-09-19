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

	/** The array suffix */
	public final static String SUFFIX = "[]";
	
    /** The type of the array elements */
    public final ValueType elementType;
    
    /** The number of dimensions */
    public final int dimensionCount;
    
    /**
     * @param type the element type
     * @param dimCount the number of dimensions
     */
    public ArrayType( ValueType type, int dimCount ) {
        this( makeName( type.name, dimCount ), type, dimCount );
    }

    private ArrayType( String name, ValueType type, int dimCount ) {
    	super( name );
        elementType    = type;
        dimensionCount = dimCount;
    }
    
    private static String makeName( String name, int dimCount ) {
        while( dimCount-- > 0 ) name += "[]";
        return name;
    }
    
    /** Whether a type name is an array type */
    public static boolean isArrayTypeName( String name ) {
    	return name.endsWith( SUFFIX );
    }
    
    /**
     * Create an ArrayType from a type name 
     */
    public static ArrayType fromName( String name ) {
    
    	if( ! isArrayTypeName( name )) {
    		throw new IllegalArgumentException( "Array type name must end with " + SUFFIX );
    	}
    	
    	String elemName = name;
    	int dimCount = 0;
    	while( name.endsWith( ArrayType.SUFFIX )) {
    		dimCount++;
    		elemName = elemName.substring( 0, elemName.length() - ArrayType.SUFFIX.length() );
    	}
    	
		JavaType elementType = JavaType.fromName( elemName );
		if( elementType instanceof ValueType ) {
			return new ArrayType( name, ((ValueType) elementType), dimCount );
		} else {
			throw new IllegalArgumentException( "void array is illegal" );
		}
    }
}
