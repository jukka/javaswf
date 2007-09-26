package org.javaswf.j2avm.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.javaswf.j2avm.targets.TargetSWF;

/**
 * Element representing a SWF target
 * 
 * @author nick main
 */
public class TargetSWFElement {
	
    private File targetSWFFile;
    
    private int width     = 550;
    private int height    = 400;
    private int frameRate = 12;
    private int bgColor   = 0xffffff;
    
	public void setFile     ( File file     ) { this.targetSWFFile = file; }
	public void setWidth    ( int width     ) { this.width = width; }
	public void setHeight   ( int height    ) { this.height = height; }
	public void setFrameRate( int frameRate ) { this.frameRate = frameRate; }
	
    public void setBgColor( String bgColor ) {
        if( bgColor.startsWith( "0x" ) || bgColor.startsWith( "0X" ) ) {
            bgColor = bgColor.substring( 2 );
        }
        
        if( bgColor.startsWith( "#" )) {
            bgColor = bgColor.substring( 1 );
        }
        
        this.bgColor = Integer.parseInt( bgColor, 16 );
    }

	/*pkg*/ TargetSWF getTarget() throws BuildException {
    	
    	if( targetSWFFile == null ) throw new BuildException( "Missing target SWF filename" );    	
    	return new TargetSWF( targetSWFFile, width, height, frameRate, bgColor );
    }
}
