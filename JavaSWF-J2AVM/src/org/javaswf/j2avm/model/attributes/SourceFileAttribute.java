package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.visitor.AttributeVisitor;
import org.javaswf.j2avm.model.visitor.ClassAttributeVisitor;

/**
 * The SourceFile attribute
 *
 * @author nickmain
 */
public class SourceFileAttribute extends AttributeModel {
    
    /** The source file name */
    public final String filename;
    
    public SourceFileAttribute( String filename ) {
        super( AttributeModel.Name.SourceFile.name() );
        this.filename = filename;
    }
    
    public static SourceFileAttribute parse( ConstantPool pool, DataInput in ) throws IOException {        
        int index = in.readUnsignedShort();
        return new SourceFileAttribute( pool.getUTF8Value( index ));
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name + " = " + filename );
    }

    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        if( visitor instanceof ClassAttributeVisitor ) {
            ((ClassAttributeVisitor) visitor).attrSourceFile( filename );
        }        
    }
}
