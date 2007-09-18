package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.visitor.AttributeVisitor;
import org.javaswf.j2avm.model.visitor.MethodAttributeVisitor;


/**
 * Runtime invisible parameter annotations
 *
 * @author nickmain
 */
public class RuntimeInvisibleParameterAnnotationsAttribute extends ParameterAnnotationAttribute {
    public RuntimeInvisibleParameterAnnotationsAttribute() {
        super( AttributeModel.Name.RuntimeInvisibleParameterAnnotations, false );
    }
    
    public static RuntimeInvisibleParameterAnnotationsAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        RuntimeInvisibleParameterAnnotationsAttribute attr = new RuntimeInvisibleParameterAnnotationsAttribute();
        attr.parseAnnotations( pool, in );
        return attr;
    }

    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        if( visitor instanceof MethodAttributeVisitor ) {
            ((MethodAttributeVisitor) visitor).attrRuntimeInvisibleParameterAnnotations( parameterAnnotations );
        }
    }
}
