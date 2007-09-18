package org.javaswf.j2avm.model.attributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.util.Hex;
import org.javaswf.j2avm.model.visitor.AttributeVisitor;

/**
 * An unknown attribute.
 *
 * @author nickmain
 */
public class UnknownAttribute extends AttributeModel {

    private final byte[] data;
    
    /** The byte size of the data */
    public final int dataSize;
    
    /**
     * Get a stream for reading the data
     */
    public final InputStream data() {
        return new ByteArrayInputStream( data );
    }
    
    /**
     * Write the data to an output stream
     */
    public final void write( OutputStream out ) throws IOException {
        out.write( data );
    }
    
    /**
     * @param name the attribute name
     * @param data the attribute data - may be null
     */
    public UnknownAttribute( String name, byte[] data ) {
        super( name );
        
        if( data == null ) data = new byte[0];
        this.data     = data;
        this.dataSize = data.length;
    }
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        out.println( "unknown attribute: " + name + " {" );
        out.indent();        
        out.print( Hex.dump( data, 0, "" ));        
        out.unindent();
        out.println( "}" );        
    }

    /** @see org.javaswf.j2avm.model.attributes.AttributeModel#accept(org.javaswf.j2avm.model.visitor.AttributeVisitor) */
    @Override
    public void accept(AttributeVisitor visitor) {
        visitor.attrUnknown( name, data );
    }
}
