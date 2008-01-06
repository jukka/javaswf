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
 * A try-catch-finally block. 
 *
 * @author nickmain
 */
public class Try extends AVM1Operation {

    public final String catchVarName; //may be null
    public final int    catchRegister; //may be -1 (no register)
    
    /**
     * Label after the try-block, at the start of the catch block.
     * Null if there is no catch block 
     */
    public final String catchLabel; 
    
    /**
     * Label at the start of the finally block.
     * Null if there is no finally block.
     */
    public final String finallyLabel;
    
    /**
     * Label after the end of the finally block (or catch if there
     * is no finally).
     */
    public final String endLabel;
    
    public Try( int catchRegister, 
                String catchLabel, 
                String finallyLabel,
                String endLabel ) {
        this.catchRegister = catchRegister;
        this.catchVarName  = null;
        this.catchLabel    = catchLabel;
        this.finallyLabel  = finallyLabel;
        this.endLabel      = endLabel;
    }

    public Try( String catchVarName, 
                String catchLabel, 
                String finallyLabel,
                String endLabel ) {
        this.catchRegister = -1;
        this.catchVarName  = catchVarName;
        this.catchLabel    = catchLabel;
        this.finallyLabel  = finallyLabel;
        this.endLabel      = endLabel;
    }
        
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#labelReferences() */
    @Override
    public String[] labelReferences() {
        if( catchLabel != null && finallyLabel != null ) {
            return new String[] { catchLabel, finallyLabel, endLabel };
        }

        if( catchLabel != null ) {
            return new String[] { catchLabel, endLabel };
        }

        if( finallyLabel != null ) {
            return new String[] { finallyLabel, endLabel };
        }

        return new String[] { endLabel };
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
