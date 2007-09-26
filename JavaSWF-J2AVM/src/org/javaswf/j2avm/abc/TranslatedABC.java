package org.javaswf.j2avm.abc;

import java.util.HashMap;
import java.util.Map;

import org.javaswf.j2avm.TranslationTarget;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;

/**
 * Represents the target ABC data.
 *
 * @author nickmain
 */
public class TranslatedABC {

    /**
     * Write the ABC data to the given target.
     */
    public void writeToTarget( TranslationTarget target ) {
        ABC abc = target.insertABC();
        abcFile.write( abc );
    }

    /**
     * Get the translated class for the given Java class name
     * @param className the Java class name
     * @return null if no such class was translated
     */
    public TranslatedClass forJavaClass( String className ) {
        return translatedClasses.get( className );
    }
    
    public final AVM2ABCFile abcFile = new AVM2ABCFile();
    
    /**
     * Map of Java class name to AVM2 class
     */
    private final Map<String, TranslatedClass> translatedClasses = 
        new HashMap<String, TranslatedClass>();    
}
