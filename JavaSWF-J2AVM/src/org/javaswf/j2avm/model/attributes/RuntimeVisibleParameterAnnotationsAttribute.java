package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

/**
 * Runtime visible parameter annotations
 *
 * @author nickmain
 */
public class RuntimeVisibleParameterAnnotationsAttribute extends ParameterAnnotationAttribute {
    public RuntimeVisibleParameterAnnotationsAttribute() {
        super( AttributeName.RuntimeVisibleParameterAnnotations, true );
    }
    
    public static RuntimeVisibleParameterAnnotationsAttribute parse( org.javaswf.j2avm.model.parser.ConstantPool pool, DataInput in ) throws IOException {
        RuntimeVisibleParameterAnnotationsAttribute attr = new RuntimeVisibleParameterAnnotationsAttribute();
        attr.parseAnnotations( pool, in );
        return attr;
    }

}
