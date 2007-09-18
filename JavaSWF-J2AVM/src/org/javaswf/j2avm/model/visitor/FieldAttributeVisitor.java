package org.javaswf.j2avm.model.visitor;

/**
 * Visitor interface for field specific attributes
 *
 * @author nickmain
 */
public interface FieldAttributeVisitor extends AttributeVisitor {

    /**
     * The ConstantValue attribute
     * 
     * @param value Integer, Double, Float, Long or String
     */
    public void attrConstantValue( Object value );
}
