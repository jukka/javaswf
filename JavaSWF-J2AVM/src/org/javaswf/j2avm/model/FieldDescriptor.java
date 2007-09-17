package org.javaswf.j2avm.model;

import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A field descriptor
 *
 * @author nickmain
 */
public final class FieldDescriptor extends MemberDescriptor {
    
    /** The field type */
    public final ValueType fieldType;
    
    /**
     * @param owner the class that owns the field
     * @param name the field name
     * @param type the field type
     */
    public FieldDescriptor( ObjectType owner, String name, ValueType type ) {
        super( owner, name, type );
        this.fieldType = type;
    }
}
