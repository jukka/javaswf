package com.anotherbigidea.flash.avm1.ops;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;

/**
 * An anonymous function
 *
 * @author nickmain
 */
public final class AnonymousFunction extends Function implements AVM1ValueProducer {

    public AnonymousFunction( int numRegistersToAllocate,
                              String[] paramNames,
                              int[] registersForArguments,
                              int preloadingFlags ) {
        super( "", numRegistersToAllocate, paramNames, registersForArguments, preloadingFlags );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitAnonymousFunction( this );        
    }
}
