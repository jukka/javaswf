package com.anotherbigidea.flash.avm2.model;

import static com.anotherbigidea.flash.avm2.NamespaceKind.*;

import com.anotherbigidea.flash.avm2.NamespaceKind;

/**
 * Some of the standard namespaces that occur when dealing with AVM2 code.
 *
 * @author nickmain
 */
public enum AVM2StandardNamespace {

    EmptyPackage ( PackageNamespace, "" ),
    FlashPackage ( PackageNamespace, "flash" );
    
    public final AVM2Namespace namespace;
    
    private AVM2StandardNamespace( NamespaceKind kind, String name ) {
        namespace = new AVM2Namespace( kind, name );
    }
    
}
