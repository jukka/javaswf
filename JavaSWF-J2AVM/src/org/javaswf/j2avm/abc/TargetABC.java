package org.javaswf.j2avm.abc;

import java.util.HashMap;
import java.util.Map;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationTarget;
import org.javaswf.j2avm.model.ClassModel;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;

/**
 * Represents the target ABC data.
 *
 * @author nickmain
 */
public class TargetABC {

	public TargetABC( TranslationContext context ) {
		this.context = context;
	}
		
    /**
     * Write the ABC data to the given target.
     */
    public void writeToTarget( TranslationTarget target ) {
        target.insertABC( abcFile );
    }

    /**
     * Get the translated class for the given Java class
     * 
     * @param javaClass the Java class
     */
    public ClassTranslation forJavaClass( ClassModel javaClass ) {
    	String className = javaClass.type.name;
    	
    	ClassTranslation ct = translatedClasses.get( className );
    	
    	if( ct == null ) {
    		ct = new ClassTranslation( this, javaClass );
    		translatedClasses.put( className, ct );
    	}
    	
    	return ct;
    }

    /**
     * Get the translated class for the given Java class name
     * 
     * @param className the Java class
     * @return null if no such class was translated
     */
    public ClassTranslation forJavaClass( String className ) {
    	return translatedClasses.get( className );
    }
    
    /*pkg*/ final AVM2ABCFile abcFile = new AVM2ABCFile();    
	/*pkg*/ final TranslationContext context;
    
    /**
     * Map of Java class name to AVM2 class
     */
    private final Map<String, ClassTranslation> translatedClasses = 
        new HashMap<String, ClassTranslation>();    
}
