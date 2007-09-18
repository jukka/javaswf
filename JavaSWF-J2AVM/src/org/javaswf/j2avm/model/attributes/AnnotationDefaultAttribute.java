package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.AnnotationDefault;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.jclass.JAttribute;
import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;

/**
 * The default values for annotation methods
 *
 * @author nickmain
 */
public class AnnotationDefaultAttribute extends AttributeModel {

    /** The default value */
    public final AnnotationModel.Value value;
    
    /**
     * @param value the default value
     */
    public AnnotationDefaultAttribute( AnnotationModel.Value value ) {
        super( AnnotationDefault.name() );
        this.value = value;
    }
    
    public static AnnotationDefaultAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        
        return new AnnotationDefaultAttribute( AnnotationAttribute.parseValue( pool, loader, in ));
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.print( name + " = " );
        value.dump( out );
        out.println();
    }    
}
