package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * The optional catch clause of a Try.
 *
 * @author nickmain
 */
public class TryCatch extends AVM1Operation {

    public final Try    tryStart;
    public final String catchVarName; //may be null
    public final int    catchRegister; //may be -1 (no register)

    public TryCatch( Try tryStart, int catchRegister ) {
        this.tryStart = tryStart;
        
        this.catchRegister   = catchRegister;
        this.catchVarName    = null;
    }

    public TryCatch( Try tryStart, String catchVarName ) {
        this.tryStart = tryStart;
        
        this.catchRegister   = -1;
        this.catchVarName    = catchVarName;
    }
    
    @Override
    public void accept( AVM1OpVisitor visitor ) {
        visitor.visitTryCatch( this );
    }

    @Override
    public void write( SWFActionBlock block ) throws IOException {
        // nada - this is a virtual op
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#getFollowOnInstructions(com.anotherbigidea.flash.avm1.AVM1ActionBlock) */
    @Override
    public Collection<AVM1Operation> getFollowOnInstructions( AVM1ActionBlock block ) {
        return Collections.singleton( (AVM1Operation) tryStart.tryEnd );
    }
}
