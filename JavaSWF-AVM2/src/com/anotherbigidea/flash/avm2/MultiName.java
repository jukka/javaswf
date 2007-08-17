package com.anotherbigidea.flash.avm2;

import java.util.List;


/**
 * A multiname
 *
 * @author nickmain
 */
public class MultiName {

    /** The pool index of this name */
    public final int poolIndex;
    
    /** The kind of name */
    public final MultiNameKind kind;

    /** The simple name - may be null */
    public final String name;
    
    /** The pool index of the simple name */
    public final int nameIndex;
    
    /** The namespace set - may be null */
    public final List<Namespace> namespaceSet;
    
    /** The pool index of the set */
    public final int namespaceSetIndex;
    
    /** The namespace - may be null */
    public final Namespace namespace;
    
    public MultiName( MultiNameKind kind, int index,
                       String name, int nameIndex,
                       List<Namespace> set, int setIndex,
                       Namespace namespace ) {
        this.kind = kind;
        this.poolIndex = index;
        
        this.name = name;
        this.nameIndex = nameIndex;
        
        this.namespaceSet = set;
        this.namespaceSetIndex = setIndex;
        
        this.namespace = namespace;
    }
     
    /** Make a string representation of the name */
    private static String makeKey( MultiNameKind kind, 
                                   String name, 
                                   String setKey, 
                                   Namespace namespace ) {
        
        StringBuilder buff = new StringBuilder( kind.name() );
        buff.append( "(" );
        
        if( namespace != null ) {
            buff.append( namespace.toString() );
        }
        
        if( setKey != null ) {
            buff.append( setKey );
        }
        
        if( name != null ) {
            buff.append( "::" );
            buff.append( name );
        }

        buff.append( ")" );
        return buff.toString();
    }
    
    /** Make a string representation of the name */
    public static String makeKey( MultiNameKind kind, 
                                  String name, 
                                  Namespace[] set, 
                                  Namespace namespace ) {
        
        String setKey = null;
        
        if( set != null ) {
            StringBuilder buff = new StringBuilder();
            if( set != null ) {
                boolean first = true;
                for (Namespace ns : set) {
                    
                    if( ! first ) buff.append( "," );
                    else first = false;
                    
                    buff.append( ns.toString() );
                }
            }
            setKey = buff.toString();
        } 

        return makeKey(kind, name, setKey, namespace);
    }
    
    /** Make a string representation of the name */
    public static String makeKey( MultiNameKind kind, 
                                  String name, 
                                  List<Namespace> set, 
                                  Namespace namespace ) {
        
        String setKey = null;
        
        if( set != null ) {
            StringBuilder buff = new StringBuilder();
            if( set != null ) {
                boolean first = true;
                for (Namespace ns : set) {
                    
                    if( ! first ) buff.append( "," );
                    else first = false;
                    
                    buff.append( ns.toString() );
                }
            }
            setKey = buff.toString();
        } 

        return makeKey(kind, name, setKey, namespace);
    }
    
    @Override
    public String toString() {
        return makeKey(kind, name, namespaceSet, namespace);
    }
}
