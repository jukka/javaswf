package org.javaswf.j2avm.model.types;


/**
 * Common base for types that can have values (all but void)
 *
 * @author nickmain
 */
public abstract class ValueType extends JavaType {

    /*pkg*/ ValueType( String name ) {
        super( name );
    }
}
