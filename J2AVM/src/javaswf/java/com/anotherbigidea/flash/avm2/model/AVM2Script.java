package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * An AVM2 script
 *
 * @author nickmain
 */
public class AVM2Script implements AVM2MethodContainer {
    
    /** The traits of the script */
    public final AVM2Traits traits = new AVM2Traits();
    
    /** The script code */
    public final AVM2Method script;
    
    /**
     * @param script the script body
     */
    public AVM2Script( AVM2Method script ) {
        this.script = script;
    }
    
    /** @see com.anotherbigidea.flash.avm2.model.AVM2MethodContainer#getMethod() */
    public AVM2Method getMethod() {
        return script;
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.println( "script {" );        
        out.indent();
        
        for( AVM2Trait trait : traits.traits ) {
            trait.dump( out );
        }
        
        out.println();
        out.print( "script " );
        script.dump( out );
        
        out.unindent();
        out.println( "}" );    
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) { 
        traits.initPool( context );
        script.initPool( context );
    }
    
    /**
     * Write.
     */
    public void write( ABC.Scripts scripts, AVM2ABCFile.WriteContext context ) {
        
        int index = 0;
        for( int i = 0; i < context.methods.size(); i++ ) {
            if( script == context.methods.get( i ) ) {
                index = i;
                break;
            }
        }
        
        ABC.Traits t = scripts.script( index, traits.traits.size() );
        if( t != null ) {
            traits.write( t, context );
        }
    }
}
