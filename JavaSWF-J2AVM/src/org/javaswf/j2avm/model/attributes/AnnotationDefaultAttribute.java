package org.javaswf.j2avm.model.attributes;


import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;

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
        super( AttributeName.AnnotationDefault.name() );
        this.value = value;
    }
    
    public static AnnotationDefaultAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        
        return new AnnotationDefaultAttribute( AnnotationAttribute.parseValue( pool, in ));
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.print( name + " = " );
        value.dump( out );
        out.println();
    }    
}
