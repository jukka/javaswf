package org.javaswf.j2avm.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.javaswf.j2avm.J2AVM;

/**
 * The ANT task for driving J2AVM
 *
 * @author nickmain
 */
public class J2AVMAntTask extends Task {

    private List<InputClass> inputClasses = new ArrayList<InputClass>();
    private File targetABCFile;
    
    /**
     * Set the abc file to generate
     */
    public void setAbc( File abcFile ) {
        this.targetABCFile = abcFile;
    }
    
    /**
     * Create a nested inputClass element
     */
    public InputClass createInputClass() {
        InputClass inputClass = new InputClass( getProject() );
        inputClasses.add( inputClass );
        return inputClass;
    }

    @Override
    public void execute() throws BuildException {
        
        if( targetABCFile == null ) {
            throw new BuildException( "abc target file not set" );            
        }
        
        J2AVM j2avm = new J2AVM( targetABCFile );
        
        for( InputClass inputClass : inputClasses ) {
            j2avm.addClass( inputClass.getInputClass());
        }
        
        try {
            j2avm.translate();
        } catch( IOException ioe ) {
            throw new BuildException( ioe );
        }
    }
}
