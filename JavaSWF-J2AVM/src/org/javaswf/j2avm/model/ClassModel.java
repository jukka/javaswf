package org.javaswf.j2avm.model;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.flags.ClassFlag;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.Signature;

/**
 * A model of the parsed form of a class-file.
 *
 * @author nickmain
 */
public final class ClassModel extends Model {
	
	/**
	 * The class type
	 */
	public final ObjectType type;
	
	/** Implemented interfaces */
	public final Collection<ObjectType> interfaces = new HashSet<ObjectType>();
	
	/** Fields by name */
	public final Map<String,FieldModel> fields = new HashMap<String,FieldModel>();

	/** Methods by signature */
	public final Map<Signature,MethodModel> methods = new HashMap<Signature, MethodModel>();
    
	/** The class flags */
	public final Collection<ClassFlag> flags;

	/** Superclass - may be null */
    public final ObjectType superclass;
    
    public final int majorVersion;
    public final int minorVersion;
    
    /**
     * @param type the class type
     * @param superclass the superclass - null if none
     * @param majorVersion the class major version
     * @param minorVersion the class minor version
     * @param flags the class flags
     */
    public ClassModel( ObjectType type, ObjectType superclass, 
    	               int majorVersion, int minorVersion,
    	               Collection<ClassFlag> flags ) {
    	this.type         = type;
    	this.superclass   = superclass;
    	this.majorVersion = majorVersion;
    	this.minorVersion = minorVersion;
    	this.flags        = flags;
    }
    
    /**
     * Parse a class from classfile data.
     * 
     * @param in the class data - will be closed after parsing
     */
    public ClassModel( InputStream instream ) {
    	
    	DataInput in = new DataInputStream( instream );
    	
        try {            
            in.readInt(); //magic 
            minorVersion = in.readShort();
            majorVersion = in.readShort();

            ConstantPool pool = new ConstantPool( in );
            
            int flagBits  = in.readShort();
            int thisclass = in.readShort();

            String name  = pool.getClassName( thisclass );
            type = new ObjectType( name );
            flags = ClassFlag.parser.parse( flagBits );            
            
            int superclassIdx = in.readShort();
            superclass = (superclassIdx == 0) ?
            		         null :
            		         new ObjectType( pool.getClassName( superclassIdx ));
            
            //interfaces
            int ifcount  = in.readUnsignedShort();
            for (int i = 0; i < ifcount; i++) {
                int index = in.readShort();                

                String ifaceName = pool.getClassName( index );
                interfaces.add( new ObjectType( ifaceName ));
            }
            
            //fields
            int fieldcount = in.readUnsignedShort();
            for (int i = 0; i < fieldcount; i++) {
                FieldModel field = new FieldModel( this, in, pool );
                fields.put( field.name, field );
            }
            
            //methods
            int methodcount = in.readUnsignedShort();
            for (int i = 0; i < methodcount; i++) {
                MethodModel method = new MethodModel( this, in, pool );
                methods.put( method.signature, method );
            }
            
            //attributes
            int numAttrs = in.readUnsignedShort();            
            for (int i = 0; i < numAttrs; i++) {
            	AttributeModel.parseAttr( attributes, in, pool );
            }
            
        } catch( IOException ioe ) {
            throw new RuntimeException( ioe );
        } finally {
        	try{ 
        		instream.close();
            } catch( IOException ioe ) {
                //nada
        	}
        }
    }
        
    /**
     * Dump the model
     */
    public void dump( IndentingPrintWriter ipw ) {
    	ipw.println( "class " + type + " {");
    	ipw.indent();
    	
    	ipw.println( "versn: " + majorVersion + "." + minorVersion );
    	ipw.println( "flags: " + flags );
    	
    	if( superclass != null ) {
    		ipw.println( "super: " + superclass );
    	}
    	
    	for( ObjectType iface : interfaces ) {
    		ipw.println( "iface: " + iface );
    	}
    	
    	for( FieldModel f : fields.values() ) {
    		ipw.println();
    		f.dump( ipw );
    	}
    	
    	for( MethodModel m : methods.values() ) {
    		ipw.println();
    		m.dump( ipw );    		
    	}
    	
    	if( ! attributes.isEmpty() ) {
    		ipw.println();
    		ipw.println( "attributes {" );
    		ipw.indent();
    		
	    	for( AttributeModel a : attributes.values() ) {
	    		a.dump( ipw );    		    		
	    	}
	    	
	    	ipw.unindent();
	    	ipw.println( "}" );
    	}
    	
    	ipw.unindent();
    	ipw.println( "}" );
    }
}
