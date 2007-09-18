package org.javaswf.j2avm.model.visitor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.javaswf.j2avm.model.attributes.AnnotationModel;


/**
 * Visitor interface for class, field, method and code attributes
 *
 * @author nickmain
 */
public interface AttributeVisitor {

    /**
     * Called when all attributes have been passed.
     */
    public void done();
    
    /**
     * The deprecate attribute
     */
    public void attrDeprecated();
    
    /**
     * The Synthetic attribute
     */
    public void attrSynthetic();

    /**
     * An unknown attribute
     * 
     * @param name the attribute name
     * @param data the attribute data
     */
    public void attrUnknown( String name, byte[] data );
    
    /**
     * Runtime visible annotations
     */
    public void attrRuntimeVisibleAnnotations( Map<String,AnnotationModel> annotations );
    
    /**
     * Runtime invisible annotations
     */
    public void attrRuntimeInvisibleAnnotations( Map<String,AnnotationModel> annotations );
  

}
