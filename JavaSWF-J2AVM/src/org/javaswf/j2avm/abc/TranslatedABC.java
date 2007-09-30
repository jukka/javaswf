package org.javaswf.j2avm.abc;

import java.util.HashMap;
import java.util.Map;

import org.javaswf.j2avm.TranslationTarget;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

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
    
    public TranslatedClass newClass( String className,
    		                         String superName,
    		                         boolean isFinal,
    		                         boolean isInterface ) {

    	AVM2QName qname  = nameForJavaClass( className );
    	AVM2QName qsuper = nameForJavaClass( superName );  
    	
    	String protName = qname.namespace.name + ":" + qname.name;
    	AVM2Namespace protectedNamespace = 
    		new AVM2Namespace( NamespaceKind.ProtectedNamespace, protName );
    	
    	AVM2Class avm2Class = abcFile.addClass( qname, qsuper, 
								                true,         //isSealed
								                isFinal,
								                isInterface,
								                protectedNamespace );
    	
    	TranslatedClass tc = 
    		new TranslatedClass( avm2Class, qsuper, protectedNamespace );
    	
    	translatedClasses.put( className, tc );
    	
    	return tc;
    }
    
    /**
     * Get or generate the AVM2 name for the given Java class.
     * 
     * NOTE: this would be the place to plug in some sort of strategy/factory
     *       for generating names
     * 
     * @param className the Java class name
     */
    public final AVM2QName nameForJavaClass( String className ) {
    	AVM2QName qname = translatedClassNames.get( className );
    	if( qname == null ) {
    		qname = new AVM2QName( className );
    		translatedClassNames.put( className, qname );
    	}
    	
    	return qname;
    }
    
    public final AVM2ABCFile abcFile = new AVM2ABCFile();
    
    /**
     * Map of Java class name to AVM2 class
     */
    private final Map<String, TranslatedClass> translatedClasses = 
        new HashMap<String, TranslatedClass>();    
    
    //java class name to avm2 class name
    private final Map<String, AVM2QName> translatedClassNames =
        new HashMap<String, AVM2QName>();
}
