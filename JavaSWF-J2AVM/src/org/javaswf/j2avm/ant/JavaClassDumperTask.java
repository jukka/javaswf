package org.javaswf.j2avm.ant;

import java.io.File;
import java.io.FileWriter;

import org.apache.tools.ant.BuildException;
import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.ModelFactory;

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
        ModelFactory fact = new ModelFactory( loader );
        
		try {
		    ClassModel model = fact.modelForName( className );		

		    FileWriter writer = new FileWriter( dumpFile );
			IndentingPrintWriter ipw = new IndentingPrintWriter( writer );
			model.dump( ipw );
			ipw.flush();
			writer.close();
		} catch( Exception ex ) {
		    ex.printStackTrace();
			throw new BuildException( ex );
		}
    }
}
