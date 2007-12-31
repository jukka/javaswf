package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActionBlock.TryCatchFinally;

/**
 * A try-catch-finally block
 *
 * @author nickmain
 */
public class Try extends AVM1Operation {

    public final String catchVarName; //may be null
    public final int    catchRegister; //may be -1 (no register)
    
    public final AVM1ActionBlock tryBlock     = new AVM1ActionBlock( this );
    public final AVM1ActionBlock catchBlock   = new AVM1ActionBlock( this );
    public final AVM1ActionBlock finallyBlock = new AVM1ActionBlock( this );
    
    public Try( int catchRegister ) {
        this.catchRegister = catchRegister;
        this.catchVarName  = null;
    }

    public Try( String catchVarName ) {
        this.catchRegister = -1;
        this.catchVarName  = catchVarName;
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write( SWFActionBlock block ) throws IOException {
        TryCatchFinally tcf = ( catchVarName == null ) ? 
                                  block._try( catchRegister ) :
                                  block._try( catchVarName );
        
        tryBlock.write( tcf.tryBlock() );
        
        if( catchBlock.count()   > 0 ) catchBlock.write  ( tcf.catchBlock() );
        if( finallyBlock.count() > 0 ) finallyBlock.write( tcf.finallyBlock() );
                                  
        tcf.endTry();
    }
}
