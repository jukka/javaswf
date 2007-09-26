package org.javaswf.j2avm.ant;

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
import org.javaswf.j2avm.targets.TargetSWF;

/**
 * The ANT task for driving J2AVM
 *
 * @author nickmain
 */
public class J2AVMAntTask extends Task {

    private boolean verbose      = false;
    private boolean debugVerbose = false;
    
    private TargetSWFElement targetSWFElem;
    protected ClassLoader loader;
    protected String      className;
    
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
     * Create the swf sub-element
     */
    public TargetSWFElement createSwf() {
    	if( targetSWFElem != null ) throw new RuntimeException( "Only one swf element allowed" );
    	return targetSWFElem = new TargetSWFElement();
    }
    
    @Override
    public void execute() throws BuildException {
        
    	if( loader    == null ) throw new BuildException( "classpath/ref is missing" );
    	if( className == null ) throw new BuildException( "className is missing" );
    	
        if( targetSWFElem == null ) {
            throw new BuildException( "target element is missing" );            
        }
    	
    	TargetSWF target = targetSWFElem.getTarget();
    	
        //intercept logging
        J2AVM.log.setUseParentHandlers( false );
        J2AVM.log.addHandler( new LogHandler() );
        if( debugVerbose ) J2AVM.log.setLevel( Level.ALL );        
        else if( verbose ) J2AVM.log.setLevel( Level.FINE );
        else               J2AVM.log.setLevel( Level.INFO );        

        try {
            J2AVM j2avm = new J2AVM( loader, target );
            j2avm.setMainClass( className );
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


