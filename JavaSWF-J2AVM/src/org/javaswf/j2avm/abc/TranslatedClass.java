package org.javaswf.j2avm.abc;

import org.javaswf.j2avm.TranslationTarget;

import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * Represents a class that has been or may be translated to AVM2 and holds the 
 * details of how the class was/will be translated.
 *
 * @author nickmain
 */
public class TranslatedClass {

    /**
     * The AVM2 class that represents the Java class.
     */
    public final AVM2Class avm2Class;

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

    public TranslatedClass( AVM2Class avm2Class, 
    		                AVM2QName superName,
    		                AVM2Namespace protectedNS ) {
    	this.protectedNamespace = protectedNS;
    	this.avm2Class          = avm2Class;
    	this.superName          = superName;
    }
    
    /**
     * Set this class as the main class for the given target.
     */
    public void setAsMainClass( TranslationTarget target ) {
        target.setMainClass( avm2Class );
    }
}
