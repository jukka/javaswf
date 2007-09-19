package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.javaswf.j2avm.model.parser.ConstantPool;


/**
 * Runtime invisible parameter annotations
 *
 * @author nickmain
 */
public class RuntimeInvisibleParameterAnnotationsAttribute extends ParameterAnnotationAttribute {
    public RuntimeInvisibleParameterAnnotationsAttribute() {
        super( AttributeName.RuntimeInvisibleParameterAnnotations, false );
    }
    
    public static RuntimeInvisibleParameterAnnotationsAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        RuntimeInvisibleParameterAnnotationsAttribute attr = new RuntimeInvisibleParameterAnnotationsAttribute();
        attr.parseAnnotations( pool, in );
        return attr;
    }

}
