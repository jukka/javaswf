package org.javaswf.j2avm.model;

import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A field descriptor
 *
 * @author nickmain
 */
public final class FieldDescriptor {
    
    /** The field type */
    public final ValueType type;
    
    /** The owning class */
    public final ObjectType owner;
    
    /** The field name */
    public final String name;
    
    /**
     * @param owner the class that owns the field
     * @param name the field name
     * @param type the field type
     */
    public FieldDescriptor( ObjectType owner, String name, ValueType type ) {
        this.type  = type;
        this.owner = owner;
        this.name  = name;
    }
    
    @Override
    public String toString() {
    	return owner.name + "." + name + ":" + type.name;
    }
}
