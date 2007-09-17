package org.javaswf.j2avm.model;

import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * Base for method and field descriptors
 *
 * @author nickmain
 */
public abstract class MemberDescriptor {

    /** The owning class */
    public final ObjectType owner;
    
    /** The member name */
    public final String name;
    
    /** The field type or method return-type */
    public final JavaType type;
    
    /*pkg*/ MemberDescriptor( ObjectType owner, String name, JavaType type ) {
        this.owner = owner;
        this.name  = name;
        this.type  = type;
    }
}
