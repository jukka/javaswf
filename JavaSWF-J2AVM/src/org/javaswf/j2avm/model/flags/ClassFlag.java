package org.javaswf.j2avm.model.flags;

import org.epistem.util.Flag;
import org.epistem.util.FlagParser;

/**
 * The flags that apply to classes
 */
public enum ClassFlag {
    	
    @Flag( 0x0001 ) IsPublic    ,
    @Flag( 0x0010 ) IsFinal     ,
    @Flag( 0x0020 ) Super       ,
    @Flag( 0x0200 ) IsInterface ,
    @Flag( 0x0400 ) IsAbstract  ,
    @Flag( 0x1000 ) IsSynthetic ,
    @Flag( 0x2000 ) IsAnnotation,
    @Flag( 0x4000 ) IsEnum      ;

    /**
     * Parser for the flags
     */
    public static final FlagParser<ClassFlag> parser = 
    	new FlagParser<ClassFlag>( ClassFlag.class );
}