package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.Signature;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * The synthetic attribute
 *
 * @author nickmain
 */
public class EnclosingMethodAttribute extends AttributeModel {

    /** The enclosing class */
    public final ObjectType enclosingClass;
    
    /** The enclosing method (may be null) */
    public final MethodDescriptor enclosingMethod;
    
    public EnclosingMethodAttribute( ObjectType enclosingClass, MethodDescriptor enclosingMethod ) {
        super( AttributeName.EnclosingMethod.name() );
        this.enclosingMethod = enclosingMethod;
        this.enclosingClass  = enclosingClass;
    }
    
    public static EnclosingMethodAttribute parse( ConstantPool pool, DataInput in ) throws IOException {        
        int classIndex    = in.readUnsignedShort();
        int nameTypeIndex = in.readUnsignedShort();
        
        ObjectType       enclosingClass  = new ObjectType( pool.getClassName( classIndex ));
        MethodDescriptor enclosingMethod = null;
        
        if( nameTypeIndex != 0 ) {
            ConstantPool.NameAndTypeEntry nameType = 
                (ConstantPool.NameAndTypeEntry) pool.getEntry( nameTypeIndex );
            
            String methodSig   = pool.getUTF8Value( nameType.typeIndex );
            String methodName  = pool.getUTF8Value( nameType.nameIndex );
            
            String[] types = ConstantPool.readSignature( methodSig );
            JavaType retType = JavaType.fromName( types[0] );
            
            ValueType[] paramTypes = new ValueType[ types.length - 1 ];
            for (int i = 0; i < paramTypes.length; i++) {
                paramTypes[i] = ValueType.fromName( types[i+1] );
            }
            
            Signature sig = new Signature( methodName, paramTypes );
            enclosingMethod = new MethodDescriptor( enclosingClass, sig, retType );
        }
        
        return new EnclosingMethodAttribute( enclosingClass, enclosingMethod );
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.print( name + " = " );
        
        if( enclosingMethod != null ) {
            out.print( enclosingMethod );
        } else {
            out.print( enclosingClass );
        }
        
        out.println();
    }
}
