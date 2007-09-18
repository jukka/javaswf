package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.*;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;

/**
 * Runtime visible parameter annotations
 *
 * @author nickmain
 */
public class RuntimeVisibleParameterAnnotationsAttribute extends ParameterAnnotationAttribute {
    public RuntimeVisibleParameterAnnotationsAttribute() {
        super( RuntimeVisibleParameterAnnotations, true );
    }
    
    public static RuntimeVisibleParameterAnnotationsAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        RuntimeVisibleParameterAnnotationsAttribute attr = new RuntimeVisibleParameterAnnotationsAttribute();
        attr.parseAnnotations( pool, loader, in );
        return attr;
    }
}
