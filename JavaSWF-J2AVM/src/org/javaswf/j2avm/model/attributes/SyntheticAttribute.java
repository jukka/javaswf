package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.visitor.AttributeVisitor;

/**
 * The synthetic attribute
 *
 * @author nickmain
 */
public class SyntheticAttribute extends AttributeModel {
    
    public SyntheticAttribute() {
        super( AttributeModel.Name.Synthetic.name() );
    }
    
    public static SyntheticAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        return new SyntheticAttribute();
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name );
    }

    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        visitor.attrSynthetic();
    }
}
