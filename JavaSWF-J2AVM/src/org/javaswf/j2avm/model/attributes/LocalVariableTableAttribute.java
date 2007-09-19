package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ValueType;


/**
 * The local-variable-table attribute
 *
 * @author nickmain
 */
public class LocalVariableTableAttribute extends AttributeModel {
    
    /** The local var info */
    public final Collection<LocalVariableInfo> localVariables = new HashSet<LocalVariableInfo>();
     
    public LocalVariableTableAttribute( LocalVariableInfo...localVariableInfos ) {
        super( AttributeName.LocalVariableTable.name() );
        
        localVariables.addAll( Arrays.asList( localVariableInfos ));
    }
    
    public static LocalVariableTableAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        
        int count = in.readUnsignedShort();
        LocalVariableInfo[] infos = new LocalVariableInfo[ count ];
        
        for (int i = 0; i < infos.length; i++) {
            int startOffset  = in.readUnsignedShort();
            int offsetLength = in.readUnsignedShort();
            int nameIndex    = in.readUnsignedShort();
            int typeIndex    = in.readUnsignedShort();
            int varIndex     = in.readUnsignedShort();
            
            infos[i] = new LocalVariableInfo( startOffset, offsetLength,
                                              pool.getUTF8Value( nameIndex ),
                                              ValueType.fromName( pool.getTypeName( typeIndex )),
                                              varIndex );
        }
        
        return new LocalVariableTableAttribute( infos );
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name + " {" );
        out.indent();
        
        for ( LocalVariableInfo info : localVariables ) {
            info.dump( out );
            out.println();
        }
        
        out.unindent();
        out.println( "}" );
    }
}
