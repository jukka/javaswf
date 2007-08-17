package com.anotherbigidea.flash.avm2.model;

import java.util.List;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * An XML attribute name qualified by a set of namespaces.  The name is
 * specified at runtime.
 *
 * @author nickmain
 */
public class AVM2LateAttrMultiname extends AVM2Name {

    /**
     * @param namespaceSet the set of namespaces
     */
    public AVM2LateAttrMultiname( List<AVM2Namespace> namespaceSet ) {
        super( MultiNameKind.MultinameLA, null, null, namespaceSet );
    }
}
