package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

/**
 * An exception handler
 *
 * @author nickmain
 */
public class AVM2ExceptionHandler {

    /** The start address of the covered intructions */
    public final int start;
    
    /** The end address of the covered instructions */
    public final int end;
    
    /** The address of the exception handler code */
    public final int handler;
    
    /** The type of exception to catch */
    public final AVM2Name exceptionType;
    
    /** Variable name for caught exception */ 
    public final AVM2Name name;
    
    public AVM2ExceptionHandler( int start, int end, int handler, 
                                 AVM2Name exceptionType, AVM2Name name ) {
        
        this.start   = start;
        this.end     = end;
        this.handler = handler;
        this.name    = name;
        this.exceptionType = exceptionType;
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {
        if( exceptionType != null ) exceptionType.initPool( context );
        if( name != null ) name.indexIn( context.pool );
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.print( "try { " + start + " .. " + end + " } catch( " + name + " : " );
        if( exceptionType == null ) out.print( "*" );
        else exceptionType.dump( out );
        out.println( " ) --> " + handler );
    }
}
