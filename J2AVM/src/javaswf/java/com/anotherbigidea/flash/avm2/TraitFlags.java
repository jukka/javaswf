package com.anotherbigidea.flash.avm2;

import java.util.EnumSet;
import java.util.Set;

public enum TraitFlags {

    IsFinal    ( 0x10 ),
    IsOverride ( 0x20 ),
    HasMeta    ( 0x40 );
    
    /** The flag value */
    public final int value;
     
    private TraitFlags( int value ) {
        this.value = value;
    }    
    
    /** Test whether this flag is set */
    public boolean isSet( int flags ) {
        return (flags & value) != 0;
    }
    
    /** Get the set of flags that are set */
    public static EnumSet<TraitFlags> decode( int flags ) {
        EnumSet<TraitFlags> set = EnumSet.noneOf(TraitFlags.class);
        
        for (TraitFlags f : values()) {
            if( f.isSet(flags) ) set.add( f );
        }
        
        return set;
    }
    
    /** Encode the flags in an int */
    public static int encode( Set<TraitFlags> flags ) {
        int value = 0;
        
        for (TraitFlags f : flags) {
            value |= f.value;
        }
        
        return value;
    }
}
