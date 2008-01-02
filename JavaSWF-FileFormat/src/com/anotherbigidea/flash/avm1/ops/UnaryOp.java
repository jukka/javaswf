package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * A unary operation - that pops one value and pushes a result
 *
 * @author nickmain
 */
public final class UnaryOp extends AVM1OperationAggregation implements AVM1ValueProducer {
    
    public final UnaryOpType type;
    
    public AVM1Operation arg;
    
    public UnaryOp( UnaryOpType type ) {
        this.type = type;
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( arg == null ) arg = consumePrevious();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        switch( type ) {
            case UnOp_Not:             block.not(); break;
            
            case UnOp_StringLength:    block.stringLength(); break;
            case UnOp_StringLengthMB:  block.stringLengthMB(); break;
            
            case UnOp_CharToAscii:     block.charToAscii(); break;
            case UnOp_AsciiToChar:     block.asciiToChar(); break;
            case UnOp_CharMBToAscii:   block.charMBToAscii(); break;
            case UnOp_AsciiToCharMB:   block.asciiToCharMB(); break;
    
            case UnOp_ToInteger:       block.toInteger(); break;
            case UnOp_ConvertToNumber: block.convertToNumber(); break;
            case UnOp_ConvertToString: block.convertToString(); break;
            
            case UnOp_TypeOf:          block.typeOf(); break;
        }        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitUnaryOp( this );        
    }
}