package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * A function slot
 *
 * @author nickmain
 */
public class AVM2FunctionSlot extends AVM2Trait 
                              implements AVM2MethodContainer {

    /** The function */
    public final AVM2Method function;
        
    /*pkg*/ AVM2FunctionSlot( AVM2QName name, AVM2Method method ) {
        super(name);
 
        this.function = method;
    }
    
    protected void dumpEx( IndentingPrintWriter out ) {
        out.print( "function " );
        function.dump( out );
    }
    
    protected void initPoolEx( AVM2ABCFile.WriteContext context ) {
        function.initPool( context );
    }
    
    /** @see com.anotherbigidea.flash.avm2.model.AVM2MethodContainer#getMethod() */
    public AVM2Method getMethod() {
        return function;
    }
    
    /**
     * Write.
     */
    public void write( ABC.Traits ts, AVM2ABCFile.WriteContext context ) {
        
        int nameIndex = name.indexIn( context.pool );
        int slotId    = indexId + 1;
        
        int methIndex = 0;
        for( int i = 0; i < context.methods.size(); i++ ) {
            if( context.methods.get( i ) == function ) {
                methIndex = i;
                break;
            }
        }
        
        ts.function( nameIndex, slotId, methIndex, metadataIndices( context ));
    }
}
