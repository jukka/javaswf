package com.anotherbigidea.flash.avm2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.epistem.io.InStream;
import org.epistem.io.OutStreamWrapper;


/**
 * MultiName kinds.
 *
 * @author nickmain
 */
public enum MultiNameKind {

    Qname      ( 7  ), //qualified name
    QnameA     ( 13 ), //qualified attribute name
    Multiname  ( 9  ), //name qualified by a set of namespaces
    MultinameA ( 14 ), //attribute name qualified by a set of namespaces
    RTQname    ( 15 ), //name with a runtime specified namespace
    RTQnameA   ( 16 ), //attribute name with a runtime specified namespace
    MultinameL ( 27 ), //runtime specified name qualified by a set of namespaces
    MultinameLA( 28 ), //runtime specified attribute name qualified by a set of namespaces  
    RTQnameL   ( 17 ), //name and namespace both specified at runtime
    RTQnameLA  ( 18 ); //attribute name and namespace both specified at runtime

    /**
     * The byte value
     */
    public final int value;
     
    private MultiNameKind( int value ) {
        this.value = value;
    }    

    private static Map<Integer, MultiNameKind> val2enum =
        new HashMap<Integer, MultiNameKind>();

    static {
        for (MultiNameKind nk : values()) {
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
    public static MultiNameKind parse( InStream in ) throws IOException {        
        int val = in.readUI8();
        MultiNameKind nk = val2enum.get( val );
        
        if( nk == null ) throw new IOException( "Unrecognized multiname kind " + val );
        
        return nk;
    }
}
