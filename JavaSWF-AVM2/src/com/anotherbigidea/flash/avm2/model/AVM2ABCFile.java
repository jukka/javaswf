package com.anotherbigidea.flash.avm2.model;

import java.util.*;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.model.io.ConstantPool;

/**
 * An AVM2 ABC "file"
 *
 * @author nickmain
 */
public class AVM2ABCFile {

    public final int majorVersion;    
    public final int minorVersion;
    
    /** Read-only map of class name to class */
    public final Map<AVM2QName,AVM2Class> classes;

    /** Read-only list of methods that are not part of classes */
    public final List<AVM2Method> methods;
    
    /** Read-only list of scripts */
    public final List<AVM2Script> scripts;
    
    private final HashMap<AVM2QName,AVM2Class> classes_internal = new HashMap<AVM2QName,AVM2Class>();
    { classes = Collections.unmodifiableMap( classes_internal ); }

    private final List<AVM2Script> scripts_internal = new ArrayList<AVM2Script>();
    { scripts = Collections.unmodifiableList( scripts_internal ); }

    private final List<AVM2Method> methods_internal = new ArrayList<AVM2Method>();
    { methods = Collections.unmodifiableList( methods_internal ); }
    
    /**
     * Use the default (most current) major and minor versions
     */
    public AVM2ABCFile() {
        this( ABC.MAJOR_VERSION_46, ABC.MINOR_VERSION_16 );
    }
    
    /**
     * @param majorVersion the abc major version
     * @param minorVersion the abc minor version
     */
    public AVM2ABCFile( int majorVersion, int minorVersion ) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }
    
    /**
     * Get the class with the given index
     * @return null if the index is invalid
     */
    public AVM2Class classAtIndex( int index ) {
        for( AVM2Class c : classes_internal.values() ) {
            if( c.index == index ) return c;
        }
        return null;
    }
    
    /**
     * Add an anon function closure.  The function should have a fixed index.
     */
    public void addFunctionClosure( AVM2Method func ) {
        methods_internal.add( func );
    }
    
    /**
     * Add an anon function closure.  The function will have a fixed index.
     */
    public AVM2Method addFunctionClosure( AVM2Name returnType, Set<MethodInfoFlags> flags ) {
        
        AVM2Method func = new AVM2Method( returnType, flags, methods_internal.size() );
        methods_internal.add( func );
        
        return func;
    }

    
    /**
     * Add a new class (or replace the one with the same name).
     * 
     * @param name the qualified class name
     * @param superclass the superclass
     * @param isSealed whether the class is sealed (true) or dynamic
     * @param isFinal whether the class is final
     * @param isInterface whether the class is an interface
     * @param protectedNamespace the namespace for protected access - may be null
     * @return the new class
     */
    public AVM2Class addClass( AVM2QName name, AVM2Name superclass, 
                               boolean isSealed, boolean isFinal, boolean isInterface,
                               AVM2Namespace protectedNamespace ) {
        
        int index = classes_internal.size();
        
        //find existing class and grab its index
        AVM2Class clazz = classes_internal.get( name );
        if( clazz != null ) {
            index = clazz.index;
        }
        
        clazz = new AVM2Class( this, index, name, superclass, 
                               isSealed, isFinal, isInterface, 
                               protectedNamespace );
        
        classes_internal.put( name, clazz );        
        return clazz;        
    }
    
    /**
     * Add a script.  The last script added becomes the main script.
     * 
     * @param code the code for the script
     * @return the new script
     */
    public AVM2Script addScript( AVM2Method code ) {
        
        AVM2Script script = new AVM2Script( code );
        scripts_internal.add( script );
        return script;
    }

    /**
     * Add a script.  The last script added becomes the main script.
     * 
     * @return the new script
     */
    public AVM2Script addScript( ) {
        AVM2Method code = new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ) );
        
        AVM2Script script = new AVM2Script( code );
        scripts_internal.add( script );
        return script;
    }

    
    /**
     * Add a script at the beginning of the scripts.  If there is already at 
     * least one script then this new script will NOT be the main script.
     * 
     * @return the new script
     */
    public AVM2Script prependScript( ) {
        AVM2Method code = new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ) );
        
        AVM2Script script = new AVM2Script( code );
        scripts_internal.add( 0, script );
        return script;
    }

    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.println( "abc-file {" );
        out.indent();
        
        for( AVM2Class c : classes.values() ) {
            c.dump( out );
            out.println();
        }
        
        for( AVM2Script s : scripts ) {
            s.dump( out );
            out.println();
        }
        
        for( AVM2Method m : methods ) {
            m.dump( out );
            out.println();
        }
        
        out.unindent();
        out.println( "}" );        
    }
    
    /** Context data for writing */
    public class WriteContext {
        public ConstantPool pool = new ConstantPool();
        public List<AVM2Method>     methods  = new ArrayList<AVM2Method>();
        public List<AVM2Metadata>   metadata = new ArrayList<AVM2Metadata>();
        public List<AVM2MethodBody> mbodies  = new ArrayList<AVM2MethodBody>();
        
        public AVM2ABCFile getFile() { return AVM2ABCFile.this; }
    }

    /**
     * Write the file
     */
    public void write( ABC file ) {
        
        file.version( majorVersion, minorVersion );
        
        WriteContext context = new WriteContext();
        
        //populate the constant pool and other arrays
        for( AVM2Class  c : classes.values() ) {
            c.initPool( context );
        }
        for( AVM2Script s : scripts ) {
            s.initPool( context );
        }        
        for( AVM2Method m : methods ) {
            m.initPool_2( context );
            
            //put the closure function in the correct index position
            int index = m.index;
            context.methods.add( context.methods.get( index ));
            context.methods.set( index, m );
        }
        
        context.pool.write( file );
        context.pool.lock();  //to catch any missed values in the initPool methods
        
        ABC.MethodInfos infos = file.methods( context.methods.size() );
        if( infos != null ) {
            for( AVM2Method m : context.methods ) {
                m.write( infos, context );
            }
            
            infos.done();
        }
        
        ABC.Metadata md = file.metadata( context.metadata.size() );
        if( md != null ) {
            for( AVM2Metadata m : context.metadata ) m.write( md, context );
            md.done();
        }
        
        ABC.ClassInfos cinfos = file.classes( classes_internal.size() );
        if( cinfos != null ) {
            //get classes in index order
            AVM2Class[] cc = new AVM2Class[ classes.size() ];
            for( AVM2Class  c : classes.values() ) cc[ c.index ] = c;
            
            for( AVM2Class  c : cc ) c.writeInstance( cinfos, context );
            for( AVM2Class  c : cc ) c.writeStatic( cinfos, context );
            cinfos.done();
        }
        
        ABC.Scripts scripts = file.scripts( scripts_internal.size() );
        if( scripts != null ) {
            for( AVM2Script s : scripts_internal ) s.write( scripts, context );
            scripts.done();
        }
        
        ABC.MethodBodies bodies = file.methodBodies( context.mbodies.size() );
        if( bodies != null ) {
            for( AVM2MethodBody b : context.mbodies ) b.write( bodies, context );                
            bodies.done();
        }
        
        file.done();
    }
}
