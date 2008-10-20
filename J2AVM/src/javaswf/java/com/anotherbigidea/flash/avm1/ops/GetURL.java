package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Load a URL.
 *
 * @author nickmain
 */
public final class GetURL extends AVM1OperationAggregation {
    
    public AVM1Operation url;
    public AVM1Operation target;
    
    public final boolean targetSprite;
    public final boolean loadVars;
    public final SWFActionBlock.GetURLMethod method;
    
    private final boolean legacy;
    
    public GetURL( String url, String target ) {
        this.targetSprite = false;
        this.loadVars     = false;
        this.method       = SWFActionBlock.GetURLMethod.MethodNone;
        legacy = true;
        
        this.target = prependOp( new ConstantOp.StringValue( target ));
        this.url    = prependOp( new ConstantOp.StringValue( url ));
    }
    
    public GetURL( boolean targetSprite, boolean loadVars, SWFActionBlock.GetURLMethod method ) {
        this.targetSprite = targetSprite;
        this.loadVars     = loadVars;
        this.method       = method;
        legacy = false;
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( target != null ) target = consumePrevious();
        if( url    != null ) url    = consumePrevious();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        if( legacy ) {            
            block.getURL( ((ConstantOp.StringValue) url).value, 
                          ((ConstantOp.StringValue) target).value );
        }
        else {
            block.getURL( method, loadVars, targetSprite );
        }        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitGetURL( this );        
    }
}