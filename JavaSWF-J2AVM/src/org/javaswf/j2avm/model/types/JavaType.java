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
}
