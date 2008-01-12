package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * The optional finally clause of a Try
 *
 * @author nickmain
 */
public class TryFinally extends AVM1Operation {

    public final Try tryStart;

    public TryFinally( Try tryStart ) {
        this.tryStart = tryStart;
    }
    
    @Override
    public void accept( AVM1OpVisitor visitor ) {
        visitor.visitTryFinally( this );
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
