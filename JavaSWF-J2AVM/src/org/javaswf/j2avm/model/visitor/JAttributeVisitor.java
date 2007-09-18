package org.javaswf.j2avm.model.visitor;


/**
 * Visitor interface for class, field, method and code attributes
 *
 * @author nickmain
 */
public interface JAttributeVisitor {

    public CodeAttributeVisitor visitAttr_Code();

    public JClassVisitor visitAttr_Exceptions( int count );

    public void visitAttr_ConstantValue( Object value );
    Deprecated                           (DeprecatedAttribute.class),
    Synthetic                            (SyntheticAttribute.class),
    SourceFile                           (SourceFileAttribute.class),
    LineNumberTable                      (LineNumberTableAttribute.class),
    LocalVariableTable                   (LocalVariableTableAttribute.class),
    LocalVariableTypeTable               (LocalVariableTypeTableAttribute.class),
    AnnotationDefault                    (AnnotationDefaultAttribute.class),
    EnclosingMethod                      (EnclosingMethodAttribute.class),
    Signature                            (SignatureAttribute.class), 
    InnerClasses                         (InnerClassesAttribute.class), 
    RuntimeVisibleAnnotations            (RuntimeVisibleAnnotationsAttribute.class),
    RuntimeInvisibleAnnotations          (RuntimeInvisibleAnnotationsAttribute.class),
    RuntimeVisibleParameterAnnotations   (RuntimeVisibleParameterAnnotationsAttribute.class),
    RuntimeInvisibleParameterAnnotations (RuntimeInvisibleParameterAnnotationsAttribute.class);    
}
