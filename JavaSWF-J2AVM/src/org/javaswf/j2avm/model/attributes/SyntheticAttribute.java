package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;

/**
 * The synthetic attribute
 *
 * @author nickmain
 */
public class SyntheticAttribute extends AttributeModel {
    
    public SyntheticAttribute() {
        super( AttributeName.Synthetic.name() );
    }
    
    public static SyntheticAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        return new SyntheticAttribute();
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name );
    }
}
