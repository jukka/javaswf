package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.jclass.JAttribute;
import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;

import static org.epistem.jclass.JAttribute.Name.*;

/**
 * The deprecated attribute
 *
 * @author nickmain
 */
public class DeprecatedAttribute extends AttributeModel {
    
    public DeprecatedAttribute() {
        super( Deprecated.name() );
    }
    
    public static DeprecatedAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        return new DeprecatedAttribute();
    }

    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name );
    }
}
