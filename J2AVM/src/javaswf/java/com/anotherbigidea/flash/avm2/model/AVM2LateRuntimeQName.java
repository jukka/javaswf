package com.anotherbigidea.flash.avm2.model;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * A name qualified by a namespace - name and namespace both
 * specified at runtime.
 *
 * @author nickmain
 */
public class AVM2LateRuntimeQName extends AVM2Name {

    public AVM2LateRuntimeQName() {
        super( MultiNameKind.RTQnameL, null, null, null );
    }    
}
