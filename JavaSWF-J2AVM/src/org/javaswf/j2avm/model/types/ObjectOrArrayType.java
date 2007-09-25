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
}
