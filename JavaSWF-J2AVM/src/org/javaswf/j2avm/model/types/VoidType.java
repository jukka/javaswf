package org.javaswf.j2avm.model.types;


/**
 * The void type
 *
 * @author nickmain
 */
public final class VoidType extends JavaType {
    
    public static final VoidType VOID = new VoidType();
    
    private VoidType() { super( "void", "V" ); }
}
