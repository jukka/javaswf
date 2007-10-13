package org.javaswf.j2avm.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A caching factory for Class models.
 * 
 * @author nickmain
 */
public class ModelFactory {

	/**
	 * @param loader the loader to use when finding class files
	 */
    public ModelFactory( ClassLoader loader ) {
    	this.loader = loader;
    }
	
    /**
     * Get a cached class model, or create a new one and cache it.
     * 
     * @param className the fully qualified class name
     * @return the class model
     */
    public ClassModel modelForName( String className ) {
        ClassModel model = classModels.get( className );
        if( model == null ) {
            String filename = className.replace( '.', '/' ) + ".class";
            InputStream in = findDataStream( filename );
            
            if( in == null ) {
                throw new RuntimeException( "Could not find class file for " + className );
            }
            
            model = new ClassModel( this, in );
            classModels.put( className, model );
        }
        
        return model;
    }
	
	/**
	 * Get a method from a descriptor. Will search up the inheritance
	 * chain until the method is found.
	 * 
	 * @param desc the method descriptor
	 * 
	 * @return null if the referenced method could not be found
	 */
	public final MethodModel methodFor( MethodDescriptor desc ) {	
		return methodFor( desc.owner, desc );
	}
    
    /**
     * Find the given resource stream
     */
    private InputStream findDataStream( String filename ) {
        return loader.getResourceAsStream( filename );
    }
	
	private MethodModel methodFor( ObjectType type, MethodDescriptor desc ) {
		ClassModel clazz = modelForName( type.name );
		if( clazz == null ) return null;

		MethodModel mm = clazz.methods.get( desc.signature );		
		if( mm != null ) return mm;
		
		return methodFor( clazz.superclass, desc );
	}
	
    private final ClassLoader loader;
    
    private final Map<String , ClassModel> classModels = 
        new HashMap<String, ClassModel>();
    
    /*pkg*/ void register( ClassModel model ) {
    	classModels.put( model.type.name, model );
    }
}
