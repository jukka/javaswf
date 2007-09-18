package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.visitor.AttributeVisitor;


/**
 * Runtime invisible annotations
 *
 * @author nickmain
 */
public class RuntimeInvisibleAnnotationsAttribute extends Annotations {
    public RuntimeInvisibleAnnotationsAttribute() {
        super( AttributeModel.Name.RuntimeInvisibleAnnotations, false );
    }
    
    public static RuntimeInvisibleAnnotationsAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        RuntimeInvisibleAnnotationsAttribute attr = new RuntimeInvisibleAnnotationsAttribute();
        attr.parseAnnotations( pool, in );
        return attr;
    }

    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        visitor.attrRuntimeInvisibleAnnotations( annotations );        
    }
}
