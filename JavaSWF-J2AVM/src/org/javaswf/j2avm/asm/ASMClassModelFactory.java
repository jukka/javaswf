package org.javaswf.j2avm.asm;

import java.io.IOException;
import java.io.InputStream;

import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.ClassModelFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

/**
 * A factory that creates ASM-based ClassModels.
 *
 * @author nickmain
 */
public class ASMClassModelFactory implements ClassModelFactory {

    /** @see org.javaswf.j2avm.model.ClassModelFactory#modelFromStream(java.lang.String, java.io.InputStream) */
    public ClassModel modelFromStream(String className, InputStream in) {
        try {
            ClassReader reader = new ClassReader( in );
            
            ClassNode node = new ClassNode();
            reader.accept( node, 0 );
            
            return new ASMClassModel( node );
            
        } catch( IOException ioe ) {
            throw new RuntimeException( "While parsing class " + className, ioe );
        }
    }
}
