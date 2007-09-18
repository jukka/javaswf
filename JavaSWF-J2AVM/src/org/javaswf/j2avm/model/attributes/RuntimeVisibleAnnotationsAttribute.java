package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.*;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;

/**
 * Runtime visible annotations
 *
 * @author nickmain
 */
public class RuntimeVisibleAnnotationsAttribute extends Annotations {
    public RuntimeVisibleAnnotationsAttribute() {
        super( RuntimeVisibleAnnotations, true );
    }
    
    public static RuntimeVisibleAnnotationsAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        RuntimeVisibleAnnotationsAttribute attr = new RuntimeVisibleAnnotationsAttribute();
        attr.parseAnnotations( pool, loader, in );
        return attr;
    }
}
