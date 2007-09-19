package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;

/**
 * The SourceFile attribute
 *
 * @author nickmain
 */
public class SourceFileAttribute extends AttributeModel {
    
    /** The source file name */
    public final String filename;
    
    public SourceFileAttribute( String filename ) {
        super( AttributeName.SourceFile.name() );
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
}
