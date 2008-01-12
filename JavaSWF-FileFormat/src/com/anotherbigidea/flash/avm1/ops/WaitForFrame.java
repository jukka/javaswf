package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Wait for a given frame
 *
 * @author nickmain
 */
public final class WaitForFrame extends AVM1OperationAggregation {

    /** The target frame - either a number or string label */
    public AVM1Operation frame;
        
    public final String jumpLabel;
    
    public WaitForFrame( int frameNumber, String jumpLabel ) {
        this( jumpLabel );
        
        frame = prependOp( new ConstantOp.IntValue( frameNumber ));
    }

    public WaitForFrame( String jumpLabel ) {
        this.jumpLabel = jumpLabel;
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( frame == null ) frame = consumePrevious();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.waitForFrame( jumpLabel );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#labelReferences() */
    @Override
    public String[] labelReferences() {
        return new String[] { jumpLabel };
    }    

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitWaitForFrame( this );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#getFollowOnInstructions(com.anotherbigidea.flash.avm1.AVM1ActionBlock) */
    @Override
    public Collection<AVM1Operation> getFollowOnInstructions( AVM1ActionBlock block ) {
        Collection<AVM1Operation> ops = new HashSet<AVM1Operation>();
        ops.add( block.findLabel( jumpLabel ) );
        ops.add( next() );
        return ops;
    }
}