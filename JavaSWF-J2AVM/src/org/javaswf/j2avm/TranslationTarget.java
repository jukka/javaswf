package org.javaswf.j2avm;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
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
     * Insert ABC data into the target.
     */
    public void insertABC( AVM2ABCFile abc );
    
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
