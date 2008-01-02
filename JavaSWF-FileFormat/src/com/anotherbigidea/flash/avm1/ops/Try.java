package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1BlockContainer;
import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActionBlock.TryCatchFinally;
import com.anotherbigidea.flash.writers.ActionTextWriter;

/**
 * A try-catch-finally block
 *
 * @author nickmain
 */
public class Try extends AVM1Operation implements AVM1BlockContainer {

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
    
    /** @see com.anotherbigidea.flash.avm1.AVM1BlockContainer#subBlocks() */
    public AVM1ActionBlock[] subBlocks() {
        return new AVM1ActionBlock[] { tryBlock, catchBlock, finallyBlock };
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
    
    /**
     * Print the operation
     */
    public void print( ActionTextWriter writer ) throws IOException {

        TryCatchFinally tcf = ( catchVarName == null ) ? 
                                    writer._try( catchRegister ) :
                                    writer._try( catchVarName );

        tryBlock.print( (ActionTextWriter) tcf.tryBlock() );
        
        if( catchBlock.count()   > 0 ) catchBlock.print  ( (ActionTextWriter) tcf.catchBlock() );
        if( finallyBlock.count() > 0 ) finallyBlock.print( (ActionTextWriter) tcf.finallyBlock() );
                                  
        tcf.endTry();                          
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitTry( this );        
    }
}
