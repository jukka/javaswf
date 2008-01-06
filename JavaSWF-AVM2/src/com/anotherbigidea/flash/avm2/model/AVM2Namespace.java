package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.Namespace;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.model.io.ConstantPool;

public class AVM2Namespace implements Comparable<AVM2Namespace> {

    /** The kind of namespace */
    public final NamespaceKind kind;
    
    /** The namespace name - may be null */
    public final String name;

    /** A singleton for the private namespace */
    public final static AVM2Namespace privateNamespace = 
        new AVM2Namespace( NamespaceKind.PrivateNamespace, null ); 
    
    /**
     * @param kind the namespace kind
     * @param name the namespace name - may be null (wildcard or moot)
     */
    public AVM2Namespace( NamespaceKind kind, String name ) {
        this.name = name;
        this.kind = kind;
    }

    /**
     * @param ns namespace to copy
     */
    public AVM2Namespace( Namespace ns ) {
        this.name = ns.name;
        this.kind = ns.kind;
    }
    
    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( ! (obj instanceof AVM2Namespace) ) return false;
        
        AVM2Namespace other = (AVM2Namespace) obj;
        
        if( kind != other.kind ) return false;
        if( name == null ) {
            return other.name == null;
        }
        
        return name.equals( other.name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        if( name == null ) return kind.hashCode();        
        return name.hashCode() * kind.hashCode();
    }
    
    /**
     * Get the namespace at the given index in the given pool
     */
    public static AVM2Namespace atIndex( ConstantPool pool, int index ) {
        if( index == 0 ) return null;
        
        return new AVM2Namespace( pool.namespaceAt( index ));
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        if( kind == NamespaceKind.PackageNamespace && name.length() == 0 ) {
            out.print( "Public" );
            return;
        }
        
        out.print( kind.name() + "(" + name + ")" );
    }

    /** @see java.lang.Comparable#compareTo(java.lang.Object) */
    public int compareTo(AVM2Namespace o) {
        if( o == null ) return -1;
        if( !( o instanceof AVM2Namespace )) return -1;
        
        AVM2Namespace other = (AVM2Namespace) o;
        
        //order by kind first
        if( kind != other.kind ) return kind.compareTo( other.kind );
        
        return name.compareTo( other.name );
    }
}
