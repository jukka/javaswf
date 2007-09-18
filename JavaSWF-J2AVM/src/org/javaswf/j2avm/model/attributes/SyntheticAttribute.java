package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.jclass.JAttribute;
import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;

import static org.epistem.jclass.JAttribute.Name.*;

/**
 * The synthetic attribute
 *
 * @author nickmain
 */
public class SyntheticAttribute extends AttributeModel {
    
    public SyntheticAttribute() {
        super( Synthetic.name() );
    }
    
    public static SyntheticAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        return new SyntheticAttribute();
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name );
    }
}
