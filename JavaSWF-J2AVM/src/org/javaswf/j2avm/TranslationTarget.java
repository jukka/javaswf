package org.javaswf.j2avm;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2Class;

/**
 * Represents an output target for the translation process.
 * Methods are called in order:
 *   insertABC
 *   setMainClass (optional)
 *   finish
 *
 * @author nickmain
 */
public interface TranslationTarget {

    /**
     * Return an interface to receive ABC data to be inserted into the target.
     */
    public ABC insertABC();
    
    /**
     * Set the main class to be instantiated or linked to the 
     * main timeline.
     * 
     * @param mainClass the AVM2 class
     */
    public void setMainClass( AVM2Class mainClass );
    
    /**
     * Finish writing the target
     */
    public void finish();
}
