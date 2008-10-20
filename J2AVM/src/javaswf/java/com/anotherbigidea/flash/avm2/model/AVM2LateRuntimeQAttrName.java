package com.anotherbigidea.flash.avm2.model;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * An XML attribute name qualified by a namespace - name and namespace both
 * specified at runtime.
 *
 * @author nickmain
 */
public class AVM2LateRuntimeQAttrName extends AVM2Name {

    public AVM2LateRuntimeQAttrName() {
        super( MultiNameKind.RTQnameLA, null, null, null );
    }    
}
