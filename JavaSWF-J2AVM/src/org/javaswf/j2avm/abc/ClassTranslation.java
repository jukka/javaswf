package org.javaswf.j2avm.abc;

import java.security.Signature;
import java.util.HashMap;
import java.util.Map;

import org.javaswf.j2avm.TranslationTarget;
import org.javaswf.j2avm.model.ClassModel;

import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * Represents a class that has been or may be translated to AVM2 and holds the 
 * details of how the class was/will be translated.
 *
 * @author nickmain
 */
public class ClassTranslation {

	/**
	 * The original Java class
	 */
	public final ClassModel javaClass;
	
    /**
     * The qualifies class name
     */
    public final AVM2QName className;    
    
    /**
     * The namespace for protected methods
     */
    public final AVM2Namespace protectedNamespace;
    
    /**
     * The superclass name
     */
    public final AVM2QName superName;
    
    /**
     * The scope depth of the class definition
     */
    public int classScopeDepth;      

    public ClassTranslation( ClassModel javaClass ) {
    	this.javaClass = javaClass;
    	this.className = nameForJavaClass( javaClass.type.name );
    	
    	if( javaClass.superclass != null ) {
    		String supername = javaClass.superclass.name;
    		this.superName = nameForJavaClass( supername );
    	}
    	else {
    		this.superName = null;
    	}
    	    	
    	String protName = className.namespace.name + ":" + className.name;
    	this.protectedNamespace = 
    		new AVM2Namespace( NamespaceKind.ProtectedNamespace, protName );
    }
    
    /**
     * Set this class as the main class for the given target.
     */
    public void setAsMainClass( TranslationTarget target ) {
        target.setMainClass( avm2Class );
    }
    
    /**
     * Get or make the target AVM2Class.
     */
    public AVM2Class avm2Class() {
    	if( avm2Class == null ) {
    		
    	}
    	
    	return avm2Class;
    }
    
    /**
     * Get or generate the AVM2 name for the given Java class.
     * 
     * TODO: this would be the place to plug in some sort of strategy/factory
     *       for generating names
     * 
     * @param className the Java class name
     */
    private static final AVM2QName nameForJavaClass( String className ) {
		if( className.startsWith( "flash.Flash" ) ) {
			className = className.substring( 11 );
		}
    		
		return new AVM2QName( className );
    }
    
    
    /**
     * The AVM2 class that represents the Java class.
     */
    private AVM2Class avm2Class;

    private Map<Signature, MethodTranslation> methods = new HashMap<Signature, MethodTranslation>();
}
