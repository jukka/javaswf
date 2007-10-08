package org.javaswf.j2avm.model.types;

/**
 * An Object type, but not arrays.
 *
 * @author nickmain
 */
public class ObjectType extends ObjectOrArrayType {

    public static final ObjectType OBJECT         = new ObjectType( "java.lang.Object" );
    public static final ObjectType STRING         = new ObjectType( "java.lang.String" );
    public static final ObjectType CLASS          = new ObjectType( "java.lang.Class" );
    public static final ObjectType STRING_BUIDLER = new ObjectType( "java.lang.StringBuilder" );
    public static final ObjectType THROWABLE      = new ObjectType( "java.lang.Throwable" );
    
    /**
     * The package name or empty string
     */
    public final String packageName;
    
    /**
     * The unqualified name of the class
     */
    public final String simpleName;
    
    /**
     * @param className the fully qualified name of the class
     */
    public ObjectType( String className ) {
        super( className, abbrev( className ) );
        
        int period = className.lastIndexOf( '.' );
        if( period < 0 ) {
            packageName = "";
            simpleName  = className;
            return;
        }
        
        packageName = className.substring( 0, period );
        simpleName  = className.substring( period + 1 );
    }   
    
    private static String abbrev( String name ) {
    	if( name.equals( "java.lang.String"    ) ) return "$";
    	if( name.equals( "java.lang.Throwable" ) ) return "T";
        if( name.equals( "java.lang.Exception" ) ) return "E";

    	return "O";
    }
}
