package com.anotherbigidea.flash.avm2;

import static com.anotherbigidea.flash.avm2.ArgType.*;



/**
 * The AVM2 instructions that can appear in an ABC file.
 * Does not include the internal instructions used by the AVM2.
 *
 * @author nickmain
 */
public enum Operation {

    OP_add              ( 0xA0, 2, 1, 0, 0 ),
    OP_add_i            ( 0xC5, 2, 1, 0, 0 ),
    OP_astype           ( 0x86, 1, 1, 0, 0, NAME_INDEX ),
    OP_astypelate       ( 0x87, 2, 1, 0, 0 ),
    OP_bitand           ( 0xA8, 2, 1, 0, 0 ),
    OP_bitnot           ( 0x97, 1, 1, 0, 0  ),
    OP_bitor            ( 0xA9, 2, 1, 0, 0 ),
    OP_bitxor           ( 0xAA, 2, 1, 0, 0 ),
    OP_bkpt             ( 0x01, 0, 0, 0, 0 ),
    OP_bkptline         ( 0xF2, 0, 0, 0, 0, LINE_NUMBER ),
    OP_call             ( 0x41, 2, 1, 0, 0, ARG_COUNT ),
    OP_callmethod       ( 0x43, 1, 1, 0, 0, DISP_ID, ARG_COUNT ),
    OP_callproperty     ( 0x46, 1, 1, 0, 0, NAME_INDEX, ARG_COUNT ),
    OP_callproplex      ( 0x4C, 1, 1, 0, 0, NAME_INDEX, ARG_COUNT ),
    OP_callpropvoid     ( 0x4F, 1, 0, 0, 0, NAME_INDEX, ARG_COUNT ),
    OP_callstatic       ( 0x44, 1, 1, 0, 0, METHOD_ID, ARG_COUNT ),
    OP_callsuper        ( 0x45, 1, 1, 0, 0, NAME_INDEX, ARG_COUNT ),
    OP_callsupervoid    ( 0x4E, 1, 0, 0, 0, NAME_INDEX, ARG_COUNT ),
    OP_checkfilter      ( 0x78, 1, 1, 0, 0 ),
    OP_coerce           ( 0x80, 1, 1, 0, 0, NAME_INDEX ),
    OP_coerce_a         ( 0x82, 1, 1, 0, 0 ),
    OP_coerce_b         ( 0x81, 1, 1, 0, 0 ),
    OP_coerce_d         ( 0x84, 1, 1, 0, 0 ),
    OP_coerce_i         ( 0x83, 1, 1, 0, 0 ),
    OP_coerce_o         ( 0x89, 1, 1, 0, 0 ),
    OP_coerce_s         ( 0x85, 1, 1, 0, 0 ),
    OP_coerce_u         ( 0x88, 1, 1, 0, 0 ),
    OP_construct        ( 0x42, 1, 1, 0, 0, ARG_COUNT ),
    OP_constructprop    ( 0x4A, 1, 1, 0, 0, NAME_INDEX, ARG_COUNT ),
    OP_constructsuper   ( 0x49, 1, 0, 0, 0, ARG_COUNT ),
    OP_convert_b        ( 0x76, 1, 1, 0, 0 ),
    OP_convert_d        ( 0x75, 1, 1, 0, 0 ),
    OP_convert_i        ( 0x73, 1, 1, 0, 0 ),
    OP_convert_o        ( 0x77, 1, 1, 0, 0 ),
    OP_convert_s        ( 0x70, 1, 1, 0, 0 ),
    OP_convert_u        ( 0x74, 1, 1, 0, 0 ),
    OP_debug            ( 0xEF, 0, 0, 0, 0 ),
    OP_debugfile        ( 0xF1, 0, 0, 0, 0, STRING_INDEX ),
    OP_debugline        ( 0xF0, 0, 0, 0, 0, LINE_NUMBER ),
    OP_declocal         ( 0x94, 0, 0, 0, 0, REGISTER ),
    OP_declocal_i       ( 0xC3, 0, 0, 0, 0, REGISTER ),
    OP_decrement        ( 0x93, 1, 1, 0, 0 ),
    OP_decrement_i      ( 0xC1, 1, 1, 0, 0 ),
    OP_deleteproperty   ( 0x6A, 1, 0, 0, 0, NAME_INDEX ),
    OP_divide           ( 0xA3, 2, 1, 0, 0 ),
    OP_dup              ( 0x2A, 1, 2, 0, 0 ),
    OP_dxns             ( 0x06, 0, 0, 0, 0, STRING_INDEX ),
    OP_dxnslate         ( 0x07, 1, 0, 0, 0 ),
    OP_equals           ( 0xAB, 2, 1, 0, 0 ),
    OP_esc_xattr        ( 0x72, 1, 1, 0, 0 ),
    OP_esc_xelem        ( 0x71, 1, 1, 0, 0 ),
 // OP_finddef          ( 0x5F, 0, 1, 0, 0, NAME_INDEX ),
    OP_findproperty     ( 0x5E, 0, 1, 0, 0, NAME_INDEX ),
    OP_findpropstrict   ( 0x5D, 0, 1, 0, 0, NAME_INDEX ),
    OP_getdescendants   ( 0x59, 1, 1, 0, 0, NAME_INDEX ),
    OP_getglobalscope   ( 0x64, 0, 1, 0, 0 ),
    OP_getglobalslot    ( 0x6E, 0, 1, 0, 0, SLOT_ID ),
    OP_getlex           ( 0x60, 0, 1, 0, 0, NAME_INDEX ),
    OP_getlocal         ( 0x62, 0, 1, 0, 0, REGISTER ),
    OP_getlocal0        ( 0xD0, 0, 1, 0, 0 ),
    OP_getlocal1        ( 0xD1, 0, 1, 0, 0 ),
    OP_getlocal2        ( 0xD2, 0, 1, 0, 0 ),
    OP_getlocal3        ( 0xD3, 0, 1, 0, 0 ),
    OP_getproperty      ( 0x66, 1, 1, 0, 0, NAME_INDEX ),
    OP_getscopeobject   ( 0x65, 0, 1, 0, 0, SCOPE_INDEX ),
    OP_getslot          ( 0x6C, 1, 1, 0, 0, SLOT_ID ),
    OP_getsuper         ( 0x04, 1, 1, 0, 0, NAME_INDEX ),
    OP_greaterequals    ( 0xB0, 2, 1, 0, 0 ),
    OP_greaterthan      ( 0xAF, 2, 1, 0, 0 ),
    OP_hasnext          ( 0x1F, 2, 1, 0, 0 ),
    OP_hasnext2         ( 0x32, 0, 1, 0, 0, TARGET_REGISTER, INDEX_REGISTER ),
    OP_ifeq             ( 0x13, 2, 0, 0, 0, OFFSET ),
    OP_iffalse          ( 0x12, 1, 0, 0, 0, OFFSET ),
    OP_ifge             ( 0x18, 2, 0, 0, 0, OFFSET ),
    OP_ifgt             ( 0x17, 2, 0, 0, 0, OFFSET ),
    OP_ifle             ( 0x16, 2, 0, 0, 0, OFFSET ),
    OP_iflt             ( 0x15, 2, 0, 0, 0, OFFSET ),
    OP_ifne             ( 0x14, 2, 0, 0, 0, OFFSET ),
    OP_ifnge            ( 0x0F, 2, 0, 0, 0, OFFSET ),
    OP_ifngt            ( 0x0E, 2, 0, 0, 0, OFFSET ),
    OP_ifnle            ( 0x0D, 2, 0, 0, 0, OFFSET ),
    OP_ifnlt            ( 0x0C, 2, 0, 0, 0, OFFSET ),
    OP_ifstricteq       ( 0x19, 2, 0, 0, 0, OFFSET ),
    OP_ifstrictne       ( 0x1A, 2, 0, 0, 0, OFFSET ),
    OP_iftrue           ( 0x11, 1, 0, 0, 0, OFFSET ),
    OP_in               ( 0xB4, 2, 1, 0, 0 ),
    OP_inclocal         ( 0x92, 0, 0, 0, 0, REGISTER ),
    OP_inclocal_i       ( 0xC2, 0, 0, 0, 0, REGISTER ),
    OP_increment        ( 0x91, 1, 1, 0, 0 ),
    OP_increment_i      ( 0xC0, 1, 1, 0, 0 ),
    OP_initproperty     ( 0x68, 2, 1, 0, 0, NAME_INDEX ),
    OP_instanceof       ( 0xB1, 2, 1, 0, 0 ),
    OP_istype           ( 0xB2, 1, 1, 0, 0, NAME_INDEX ),
    OP_istypelate       ( 0xB3, 2, 1, 0, 0 ),
    OP_jump             ( 0x10, 0, 0, 0, 0, OFFSET ),
    OP_kill             ( 0x08, 0, 0, 0, 0, REGISTER ),
    OP_label            ( 0x09, 0, 0, 0, 0 ),
    OP_lessequals       ( 0xAE, 2, 1, 0, 0 ),
    OP_lessthan         ( 0xAD, 2, 1, 0, 0 ),
    OP_lookupswitch     ( 0x1B, 1, 0, 0, 0, DEFAULT_OFFSET, SWITCH_OFFSETS ),
    OP_lshift           ( 0xA5, 2, 1, 0, 0 ),
    OP_modulo           ( 0xA4, 2, 1, 0, 0 ),
    OP_multiply         ( 0xA2, 2, 1, 0, 0 ),
    OP_multiply_i       ( 0xC7, 2, 1, 0, 0 ),
    OP_negate           ( 0x90, 1, 1, 0, 0 ),
    OP_negate_i         ( 0xC4, 1, 1, 0, 0 ),
    OP_newactivation    ( 0x57, 0, 1, 0, 0 ),
    OP_newarray         ( 0x56, 0, 1, 0, 0, ARG_COUNT ),
    OP_newcatch         ( 0x5A, 0, 1, 0, 0, CATCH_INDEX ),
    OP_newclass         ( 0x58, 1, 1, 0, 0, CLASS_INDEX ),
    OP_newfunction      ( 0x40, 0, 1, 0, 0, METHOD_INDEX ),
    OP_newobject        ( 0x55, 0, 1, 0, 0, KEY_VALUE_COUNT ),
    OP_nextname         ( 0x1E, 2, 1, 0, 0 ),
    OP_nextvalue        ( 0x23, 2, 1, 0, 0 ),
    OP_nop              ( 0x02, 0, 0, 0, 0 ),
    OP_not              ( 0x96, 1, 1, 0, 0 ),
    OP_pop              ( 0x29, 1, 0, 0, 0 ),
    OP_popscope         ( 0x1D, 0, 0, 1, 0 ),
    OP_pushbyte         ( 0x24, 0, 1, 0, 0, BYTE ),
    OP_pushdouble       ( 0x2F, 0, 1, 0, 0, DOUBLE_INDEX ),
    OP_pushfalse        ( 0x27, 0, 1, 0, 0 ),
    OP_pushint          ( 0x2D, 0, 1, 0, 0, INT_INDEX ),
    OP_pushnamespace    ( 0x31, 0, 1, 0, 0, NAMESPACE_INDEX ),
    OP_pushnan          ( 0x28, 0, 1, 0, 0 ),
    OP_pushnull         ( 0x20, 0, 1, 0, 0 ),
    OP_pushscope        ( 0x30, 1, 0, 0, 1 ),
    OP_pushshort        ( 0x25, 0, 1, 0, 0, SHORT ),
    OP_pushstring       ( 0x2C, 0, 1, 0, 0, STRING_INDEX ),
    OP_pushtrue         ( 0x26, 0, 1, 0, 0 ),
    OP_pushuint         ( 0x2E, 0, 1, 0, 0, UINT_INDEX ),
    OP_pushundefined    ( 0x21, 0, 1, 0, 0 ),
    OP_pushwith         ( 0x1C, 1, 0, 0, 1 ),
    OP_returnvalue      ( 0x48, 1, 0, 0, 0 ),
    OP_returnvoid       ( 0x47, 0, 0, 0, 0 ),
    OP_rshift           ( 0xA6, 2, 1, 0, 0 ),
    OP_setglobalslot    ( 0x6F, 1, 0, 0, 0, SLOT_ID ),
    OP_setlocal         ( 0x63, 1, 0, 0, 0, REGISTER ),
    OP_setlocal0        ( 0xD4, 1, 0, 0, 0 ),
    OP_setlocal1        ( 0xD5, 1, 0, 0, 0 ),
    OP_setlocal2        ( 0xD6, 1, 0, 0, 0 ),
    OP_setlocal3        ( 0xD7, 1, 0, 0, 0 ),
    OP_setproperty      ( 0x61, 2, 0, 0, 0, NAME_INDEX ),
    OP_setslot          ( 0x6D, 2, 0, 0, 0, SLOT_ID ),
    OP_setsuper         ( 0x05, 2, 0, 0, 0, NAME_INDEX ),
    OP_strictequals     ( 0xAC, 2, 1, 0, 0 ),
    OP_subtract         ( 0xA1, 2, 1, 0, 0 ),
    OP_subtract_i       ( 0xC6, 2, 1, 0, 0 ),
    OP_swap             ( 0x2B, 1, 1, 0, 0 ),
    OP_throw            ( 0x03, 1, 0, 0, 0 ),
    OP_timestamp        ( 0xF3, 0, 0, 0, 0 ),
    OP_typeof           ( 0x95, 1, 1, 0, 0 ),
    OP_urshift          ( 0xA7, 2, 1, 0, 0 );
    
    public final int    opcode;
    public final String mnemonic;
    public final int    pushCount;
    public final int    popCount;
    public final int    scopePushCount;
    public final int    scopePopCount;
    public final ArgType[] argTypes;
    
    /** Whether this op is a branch (to another instruction in the method) */
    public boolean isBranch() {
        switch( this ) {
            case OP_jump:
            case OP_ifeq:      
            case OP_iffalse:   
            case OP_ifge:      
            case OP_ifgt:      
            case OP_ifle:      
            case OP_iflt:      
            case OP_ifne:      
            case OP_ifnge:     
            case OP_ifngt:     
            case OP_ifnle:     
            case OP_ifnlt:     
            case OP_ifstricteq:
            case OP_ifstrictne:
            case OP_iftrue: 
            case OP_lookupswitch:
                return true;
            
            default: return false;
        }
        
    }
    
    /** Whether this op is an abrupt flow (never directly reaches next instruction) */
    public boolean isAbruptFlow() {
        switch( this ) {
            case OP_jump:
            case OP_returnvalue:
            case OP_returnvoid:
            case OP_lookupswitch:            
            case OP_throw:
                return true;
        
            default: return false;
        }
    }
    
    private Operation( int opcode, 
                       int popCount, int pushCount, 
                       int scopePopCount, int scopePushCount, 
                       ArgType... argTypes ) {
        this.opcode    = opcode;
        this.mnemonic  = this.name().substring( 3 );
        this.argTypes  = argTypes;
        this.popCount  = popCount;
        this.pushCount = pushCount;
        this.scopePopCount  = scopePopCount;
        this.scopePushCount = scopePushCount;
        
        register(this);
    }
    
    /**
     * Get an Operation from an opcode 
     * @param opcode >= 0, < 256
     * @return null if the opcode is unknown or out of range
     */
    public static Operation fromOpcode( int opcode ) {
        if( opcode >= 0 && opcode < 256 ) {
            return byOpcode[ opcode ];
        }
        
        return null;
    }
    
    private static Operation[] byOpcode;
    private static void register( Operation op ) {
        if( byOpcode == null ) byOpcode = new Operation[256];
        byOpcode[ op.opcode ] = op;
    }
}
