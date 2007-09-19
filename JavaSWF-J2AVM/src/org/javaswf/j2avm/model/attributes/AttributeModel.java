package org.javaswf.j2avm.model.attributes;

import org.epistem.io.IndentingPrintWriter;

/**
 * Base for class, field, method and code attributes
 *
 * @author nickmain
 */
public abstract class AttributeModel {

    /** The attribute name */
    public final String name;
    
    /**
     * @param name the attribute name
     */
    public AttributeModel( String name ) {
        this.name = name;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public final boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof AttributeModel )) return false;
        return ((AttributeModel) obj).name.equals( name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public final int hashCode() {
        return name.hashCode();
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.println( name );
    }    
}
