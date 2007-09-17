package org.javaswf.j2avm.abc;

import org.javaswf.j2avm.TranslationTarget;

import com.anotherbigidea.flash.avm2.model.AVM2Class;

/**
 * Represents a class that had been translated to AVM2 and holds the details
 * of how the class was translated.
 *
 * @author nickmain
 */
public class TranslatedClass {

    /**
     * Set this class as the main class for the given target.
     */
    public void setAsMainClass( TranslationTarget target ) {
        target.setMainClass( avm2Class );
    }
    
    /**
     * The AVM2 class that represents the Java class.
     */
    private final AVM2Class avm2Class;
    
    /*pkg*/ TranslatedClass( AVM2Class avm2Class ) {
        this.avm2Class = avm2Class;
    }
}
