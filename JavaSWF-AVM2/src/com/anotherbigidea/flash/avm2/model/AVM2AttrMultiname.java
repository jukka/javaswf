package com.anotherbigidea.flash.avm2.model;

import java.util.List;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * An XML attribute name qualified by a set of namespaces
 *
 * @author nickmain
 */
public class AVM2AttrMultiname extends AVM2Name {

    /**
     * @param name the simple name
     * @param namespaceSet the set of namespaces
     */
    public AVM2AttrMultiname( String name, List<AVM2Namespace> namespaceSet ) {
        super( MultiNameKind.MultinameA, null, name, namespaceSet );
    }
}
