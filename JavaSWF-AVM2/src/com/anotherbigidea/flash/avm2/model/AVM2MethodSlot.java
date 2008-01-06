package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * A member method.
 *
 * @author nickmain
 */
public class AVM2MethodSlot extends AVM2Trait 
                            implements AVM2MethodContainer {

    /** The method */
    public final AVM2Method method;
    
    /** Whether the method is final */
    public final boolean isFinal;
    
    /** Whether the method is an override */
    public final boolean isOverride;
    
    /*pkg*/ AVM2MethodSlot( AVM2QName name, AVM2Method method,
                            boolean isFinal, boolean isOverride ) {
        super(name);
        
        this.isFinal    = isFinal;
        this.isOverride = isOverride;
        this.method     = method;
    }
    
    /** @see com.anotherbigidea.flash.avm2.model.AVM2MethodContainer#getMethod() */
    public AVM2Method getMethod() {
        return method;
    }

    protected void dumpEx( IndentingPrintWriter out ) {
        if( isFinal    ) out.print( "final " );
        if( isOverride ) out.print( "override " );
        method.dump( out );
    }
    
    protected void initPoolEx( AVM2ABCFile.WriteContext context ) {
        method.initPool( context );        
    }
    
    /**
     * Write.
     */
    public void write( ABC.Traits ts, AVM2ABCFile.WriteContext context ) {
        
        int nameIndex = name.indexIn( context.pool );
        int dispId    = indexId + 1;
        
        int methIndex = 0;
        for( int i = 0; i < context.methods.size(); i++ ) {
            if( context.methods.get( i ) == method ) {
                methIndex = i;
                break;
            }
        }

        //System.out.println( "METHOD SLOT: " + name );
        
        if( this instanceof AVM2Getter ) {
            ts.getter( nameIndex, dispId, methIndex, isFinal, isOverride, metadataIndices( context ));
        }
        else if( this instanceof AVM2Setter ) {
            ts.setter( nameIndex, dispId, methIndex, isFinal, isOverride, metadataIndices( context ));
        } 
        else {
            ts.method( nameIndex, dispId, methIndex, isFinal, isOverride, metadataIndices( context ));
        }
    }
}
