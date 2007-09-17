package org.javaswf.j2avm.model;

import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A method descriptor
 *
 * @author nickmain
 */
public final class MethodDescriptor extends MemberDescriptor {
    
    /** The parameter types */
    public final ValueType[] paramTypes;
    
    /**
     * @param owner the class that owns the method
     * @param name the method name
     * @param type the return type
     * @param paramTypes the parameter types
     */
    public MethodDescriptor( ObjectType owner, String name, JavaType type, 
                             ValueType...paramTypes ) {
        super( owner, name, type );
        this.paramTypes = paramTypes;
    }
}
