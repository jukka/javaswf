package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1OperationAggregation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

public final class Substring extends AVM1OperationAggregation implements AVM1ValueProducer {

    public final boolean isMultibyte;
    
    public AVM1Operation count;
    public AVM1Operation index;
    public AVM1Operation string;
    
    public Substring( boolean isMultibyte ) {
        this.isMultibyte = isMultibyte;
    }
    
    @Override
    public void aggregate() {
        if( count  == null ) count  = consumePrevious();
        if( index  == null ) index  = consumePrevious();
        if( string == null ) string = consumePrevious();
    }

    @Override
    protected void writeOp(SWFActionBlock block) throws IOException {
        if( isMultibyte ) block.substringMB();
        else              block.substring();
    }
}
