package com.anotherbigidea.flash.avm1.ops;

/**
 * The AVM1 binary operations
 *
 * @author nickmain
 */
public enum BinaryOpType {

    BinOp_Add,
    BinOp_TypedAdd,
    BinOp_Subtract,
    BinOp_Multiply,
    BinOp_Divide,
    BinOp_Modulo,

    BinOp_Equals,
    BinOp_TypedEquals,
    BinOp_StrictEquals,
    BinOp_LessThan,
    BinOp_TypedLessThan,
    BinOp_GreaterThan,

    BinOp_And,
    BinOp_Or,

    BinOp_BitAnd,
    BinOp_BitOr,
    BinOp_BitXor,
    BinOp_ShiftLeft,
    BinOp_ShiftRight,
    BinOp_ShiftRightUnsigned,
    
    BinOp_InstanceOf,
    BinOp_Cast,

    BinOp_Concat,
    BinOp_StringEquals,
    BinOp_StringLessThan,
    BinOp_StringGreaterThan;
    
    /** Get the op name without prefix */
    public String opName() {
        return name().substring( 6 );
    }
}
