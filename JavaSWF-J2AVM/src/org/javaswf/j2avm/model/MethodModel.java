package org.javaswf.j2avm.model;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collection;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
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
public final class MethodModel extends Model {

	/** Name for constructor methods */
	public static final String CONSTRUCTOR_NAME = "<init>";
	
	/** Name for static initializer method */
	public static final String STATIC_INIT_NAME = "<clinit>";
	
	public final Signature signature;
	
	public final JavaType returnType;
		
	/** The method flags */
	public final Collection<MethodFlag> flags;

	/** The owning class */
	public final ClassModel owner;

	/**
	 * @param sig the method signature
	 * @param returnType the return type
	 * @param flags the method flags
	 */
	public MethodModel( ClassModel owner, Signature sig, ValueType returnType, 
			            Collection<MethodFlag> flags ) {
		this.flags      = flags;
		this.signature  = sig;
		this.returnType = returnType;
		this.owner      = owner;
	}
	
	/**
	 * Get the code attribute
	 * @return null if there is no code (method is abstract or native)
	 */
	public CodeAttribute code() {
		return attribute( CodeAttribute.class );
	}
	
	/**
	 * Determine the visibility of the method
	 */
	public Visibility visibility() {
		if( flags.contains( MethodFlag.MethodIsPublic    )) return Visibility.PUBLIC;
		if( flags.contains( MethodFlag.MethodIsProtected )) return Visibility.PROTECTED;	
		if( flags.contains( MethodFlag.MethodIsPrivate   )) return Visibility.PRIVATE;
		return Visibility.PACKAGE;
	}
	
	/**
	 * Parse a method
	 */
	/*pkg*/ MethodModel( ClassModel owner, DataInput in, ConstantPool pool ) throws IOException {
        
		this.owner = owner; 
		
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
        
        //--build statements
        CodeAttribute code = code();
//        if( code != null ) {
//            new StatementConstructor( code.statements, this ).perform();
//        }
	}
	
    /**
     * Dump the model
     */
    public void dump( IndentingPrintWriter ipw ) {
    	ipw.print( "method " + signature + " : " + returnType + " " + flags );
    	
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
    
    /**
     * Determine whether a method is an override. This is computed each time
     * since methods added to a superclass may change this flag.
     * 
     * @return true if the method overrides a super method
     */
    public boolean isOverride() {

        for( ClassModel superclass = owner.superclass(); 
             superclass != null;
             superclass = superclass.superclass() ) {
        	
            for( Signature superSig : superclass.methods.keySet() ) {
                if( superSig.equals( signature )) {
                    return true; //found matching method signature
                }
            }
        }
        
        return false;
    }
}
