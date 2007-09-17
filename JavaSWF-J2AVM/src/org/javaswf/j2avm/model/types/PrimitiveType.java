package org.javaswf.j2avm.model.types;


/**
 * A primitive type
 *
 * @author nickmain
 */
public final class PrimitiveType extends ValueType {

    public static final PrimitiveType BYTE    = new PrimitiveType( "byte" );
    public static final PrimitiveType BOOLEAN = new PrimitiveType( "boolean" );
    public static final PrimitiveType SHORT   = new PrimitiveType( "short" );
    public static final PrimitiveType CHAR    = new PrimitiveType( "char" );
    public static final PrimitiveType INT     = new PrimitiveType( "int" );
    public static final PrimitiveType FLOAT   = new PrimitiveType( "float" );
    public static final PrimitiveType LONG    = new PrimitiveType( "long" );
    public static final PrimitiveType DOUBLE  = new PrimitiveType( "double" );
    
    private PrimitiveType( String name ) {
        super( name );
    }
}
