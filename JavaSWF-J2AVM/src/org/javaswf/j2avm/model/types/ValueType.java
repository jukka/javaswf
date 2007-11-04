package org.javaswf.j2avm.model.types;


/**
 * Common base for types that can have values (all but void)
 *
 * @author nickmain
 */
public abstract class ValueType extends JavaType {

    /*pkg*/ ValueType( String name, String abbreviation ) {
        super( name, abbreviation );
    }
    
    /**
     * Create a ValueType from a type name 
     */
    public static ValueType fromName( String name ) {
    	
    	if( name.equals( "void" ) ) {
    		throw new IllegalArgumentException( "Value type cannot be void" );
    	}
    	
        return (ValueType) JavaType.fromName( name );
    }
    
    /**
     * Whether this type is a 64 bit type.
     */
    public boolean is64Bit() {
        return false;
    }
}
