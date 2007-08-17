package com.anotherbigidea.flash.avm2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.epistem.io.InStream;
import org.epistem.io.OutStreamWrapper;


/**
 * Namespace kinds.
 *
 * @author nickmain
 */
public enum NamespaceKind {
    
    Namespace                ( 8 ), 
    PrivateNamespace         ( 5 ), 
    PackageNamespace         ( 22 ), 
    PackageInternalNamespace ( 23 ), 
    ProtectedNamespace       ( 24 ), 
    ExplicitNamespace        ( 25 ), 
    StaticProtectedNamespace ( 26 );
    
    /**
     * The byte value
     */
    public final int value;
    
    private NamespaceKind( int value ) {
        this.value = value;
    }    

    private static Map<Integer, NamespaceKind> val2enum =
        new HashMap<Integer, NamespaceKind>();

    static {
        for (NamespaceKind nk : values()) {
            val2enum.put( nk.value, nk );
        }        
    }
    
    /** Write out the kind */
    public void write( OutStreamWrapper out ) {
        out.writeUI8( value );
    }
    
    /**
     * Parse an enum instance
     */
    public static NamespaceKind parse( InStream in ) throws IOException {        
        int val = in.readUI8();
        NamespaceKind nk = val2enum.get( val );
        
        if( nk == null ) throw new IOException( "Unrecognized namespace kind " + val );
        
        return nk;
    }
}
