package com.anotherbigidea.flash.avm2.model;

import static com.anotherbigidea.flash.avm2.model.AVM2StandardNamespace.*;

/**
 * Some standard names that crop up when dealing with AVM2 code.
 *
 * @author nickmain
 */
public enum AVM2StandardName {

    TypeVoid    ( EmptyPackage, "void" ),    
    TypeString  ( EmptyPackage, "String" ),    
    TypeArray   ( EmptyPackage, "Array" ),    
    TypeObject  ( EmptyPackage, "Object" ),    
    TypeInt     ( EmptyPackage, "int" ),    
    TypeUInt    ( EmptyPackage, "uint" ),    
    TypeNumber  ( EmptyPackage, "Number" ),    
    TypeBoolean ( EmptyPackage, "Boolean" );
    
    public final AVM2QName qname;
    
    private AVM2StandardName( AVM2StandardNamespace ns, String name ) {
        qname = new AVM2QName( ns.namespace, name );
    }
    
}
