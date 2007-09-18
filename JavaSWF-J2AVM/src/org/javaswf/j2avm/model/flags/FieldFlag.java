package org.javaswf.j2avm.model.flags;

import org.epistem.util.Flag;
import org.epistem.util.FlagParser;

/**
 * The flags that apply to fields
 */
public enum FieldFlag {
	
    @Flag( 0x0001 ) FieldIsPublic    ,
    @Flag( 0x0002 ) FieldIsPrivate   ,
    @Flag( 0x0004 ) FieldIsProtected ,
    @Flag( 0x0008 ) FieldIsStatic    ,
    @Flag( 0x0010 ) FieldIsFinal     ,
    @Flag( 0x0040 ) FieldIsVolatile  ,
    @Flag( 0x0080 ) FieldIsTransient ,
    @Flag( 0x1000 ) FieldIsSynthetic ,
    @Flag( 0x4000 ) FieldIsEnum      ;

    /**
     * Parser for the flags
     */
    public static final FlagParser<FieldFlag> parser = 
    	new FlagParser<FieldFlag>( FieldFlag.class );
}