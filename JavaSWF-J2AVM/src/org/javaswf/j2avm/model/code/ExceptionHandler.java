package org.javaswf.j2avm.model.code;

import java.util.Set;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * An exception handler.
 *  
 * @author nickmain
 */
public class ExceptionHandler implements LabelTargetter, ValueGenerator {
    /** First instruction in the try block */        
    public final CodeLabel start;
    
    /** First instruction after the try block */
    public final CodeLabel end;
    
    /** First instruction of the catch handler */
    public final CodeLabel handler;
    
    /** The type to catch - may be null (means any exception) */
    public final ObjectType exceptionType;
    
    public ExceptionHandler( CodeLabel start, 
    		                 CodeLabel end, 
    		                 CodeLabel handler, 
    		                 ObjectType exceptionType ) {
    	
        this.start         = start;
        this.end           = end;
        this.handler       = handler;
        this.exceptionType = exceptionType;
        
        start  .targetters.add( this );
        end    .targetters.add( this );
        handler.targetters.add( this );
    }
    
    /**
     * Remove this handler from the targetted CodeLabels
     */
    public void remove() {
        start  .targetters.remove( this );
        end    .targetters.remove( this );
        handler.targetters.remove( this );            
    }
    
    /** @see org.javaswf.j2avm.model.code.LabelTargetter#targets(Set) */
    public void targets( Set<CodeLabel> targets ) {
        targets.add( start );
        targets.add( end );
        targets.add( handler );
    }

    /** @see org.javaswf.j2avm.model.code.LabelTargetter#release() */
    public void release() {
        start  .targetters.remove( this );
        end    .targetters.remove( this );
        handler.targetters.remove( this );            
    }

    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        String type = (exceptionType != null) ? exceptionType.name : "<any>";
        out.println( "try{ " + start + " .. " + end + 
                     " } catch( " + type + " ) --> " + handler );
    }
}