package com.anotherbigidea.flash.avm2.model;

import java.util.Collections;
import java.util.List;

import com.anotherbigidea.flash.avm2.MultiNameKind;

/**
 * A name qualified by a set of namespaces. The name is specified at runtime.
 *
 * @author nickmain
 */
public class AVM2LateMultiname extends AVM2Name {

    /**
     * A late-multiname specifying only the empty/public package.
     */
    public static final AVM2LateMultiname EMPTY_PACKAGE = 
        new AVM2LateMultiname( 
                Collections.singletonList( AVM2StandardNamespace.EmptyPackage.namespace ) );
    
    /**
     * @param namespaceSet the set of namespaces
     */
    public AVM2LateMultiname( List<AVM2Namespace> namespaceSet ) {
        super( MultiNameKind.MultinameL, null, null, namespaceSet );
    }
}
