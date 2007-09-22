package org.javaswf.j2avm.model;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.flags.FieldFlag;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Model of a parsed Java field
 *
 * @author nickmain
 */
public final class FieldModel {

	/** The field name */
	public final String name;
	
	/** The field type */
	public final ValueType type;
	
	/** The field flags */
	public final Collection<FieldFlag> flags;
	
	/** Attributes by name */
	public final Map<AttributeName,AttributeModel> attributes = 
		new EnumMap<AttributeName,AttributeModel>(AttributeName.class);
	
	/**
	 * @param name the field name
	 * @param type the field type
	 * @param flags the field flags
	 */
	public FieldModel( String name, ValueType type, Collection<FieldFlag> flags ) {
		this.flags = flags;
		this.name  = name;
		this.type  = type;
	}
	
	/**
	 * Parse a field
	 */
	/*pkg*/ FieldModel( DataInput in, ConstantPool pool ) throws IOException {
        
		int flagBits = in.readUnsignedShort();
        int nameIdx  = in.readUnsignedShort();
        int typeIdx  = in.readUnsignedShort();
        
        name  = pool.getUTF8Value( nameIdx );
        flags = FieldFlag.parser.parse( flagBits );
        
        String typeName = pool.getTypeName(typeIdx);        
        type = ValueType.fromName( typeName );
                
        int numAttrs = in.readUnsignedShort();       
        for (int i = 0; i < numAttrs; i++) {
        	AttributeModel.parseAttr( attributes, in, pool );
        }
	}
	
    /**
     * Dump the model
     */
    public void dump( IndentingPrintWriter ipw ) {
    	ipw.print( "field " + name + " : " + type + " " + flags );
    	
    	if( ! attributes.isEmpty()) {
    		ipw.println( " {" );
    		ipw.indent();

    		for( AttributeModel a : attributes.values() ) {
        		ipw.println();
        		a.dump( ipw );    		    		
        	}
    		
    		ipw.unindent();
    		ipw.print( "}" );
    	}
    	
    	ipw.println();
    }
}
