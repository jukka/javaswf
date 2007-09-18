package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.visitor.AttributeVisitor;

/**
 * Runtime visible annotations
 *
 * @author nickmain
 */
public class RuntimeVisibleAnnotationsAttribute extends Annotations {
    public RuntimeVisibleAnnotationsAttribute() {
        super( AttributeModel.Name.RuntimeVisibleAnnotations, true );
    }
    
    public static RuntimeVisibleAnnotationsAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        RuntimeVisibleAnnotationsAttribute attr = new RuntimeVisibleAnnotationsAttribute();
        attr.parseAnnotations( pool, in );
        return attr;
    }

    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        visitor.attrRuntimeVisibleAnnotations( annotations );
    }
}
