package org.javaswf.j2avm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.javaswf.j2avm.asm.ASMClassModelFactory;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.ClassModelFactory;

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
         || translatedClassNames.containsKey( className ) ) {
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
        return modelForName( className );
    }
    
    /**
     * Get a cached class model, or create a new one and cache it.
     * 
     * @param className the fully qualified class name
     * @return the class model
     */
    public ClassModel modelForName( String className ) {
        ClassModel model = classModels.get( className );
        if( model == null ) {
            String filename = className.replace( '.', '/' ) + ".class";
            InputStream in = findDataStream( filename );
            
            if( in == null ) {
                throw new RuntimeException( "Could not find class file for " + className );
            }
            
            model = factory.modelFromStream( className, in );
            classModels.put( className, model );
        }
        
        return model;
    }
    
    /**
     * True if debug messages should be generated
     */
    public final boolean debug;

    private final Set<String> classesToBeTranslated = new HashSet<String>();
    
    private final ClassModelFactory factory = new ASMClassModelFactory();    
    private final ClassLoader       loader;
    
    //java class name to avm2 class name
    private final Map<String, String> translatedClassNames =
        new HashMap<String, String>();
    
    private final Map<String , ClassModel> classModels = 
        new HashMap<String, ClassModel>();
    
    /**
     * Find the given resource stream
     */
    private InputStream findDataStream( String filename ) {
        return loader.getResourceAsStream( filename );
    }
    
    /*pkg*/ TranslationContext( ClassLoader loader ) {  
        this.loader = loader;
        debug = J2AVM.log.isLoggable( Level.FINEST );
    }    
}
