package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Conditional branch
 *
 * @author nickmain
 */
public class IfJump extends AVM1OperationAggregation {

    public final String jumpLabel;
    public AVM1Operation condition;
    
    public IfJump( String jumpLabel ) {
        this.jumpLabel = jumpLabel;
    }
    
    @Override
    public void aggregate() {
        if( condition == null ) condition = consumePrevious();
    }

    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.ifJump( jumpLabel );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#labelReferences() */
    @Override
    public String[] labelReferences() {
        return new String[] { jumpLabel };
    }    

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitIfJump( this );        
    }
}
