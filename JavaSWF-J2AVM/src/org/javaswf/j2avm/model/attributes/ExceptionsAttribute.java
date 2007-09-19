package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ObjectType;


/**
 * The Exceptions attribute
 *
 * @author nickmain
 */
public class ExceptionsAttribute extends AttributeModel {

    /**
     * Immutable set of the thrown exception types
     */
    public final Collection<ObjectType> thrownExceptions;
    
    /**
     * @param exceptionTypes the exception types thrown by the method
     */
    public ExceptionsAttribute( ObjectType...exceptionTypes ) {
        super( AttributeName.Exceptions.name() );
        thrownExceptions = Collections.unmodifiableCollection( Arrays.asList( exceptionTypes ));
    }
    
    public static ExceptionsAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        
        int count = in.readUnsignedShort();
        ObjectType[] thrownExceptions = new ObjectType[ count ];
        for (int i = 0; i < count; i++) {
            int index = in.readUnsignedShort();
            thrownExceptions[i] = new ObjectType( pool.getClassName( index ));
        }
        
        return new ExceptionsAttribute( thrownExceptions );
    }
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        out.println( name + " {" );
        out.indent();
        for( ObjectType ex : thrownExceptions ) {
            out.println( ex.name );
        }
        out.unindent();
        out.println( "}" );
    }
}
