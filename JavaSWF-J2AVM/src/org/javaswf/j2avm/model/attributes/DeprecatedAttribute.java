package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.visitor.AttributeVisitor;


/**
 * The deprecated attribute
 *
 * @author nickmain
 */
public class DeprecatedAttribute extends AttributeModel {
    
    public DeprecatedAttribute() {
        super( AttributeModel.Name.Deprecated.name() );
    }
    
    public static DeprecatedAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        return new DeprecatedAttribute();
    }

    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name );
    }

    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        visitor.attrDeprecated();        
    }
}
