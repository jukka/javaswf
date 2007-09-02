package org.javaswf.j2avm.ant;

import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.LogLevel;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.ClasspathUtils;
import org.javaswf.j2avm.J2AVM;

/**
 * The ANT task for driving J2AVM
 *
 * @author nickmain
 */
public class J2AVMAntTask extends Task {

    private File targetSWFFile;
  
    private int width     = 550;
    private int height    = 400;
    private int frameRate = 12;
    private int bgColor   = 0xffffff;
    
    private boolean verbose      = false;
    private boolean debugVerbose = false;
    
    private ClassLoader loader;
    private String      className;
    
    /**
     * Set the verbose flag
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Set the debug verbose flag
     */
    public void setDebugVerbose(boolean verbose) {
        this.debugVerbose = verbose;
    }

    /**
     * Set the swf file to generate
     */
    public void setSwf( File swfFile ) {
        this.targetSWFFile = swfFile;
    }

    /**
     * @param width the pixel width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height the pixel height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param frameRate the frames-per-second
     */
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * @param bgColor the background color
     */
    public void setBgColor( String bgColor ) {
        if( bgColor.startsWith( "0x" ) || bgColor.startsWith( "0X" ) ) {
            bgColor = bgColor.substring( 2 );
        }
        
        if( bgColor.startsWith( "#" )) {
            bgColor = bgColor.substring( 1 );
        }
        
        this.bgColor = Integer.parseInt( bgColor, 16 );
    }

    /*pkg*/ Class<?> loadClass() throws BuildException {
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
     * Set the main class name
     */
    public void setMainClass( String className ) {
        this.className = className;
    }
    
    @Override
    public void execute() throws BuildException {
                
        if( targetSWFFile == null ) {
            throw new BuildException( "target file not set - need a swf file to generate" );            
        }

        Class<?> mainClass = loadClass();

        //intercept logging
        J2AVM.log.setUseParentHandlers( false );
        J2AVM.log.addHandler( new LogHandler() );
        if( debugVerbose ) J2AVM.log.setLevel( Level.ALL );        
        else if( verbose ) J2AVM.log.setLevel( Level.FINE );
        else               J2AVM.log.setLevel( Level.INFO );        
        
        J2AVM j2avm = new J2AVM( targetSWFFile, mainClass, 
                                 width, height, frameRate, bgColor );
        
        try {
            j2avm.translate();
        } catch( Exception ex ) {
            ex.printStackTrace();
            throw new BuildException( ex );
        }
    }
    
    
    class LogHandler extends Handler {
        
        /** @see java.util.logging.Handler#close() */
        @Override
        public void close() throws SecurityException {
            // nada                
        }

        /** @see java.util.logging.Handler#flush() */
        @Override
        public void flush() {
            // nada                
        }

        /** @see java.util.logging.Handler#publish(java.util.logging.LogRecord) */
        @Override
        public void publish( LogRecord record ) {
            int level = LogLevel.INFO.getLevel();
            
            Level l = record.getLevel();
            if     ( l == Level.CONFIG  ) level = LogLevel.INFO.getLevel();
            else if( l == Level.FINE    ) level = LogLevel.INFO.getLevel();
            else if( l == Level.FINER   ) level = LogLevel.INFO.getLevel();
            else if( l == Level.FINEST  ) level = LogLevel.INFO.getLevel();
            else if( l == Level.INFO    ) level = LogLevel.INFO.getLevel();
            else if( l == Level.SEVERE  ) level = LogLevel.ERR.getLevel();
            else if( l == Level.WARNING ) level = LogLevel.WARN.getLevel();
            
            log( record.getMessage(), level );                
        }  
    }
}


