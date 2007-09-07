package org.javaswf.j2avm;

import java.io.IOException;
import java.io.InputStream;

import org.javaswf.j2avm.emitter.TranslationInfo;
import org.javaswf.j2avm.runtime.annotations.FlashNativeClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.model.AVM2Name;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * A Java class to be translated.
 * Equality is based on the Class.
 *
 * @author nickmain
 */
public class JavaClass {

    /**
     * The Class
     */
    public final Class<?> clazz;
    
    /**
     * The superclass - null if this is java-lang-Object
     */
    public final JavaClass superclass;
    
    /**
     * The ASM node for this class
     */
    public final ClassNode node = new ClassNode();
        
    /**
     * The package qualifier
     */
    public final String packageName;
    
    /**
     * Unqualified class name
     */
    public final String simpleName;
    
    /**
     * AVM2 namespace for the package
     */
    public final AVM2Namespace packageNamespace;
    
    /**
     * AVM2 namespace for protected methods
     */
    public final AVM2Namespace protectedNamespace;
    
    /**
     * AVM2 name for the class
     */
    public final AVM2QName classQName;
    
    /**
     * Whether the class is an AVM2 runtime class (that does not need to be
     * translated)
     */
    public final boolean isAVM2RuntimeClass;
    
    /**
     * Information about the translation of the class
     */
    public final TranslationInfo info = new TranslationInfo();
    
    public JavaClass( Class<?> clazz ) {
        this.clazz = clazz;
        
        isAVM2RuntimeClass = clazz.isAnnotationPresent( FlashNativeClass.class );
        
        Class<?> superClass = clazz.getSuperclass();
        this.superclass = (superClass == null) ?
                              null :
                              new JavaClass( superClass );
        
        String className = clazz.getName();
        String simpleName  = AVM2Name.simpleName   ( className );
        String packageName = AVM2Name.packagePrefix( className );
        
        //strip off the prefixes for special Flash classes
        if( packageName.equals( "flash" ) && simpleName.startsWith( "Flash" )) { 
            packageName = "";
            simpleName  = simpleName.substring( 5 );
        }
        
        this.simpleName  = simpleName;
        this.packageName = packageName;

        packageNamespace   = new AVM2Namespace( NamespaceKind.PackageNamespace,
                                                packageName );
        classQName         = new AVM2QName( packageNamespace, simpleName );
        protectedNamespace = new AVM2Namespace( NamespaceKind.ProtectedNamespace, 
                                                packageName + ":" + simpleName );
        
        try {
            String fileName = clazz.getName().replace( '.', '/' ) + ".class";
            
            InputStream in;
            ClassLoader loader = clazz.getClassLoader();
            if( loader != null ) {            
                in = clazz.getClassLoader().getResourceAsStream( fileName );
            } else {
                in = ClassLoader.getSystemResourceAsStream( fileName );
            }
            
            ClassReader reader = new ClassReader( in );        
            reader.accept( node, 0 );
        } catch( IOException ioe ) {
            throw new RuntimeException( ioe );
        }
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        return clazz.equals( obj );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return clazz.toString();
    }     
}
