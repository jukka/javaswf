package org.javaswf.j2avm;

import java.util.HashSet;
import java.util.Set;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;

/**
 * The context of a particular translation.  Translation steps should look to
 * this object for their configuration.
 *
 * @author nickmain
 */
public class TranslationContext {

    //The class currently being translated
    private Class<?> classBeingTranslated;
    
    //The ABC file being written to
    private final AVM2ABCFile abcFile;

    //The set of classes to be translated
    private final Set<Class<?>> classesToBeTranslated = new HashSet<Class<?>>();

    //The set of classes that have been translated
    private final Set<Class<?>> translatedClasses = new HashSet<Class<?>>();

    /**
     * @param abcFile the abc file to generate
     */
    /*pkg*/ TranslationContext( AVM2ABCFile abcFile ) {
        this.abcFile = abcFile;
    }
    
    /**
     * The ABC file being written to
     */
    public AVM2ABCFile getAbcFile() {
        return abcFile;
    }
    
    /**
     * Add the given class to be translated, if not already processed.
     * The class is looked up via the classloader of the class currently
     * being translated.
     * 
     * @param name the name of the class to be translated.
     * @throws ClassNotFoundException if the class could not be found
     */
    public void translateClass( String name ) throws ClassNotFoundException {
        name = name.replace( '/', '.' );
        
        ClassLoader loader = classBeingTranslated.getClassLoader();
        Class<?> clazz = loader.loadClass( name );
        addClass( clazz );
    }

    /**
     * Add a class to be translated
     */
    /*pkg*/ void addClass( Class<?> clazz ) {
        if( ! translatedClasses.contains( clazz ) ) {
            classesToBeTranslated.add( clazz );
        }        
    }
    
    /**
     * Get the next class to be translated and set it into the context.
     * 
     * @return null if there are no more classes pending.
     */
    /*pkg*/ Class<?> classToBeTranslated() {
        if( classesToBeTranslated.isEmpty() ) {
            classBeingTranslated = null;
            return null;
        }
        
        Class<?> clazz = classesToBeTranslated.iterator().next();
        classesToBeTranslated.remove( clazz );
        translatedClasses    .add( clazz );
        classBeingTranslated = clazz;
        
        return clazz;
    }
}
