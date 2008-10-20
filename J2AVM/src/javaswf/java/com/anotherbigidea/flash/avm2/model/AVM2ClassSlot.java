package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * A class trait
 *
 * @author nickmain
 */
public class AVM2ClassSlot extends AVM2Trait {

    /** The referenced class */
    public final AVM2QName className;
    
    /*pkg*/ AVM2ClassSlot( AVM2QName name, AVM2QName className ) {
        super(name);
        this.className = className;
    }
    
    protected void dumpEx( IndentingPrintWriter out ) {
        out.print( "class = " );
        className.dump( out );
        out.println();
    }
    
    protected void initPoolEx( AVM2ABCFile.WriteContext context ) {
        className.initPool( context );      
    }
    
    /**
     * Write.
     */
    public void write( ABC.Traits ts, AVM2ABCFile.WriteContext context ) {
        
        int nameIndex = name.indexIn( context.pool );
        int slotId    = indexId + 1;
        
        int classIndex = 0;
        for( AVM2Class c : context.getFile().classes.values() ) {
            if( c.name.equals( className ) ) {
                classIndex = c.index;
                break;
            }
        }
        
        ts.class_( nameIndex, slotId, classIndex, metadataIndices( context ));
    }
}
