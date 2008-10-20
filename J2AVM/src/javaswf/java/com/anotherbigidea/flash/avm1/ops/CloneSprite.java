package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Clone a sprite
 *
 * @author nickmain
 */
public class CloneSprite extends AVM1OperationAggregation {

    public AVM1Operation depth;
    public AVM1Operation newName;
    public AVM1Operation clipPath;
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( depth    == null ) depth = consumePrevious();
        if( newName  == null ) newName = consumePrevious();
        if( clipPath == null ) clipPath = consumePrevious();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.cloneSprite();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitCloneSprite( this );        
    }
}
