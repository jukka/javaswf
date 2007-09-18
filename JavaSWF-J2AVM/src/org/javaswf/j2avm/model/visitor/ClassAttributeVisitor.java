package org.javaswf.j2avm.model.visitor;

import java.util.Collection;

import org.javaswf.j2avm.model.attributes.InnerClass;

/**
 * Visitor interface for class specific attributes
 *
 * @author nickmain
 */
public interface ClassAttributeVisitor extends AttributeVisitor {

    /**
     * The sourcefile attribute
     * @param filename the source filename
     */
    public void attrSourceFile( String filename );

    /**
     * The InnerClasses attribute
     * 
     * @param innerClasses the innerclasses
     */
    public void attrInnerClasses( Collection<InnerClass> innerClasses );
}
