package org.javaswf.j2avm.model.visitor;

import java.util.List;
import java.util.Map;

import org.javaswf.j2avm.model.attributes.AnnotationModel;

/**
 * Visitor interface for method specific attributes.
 *
 * @author nickmain
 */
public interface MethodAttributeVisitor extends AttributeVisitor {

    /**
     * Runtime visible parameter annotations
     */
    public void attrRuntimeVisibleParameterAnnotations( List<Map<String,AnnotationModel>> annotations );

    /**
     * Runtime invisible parameter annotations
     */
    public void attrRuntimeInvisibleParameterAnnotations( List<Map<String,AnnotationModel>> annotations );  
    
//  public CodeAttributeVisitor visitAttr_Code();
    //
//        public JClassVisitor visitAttr_Exceptions( int count );
    
//  LineNumberTable                      (LineNumberTableAttribute.class),
//  LocalVariableTable                   (LocalVariableTableAttribute.class),
//  LocalVariableTypeTable               (LocalVariableTypeTableAttribute.class),
//  AnnotationDefault                    (AnnotationDefaultAttribute.class),
//  EnclosingMethod                      (EnclosingMethodAttribute.class),
//  Signature                            (SignatureAttribute.class), 
}
