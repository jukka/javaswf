package org.javaswf.j2avm.model.types;


/**
 * Common base for Object and Array types
 *
 * @author nickmain
 */
public abstract class ObjectOrArrayType extends ValueType {

    /*pkg*/ ObjectOrArrayType( String name, String abbreviation ) {
        super( name, abbreviation );
    }
    
    /**
     * Get an ObjectOrArrayType from a name
     */
    public static ObjectOrArrayType fromName( String name ) {
    	ValueType vt = ValueType.fromName( name );
    	if( vt instanceof ObjectOrArrayType ) {
    		return (ObjectOrArrayType) vt;
    	}
    	
    	throw new RuntimeException( "Not an Object or Array type: " + name );
    }
}
