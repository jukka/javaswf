package com.anotherbigidea.flash.avm2.model;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.InstanceInfoFlags;

/**
 * An AVM2 Class.
 *
 * @author nickmain
 */
public class AVM2Class implements Comparable<AVM2Class> {

	/**
	 * The ABC file this class belongs to
	 */
	public final AVM2ABCFile abcFile;
	
    /** The class index */
    public final int index;
    
    /** The qualified class name */
    public final AVM2QName name;
    
    /** The superclass */
    public final AVM2Name  superclass;
    
    /** True if this class is sealed, false if dynamic */
    public final boolean isSealed;
    
    /** True if this class is final */
    public final boolean isFinal;
    
    /** True if this class is an interface */
    public final boolean isInterface;
    
    /** The namespace for "protected" access - may be null */
    public final AVM2Namespace protectedNamespace;
    
    /** The implemented interfaces. Read-only collection */
    public final Collection<AVM2Name> interfaces;
    private final Collection<AVM2Name> interfaces_internal = new HashSet<AVM2Name>();
    { interfaces = Collections.unmodifiableCollection( interfaces_internal ); }
    
    /** The instance constructor */
    public AVM2Method constructor;

    /** The instance traits. Read-only collection. */
    public final AVM2Traits traits = new AVM2Traits();
    
    /** The static initializer */
    public AVM2Method staticInitializer;

    /** The static traits. Read-only collection. */
    public final AVM2Traits staticTraits = new AVM2Traits();
    
    /**
     * Add an implemented interface
     */
    public final void addInterface( AVM2Name interfaceName ) {
        interfaces_internal.add( interfaceName );
    }
    
    /*pkg*/ AVM2Class( AVM2ABCFile abcFile, 
                       int index, AVM2QName name, AVM2Name superclass, 
                       boolean isSealed, boolean isFinal, boolean isInterface,
                       AVM2Namespace protectedNamespace ) {
        
    	this.abcFile            = abcFile;
        this.index              = index;
        this.name               = name;
        this.superclass         = superclass;
        this.isSealed           = isSealed;
        this.isFinal            = isFinal;
        this.isInterface        = isInterface;
        this.protectedNamespace = protectedNamespace;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        
        AVM2QName qname = null;
        if     ( obj instanceof AVM2Class ) qname = ((AVM2Class) obj).name;
        else if( obj instanceof AVM2QName ) qname = (AVM2QName) obj;
        
        if( qname == null ) return false;        
        return qname.equals( name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /** @see java.lang.Comparable#compareTo(java.lang.Object) */
    public int compareTo(AVM2Class obj) {
        if( obj == null ) return -1;
        
        if( obj instanceof AVM2Class ) {
            return index - ((AVM2Class) obj).index;
        }
        
        return -1;
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        
        out.print( "[" + index + "]" );
        out.print( isSealed    ? " sealed"    : " dynamic" );
        if( isFinal ) out.print( " final" );
        out.print( isInterface ? " interface" : " class" );        
        out.println( " " + name.namespace.name + "." + name.name + " {" );        
        out.indent();
        
        out.print( "extends " );
        superclass.dump( out );
        out.println();
        
        for( AVM2Name iface : interfaces ) {
            out.print( "implements " );
            iface.dump( out );            
            out.println();
        }  
        
        if( protectedNamespace != null ) {
            out.print( "protected-namespace " );
            protectedNamespace.dump( out );
            out.println();
        }
        
        out.println();

        out.print( "constructor: " );
        constructor.dump( out );
        out.println();

        out.print( "static initializer: " );
        staticInitializer.dump( out );
        out.println();

        for( AVM2Trait trait : traits.traits ) {
            trait.dump( out );
        }

        for( AVM2Trait trait : staticTraits.traits ) {
            out.print( "static " );
            trait.dump( out );
        }
        
        out.unindent();
        out.println( "}" );        
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {        
        name.initPool( context );        
        if( superclass != null ) superclass.initPool( context );
        if( protectedNamespace != null ) {
            context.pool.namespaceIndex( protectedNamespace.kind, 
                                         protectedNamespace.name );
        }
        
        for( AVM2Name n : interfaces_internal ) {
            n.initPool( context );
        }

        constructor      .initPool( context );
        staticInitializer.initPool( context );
        traits           .initPool( context );
        staticTraits     .initPool( context );
    }
    
    /**
     * Write the instance info.
     */
    public void writeInstance( ABC.ClassInfos cinfos, AVM2ABCFile.WriteContext context ) {

        int nameIndex  = name.indexIn( context.pool );
        int superIndex = (superclass == null) ? 0 : superclass.indexIn( context.pool );
        
        Set<InstanceInfoFlags> iif = EnumSet.noneOf( InstanceInfoFlags.class );
        if( isFinal     ) iif.add( InstanceInfoFlags.Final );
        if( isSealed    ) iif.add( InstanceInfoFlags.Sealed );
        if( isInterface ) iif.add( InstanceInfoFlags.Interface );
        if( protectedNamespace != null ) iif.add( InstanceInfoFlags.HasProtectedNS );
        
        int protectedNS = 0;
        if( protectedNamespace != null ) {
            protectedNS = context.pool.namespaceIndex( protectedNamespace.kind, 
                                                       protectedNamespace.name ).poolIndex;
        }
        
        int constructorIndex = 0;
        for( int i = 0; i < context.methods.size(); i++ ) {
            if( context.methods.get( i ) == constructor ) {
                constructorIndex = i;
                break;
            }
        }
        
        int[] ifaces = new int[ interfaces.size() ];
        int i = 0;
        for( AVM2Name iface : interfaces ) {
            ifaces[i] = iface.indexIn( context.pool );            
            i++;
        }
        
        ABC.Traits ts = cinfos.instanceInfo( nameIndex, superIndex, 
                                             InstanceInfoFlags.encode( iif ), 
                                             protectedNS,                                              
                                             ifaces, 
                                             constructorIndex, traits.traits.size() );        
        if( ts != null ) {
            traits.write( ts, context );
        }
    }

    /**
     * Write the class info.
     */
    public void writeStatic( ABC.ClassInfos cinfos, AVM2ABCFile.WriteContext context ) {
        
        int constructorIndex = 0;
        for( int i = 0; i < context.methods.size(); i++ ) {
            if( context.methods.get( i ) == staticInitializer ) {
                constructorIndex = i;
                break;
            }
        }
        
        ABC.Traits ts = cinfos.classInfo( constructorIndex, staticTraits.traits.size() );

        if( ts != null ) {
            staticTraits.write( ts, context );
        }
    }
}
