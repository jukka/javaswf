package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.javaswf.j2avm.model.parser.ConstantPool;

/**
 * Runtime visible annotations
 *
 * @author nickmain
 */
public class RuntimeVisibleAnnotationsAttribute extends Annotations {
    public RuntimeVisibleAnnotationsAttribute() {
        super( AttributeName.RuntimeVisibleAnnotations, true );
    }
    
    public static RuntimeVisibleAnnotationsAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        RuntimeVisibleAnnotationsAttribute attr = new RuntimeVisibleAnnotationsAttribute();
        attr.parseAnnotations( pool, in );
        return attr;
    }

}
