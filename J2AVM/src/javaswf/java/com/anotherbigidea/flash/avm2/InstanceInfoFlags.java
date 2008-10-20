package com.anotherbigidea.flash.avm2;

import java.util.EnumSet;
import java.util.Set;

public enum InstanceInfoFlags {

    Sealed         ( 0x01 ),
    Final          ( 0x02 ),
    Interface      ( 0x04 ),    
    HasProtectedNS ( 0x08 );
    
    /** The flag value */
    public final int mask;
     
    private InstanceInfoFlags( int mask ) {
        this.mask = mask;
    }    
    
    /** Test whether this flag is set */
    public boolean isSet( int flags ) {
        return (flags & mask) != 0;
    }
    
    /** Get the set of flags that are set */
    public static EnumSet<InstanceInfoFlags> decode( int flags ) {
        EnumSet<InstanceInfoFlags> set = EnumSet.noneOf(InstanceInfoFlags.class);
        
        for (InstanceInfoFlags f : values()) {
            if( f.isSet(flags) ) set.add( f );
        }
        
        return set;
    }
    
    /** Encode the flags in an int */
    public static int encode( Set<InstanceInfoFlags> flags ) {
        int value = 0;
        
        for (InstanceInfoFlags f : flags) {
            value |= f.mask;
        }
        
        return value;
    }
}
