package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.attributes.InnerClass.InnerClassFlag;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ObjectType;



/**
 * The inner-classes attribute
 *
 * @author nickmain
 */
public class InnerClassesAttribute extends AttributeModel {

    /**
     * Immutable collection of inner classes
     */
    public final Collection<InnerClass> innerClasses;
    
    public InnerClassesAttribute( InnerClass...innerClasses ) {
        super( AttributeName.InnerClasses.name() );
        this.innerClasses = Collections.unmodifiableCollection( Arrays.asList( innerClasses ) );
    }
    
    public static InnerClassesAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        
        int count = in.readUnsignedShort();
        
        InnerClass[] inners = new InnerClass[ count ];
        for (int i = 0; i < inners.length; i++) {
            
            int innerIndex = in.readUnsignedShort();
            int outerIndex = in.readUnsignedShort();
            int innerName  = in.readUnsignedShort();
            int flagBits   = in.readUnsignedShort();
            Collection<InnerClassFlag> flags = 
                InnerClassFlag.parser.parse( flagBits );
            
            InnerClass ic = 
                new InnerClass( pool.getUTF8Value( innerName ), 
                                (outerIndex != 0) ? 
                                    new ObjectType( pool.getClassName(outerIndex)) : 
                                    null,
                                new ObjectType( pool.getClassName( innerIndex )),
                                flags );
            inners[i] = ic;
        }
        
        return new InnerClassesAttribute( inners );
    }
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        out.println( name + " {" );
        out.indent();
        
        for( InnerClass ic : innerClasses ) {
            ic.dump( out );
        }
        
        out.unindent();
        out.println( "}" );
    }

}
