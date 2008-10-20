package com.anotherbigidea.flash.avm2;

import java.util.HashMap;
import java.util.Map;

/**
 * Types of trait
 *
 * @author nickmain
 */
public enum TraitKind {

    TraitSlot     ( 0x00 ),
    TraitMethod   ( 0x01 ),
    TraitGetter   ( 0x02 ),
    TraitSetter   ( 0x03 ),
    TraitClass    ( 0x04 ),
    TraitFunction ( 0x05 ),
    TraitConst    ( 0x06 );
    
    public static final int TRAIT_MASK  = 0x0f; //mask for the kind value
    
    /** The kind value */
    public final int value;
    
    private TraitKind( int value ) { this.value = value; }
    
    private static Map<Integer, TraitKind> val2enum =
        new HashMap<Integer, TraitKind>();

    static {
        for (TraitKind nk : values()) {
            val2enum.put( nk.value, nk );
        }        
    }
     
    /**
     * Find an enum instance
     */
    public static TraitKind fromValue( int value ) {        
        TraitKind nk = val2enum.get( value & TRAIT_MASK );        
        if( nk == null ) throw new RuntimeException( "Unrecognized trait kind " + value );        
        return nk;
    }
}
