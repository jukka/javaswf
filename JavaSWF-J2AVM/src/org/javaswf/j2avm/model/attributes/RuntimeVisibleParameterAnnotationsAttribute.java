package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.javaswf.j2avm.model.visitor.AttributeVisitor;
import org.javaswf.j2avm.model.visitor.MethodAttributeVisitor;

/**
 * Runtime visible parameter annotations
 *
 * @author nickmain
 */
public class RuntimeVisibleParameterAnnotationsAttribute extends ParameterAnnotationAttribute {
    public RuntimeVisibleParameterAnnotationsAttribute() {
        super( AttributeModel.Name.RuntimeVisibleParameterAnnotations, true );
    }
    
    public static RuntimeVisibleParameterAnnotationsAttribute parse( org.javaswf.j2avm.model.parser.ConstantPool pool, DataInput in ) throws IOException {
        RuntimeVisibleParameterAnnotationsAttribute attr = new RuntimeVisibleParameterAnnotationsAttribute();
        attr.parseAnnotations( pool, in );
        return attr;
    }
    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        if( visitor instanceof MethodAttributeVisitor ) {
            ((MethodAttributeVisitor) visitor).attrRuntimeVisibleParameterAnnotations( parameterAnnotations );
        }
    }
}
