package org.javaswf.j2avm.model;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collection;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.flags.FieldFlag;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Model of a parsed Java field
 *
 * @author nickmain
 */
public final class FieldModel extends Model {

	/** The field name */
	public final String name;
	
	/** The field type */
	public final ValueType type;
	
	/** The field flags */
	public final Collection<FieldFlag> flags;

	/** The owning class */
	public final ClassModel owner;
	
	/**
	 * @param name the field name
	 * @param type the field type
	 * @param flags the field flags
	 */
	public FieldModel( String name, ValueType type, Collection<FieldFlag> flags,
			           ClassModel owner ) {
		this.flags = flags;
		this.name  = name;
		this.type  = type;
		this.owner = owner;
	}
	
	/**
	 * Determine the visibility of the field
	 */
	public Visibility visibility() {
		if( flags.contains( FieldFlag.FieldIsPublic    )) return Visibility.PUBLIC;
		if( flags.contains( FieldFlag.FieldIsProtected )) return Visibility.PROTECTED;	
		if( flags.contains( FieldFlag.FieldIsPrivate   )) return Visibility.PRIVATE;
		return Visibility.PACKAGE;
	}
	
	/**
	 * Parse a field
	 */
	/*pkg*/ FieldModel( ClassModel owner, DataInput in, ConstantPool pool ) throws IOException {
        
		this.owner = owner;
		
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
