package com.anotherbigidea.flash.avm2.model;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * An XML attribute name qualified by a namespace.  The namespace is
 * specified at runtime.
 *
 * @author nickmain
 */
public class AVM2RuntimeQAttrName extends AVM2Name {

    /**
     * @param name the simple name
     */
    public AVM2RuntimeQAttrName( String name ) {
        super( MultiNameKind.RTQnameA, null, name, null );
    }    
}
