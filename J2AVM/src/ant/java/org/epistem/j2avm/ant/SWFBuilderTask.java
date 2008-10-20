package org.epistem.j2avm.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Sequential;
import org.epistem.j2avm.swf.SWFBuilder;

/**
 * ANT task for creating a SWF file. This is a task container.
 *
 * @author nickmain
 */
public class SWFBuilderTask extends Sequential {

    private SWFBuilder builder;
    private File swfFile;    
    
    /**
     * @param width the movie width in pixels
     */
    public void setWidth( int width ) {
        builder.setWidth( width );
    }

    /**
     * @param height the movie height in pixels
     */
    public void setHeight( int height ) {
        builder.setHeight( height );
    }

    /**
     * @param frameRate the frames per second
     */
    public void setFrameRate( int frameRate ) {
        builder.setFrameRate( frameRate );
    }

    /**
     * @param version the flash version
     */
    public void setVersion( int version ) {
        builder.setVersion( version );
    }

    /**
     * @param background the RGB background color
     */
    public void setBackground( int background ) {
        builder.setBackground( background );
    }

    /**
     * @param compressed whether the movie is compressed
     */
    public void setCompressed( boolean compressed ) {
        builder.setCompressed( compressed );
    }
    
    /**
     * @param swfFile the file to write
     */
    public void setFile( File swfFile ) {
        this.swfFile = swfFile;
    }
    
    /** @see org.apache.tools.ant.taskdefs.Sequential#addTask(org.apache.tools.ant.Task) */
    @Override
    public void addTask( Task nestedTask ) {
        if( nestedTask instanceof SWFTask ) {
            ((SWFTask) nestedTask).setSWFBuilder( builder );
        }
        
        super.addTask( nestedTask );
    }

    /** @see org.apache.tools.ant.Task#execute() */
    @Override
    public void execute() throws BuildException {
 
        super.execute(); //process the nested tasks
        
        try {
            builder.write( swfFile );
        } catch( IOException ioe ) {
            throw new BuildException( ioe );
        }
    }
}
