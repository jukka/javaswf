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

    public final String catchVarName; //may be null
    public final int    catchRegister; //may be -1 (no register)
    
    /**
     * Label after the try-block
     */
    public String endTryLabel; 

    /**
     * Label at the end of the catch block.
     * Null if there is no catch block.
     */
    public String endCatchLabel;
    
    /**
     * Label at the end of the finally block.
     * Null if there is no finally block.
     */
    public String endFinallyLabel;
    
    
    public Try( int catchRegister ) {
        this.catchRegister   = catchRegister;
        this.catchVarName    = null;
    }

    public Try( String catchVarName ) {
        this.catchRegister   = -1;
        this.catchVarName    = catchVarName;
    }
        
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#labelReferences() */
    @Override
    public String[] labelReferences() {
        if( endCatchLabel != null && endFinallyLabel != null ) {
            return new String[] { endCatchLabel, endFinallyLabel, endTryLabel };
        }

        if( endCatchLabel != null ) {
            return new String[] { endCatchLabel, endTryLabel };
        }

        if( endFinallyLabel != null ) {
            return new String[] { endFinallyLabel, endTryLabel };
        }

        return new String[] { endTryLabel };
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write( SWFActionBlock block ) throws IOException {
        TryCatchFinally tcf = ( catchVarName == null ) ? 
                                  block._try( catchRegister ) :
                                  block._try( catchVarName );
        
        String endLabel = null;                                  
                                  
        SWFActionBlock tryBlock = tcf.tryBlock();
        for( AVM1Operation op = next(); op != null; op = op.next() ) {
            if( op instanceof JumpLabel && ((JumpLabel) op).label.equals( endTryLabel ) ) {
                endLabel = ((JumpLabel) op).label;
                break;
            }
             
            op.write( tryBlock );
        }          
        tryBlock.end();
                                  
        if( endCatchLabel != null ) {
            SWFActionBlock catchBlock = tcf.catchBlock();
            catchBlock.jumpLabel( endLabel );
            
            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op instanceof JumpLabel && ((JumpLabel) op).label.equals( endCatchLabel ) ) {
                    endLabel = ((JumpLabel) op).label;
                    break;
                }
                 
                op.write( catchBlock );
            }          
            catchBlock.end();                                              
        }

        if( endFinallyLabel != null ) {
            SWFActionBlock finallyBlock = tcf.finallyBlock();
            finallyBlock.jumpLabel( endLabel );

            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op instanceof JumpLabel && ((JumpLabel) op).label.equals( endFinallyLabel ) ) {
                    endLabel = ((JumpLabel) op).label;
                    break;
                }
                 
                op.write( finallyBlock );
            }          
            finallyBlock.end();                                              
        }
        
        tcf.endTry();
        block.jumpLabel( endLabel );
    }
    
    /**
     * Print the operation
     */
    public void print( ActionTextWriter writer ) throws IOException {

        TryCatchFinally tcf = ( catchVarName == null ) ? 
                writer._try( catchRegister ) :
                writer._try( catchVarName );

        String endLabel = null;                                  
        
        tcf.tryBlock();
        for( AVM1Operation op = next(); op != null; op = op.next() ) {
            if( op instanceof JumpLabel && ((JumpLabel) op).label.equals( endTryLabel ) ) {
                endLabel = ((JumpLabel) op).label;
                break;
            }
             
            op.print( writer );
        }          
        writer.end();
                                  
        if( endCatchLabel != null ) {
            tcf.catchBlock();
            writer.jumpLabel( endLabel );
            
            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op instanceof JumpLabel && ((JumpLabel) op).label.equals( endCatchLabel ) ) {
                    endLabel = ((JumpLabel) op).label;
                    break;
                }
                 
                op.print( writer );
            }          
            writer.end();                                              
        }

        if( endFinallyLabel != null ) {
            tcf.finallyBlock();
            writer.jumpLabel( endLabel );

            for( AVM1Operation op = next(); op != null; op = op.next() ) {
                if( op instanceof JumpLabel && ((JumpLabel) op).label.equals( endFinallyLabel ) ) {
                    endLabel = ((JumpLabel) op).label;
                    break;
                }
                 
                op.print( writer );
            }          
            writer.end();                                              
        }
        
        tcf.endTry();
        writer.jumpLabel( endLabel );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitTry( this );        
    }
}
