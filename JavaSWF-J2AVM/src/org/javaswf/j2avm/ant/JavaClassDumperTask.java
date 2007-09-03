package org.javaswf.j2avm.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.tools.ant.BuildException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 * ANT task to dump a Java class
 *
 * @author nickmain
 */
public class JavaClassDumperTask extends TaskWithAClass {

    private File dumpFile;
    
    /**
     * Set the file to dump to
     */
    public void setFile( File dumpFile ) {
        this.dumpFile = dumpFile;
    }
    
    /** @see org.apache.tools.ant.Task#execute() */
    @Override
    public void execute() throws BuildException {

        if( dumpFile == null ) throw new BuildException( "File to dump to is missing" );
        
        Class<?> clazz = loadClass();
        
        try {
            PrintWriter pw = new PrintWriter( dumpFile );
            try {
                String fileName = clazz.getName().replace( '.', '/' ) + ".class";
                
                InputStream in = clazz.getClassLoader().getResourceAsStream( fileName ); 
                ClassReader reader = new ClassReader( in );        
                reader.accept( new TraceClassVisitor( pw ), 0 );
            } finally {
                pw.close();
            }
        } catch( IOException ioe ) {
            throw new BuildException( ioe );
        }
    }
}
