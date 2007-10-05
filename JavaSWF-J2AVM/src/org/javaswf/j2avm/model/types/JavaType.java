package org.javaswf.j2avm.model.types;

/**
 * Base for Java types.  Equality is based on the type name.
 *  
 * @author nickmain
 */
public abstract class JavaType {

	/**
	 * The type name
	 */
    public final String name;
    
    /**
     * An abbreviation for the type. 
     */
    public final String abbreviation;
    
    /*pkg*/ JavaType( String name, String abbreviation ) {
        this.name = name;
        this.abbreviation = abbreviation;
    }
    
    @Override
    public final String toString() {
        return name;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public final boolean equals( Object obj ) {
        if( obj == null || !(obj instanceof JavaType)) return false;
        
        JavaType otherType = (JavaType) obj;
        return name.equals( otherType.name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public final int hashCode() {
        return name.hashCode();
    }

    /**
     * Get the type that is the common denominator between two types.
     * This does not attempt to determine common super-types.
     * 
     * @return null if the types are incompatible
     */
    public static JavaType common( JavaType a, JavaType b ) {
    	
    	if( a.equals( b ) ) return a;

    	if( a instanceof ObjectOrArrayType && b instanceof ObjectOrArrayType ) {
    		return ObjectType.OBJECT;
    	}
    	
    	if( a instanceof PrimitiveType && b instanceof PrimitiveType ) {
    		PrimitiveType pa = (PrimitiveType) a;
    		PrimitiveType pb = (PrimitiveType) b;
    		
    		if( pa.isIntType && pb.isIntType ) return PrimitiveType.INT;
    	}
    	
    	return null;
    }
    
    /**
     * Create a JavaType from a type name 
     */
    public static JavaType fromName( String name ) {
    	
    	if( name.equals( "void" ) ) return VoidType.VOID;
    	
    	if( PrimitiveType.isPrimitiveTypeName( name )) {
    		return PrimitiveType.fromName( name );
    	}

    	if( ArrayType.isArrayTypeName( name )) {
    		return ArrayType.fromName( name );
    	}
    	
    	return new ObjectType( name );
    }
}
