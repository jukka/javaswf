package com.anotherbigidea.flash.avm1.ops;

/**
 * The AVM1 unary operations
 *
 * @author nickmain
 */
public enum UnaryOpType {

    UnOp_Not,
    
    UnOp_StringLength,
    UnOp_StringLengthMB,
    
    UnOp_CharToAscii,
    UnOp_AsciiToChar,
    UnOp_CharMBToAscii,
    UnOp_AsciiToCharMB,

    UnOp_ToInteger,
    UnOp_ConvertToNumber,
    UnOp_ConvertToString,
    
    UnOp_TypeOf;
    
    /** Get the op name without prefix */
    public String opName() {
        return name().substring( 5 );
    }
}
