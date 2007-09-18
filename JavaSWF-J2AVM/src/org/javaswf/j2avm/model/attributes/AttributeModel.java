package org.javaswf.j2avm.model.attributes;

import org.epistem.io.IndentingPrintWriter;

/**
 * Base for class, field, method and code attributes
 *
 * @author nickmain
 */
public class AttributeModel {

    /** The standard attribute names */
    public static enum Name {
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
        
        private Name( Class<? extends AttributeModel> attributeClass ) {
            this.attributeClass = attributeClass;
        }        
    }
    
    /** The attribute name */
    private final String name;
    
    /**
     * @param name the attribute name
     */
    public AttributeModel( String name ) {
        this.name = name;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof AttributeModel )) return false;
        return ((AttributeModel) obj).name.equals( name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.println( name );
    }
}
