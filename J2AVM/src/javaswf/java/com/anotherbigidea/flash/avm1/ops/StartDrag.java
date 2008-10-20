package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Start a drag
 *
 * @author nickmain
 */
public class StartDrag extends AVM1OperationAggregation {

    public AVM1Operation target;
    public AVM1Operation lockCenter;
    public AVM1Operation constrain;
    public AVM1Operation y2;
    public AVM1Operation x2;
    public AVM1Operation y1;
    public AVM1Operation x1;

    @Override
    public void aggregate() {
        if( target     == null ) target = consumePrevious();
        if( lockCenter == null ) lockCenter = consumePrevious();
        if( constrain  == null ) constrain = consumePrevious();

        if( constrain.isNonZero() ) {
            if( y2 == null ) y2 = consumePrevious();
            if( x2 == null ) x2 = consumePrevious();
            if( y1 == null ) y1 = consumePrevious();
            if( x1 == null ) x1 = consumePrevious();            
        }
    }

    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.startDrag();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitStartDrag( this );        
    }
}
