package com.anotherbigidea.flash.avm2;

import java.util.EnumSet;
import java.util.Set;

public enum MethodInfoFlags {

    NeedArguments  ( 0x01 ),
    NeedActivation ( 0x02 ),
    NeedRest       ( 0x04 ),
    HasOptional    ( 0x08 ),
    IgnoreRest     ( 0x10 ),
    Native         ( 0x20 ),
    SetsDXNS       ( 0x40 ),
    HasParamNames  ( 0x80 );
    
    /** The flag value */
    public final int mask;
     
    private MethodInfoFlags( int mask ) {
        this.mask = mask;
    }    
    
    /** Test whether this flag is set */
    public boolean isSet( int flags ) {
        return (flags & mask) != 0;
    }
    
    /** Get the set of flags that are set */
    public static EnumSet<MethodInfoFlags> decode( int flags ) {
        EnumSet<MethodInfoFlags> set = EnumSet.noneOf(MethodInfoFlags.class);
        
        for (MethodInfoFlags f : values()) {
            if( f.isSet(flags) ) set.add( f );
        }
        
        return set;
    }
    
    /** Encode the flags in an int */
    public static int encode( Set<MethodInfoFlags> flags ) {
        int value = 0;
        
        for (MethodInfoFlags f : flags) {
            value |= f.mask;
        }
        
        return value;
    }
}
