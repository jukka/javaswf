package com.anotherbigidea.flash.avm2.model;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * A name qualified by a namespace.  The namespace is specified at runtime.
 *
 * @author nickmain
 */
public class AVM2RuntimeQName extends AVM2Name {

    /**
     * @param name the simple name
     */
    public AVM2RuntimeQName( String name ) {
        super( MultiNameKind.RTQname, null, name, null );
    }    
}
