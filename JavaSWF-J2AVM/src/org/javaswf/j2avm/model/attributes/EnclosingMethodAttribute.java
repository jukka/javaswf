package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.jclass.JAttribute;
import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.io.internal.ConstantPool;
import org.epistem.jclass.reference.JClassReference;
import org.epistem.jclass.reference.JMethodReference;

import static org.epistem.jclass.JAttribute.Name.*;

/**
 * The synthetic attribute
 *
 * @author nickmain
 */
public class EnclosingMethodAttribute extends AttributeModel {

    /** The enclosing class */
    public final JClassReference enclosingClass;
    
    /** The enclosing method (may be null) */
    public final JMethodReference enclosingMethod;
    
    public EnclosingMethodAttribute( JClassReference enclosingClass, JMethodReference enclosingMethod ) {
        super( EnclosingMethod.name() );
        this.enclosingMethod = enclosingMethod;
        this.enclosingClass  = enclosingClass;
    }
    
    public static EnclosingMethodAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {        
        int classIndex    = in.readUnsignedShort();
        int nameTypeIndex = in.readUnsignedShort();
        
        JClassReference    enclosingClass  = new JClassReference( loader, pool.getClassName( classIndex ));
        JMethodReference enclosingMethod = null;
        
        if( nameTypeIndex != 0 ) {
            ConstantPool.NameAndTypeEntry nameType = 
                (ConstantPool.NameAndTypeEntry) pool.getEntry( nameTypeIndex );
            
            String methodSig   = pool.getUTF8Value( nameType.typeIndex );
            String methodName  = pool.getUTF8Value( nameType.nameIndex );
            
            String[] types = ConstantPool.readSignature( methodSig );
            JClassReference retType = new JClassReference( loader, types[0] );
            
            JClassReference[] paramTypes = new JClassReference[ types.length - 1 ];
            for (int i = 0; i < paramTypes.length; i++) {
                paramTypes[i] = new JClassReference( loader, types[i+1] );
            }
            
            enclosingMethod = new JMethodReference( enclosingClass, methodName, retType, paramTypes );
        }
        
        return new EnclosingMethodAttribute( enclosingClass, enclosingMethod );
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.print( name + " = " );
        
        if( enclosingMethod != null ) {
            enclosingMethod.dump( out );
        } else {
            out.print( enclosingClass );
        }
        
        out.println();
    }
}
