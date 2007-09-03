package org.javaswf.j2avm;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

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
     * The ASM node for this class
     */
    public final ClassNode node = new ClassNode();
        
    public JavaClass( Class<?> clazz ) {
        this.clazz = clazz;
        
        try {
            String fileName = clazz.getName().replace( '.', '/' ) + ".class";
            
            InputStream in = clazz.getClassLoader().getResourceAsStream( fileName ); 
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
