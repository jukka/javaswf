package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Go to a given frame.
 *
 * @author nickmain
 */
public class GotoFrame extends AVM1OperationAggregation {

    public static enum PlayAction { AndPlay, AndStop, AndSame };
    
    /**
     * The action to perform when the target frame is reached
     */
    public final PlayAction playAction;
    
    /**
     * The target frame (either a frame number or frame label)
     */
    public AVM1Operation frame;
    
    /**
     * Goto a frame number
     */
    public static GotoFrame number( int number ) {
        GotoFrame gf = new GotoFrame( PlayAction.AndSame );
        gf.setFrame( new ConstantOp.IntValue( number ) );
        return gf;
    }

    /**
     * Goto a frame label
     */
    public static GotoFrame label( String label ) {
        GotoFrame gf = new GotoFrame( PlayAction.AndSame );
        gf.setFrame( new ConstantOp.StringValue( label ) );
        return gf;        
    }

    /**
     * Goto a frame and play (or stop)
     */
    public static GotoFrame andPlay( boolean play ) {
        return new GotoFrame( play ? PlayAction.AndPlay : PlayAction.AndStop );        
    }
    
    private GotoFrame( PlayAction playAction ) {
        this.playAction = playAction;
    }
    
    private void setFrame( AVM1Operation op ) {
        frame = prependOp( op );
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( frame == null ) frame = consumePrevious();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        boolean play = playAction == PlayAction.AndPlay;
        block.gotoFrame( play );
    }        
    
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write(SWFActionBlock block) throws IOException {
        if( playAction == PlayAction.AndSame ) {
            if( frame instanceof ConstantOp.IntValue ) {
                block.gotoFrame( ((ConstantOp.IntValue) frame).value );
            }
            else if( frame instanceof ConstantOp.StringValue ) {
                block.gotoFrame( ((ConstantOp.StringValue) frame).value );
            }
            else throw new IOException( "target frame must be number or string label" );
        }
        else {
            super.write( block );
        }
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitGotoFrame( this );        
    }
}
