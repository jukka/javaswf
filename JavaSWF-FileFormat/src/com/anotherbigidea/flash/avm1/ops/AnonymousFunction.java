package com.anotherbigidea.flash.avm1.ops;

import com.anotherbigidea.flash.avm1.AVM1ValueProducer;

/**
 * An anonymous function
 *
 * @author nickmain
 */
public class AnonymousFunction extends Function implements AVM1ValueProducer {

    public AnonymousFunction( int numRegistersToAllocate,
                              String[] paramNames,
                              int[] registersForArguments ) {
        super( "", numRegistersToAllocate, paramNames, registersForArguments );
    }
}
