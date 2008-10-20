package com.anotherbigidea.flash.avm2.model;

import java.util.Collection;
import java.util.HashSet;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * AVM2 metadata
 *
 * @author nickmain
 */
public class AVM2Metadata {

    /** A key/value pair */
    public static class Value {
        
        /** The key - may be null if there is no key */
        public final String key;
        
        /** The value */
        public final String value;
        
        public Value( String key, String value ) {
            this.key   = key;
            this.value = value;
        }
        
        /** @see java.lang.Object#equals(java.lang.Object) */
        @Override
        public boolean equals(Object obj) {
            if( obj == null ) return false;
            if( !( obj instanceof Value) ) return false;
            
            Value other = (Value) obj;
            
            if( key == null ) {
                if( other.key != null ) return false;
                return value.equals( other.value );
            }
            
            return key.equals( other.key ) && value.equals( other.value );
        }

        /** @see java.lang.Object#hashCode() */
        @Override
        public int hashCode() {
            return ("" + key + ":" + value ).hashCode(); 
        }                
    }
    
    /** The metadata values */
    public final Collection<Value> values = new HashSet<Value>();
    
    /** The metadata name */
    public final String name;
    
    /**
     * @param name the metadata name
     */
    public AVM2Metadata( String name ) {
        this.name = name;
    }

    /**
     * Add a value
     * @param key may be null
     * @param value may not be null
     * @return the new value
     */
    public Value addValue( String key, String value ) {
        Value v = new Value( key, value );
        values.add( v );
        return v;
    }
    
    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof AVM2Metadata )) return false;
        return ((AVM2Metadata) obj).name.equals( name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.print( name );
        
        if( ! values.isEmpty() ) {
            
            out.println( " {" );
            out.indent();
            
            for( Value v : values ) {
                if( v.key != null ) {
                    out.print( v.key + " = " );
                }
                out.writeDoubleQuotedString( v.value );
                out.println();
            }
            
            out.unindent();
            out.println( "}" );
            
        } 
        else out.println();
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {
        context.pool.stringIndex( name );
        
        for( Value v : values ) {
            if( v.key != null ) context.pool.stringIndex( v.key );
            context.pool.stringIndex( v.value );
        }
    }
    
    /**
     * Write.
     */
    public void write( ABC.Metadata md, AVM2ABCFile.WriteContext context ) {
        context.metadata.add( this );
        
        int[] keys = new int[ values.size() ];
        int[] vals = new int[ values.size() ];
        
        int i = 0;
        for( Value v : values ) {
            if( v.key == null ) keys[i] = 0;
            else                keys[i] = context.pool.stringIndex( v.key );
            
            vals[i] = context.pool.stringIndex( v.value );
            
            i++;
        }
        
        md.metadata( context.pool.stringIndex( name ), keys, vals );
    }
}
