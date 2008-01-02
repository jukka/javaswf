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

    Qname      ( 7 , false, false ), //qualified name
    QnameA     ( 13, false, false ), //qualified attribute name
    Multiname  ( 9 , false, false ), //name qualified by a set of namespaces
    MultinameA ( 14, false, false ), //attribute name qualified by a set of namespaces
    RTQname    ( 15, true , false ), //name with a runtime specified namespace
    RTQnameA   ( 16, true , false ), //attribute name with a runtime specified namespace
    MultinameL ( 27, false, true  ), //runtime specified name qualified by a set of namespaces
    MultinameLA( 28, false, true  ), //runtime specified attribute name qualified by a set of namespaces  
    RTQnameL   ( 17, true , true  ), //name and namespace both specified at runtime
    RTQnameLA  ( 18, true , true  ); //attribute name and namespace both specified at runtime
    
    public final boolean hasRuntimeNamespace;
    public final boolean hasRuntimeName;
    
    /**
     * The byte value
     */
    public final int value;
     
    private MultiNameKind( int value, 
                           boolean hasRuntimeNamespace,
                           boolean hasRuntimeName ) {
        this.value = value;
        this.hasRuntimeNamespace = hasRuntimeNamespace;
        this.hasRuntimeName      = hasRuntimeName;
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
