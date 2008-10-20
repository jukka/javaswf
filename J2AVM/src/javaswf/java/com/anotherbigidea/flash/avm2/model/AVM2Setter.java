package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

/**
 * A getter method
 *
 * @author nickmain
 */
public class AVM2Setter extends AVM2MethodSlot {

    /*pkg*/ AVM2Setter( AVM2QName name, AVM2Method method,
                        boolean isFinal, boolean isOverride ) {
        super( name, method, isFinal, isOverride );
    }
    
    protected void dumpEx( IndentingPrintWriter out ) {
        out.print( "setter " );
        super.dumpEx( out );
    }
}
