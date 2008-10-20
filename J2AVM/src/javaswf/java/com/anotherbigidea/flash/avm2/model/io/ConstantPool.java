package com.anotherbigidea.flash.avm2.model.io;

import java.util.*;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.MultiName;
import com.anotherbigidea.flash.avm2.MultiNameKind;
import com.anotherbigidea.flash.avm2.Namespace;
import com.anotherbigidea.flash.avm2.NamespaceKind;


/**
 * Holds the contents of an AVM2 ABC file constant pool.
 *
 * @author nickmain
 */
public final class ConstantPool {

    //-------------------------------------------------------------------------
    // Constant Pool
    //-------------------------------------------------------------------------
    private List<Long>       uintPool      = new ArrayList<Long>();
    private List<Integer>    intPool       = new ArrayList<Integer>();
    private List<String>     stringPool    = new ArrayList<String>();
    private List<Double>     doublePool    = new ArrayList<Double>();    
    private List<Namespace>  namespacePool = new ArrayList<Namespace>();
    
    private List<List<Namespace>> nsSetPool = new ArrayList<List<Namespace>>();
    private List<MultiName> namePool = new ArrayList<MultiName>();
    
    private Map<Long   ,Integer> uintIndices   = new HashMap<Long, Integer>();
    private Map<Integer,Integer> intIndices    = new HashMap<Integer, Integer>();
    private Map<String ,Integer> stringIndices = new HashMap<String, Integer>();
    private Map<Double ,Integer> doubleIndices = new HashMap<Double, Integer>();
    private Map<String ,Integer> namespaceIndices = new HashMap<String, Integer>();
    
    private Map<String, Integer> nsSetIndices = new HashMap<String, Integer>();
    private Map<String, Integer> nameIndices  = new HashMap<String, Integer>();    
    //-------------------------------------------------------------------------
    
    /**
     * Lock the constant pool to prevent further additions.  Any addition
     * will cause an {@link UnsupportedOperationException} to be thrown.
     */
    public void lock() {
        uintPool         = Collections.unmodifiableList( uintPool         );
        intPool          = Collections.unmodifiableList( intPool          );
        stringPool       = Collections.unmodifiableList( stringPool       );
        doublePool       = Collections.unmodifiableList( doublePool       );    
        namespacePool    = Collections.unmodifiableList( namespacePool    );
        nsSetPool        = Collections.unmodifiableList( nsSetPool        );
        namePool         = Collections.unmodifiableList( namePool         );
        uintIndices      = Collections.unmodifiableMap ( uintIndices      );
        intIndices       = Collections.unmodifiableMap ( intIndices       );
        stringIndices    = Collections.unmodifiableMap ( stringIndices    );
        doubleIndices    = Collections.unmodifiableMap ( doubleIndices    );
        namespaceIndices = Collections.unmodifiableMap ( namespaceIndices );
        nsSetIndices     = Collections.unmodifiableMap ( nsSetIndices     );
        nameIndices      = Collections.unmodifiableMap ( nameIndices      );      
    }
    
    /**
     * Get, or create, an int constant pool entry.
     * 
     * @param value the entry value
     * @return the index of the existing or new entry
     */
    public int intIndex( int value ) {
        Integer index = intIndices.get( value );
        if( index == null ) {
            index = intPool.size() + 1;
            intPool.add( value );
            intIndices.put( value, index );
        }
        
        return index;
    }
    
    /**
     * Get, or create, a uint constant pool entry.
     * 
     * @param value the entry value
     * @return the index of the existing or new entry
     */
    public int uintIndex( long value ) {
        Integer index = uintIndices.get( value );
        if( index == null ) {
            index = uintPool.size() + 1;
            uintPool.add( value );
            uintIndices.put( value, index );
        }
        
        return index;
    }

    /**
     * Get, or create, a double constant pool entry.
     * 
     * @param value the entry value
     * @return the index of the existing or new entry
     */
    public int doubleIndex( double value ) {
        Integer index = doubleIndices.get( value );
        if( index == null ) {
            index = doublePool.size() + 1;
            doublePool.add( value );
            doubleIndices.put( value, index );
        }
        
        return index;
    }

    /**
     * Get, or create, a String constant pool entry.
     * 
     * @param value the entry value
     * @return the index of the existing or new entry
     */
    public int stringIndex( String value ) {
        if( value == null ) throw new RuntimeException( "Cannot add null string to constant pool" );
        
        Integer index = stringIndices.get( value );
        if( index == null ) {
            index = stringPool.size() + 1;
            stringPool.add( value );
            stringIndices.put( value, index );
        }
        
        return index;
    }

    /**
     * Get, or create, a Namespace constant pool entry.
     * 
     * @param kind the kind of namespace
     * @param name the name - may be null
     * @return the new or existing namespace
     */
    public Namespace namespaceIndex( NamespaceKind kind, String name ) {
        String key = Namespace.toString(kind, name);
        Integer index = namespaceIndices.get( key );
        if( index == null ) {
            int nameIndex = ( name != null ) ? stringIndex( name ) : 0;            
            index = namespacePool.size() + 1;
            
            Namespace ns = new Namespace( kind, name, index, nameIndex );
            
            namespaceIndices.put( key, index );
            namespacePool.add( ns );
            stringIndices.put( key, index );
            return ns;
        }
        
        return namespacePool.get(index-1);
    }

    /**
     * Get, or create, a namespace set entry.
     * 
     * @param namespaces the namespaces in the set
     * @return the set index
     */
    public int namespaceSetIndex( Namespace...namespaces ) {
        List<Namespace> set = new ArrayList<Namespace>();
        for (Namespace namespace : namespaces) {
            set.add( namespace );
        }
        set = Collections.unmodifiableList( set );

        return namespaceSetIndex( set );
    }
    
    /**
     * Get, or create, a namespace set entry.
     * 
     * @param namespaces the namespaces in the set
     * @return the set index
     */
    public int namespaceSetIndex( List<Namespace> namespaces ) {
        StringBuilder buff = new StringBuilder();
        for (Namespace namespace : namespaces) {
            buff.append("|");
            buff.append( namespace.toString() );
        }
        
        String key = buff.toString();
        Integer index = nsSetIndices.get( key );
        if( index != null ) return index;
        
        index = nsSetPool.size() + 1;
        nsSetPool.add( namespaces );
        nsSetIndices.put(key, index);
        return index;
    }
    
    /**
     * Get, or create, a name entry.
     * @param kind the kind of name
     * @param name the simple name - may be null
     * @param set the namespace set - may be null
     * @param namespace the namespace - may be null
     * @return the new or existing name
     */
    public MultiName nameIndex( MultiNameKind kind, 
                                String name, 
                                Namespace namespace,
                                List<Namespace> set ) {
    
        String key = MultiName.makeKey(kind, name, set, namespace);
        
        Integer index = nameIndices.get( key );
        if( index != null ) {
            return namePool.get( index - 1 );
        }
        
        int nameIndex = (name != null) ? stringIndex( name ) : 0;
        int setIndex  = (set != null) ? 
                            namespaceSetIndex( set ) :
                            0;

        index = namePool.size() + 1;
        MultiName mn = 
            new MultiName( kind, index, name, nameIndex, set, setIndex, namespace );
        namePool.add( mn );
        nameIndices.put( key, index );
        return mn;
    }
    
    /**
     * Get the name at the given pool index
     */
    public MultiName nameAt( int index ) {
        return namePool.get( index - 1 );
    }
    
    /**
     * Get the namespace set at the given pool index
     */
    public List<Namespace> namespaceSetAt( int index ) {
        return nsSetPool.get( index - 1 );
    }
    
    /**
     * Get the namespace at the given pool index
     */
    public Namespace namespaceAt( int index ) {
        return namespacePool.get( index - 1 );
    }
    
    /**
     * Get the int value at the given pool index.
     */
    public int intAt( int index ) {
        return intPool.get( index - 1 );
    }

    /**
     * Get the uint value at the given pool index.
     */
    public long uintAt( int index ) {
        return uintPool.get( index - 1 );
    }

    /**
     * Get the double value at the given pool index.
     */
    public double doubleAt( int index ) {
        return doublePool.get( index - 1 );
    }

    /**
     * Get the String value at the given pool index.
     */
    public String stringAt( int index ) {
        return stringPool.get( index - 1 );
    }
    
    /**
     * Dump the constant pools for debug purposes.
     */
    public void dumpPools( IndentingPrintWriter out ) {
        
        out.println( "int pool {" );
        out.indent();
        int index = 1;
        for (Integer i : intPool) {
            out.writeField( (index++) + ": ", 4, true );
            out.println( i );
        }
        out.unindent();
        out.println( "}" );
        

        out.println( "uint pool {" );
        out.indent();
        index = 1;
        for (Long i : uintPool) {
            out.writeField( (index++) + ": ", 4, true );
            out.println( i );
        }
        out.unindent();
        out.println( "}" );

        out.println( "double pool {" );
        out.indent();
        index = 1;
        for (Double d : doublePool) {
            out.writeField( (index++) + ": ", 4, true );
            out.println( d );
        }
        out.unindent();
        out.println( "}" );

        out.println( "string pool {" );
        out.indent();
        index = 1;
        for (String s : stringPool) {
            out.writeField( (index++) + ": ", 4, true );
            out.println( "\"" + IndentingPrintWriter.escapeString( s ) + "\"" );
        }
        out.unindent();
        out.println( "}" );

        out.println( "namespace pool {" );
        out.indent();
        index = 1;
        for (Namespace ns : namespacePool) {
            out.writeField( (index++) + ": ", 4, true );
            out.println( ns );
        }
        out.unindent();
        out.println( "}" );

        out.println( "namespace-set pool {" );
        out.indent();
        index = 1;
        for (List<Namespace> nss : nsSetPool) {
            out.writeField( (index++) + ": ", 4, true );
            out.print( "[" );
            for (Namespace namespace : nss) {
                out.print( " " );
                out.print( namespace.poolIndex );
            }
            out.print( " ]" );
        }
        out.unindent();
        out.println( "}" );

        out.println( "name pool {" );
        out.indent();
        index = 1;
        for (MultiName mn : namePool) {
            out.writeField( (index++) + ": ", 4, true );
            out.println( mn );
        }
        out.unindent();
        out.println( "}" );
    }
    
    public void initIntPool( int[] values ) {
        intIndices.clear();
        intPool.clear();
        for (int i = 0; i < values.length; i++) {
            intPool.add( values[i] );
            intIndices.put( values[i], i+1 );
        }        
    }

    public void initUIntPool( long[] values ) {
        uintIndices.clear();
        uintPool.clear();
        for (int i = 0; i < values.length; i++) {
            uintPool.add( values[i] );
            uintIndices.put( values[i], i+1 );
        }        
    }
    
    public void initDoublePool( double[] values ) {
        doubleIndices.clear();
        doublePool.clear();
        for (int i = 0; i < values.length; i++) {
            doublePool.add( values[i] );
            doubleIndices.put( values[i], i+1 );
        }        
    }

    public void initStringPool( String[] values ) {
        stringIndices.clear();
        stringPool.clear();
        for (int i = 0; i < values.length; i++) {
            stringPool.add( values[i] );
            stringIndices.put( values[i], i+1 );
        }        
    }
    
    public void addNamespace( NamespaceKind kind, int nameIndex ) {
        String name = (nameIndex == 0) ? "" : stringPool.get( nameIndex - 1);
        int index = namespacePool.size() + 1;
        namespacePool.add( new Namespace( kind, name, index, nameIndex ) );
    }
    
    public void addNamespaceSet( int[] namespaceIndices ) {
        List<Namespace> set = new ArrayList<Namespace>();
        StringBuilder buff = new StringBuilder();
        for (int index : namespaceIndices) {
            Namespace ns = namespacePool.get(index - 1);
            set.add( ns );

            buff.append("|");
            buff.append( ns.toString() );
        } 
        set = Collections.unmodifiableList( set );

        int index = nsSetPool.size() + 1;
        nsSetPool.add( set );
        
        String key = buff.toString();
        nsSetIndices.put(key, index);
    }
    
    public void addName( MultiNameKind kind, int nameIndex, int namespaceIndex, 
                          int namespaceSetIndex ) {

        int index = namePool.size() + 1;
        String    name = (nameIndex > 0) ? stringAt( nameIndex ) : null;
        Namespace ns   = (namespaceIndex > 0) ? namespaceAt( namespaceIndex ) : null;
        List<Namespace> nsSet = (namespaceSetIndex > 0) ? 
                                   namespaceSetAt( namespaceSetIndex ) : 
                                   null;
        
        String key = MultiName.makeKey(kind, name, nsSet, ns );
        
        MultiName mn = 
            new MultiName( kind, index, name, nameIndex, nsSet, namespaceSetIndex, ns );
        namePool.add( mn );
        nameIndices.put( key, index );
    }
    
    /**
     * Write the pool
     */
    public void write( ABC file ) {
        
        int[] ints = new int[ intPool.size() ];
        for( int i = 0; i < ints.length; i++ ) {
            ints[i] = intPool.get( i );
        }        
        file.intPool( ints );
        
        long[] uints = new long[ uintPool.size() ];
        for( int i = 0; i < uints.length; i++ ) {
            uints[i] = uintPool.get( i );
        }        
        file.uintPool( uints );

        double[] dubs =  new double[ doublePool.size() ];
        for( int i = 0; i < dubs.length; i++ ) {
            dubs[i] = doublePool.get( i );
        }
        file.doublePool( dubs );

        String[] strings = stringPool.toArray( new String[ stringPool.size() ] );        
        file.stringPool( strings );
        
        ABC.Namespaces nspaces = file.namespacePool( namespacePool.isEmpty() ? 0 : namespacePool.size() + 1 );
        if( nspaces != null ) {
            for( Namespace ns : namespacePool ) {
                nspaces.namespace( ns.kind, ns.nameIndex );
            }
            nspaces.done();
        }
        
        ABC.NamespaceSets sets = file.namespaceSetPool( nsSetPool.isEmpty() ? 0 : nsSetPool.size() + 1 );
        if( sets != null ) {
            for( List<Namespace> set : nsSetPool ) {
                int[] namespaces = new int[ set.size() ];
                Namespace[] nn = set.toArray( new Namespace[ set.size() ] );
                for( int i = 0; i < nn.length; i++ ) {
                    namespaces[i] = nn[i].poolIndex;
                }
                
                sets.namespaceSet( namespaces );
            }
            sets.done();
        }
        
        ABC.Names names = file.namePool( namePool.isEmpty() ? 0 : namePool.size() + 1 );
        if( names != null ) {
            for( MultiName n : namePool ) {
                names.name( n.kind, 
                            n.nameIndex, 
                            (n.namespace == null) ? 0 : n.namespace.poolIndex, 
                            n.namespaceSetIndex );                
            }
            
            names.done();
        }
    }
}
