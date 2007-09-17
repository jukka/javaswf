package org.javaswf.j2avm.model;

import java.io.InputStream;

/**
 * A factory for ClassModel.
 *
 * @author nickmain
 */
public interface ClassModelFactory {

    /**
     * Create a class model from the given data.
     * 
     * @param className the class name
     * @param in the class data
     */
    public ClassModel modelFromStream( String className, InputStream in );
}
