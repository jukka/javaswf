package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;

/**
 * A constant field value
 *
 * @author nickmain
 */
public class ConstantValueAttribute extends AttributeModel {

    /**
     * The constant value.
     * Number or String
     */
    public final Object value;
    
    public ConstantValueAttribute( Number value ) {
        super( AttributeName.ConstantValue.name() );
        this.value = value;
    }

    public ConstantValueAttribute( String value ) {
        super( AttributeName.ConstantValue.name() );
        this.value = value;
    }
    
    public static ConstantValueAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        int index = in.readUnsignedShort();
        
        Object value = pool.getConstant( index );
        
        if( value instanceof Number ) return new ConstantValueAttribute( (Number) value );
        if( value instanceof String ) return new ConstantValueAttribute( (String) value );
        
        throw new IOException( "Field constant value invalid type " + value.getClass().getName() );
    }
    
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        if( value instanceof String ) {
            out.print( name + " = " );
            out.writeDoubleQuotedString( (String) value );
            out.println();
        } else {
            out.println( name + " = " + value );            
        }        
    }
}
