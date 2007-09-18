package org.javaswf.j2avm.model.visitor;

import java.util.Collection;

import org.javaswf.j2avm.model.flags.ClassFlag;
import org.javaswf.j2avm.model.flags.FieldFlag;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Visitor interface for a ClassModel.
 * 
 * Methods are called in the following order:
 * 
 *   classStart
 *   classField *
 *   classMethod *
 *   classAttributes
 *   classEnd
 *   
 * @author nickmain
 */
public interface ClassVisitor {

    /**
     * First method to be called for a given class. 
     *
     * @param className the class name
     * @param superclass may be null if there is no superclass
     * @param majorVersion the class major version
     * @param minorVersion the class minor version
     * @param flags the class flags
     * @param interfaces the implemented interfaces
     * 
     * @return true to continue the visit, false to abort it
     */
    public boolean classStart( String className, String superclass, 
    		                   int majorVersion, int minorVersion, 
    		                   Collection<ClassFlag> flags,
    		                   Collection<String> interfaces );

    /**
     * Pass a field.
     * 
     * @param name the field name
     * @param type the field type
     * @param flags the field flags
     * @return interface to receive the attributes, null to skip
     */
    public FieldAttributeVisitor classField( String name, ValueType type, 
                                             Collection<FieldFlag> flags );

    /**
     * Pass a method.
     * 
     * @param name the method name
     * @param returnType the return type
     * @param flags the method flags
     * @param parameterTypes the parameter types
     * @return interface to receive the attributes, null to skip
     */
    public MethodAttributeVisitor classMethod( String name, JavaType returnType,
    		                                   Collection<MethodFlag> flags,
    		                                   ValueType...parameterTypes );
    
    /**
     * Pass the class attributes.
     * 
     * @return object to receive the attributes, null to skip
     */
    public ClassAttributeVisitor classAttributes();
    
    /**
     * Called at the end of the class contents.
     */
    public void classEnd();
}
