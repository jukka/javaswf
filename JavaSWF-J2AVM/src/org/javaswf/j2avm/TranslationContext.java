package org.javaswf.j2avm;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.ModelFactory;

/**
 * The context of the translation.  Translation steps should look to
 * this object for their environment.
 *
 * @author nickmain
 */
public final class TranslationContext {

    /**
     * Log a debug message
     */
    public void debug( String message ) {
        if( debug ) J2AVM.log.finest( message );
    }

    /**
     * Add a class to be translated (if not already translated or enqueued).
     * @param className the fully qualified class name
     */
    public void addClass( String className ) {
        
        //skip already enqueued or translated classes
        if( classesToBeTranslated.contains( className ) 
         || translatedClasses    .contains( className ) ) {
            return;
        }
        
        classesToBeTranslated.add( className );
    }
    
    /**
     * Get the next class to be translated
     * 
     * @return the class name, null if no more classes remain
     */
    public ClassModel nextClassToBeTranslated() {
        if( classesToBeTranslated.isEmpty() ) return null;
        String className = classesToBeTranslated.iterator().next();
        classesToBeTranslated.remove( className );
        return factory.modelForName( className );
    }

    public final ModelFactory factory;
    
    /**
     * True if debug messages should be generated
     */
    public final boolean debug;

    private final Set<String> classesToBeTranslated = new HashSet<String>();
    private final Set<String> translatedClasses     = new HashSet<String>();

    
    /*pkg*/ TranslationContext( ClassLoader loader ) {  
        factory = new ModelFactory( loader );
        debug = J2AVM.log.isLoggable( Level.FINEST );
    }

}
