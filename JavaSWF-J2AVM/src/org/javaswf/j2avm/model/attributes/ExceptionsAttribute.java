package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.Exceptions;

import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.jclass.JAttribute;
import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;
import org.epistem.jclass.reference.JClassReference;

/**
 * The Exceptions attribute
 *
 * @author nickmain
 */
public class ExceptionsAttribute extends AttributeModel {

    /**
     * Immutable set of the thrown exception types
     */
    public final Collection<JClassReference> thrownExceptions;
    
    /**
     * @param exceptionTypes the exception types thrown by the method
     */
    public ExceptionsAttribute( JClassReference...exceptionTypes ) {
        super( Exceptions.name() );
        thrownExceptions = Collections.unmodifiableCollection( Arrays.asList( exceptionTypes ));
    }
    
    public static ExceptionsAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        
        int count = in.readUnsignedShort();
        JClassReference[] thrownExceptions = new JClassReference[ count ];
        for (int i = 0; i < count; i++) {
            int index = in.readUnsignedShort();
            thrownExceptions[i] = new JClassReference( loader, pool.getClassName( index ));
        }
        
        return new ExceptionsAttribute( thrownExceptions );
    }
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        out.println( name + " {" );
        out.indent();
        for( JClassReference ex : thrownExceptions ) {
            out.println( ex.className );
        }
        out.unindent();
        out.println( "}" );
    }
}
