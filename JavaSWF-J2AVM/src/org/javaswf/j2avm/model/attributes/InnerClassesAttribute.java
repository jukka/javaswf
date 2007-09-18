package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.InnerClasses;

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
        super( InnerClasses.name() );
        this.innerClasses = Collections.unmodifiableCollection( Arrays.asList( innerClasses ) );
    }
    
    public static InnerClassesAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        
        int count = in.readUnsignedShort();
        
        InnerClass[] inners = new InnerClass[ count ];
        for (int i = 0; i < inners.length; i++) {
            
            int innerIndex = in.readUnsignedShort();
            int outerIndex = in.readUnsignedShort();
            int innerName  = in.readUnsignedShort();
            int flags      = in.readUnsignedShort();
            
            InnerClass ic = 
                new InnerClass( pool.getUTF8Value( innerName ), 
                                flags,
                                (outerIndex != 0) ? 
                                    new JClassReference( loader, pool.getClassName(outerIndex)) : 
                                    null,
                                new JClassReference( loader, pool.getClassName( innerIndex )));
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
