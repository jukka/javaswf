package org.javaswf.j2avm.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ClasspathUtils;

/**
 * Nested element representing an input class
 *
 * @author nickmain
 */
public class InputClass {

    private final Project project;
    
    private ClassLoader loader;
    private String      className;
    
    /*pkg*/ InputClass( Project project ) {
        this.project = project;
    }
    
    /*pkg*/ Class<?> getInputClass() throws BuildException {
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
        this.loader = ClasspathUtils.getClassLoaderForPath( project, path, "" );
    }
    
    /**
     * Set the classpath via an id reference
     */
    public void setClasspathRef( String id ) {
        Reference ref = new Reference( project, id );
        this.loader = ClasspathUtils.getClassLoaderForPath( project, ref );
    }
    
    /**
     * Set the class name
     */
    public void setName( String className ) {
        this.className = className;
    }
}
