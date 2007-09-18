package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.*;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;

/**
 * Runtime invisible annotations
 *
 * @author nickmain
 */
public class RuntimeInvisibleAnnotationsAttribute extends Annotations {
    public RuntimeInvisibleAnnotationsAttribute() {
        super( RuntimeInvisibleAnnotations, false );
    }
    
    public static RuntimeInvisibleAnnotationsAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        RuntimeInvisibleAnnotationsAttribute attr = new RuntimeInvisibleAnnotationsAttribute();
        attr.parseAnnotations( pool, loader, in );
        return attr;
    }
}
