package org.javaswf.j2avm.model.types;

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
        if( otherType.getClass() != getClass() ) return false;
        
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
    	
    	JavaType type = PrimitiveType.fromName( name );
    	if( type != null ) return type;
    		
    	int dimCount = 0;
    	while( name.endsWith( "[]" )) {
    		dimCount++;
    		name = name.substring( 0, name.length() - 2 );
    	}
    	
    	if( dimCount > 0 ) {
    		JavaType elementType = JavaType.fromName( name );
    		if( elementType instanceof ValueType ) {
    			type = new ArrayType( ((ValueType) elementType), dimCount );
    		} else {
    			throw new IllegalArgumentException( "void array is illegal" );
    		}
    	} else {
    		type = new ObjectType( name );
    	}
    	
        return type;
    }
}
