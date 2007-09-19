package org.javaswf.j2avm.model;

import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.Signature;

/**
 * A method descriptor
 *
 * @author nickmain
 */
public final class MethodDescriptor {
    
    /** The method signature */
    public final Signature signature;
    
    /** The owning class */
    public final ObjectType owner;
    
    /** The method return-type */
    public final JavaType type;
    
    /**
     * @param owner the class that owns the method
     * @param signature the method signature
     * @param type the return type
     */
    public MethodDescriptor( ObjectType owner, Signature signature, JavaType type ) {
        this.owner     = owner;
        this.signature = signature;
        this.type      = type;
    }
    
    @Override
    public String toString() {
    	return owner.name + "." + signature + ":" + type.name;
    }
}
