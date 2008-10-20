package com.anotherbigidea.flash.avm2;


/**
 * A namespace
 *
 * @author nickmain
 */
public class Namespace {
    
    /** Index within the namespace pool */
    public final int poolIndex;
    
    /** Index of the name in the string pool */
    public final int nameIndex;
    
    /** The kind of namespace */
    public final NamespaceKind kind;
    
    /** The name - may be null */
    public final String name;
    
    public Namespace( NamespaceKind kind, String name, int index, int nameIndex ) {
        this.kind = kind;
        this.name = name;
        
        this.poolIndex = index;
        this.nameIndex = nameIndex;
    }

    /** Make a string representation of the namespace */
    public static String toString( NamespaceKind kind, String name ) {
        String kindS;
        
        switch( kind ) {
            case Namespace:                kindS = "namespace"; break; 
            case PrivateNamespace:         kindS = "private"; break;
            case PackageNamespace:         kindS = "package"; break;
            case PackageInternalNamespace: kindS = "internal"; break;
            case ProtectedNamespace:       kindS = "protected"; break;
            case ExplicitNamespace:        kindS = "explicit"; break;
            case StaticProtectedNamespace: kindS = "static_protected"; break;
            default:  kindS = "???"; break;
        }
        
        if( name == null ) return kindS;
        return kindS + "(" + name + ")" ;        
    }
    
    @Override public String toString() {
        return toString( kind, name );
    }

    @Override
    public boolean equals(Object obj) {
        if( obj == null || !(obj instanceof Namespace) ) return false;
        
        Namespace ns = (Namespace) obj;
        
        if( kind != ns.kind ) return false;
        return name == ns.name;
        
    }

    @Override
    public int hashCode() {
        return kind.hashCode() + ((name == null) ? 0 : name.hashCode());
    }
}
