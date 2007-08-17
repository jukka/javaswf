package com.anotherbigidea.flash.avm2.model;

import java.util.Collection;
import java.util.HashSet;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * Base for traits
 *
 * @author nickmain
 */
public abstract class AVM2Trait {

    /** The trait name */
    public final AVM2QName name;
    
    /** The metadata */
    public final Collection<AVM2Metadata> metadata = new HashSet<AVM2Metadata>();
    
    /**
     * The slot index or dispatch id, -1 for unknown or auto-assign
     */
    public int indexId = -1;
    
    protected AVM2Trait( AVM2QName name ) {
        this.name = name;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof AVM2Trait )) return false;
        
        return ((AVM2Trait) obj).name.equals( name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
        
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        
        name.dump( out );
        out.print( " [" + ((indexId >= 0) ? (""+indexId) : "?") + "] " );
        
        dumpEx( out );
        
        if( ! metadata.isEmpty() ) {
            out.println();
            
            out.println( "  metadata {" );
            out.indent();
            
            for( AVM2Metadata md : metadata ) {
                md.dump( out );
            }
            
            out.unindent();
            out.println( "  }" );
        }
    }
     
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {
        name.initPool( context );
        
        for( AVM2Metadata d : metadata ) {
            d.initPool( context );
        }
        
        initPoolEx( context );
    }

    /**
     * Write.
     */
    public abstract void write( ABC.Traits ts, AVM2ABCFile.WriteContext context );
    
    protected abstract void initPoolEx( AVM2ABCFile.WriteContext context );   

    protected abstract void dumpEx( IndentingPrintWriter out );
    
    /** Get the metadata indices */
    protected int[] metadataIndices( AVM2ABCFile.WriteContext context ) {
        int[] metadataIndices = new int[ metadata.size() ];
        int i = 0;
        for( AVM2Metadata m : metadata ) {
            int index = 0;
            for( int j = 0; j < context.metadata.size(); j++ ) {
                if( context.metadata.get( j ) == m ) {
                    index = j;
                    break;
                }                
            }
            
            metadataIndices[i] = index;
            i++;
        }

        return metadataIndices;
    }
}
