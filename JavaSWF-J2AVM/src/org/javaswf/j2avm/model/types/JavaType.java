package org.javaswf.j2avm.model.types;

/**
 * Base for Java types.  Equality is based on the type name.
 *  
 * @author nickmain
 */
public abstract class JavaType {

    public final String name;
    
    /*pkg*/ JavaType( String name ) {
        this.name = name;
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
