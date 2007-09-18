package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.*;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;

/**
 * Runtime invisible parameter annotations
 *
 * @author nickmain
 */
public class RuntimeInvisibleParameterAnnotationsAttribute extends ParameterAnnotationAttribute {
    public RuntimeInvisibleParameterAnnotationsAttribute() {
        super( RuntimeInvisibleParameterAnnotations, false );
    }
    
    public static RuntimeInvisibleParameterAnnotationsAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        RuntimeInvisibleParameterAnnotationsAttribute attr = new RuntimeInvisibleParameterAnnotationsAttribute();
        attr.parseAnnotations( pool, loader, in );
        return attr;
    }
}
