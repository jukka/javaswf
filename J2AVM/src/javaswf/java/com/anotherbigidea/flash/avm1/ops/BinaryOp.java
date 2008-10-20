package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * A binary operation - that pops two values and pushes a result
 *
 * @author nickmain
 */
public final class BinaryOp extends AVM1OperationAggregation implements AVM1ValueProducer {
    
    public final BinaryOpType type;
    
    public AVM1Operation firstArg;
    public AVM1Operation secondArg;
    
    public BinaryOp( BinaryOpType type ) {
        this.type = type;
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( secondArg == null ) secondArg = consumePrevious();
        if( firstArg  == null ) firstArg  = consumePrevious();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        switch( type ) {
            case BinOp_Add:                block.add(); break;
            case BinOp_TypedAdd:           block.typedAdd(); break;
            case BinOp_Subtract:           block.substract(); break;
            case BinOp_Multiply:           block.multiply(); break;
            case BinOp_Divide:             block.divide(); break;
            case BinOp_Modulo:             block.modulo(); break;
    
            case BinOp_Equals:             block.equals(); break;
            case BinOp_TypedEquals:        block.typedEquals(); break;
            case BinOp_StrictEquals:       block.strictEquals(); break;
            case BinOp_LessThan:           block.lessThan(); break;
            case BinOp_TypedLessThan:      block.typedLessThan(); break;
            case BinOp_GreaterThan:        block.greaterThan(); break;
    
            case BinOp_And:                block.and(); break;
            case BinOp_Or:                 block.or(); break;
    
            case BinOp_BitAnd:             block.bitAnd(); break;
            case BinOp_BitOr:              block.bitOr(); break;
            case BinOp_BitXor:             block.bitXor(); break;
            case BinOp_ShiftLeft:          block.shiftLeft(); break;
            case BinOp_ShiftRight:         block.shiftRight(); break;
            case BinOp_ShiftRightUnsigned: block.shiftRightUnsigned(); break;
            
            case BinOp_InstanceOf:         block.instanceOf(); break;
            case BinOp_Cast:               block.cast(); break;
    
            case BinOp_Concat:             block.concat(); break;
            case BinOp_StringEquals:       block.stringEquals(); break;
            case BinOp_StringLessThan:     block.stringLessThan(); break;
            case BinOp_StringGreaterThan:  block.stringGreaterThan(); break;
        }        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitBinaryOp( this );        
    }
}