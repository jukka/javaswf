package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

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

    /**
     * The catch block - may be null
     */
    public TryCatch tryCatch;
    
    /**
     * The finally block - may be null
     */
    public TryFinally tryFinally;
    
    /**
     * The end of the try region
     */
    public TryEnd tryEnd;
 

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write( SWFActionBlock block ) throws IOException {
                
        TryCatchFinally tcf = null;
        
        if( tryCatch != null ) {
            tcf = ( tryCatch.catchVarName == null ) ? 
                                      block._try( tryCatch.catchRegister ) :
                                      block._try( tryCatch.catchVarName );
        }
        else {
            tcf = block._try( 0 );            
        }
                                          
        SWFActionBlock tryBlock = tcf.tryBlock();
        for( AVM1Operation op = next(); op != null; op = op.next() ) {
            if( op == tryCatch || op == tryFinally || op == tryEnd ) break;
            op.write( tryBlock );
        }          
        tryBlock.end();
                                  
        if( tryCatch != null ) {
            SWFActionBlock catchBlock = tcf.catchBlock();
            
            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op == tryFinally || op == tryEnd ) break;                 
                op.write( catchBlock );
            }          
            catchBlock.end();                                              
        }

        if( tryEnd != null ) {
            SWFActionBlock finallyBlock = tcf.finallyBlock();

            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op == tryEnd ) break;
                 
                op.write( finallyBlock );
            }          
            finallyBlock.end();                                              
        }
        
        tcf.endTry();
    }
    
    /**
     * Print the operation
     */
    public void print( ActionTextWriter writer ) throws IOException {

        TryCatchFinally tcf = null;
        
        if( tryCatch != null ) {
            tcf = ( tryCatch.catchVarName == null ) ? 
                    writer._try( tryCatch.catchRegister ) :
                    writer._try( tryCatch.catchVarName );
        }
        else {
            tcf = writer._try( 0 );            
        }
        
        tcf.tryBlock();
        for( AVM1Operation op = next(); op != null; op = op.next() ) {
            if( op == tryCatch || op == tryFinally || op == tryEnd ) break;             
            op.print( writer );
        }          
        writer.end();
                                  
        if( tryCatch != null ) {
            tcf.catchBlock();
            
            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op == tryFinally || op == tryEnd ) break;                 
                op.print( writer );
            }          
            writer.end();                                              
        }

        if( tryFinally != null ) {
            tcf.finallyBlock();

            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op == tryEnd ) break;                 
                op.print( writer );
            }          
            writer.end();                                              
        }
        
        tcf.endTry();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitTry( this );        
    }
}
