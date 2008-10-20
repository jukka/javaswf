package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

/**
 * A getter method
 *
 * @author nickmain
 */
public class AVM2Getter extends AVM2MethodSlot {

    /*pkg*/ AVM2Getter( AVM2QName name, AVM2Method method,
                        boolean isFinal, boolean isOverride ) {
        super( name, method, isFinal, isOverride );
    }
    
    protected void dumpEx( IndentingPrintWriter out ) {
        out.print( "getter " );
        super.dumpEx( out );
    }
}
