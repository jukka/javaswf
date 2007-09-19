package org.javaswf.j2avm.model.attributes;

/** The standard attribute names */
public enum AttributeName {
    Code                                 (CodeAttribute.class),
    Exceptions                           (ExceptionsAttribute.class),
    ConstantValue                        (ConstantValueAttribute.class),
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
    
    /** The class that holds the attribute type */
    public final Class<? extends AttributeModel> attributeClass;
    
    private AttributeName( Class<? extends AttributeModel> attributeClass ) {
        this.attributeClass = attributeClass;
    }        
}