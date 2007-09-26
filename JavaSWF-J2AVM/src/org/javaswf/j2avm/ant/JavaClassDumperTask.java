package org.javaswf.j2avm.ant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.ant.BuildException;
import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.ClassModel;

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
        
		InputStream in = loader.getResourceAsStream( 
		                        	className.replace( '.', '/' ) + ".class" );
		
		ClassModel model = new ClassModel( in );		
		
		try {
			FileWriter writer = new FileWriter( dumpFile );
			IndentingPrintWriter ipw = new IndentingPrintWriter( writer );
			model.dump( ipw );
			ipw.flush();
			writer.close();
		} catch( IOException ioe ) {
			throw new BuildException( ioe );
		}
    }
}
