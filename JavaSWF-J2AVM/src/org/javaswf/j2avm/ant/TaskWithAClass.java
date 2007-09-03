package org.javaswf.j2avm.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ClasspathUtils;

/**
 * Base for Ant tasks that need a class that is set via a class name and either
 * a classpath or a classpath reference
 *
 * @author nickmain
 */
public abstract class TaskWithAClass extends Task {

    private ClassLoader loader;
    private String      className;
    

    protected Class<?> loadClass() throws BuildException {
        if( loader    == null ) throw new BuildException( "Classpath not set or referenced" );
        if( className == null ) throw new BuildException( "Class name is missing" );
        
        try {
            return loader.loadClass( className );
        } catch( ClassNotFoundException ex ) {
            throw new BuildException( "Could not load class " + className );
        }
    }
    
    /**
     * Set the classpath (inline)
     */
    public void setClasspath( Path path ) {
        this.loader = ClasspathUtils.getClassLoaderForPath( getProject(), path, "" );
    }
    
    /**
     * Set the classpath via an id reference
     */
    public void setClasspathRef( String id ) {
        Reference ref = new Reference( getProject(), id );
        this.loader = ClasspathUtils.getClassLoaderForPath( getProject(), ref );
    }
    
    /**
     * Set the class name
     */
    public void setClass( String className ) {
        this.className = className;
    }
}
