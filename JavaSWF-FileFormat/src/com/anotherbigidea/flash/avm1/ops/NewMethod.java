package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Call a constructor method
 *
 * @author nickmain
 */
public class NewMethod extends AVM1OperationAggregation implements AVM1ValueProducer {

    public AVM1Operation name;
    public AVM1Operation object;
    public AVM1Operation numArgs;
    public AVM1Operation[] args;
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#aggregate() */
    @Override
    public void aggregate() {
        if( name    == null ) name = consumePrevious();
        if( object  == null ) object = consumePrevious();
        if( numArgs == null ) numArgs = consumePrevious();
        
        if( args == null ) {
            if( !( numArgs instanceof ConstantOp )) throw new RuntimeException( "arg count is not a constant" );
            int argCount = numArgs.intValue();
            args = new AVM1Operation[ argCount ];
            
            for( int i = 0; i < args.length; i++ ) {
                args[i] = consumePrevious();
            }
        }
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OperationAggregation#writeOp(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        block.newMethod();
    }
}
