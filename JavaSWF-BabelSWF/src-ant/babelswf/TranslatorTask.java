package babelswf;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.LogLevel;

/**
 * ANT task to drive the bytecode translator
 *
 * @author nickmain
 */
public class TranslatorTask extends Task {

    private File avm1swf;
    private File avm2swf;
    private File report;
    private File runtimeSWF; //to be embedded unless null
    
    /**
     * The AVM1 swf to process
     */
    public void setAvm1swf( File avm1swf ) {
        this.avm1swf = avm1swf;
    }
    
    /**
     * The AVM2 swf to create
     */
    public void setAvm2swf( File avm2swf ) {
        this.avm2swf = avm2swf;
    }
    
    /**
     * Optional - the file for the translation report
     */
    public void setReport( File report ) {
        this.report = report;
    }

    /**
     * Optional - the runtime SWF to embed in the translated movie
     */
    public void setRuntime( File runtime ) {
        this.runtimeSWF = runtime;
    }
    
    /** @see org.apache.tools.ant.Task#execute() */
    @Override
    public void execute() throws BuildException {
        
        if( avm1swf == null ) throw new BuildException( "AVM1 input SWF is required" );
        if( avm2swf == null ) throw new BuildException( "AVM2 output SWF is required" );
        
        //FIXME: wire up the report file
        try {
            new AVMTranslator( avm1swf, avm2swf, runtimeSWF ).translate();
            
        } catch( Exception ioe ) {
            ioe.printStackTrace();
            throw new BuildException( ioe );
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
