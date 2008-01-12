package com.anotherbigidea.flash.avm2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.MethodInfoFlags;

/**
 * A method, function, script, constructor or static initializer.
 *
 * @author nickmain
 */
public class AVM2Method {

    /**
     * The method index - only for methods that are not used by any class or
     * script
     */
    public final int index;
    
    /** The method name - usually not used (is null) */
    public String name;
    
    /** The method flags (mutable) */
    public final Set<MethodInfoFlags> flags;
    
    /** The method return type - null for any type */
    public final AVM2Name returnType;
    
    /** The parameter types (a null means any type). Read-only list. */
    public final List<AVM2Name> paramTypes;
    
    /** The parameter names. May be empty if names are not available. Read-only list */
    public final List<String> paramNames;

    /** The default values for the optional parameters. Read-only list. */
    public final List<AVM2DefaultValue> defaultValues;
    
    /** The method body - may be empty */
    public final AVM2MethodBody methodBody = new AVM2MethodBody( this );
    
    private final List<AVM2Name> paramTypes_internal = new ArrayList<AVM2Name>();
    { paramTypes = Collections.unmodifiableList( paramTypes_internal ); }

    private final List<String> paramNames_internal = new ArrayList<String>();
    { paramNames = Collections.unmodifiableList( paramNames_internal ); }

    private final List<AVM2DefaultValue> defaultValues_internal = new ArrayList<AVM2DefaultValue>();
    { defaultValues = Collections.unmodifiableList( defaultValues_internal ); }

    /**
     * @param returnType the return type - null for any type
     * @param flags the initial method flags
     */
    public AVM2Method( AVM2Name returnType, Set<MethodInfoFlags> flags ) {
        this( returnType, flags, -1 );     
    }

    /**
     * @param returnType the return type - null for any type
     * @param flags the initial method flags
     * @param index used for anon function closures
     */
    public AVM2Method( AVM2Name returnType, Set<MethodInfoFlags> flags, int index ) {
        
        this.returnType = returnType;     
        
        if( flags == null ) flags = EnumSet.noneOf( MethodInfoFlags.class ); 
        this.flags = flags;
        
        this.index = index;
    }

    
    /**
     * Add a parameter.
     * 
     * @param name the name - may be null (all params must have names - or none)
     * @param type the type, null for any type
     * @param value the default value (may be null) - all optional parameters must come at the end
     */
    public void addParameter( String name, AVM2Name type, AVM2DefaultValue value ) {
        paramTypes_internal.add( type );
        
        if( name  != null ) paramNames_internal.add( name );        
        if( value != null ) defaultValues_internal.add( value );
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        if( index >= 0 ) {
            out.print( "method[" + index + "] " );                    
        }
        else {
            out.print( "method " );
        }
        
        if(name != null ) out.print( name );
        out.println( " {" );
        out.indent();
        
        out.print( "flags:" );
        for( MethodInfoFlags mif : flags ) {
            out.print( " " );
            out.print( mif.name() );            
        }
        out.println();
        
        out.print( "return-type: " );
        if( returnType == null ) out.print( "*" );
        else returnType.dump( out );
        out.println();
        
        out.println( "parameters (" );
        out.indent();
        for( int i = 0; i < paramTypes.size(); i++ ) {
            if( paramNames != null && i < paramNames.size() && paramNames.get( i ) != null ) {
                out.print( paramNames.get( i ) );
                out.print( " : " );
            }
            
            AVM2Name ptype = paramTypes.get( i );
            if( ptype == null ) {
                out.print( "*" );
            }
            else {
                ptype.dump( out );
            }
            
            if( defaultValues != null 
             && i >= (paramTypes.size() - defaultValues.size()) ) {
                int idx = i - (paramTypes.size() - defaultValues.size());

                out.print( " = " );
                defaultValues.get( idx ).dump( out );
            }
            
            out.println();
        }
        
        out.unindent();
        out.println( ")" );
        
        methodBody.dump( out );        
        
        out.unindent();
        out.println( "}" );
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {
        for( AVM2Method meth : context.methods ) {
            if( meth == this ) return;  //already there
        }
                       
        context.methods.add( this );
        
        initPool_2( context );
    }
        
    /**
     * Initialize a write-context
     */
    public void initPool_2( AVM2ABCFile.WriteContext context ) {

        if( name != null ) context.pool.stringIndex( name );
        
        if( returnType != null ) returnType.initPool( context );
        
        for( AVM2Name n : paramTypes ) {
            //param type may be null (any)
            if( n != null ) n.initPool( context );
        }
        
        for( String n : paramNames ) {
            context.pool.stringIndex( n );
        }
        
        for( AVM2DefaultValue v : defaultValues_internal ) {
            v.initPool( context );
        }
        
        methodBody.initPool( context );
    }
    
    /**
     * Write.
     */
    public void write( ABC.MethodInfos mi, AVM2ABCFile.WriteContext context ) {
        
        int[] ptypes = new int[ paramTypes.size() ];
        for( int i = 0; i < ptypes.length; i++ ) {
            AVM2Name ptype = paramTypes.get( i );
            ptypes[i] = (ptype != null) ? ptype.indexIn( context.pool ) : 0;
        }
        
        int[] pnames = null;
        if( flags.contains( MethodInfoFlags.HasParamNames ) && ! paramNames.isEmpty() ) {
            pnames = new int[ paramNames.size() ];
            for( int i = 0; i < pnames.length; i++ ) {
                pnames[i] = context.pool.stringIndex( paramNames.get( i ) );
            }
        }
        
        int[] optValues   = null;
        int[] optValKinds = null;

        if( flags.contains( MethodInfoFlags.HasOptional ) && ! defaultValues.isEmpty()) {
            optValues   = new int[ defaultValues.size() ];
            optValKinds = new int[ defaultValues.size() ];
            
            for( int i = 0; i < optValKinds.length; i++ ) {
                AVM2DefaultValue v = defaultValues.get( i );
                optValKinds[i] = v.valueKind.value;                
                optValues  [i] = v.valueKind.poolIndex( context, v.value );
            }
        }
        
        mi.methodInfo( (name == null) ? 0 : context.pool.stringIndex( name ), 
                       MethodInfoFlags.encode( flags ),
                       (returnType == null) ? 0 : returnType.indexIn( context.pool ),
                       ptypes, 
                       optValues, 
                       optValKinds, 
                       pnames );
    }
}
