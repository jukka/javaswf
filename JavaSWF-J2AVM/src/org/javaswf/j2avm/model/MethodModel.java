package org.javaswf.j2avm.model;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.Signature;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Model of a parsed Java method
 *
 * @author nickmain
 */
public final class MethodModel {

	public final Signature signature;
	
	public final JavaType returnType;
	
	
	/** The method flags */
	public final Collection<MethodFlag> flags;
	
	/** Attributes by name */
	public final Map<AttributeName,AttributeModel> attributes = 
		new EnumMap<AttributeName,AttributeModel>(AttributeName.class);

	/**
	 * @param sig the method signature
	 * @param returnType the return type
	 * @param flags the method flags
	 */
	public MethodModel( Signature sig, ValueType returnType, 
			            Collection<MethodFlag> flags ) {
		this.flags      = flags;
		this.signature  = sig;
		this.returnType = returnType;
	}
	
	/**
	 * Parse a method
	 */
	/*pkg*/ MethodModel( DataInput in, ConstantPool pool ) throws IOException {
        
        int flagBits = in.readUnsignedShort();
        int nameIdx  = in.readUnsignedShort();

        String name = pool.getUTF8Value( nameIdx );
        flags = MethodFlag.parser.parse( flagBits );    
        
        int sigIdx = in.readUnsignedShort();
        String sig = pool.getUTF8Value( sigIdx );
        
        String rt = sig.substring( sig.indexOf(")") + 1 );
        returnType = JavaType.fromName( ConstantPool.decodeTypeName( rt ) );
        
        sig = sig.substring(1,sig.indexOf(")") );        
        String[] ptypes = ConstantPool.readTypes( sig );
        
        ValueType[] params = new ValueType[ ptypes.length ];
        for (int p = 0; p < params.length; p++) {
            params[p] = ValueType.fromName( ptypes[p] );
        }

        signature = new Signature( name, params );
        
        int numAttrs = in.readUnsignedShort();       
        for (int i = 0; i < numAttrs; i++) {
        	AttributeModel.parseAttr( attributes, in, pool );
        }
	}
}
