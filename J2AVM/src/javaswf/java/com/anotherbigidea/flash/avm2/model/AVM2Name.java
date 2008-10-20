package com.anotherbigidea.flash.avm2.model;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.MultiName;
import com.anotherbigidea.flash.avm2.MultiNameKind;
import com.anotherbigidea.flash.avm2.Namespace;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.model.io.ConstantPool;

/**
 * Base for AVM2 names
 *
 * @author nickmain
 */
public abstract class AVM2Name implements Comparable<AVM2Name> {

    /** The kind of name */
    public final MultiNameKind kind;

    /** The namespace - may be null */
    public final AVM2Namespace namespace;
    
    /** The name - may be null */
    public final String name;
    
    /** The set of namespaces - may be null */
    public final List<AVM2Namespace> namespaceSet;

    /*pkg*/ AVM2Name( MultiNameKind kind, AVM2Namespace namespace, 
                      String name, List<AVM2Namespace> namespaceSet ) {
        
        this.namespaceSet = namespaceSet;
        this.namespace    = namespace;
        this.name         = name;
        this.kind         = kind;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( ! (obj instanceof AVM2Name)) return false;
        
        AVM2Name other = (AVM2Name) obj;
        
        if( name != null ) {
            if( other.name == null ) return false;
            if( ! other.name.equals( name ) ) return false;
        } else {
            if( other.name != null ) return false;
        }
        
        if( namespace != null ) {
            if( other.namespace == null ) return false;
            if( ! other.namespace.equals( namespace ) ) return false;
            
        } else {
            if( other.namespace != null ) return false;
        }
        
        if( namespaceSet != null ) {
            if( other.namespaceSet == null ) return false;
            if( ! other.namespaceSet.equals( namespaceSet ) ) return false;
            
        } else {
            if( other.namespaceSet != null ) return false;
        }
        
        return true;        
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        
        int hashCode = 1;
        
        if( name         != null ) hashCode *= name.hashCode();
        if( namespace    != null ) hashCode *= namespace.hashCode();
        if( namespaceSet != null ) hashCode *= namespaceSet.hashCode();
        
        return hashCode;
    }
    
    /**
     * Get the simple name (last segment of a dot-puntuated name) from a string.
     */
    public static String simpleName( String name ) {
        int period = name.lastIndexOf( '.' );
        if( period >= 0 ) return name.substring( period + 1 );
        return name;
    }
    
    /**
     * Get the package part of a name (mirror to simpleName())
     * @return empty string if there is no package prefix
     */
    public static String packagePrefix( String name ) {
        int period = name.lastIndexOf( '.' );
        if( period >= 0 ) return name.substring( 0, period );
        return "";
    }
    
    /**
     * Get the name at the given index in the given constant pool
     */
    public static AVM2Name atIndex( ConstantPool pool, int index ) {
        if( index == 0 ) return null;
        
        MultiName mn = pool.nameAt( index );
        
        return from( mn );
    }
    
    /** Make from a multiname */
    public static AVM2Name from( MultiName mn ) {
        
        String name = mn.name;
        AVM2Namespace ns = ( mn.namespace != null ) ? 
                               new AVM2Namespace( mn.namespace ) :
                               null;
        
        List<AVM2Namespace> nsSet = (mn.namespaceSet != null) ?
                                        new ArrayList<AVM2Namespace>() :
                                        null;
        if( nsSet != null ) {
            for( Namespace n : mn.namespaceSet ) {
                nsSet.add( new AVM2Namespace( n ));
            }
        }
                               
        switch( mn.kind ) {
            case Qname:       return new AVM2QName( ns, name );
            case QnameA:      return new AVM2QAttrName( ns, name );
            case Multiname:   return new AVM2Multiname( name, nsSet );
            case MultinameA:  return new AVM2AttrMultiname( name, nsSet );
            case RTQname:     return new AVM2RuntimeQName( name );
            case RTQnameA:    return new AVM2RuntimeQAttrName( name );
            case MultinameL:  return new AVM2LateMultiname( nsSet );
            case MultinameLA: return new AVM2LateAttrMultiname( nsSet ); 
            case RTQnameL:    return new AVM2LateRuntimeQName();
            case RTQnameLA:   return new AVM2LateRuntimeQAttrName();
        }
        
        return null;
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        
        if( kind == MultiNameKind.Qname
         && namespace != null 
         && namespace.kind == NamespaceKind.PackageNamespace 
         && namespace.name.length() == 0 ) {

            out.print( name );
            return;
        }
        
        out.print( kind.name() + ":" );
        
        if( namespace != null ) {
            namespace.dump( out );
        }
        
        if( namespaceSet != null ) {
            out.print( "{" );
            for( AVM2Namespace ns : namespaceSet ) {
                out.print( " " );
                ns.dump( out );
            }
            out.print( " }" );
        }
        
        if( name != null ) {
            if( namespace != null || namespaceSet != null ) {
                out.print( "::" );
            }
            
            out.print( name );
        }
    }
    
    /**
     * Get the index of this name in the given pool
     */
    public int indexIn( ConstantPool pool ) {
        
        List<Namespace> nsSet = null;
        
        if( namespaceSet != null ) {
            nsSet = new ArrayList<Namespace>();
            
            for( AVM2Namespace ns : namespaceSet ) {
                Namespace n = pool.namespaceIndex( ns.kind, ns.name );
                nsSet.add( n );
            }
        }
        
        Namespace ns = null;
        if( namespace != null ) {
            ns = pool.namespaceIndex( namespace.kind, namespace.name );
        }
        
        return pool.nameIndex( kind, name, ns, nsSet ).poolIndex;
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {     

        List<Namespace> nsSet = null;
        
        if( namespaceSet != null ) {
            nsSet = new ArrayList<Namespace>();
            
            for( AVM2Namespace ns : namespaceSet ) {
                Namespace n = context.pool.namespaceIndex( ns.kind, ns.name );
                nsSet.add( n );
            }
        }
        
        Namespace ns = null;        
        if( namespace != null ) {
            ns = context.pool.namespaceIndex( namespace.kind, namespace.name );
        }
        
        context.pool.nameIndex( kind, name, ns, nsSet );
    }

    /** @see java.lang.Comparable#compareTo(java.lang.Object) */
    public int compareTo( AVM2Name o ) {
        if( o == null ) return -1;
        if(! (o instanceof AVM2Name) ) return -1;
        
        AVM2Name other = (AVM2Name) o;
        
        //sort by kind first
        if( other.kind != kind ) return kind.compareTo( other.kind );
        
        //sort by name
        if( name != null ) return name.compareTo( other.name );
        
        //sort by namespace
        if( namespace != null ) return namespace.compareTo( other.namespace );
        
        //sort by namespace set
        if( namespaceSet != null ) {
            AVM2Namespace ns1 = namespaceSet.iterator().next();
            AVM2Namespace ns2 = other.namespaceSet.iterator().next();
            if( ns1 != null && ns2 != null) return ns1.compareTo( ns2 );
        }
        
        return 0;
    }
    
    @Override
    public String toString() {
        StringWriter         s   = new StringWriter();
        IndentingPrintWriter ipw = new IndentingPrintWriter( s );
        dump( ipw );
        ipw.flush();
        
        return s.toString();
    }
}
