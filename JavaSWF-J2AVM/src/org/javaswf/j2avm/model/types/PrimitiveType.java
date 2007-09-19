package org.javaswf.j2avm.model.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


/**
 * A primitive type
 *
 * @author nickmain
 */
public final class PrimitiveType extends ValueType {

    public static final PrimitiveType BYTE    = new PrimitiveType( "byte" );
    public static final PrimitiveType BOOLEAN = new PrimitiveType( "boolean" );
    public static final PrimitiveType SHORT   = new PrimitiveType( "short" );
    public static final PrimitiveType CHAR    = new PrimitiveType( "char" );
    public static final PrimitiveType INT     = new PrimitiveType( "int" );
    public static final PrimitiveType FLOAT   = new PrimitiveType( "float" );
    public static final PrimitiveType LONG    = new PrimitiveType( "long" );
    public static final PrimitiveType DOUBLE  = new PrimitiveType( "double" );
    
    //map for looking up types by name
    private static final Map<String, PrimitiveType> types = 
    	new HashMap<String, PrimitiveType>();
    
    static {
    	for( Field f : PrimitiveType.class.getDeclaredFields() ) {
    		if( PrimitiveType.class.isAssignableFrom( f.getType() )
    		 && Modifier.isStatic( f.getModifiers() )) {
    			
    			try {
    				PrimitiveType pt = (PrimitiveType) f.get(null);
    				types.put( pt.name, pt );
    				
    			} catch( Exception ex ) {
    				throw new RuntimeException( ex );
    			}
    		}
    	}
    }
    
    private PrimitiveType( String name ) {
        super( name );
    }
    
    /**
     * Whether a name is a primitive type name
     */
    public static boolean isPrimitiveTypeName( String name ) {
    	return types.containsKey( name );
    }
    
    /**
     * Get a PrimitiveType from its name.
     */
    public static PrimitiveType fromName( String name ) {
    	PrimitiveType type = types.get( name );
    	
    	if( type == null ) {
    		throw new IllegalArgumentException( "Not a primitive type: " + name );
    	}
    	
    	return type;
    }
}
