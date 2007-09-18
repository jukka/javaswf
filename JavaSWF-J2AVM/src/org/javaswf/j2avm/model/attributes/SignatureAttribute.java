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
public class SignatureAttribute extends AttributeModel {
    
    /** The signature */
    public final String signature;
    
    public SignatureAttribute( String signature ) {
        super( Signature.name() );
        this.signature = signature;
    }
    
    public static SignatureAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {        
        int index = in.readUnsignedShort();
        return new SignatureAttribute( pool.getUTF8Value( index ));
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name + " = " + signature );
    }
}
