package com.anotherbigidea.flash.avm2.model;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * An XML attribute name qualified by a namespace
 *
 * @author nickmain
 */
public class AVM2QAttrName extends AVM2Name {

    /**
     * @param namespace the namespace
     * @param name the simple name
     */
    public AVM2QAttrName( AVM2Namespace namespace, String name ) {
        super( MultiNameKind.QnameA, namespace, name, null );
    }    
}
