package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;


/**
 * The deprecated attribute
 *
 * @author nickmain
 */
public class DeprecatedAttribute extends AttributeModel {
    
    public DeprecatedAttribute() {
        super( AttributeName.Deprecated.name() );
    }
    
    public static DeprecatedAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        return new DeprecatedAttribute();
    }

    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name );
    }
}
