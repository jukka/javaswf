package org.javaswf.j2avm.model.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.javaswf.j2avm.model.types.JavaType;

/**
 * A Java Class Constant Pool.
 * 
 * @author nick
 */
public class ConstantPool {
    public abstract class Entry {
        public final int index;

        protected Entry(int index) {
            this.index = index;
        }

        /** Get the pool that this entry belongs in. */
        public final ConstantPool getConstantPool() {
            return ConstantPool.this;
        }
        
        public int getSize() { return 1; }
    }
    
    public class UTF8Entry extends Entry {
        public final String value; 

        protected UTF8Entry(int index, String chars) {
            super(index);
            value = chars;
        }
    }

    public class StringEntry extends Entry {
        public final int utf8Index;

        protected StringEntry(int index, int utf8Index) {
            super(index);
            this.utf8Index = utf8Index;
        }

        public UTF8Entry getUTF8Entry() { return (UTF8Entry) getEntry( utf8Index ); }
    }

    public class ClassEntry extends Entry {
        public final int utf8Index;
        
        protected ClassEntry(int index, int utf8Index) {
            super(index);
            this.utf8Index = utf8Index;
        }

        public UTF8Entry getUTF8Entry() { return (UTF8Entry) getEntry( utf8Index ); }
    }

    public class NameAndTypeEntry extends Entry {
        public final int nameIndex;
        public final int typeIndex;

        protected NameAndTypeEntry(int index, int nameIndex, int typeIndex) {
            super(index);
            this.nameIndex = nameIndex;
            this.typeIndex = typeIndex;
        }

        public UTF8Entry getNameEntry() { return (UTF8Entry) getEntry( nameIndex ); }
        public UTF8Entry getTypeEntry() { return (UTF8Entry) getEntry( typeIndex ); }        
    }

    public abstract class RefEntry extends Entry {
        public final int classIndex;
        public final int nameAndTypeIndex;

        protected RefEntry(int index, int classIndex, int nameAndTypeIndex) {
            super(index);
            this.classIndex       = classIndex;
            this.nameAndTypeIndex = nameAndTypeIndex;
        }
        
        public ClassEntry getClassEntry() { return (ClassEntry) getEntry( classIndex ); }
        public NameAndTypeEntry getNameAndTypeEntry() { return (NameAndTypeEntry) getEntry( nameAndTypeIndex ); }
    }

    public class FieldRefEntry extends RefEntry {
        protected FieldRefEntry(int index, int classIndex, int nameAndTypeIndex) {
            super(index, classIndex, nameAndTypeIndex);
        }
    }

    public class MethodRefEntry extends RefEntry {
        protected MethodRefEntry(int index, int classIndex, int nameAndTypeIndex) {
            super(index, classIndex, nameAndTypeIndex);
        }
    }

    public class InterfaceMethodRefEntry extends MethodRefEntry {
        protected InterfaceMethodRefEntry(int index, int classIndex, int nameAndTypeIndex) {
            super(index, classIndex, nameAndTypeIndex);
        }
    }

    public class IntEntry extends Entry {
        public final int value;

        protected IntEntry(int index, int value) {
            super(index);
            this.value = value;
        }
    }

    public class FloatEntry extends Entry {
        public final float value;

        protected FloatEntry(int index, float value) {
            super(index);
            this.value = value;
        }
    }

    public class LongEntry extends Entry {
        public final long value;

        protected LongEntry(int index, long value) {
            super(index);
            this.value = value;
        }

        public int getSize() { return 2; }
    }

    public class DoubleEntry extends Entry {
        public final double value;

        protected DoubleEntry(int index, double value) {
            super(index);
            this.value = value;
        }

        public int getSize() { return 2; }
    }

    //pool entries in order
    private List<Entry> entries = new ArrayList<Entry>();
            
    /** Append UTF8 value */
    public UTF8Entry appendUTF8( String utf8 ) {
        int index = entries.size() + 1;
        UTF8Entry entry = new UTF8Entry( index, utf8 );
        entries.add(entry);
        return entry;
    }
    
    /** Append String value */
    public StringEntry appendString( int utf8Index ) {
        int index = entries.size() + 1;
        StringEntry entry = new StringEntry( index, utf8Index );
        entries.add(entry);        
        return entry;
    }

    /** Append Class reference */
    public ClassEntry appendClass( int utf8Index ) {
        int index = entries.size() + 1;
        ClassEntry entry = new ClassEntry( index, utf8Index );
        entries.add(entry);        
        return entry;        
    }

    /** Append Field reference */
    public FieldRefEntry appendField( int classIndex, int nameTypeIndex ) {
        int index = entries.size() + 1;
        FieldRefEntry entry = new FieldRefEntry( index, classIndex, nameTypeIndex );
        entries.add(entry);        
        return entry;
    }

    /** Append Method reference */
    public MethodRefEntry appendMethod( int classIndex, int nameTypeIndex ) {
        int index = entries.size() + 1;
        MethodRefEntry entry = new MethodRefEntry( index, classIndex, nameTypeIndex );
        entries.add(entry);        
        return entry;
    }

    /** Append interface method reference */
    public InterfaceMethodRefEntry appendInterfaceMethod( int classIndex, int nameTypeIndex ) {
        int index = entries.size() + 1;
        InterfaceMethodRefEntry entry = new InterfaceMethodRefEntry( index, classIndex, nameTypeIndex );
        entries.add(entry);        
        return entry;
    }

    /** Append int value */
    public IntEntry appendInt( int value ) {
        int index = entries.size() + 1;
        IntEntry entry = new IntEntry( index, value );
        entries.add(entry);        
        return entry;        
    }
    
    /** Append long value */
    public LongEntry appendLong( long value ) {
        int index = entries.size() + 1;
        LongEntry entry = new LongEntry( index, value );
        entries.add(entry); 
        entries.add(null); 
        return entry;        
    }
    
    /** Append float value */
    public FloatEntry appendFloat( float value ) {
        int index = entries.size() + 1;
        FloatEntry entry = new FloatEntry( index, value );
        entries.add(entry);        
        return entry;        
    }

    /** Append double value */
    public DoubleEntry appendDouble( double value ) {
        int index = entries.size() + 1;
        DoubleEntry entry = new DoubleEntry( index, value );
        entries.add(entry);        
        entries.add(null); 
        return entry;        
    }
    
    /** Append name and type */
    public NameAndTypeEntry appendNameType( int nameIndex, int typeIndex ) {
        int index = entries.size() + 1;
        NameAndTypeEntry entry = new NameAndTypeEntry( index, nameIndex, typeIndex );
        entries.add(entry);        
        return entry;                
    }

    /** Get the pool size (as per the JVM spec) */
    public int getCount() {
        return entries.size() + 1;
    }

    /** Iterator on the entries (including nulls for second slot of double size entries) */
    public Iterator<Entry> entries() { return entries.iterator(); }
    
    /**
     * Get the entry at the given index
     * @param index 1 based index
     * @return null if there is no entry at the index
     */
    public Entry getEntry( int index ) {
        if( index == 0 || index > entries.size()) return null;
        
        return (Entry) entries.get( index - 1 );
    }
    
    /**
     * Get the value of a String entry. 
     */
    public String getStringValue( int index ) {
        return ((StringEntry) getEntry(index)).getUTF8Entry().value;
    }

    /**
     * Get a class name
     * @param index the index of a ClassEntry
     */
    public String getClassName( int index ) {
        
        String name = ((ClassEntry) getEntry(index)).getUTF8Entry().value;
        
        if( name.startsWith("[") ) {
            return decodeTypeName(name);
        }
        
        return name.replace('/', '.');
    }
    
    /**
     * Get a type name from a type descriptor
     * @param index the type descriptor
     * @return the externalized name of the type
     */
    public String getTypeName( int index ) {
        return decodeTypeName( getUTF8Value(index) );
    }
    
    /**
     * Decode an internal type 
     */
    public static String decodeTypeName( String typeName ) {
        
        //primitives
        if( typeName.equals( "I" ) ) return "int";
        if( typeName.equals( "J" ) ) return "long";
        if( typeName.equals( "V" ) ) return "void";
        if( typeName.equals( "S" ) ) return "short";
        if( typeName.equals( "C" ) ) return "char";
        if( typeName.equals( "B" ) ) return "byte";
        if( typeName.equals( "Z" ) ) return "boolean";
        if( typeName.equals( "F" ) ) return "float";
        if( typeName.equals( "D" ) ) return "double";
                
        //arrays
        if( typeName.startsWith("[") ) return decodeTypeName(typeName.substring(1)) + "[]";
        
        //objects
        if( typeName.startsWith( "L" ) && typeName.endsWith(";")) {
            typeName = typeName.substring(1,typeName.length()-1);
            return typeName.replace('/', '.');
        }
        
        return "<UNKNOWN-TYPE("+ typeName +")>";
    }
    
    /**
     * Read a sequence of types.
     * @param sig the types
     * @return array of type names
     */
    public static String[] readTypes( String sig ) {
        Reader r = new StringReader(sig);
        
        List<String> types = new ArrayList<String>();
        
        String type = null;
        while((type = readTypeName(r)) != null ) {
            types.add( type );
        }
        
        return types.toArray( new String[ types.size() ] );
    }
    
    /**
     * Read a signature
     * @param sig the (param-types)ret-type string
     * @return [0] = return type, [1 to n] = param types
     */
    public static String[] readSignature( String sig ) {
        int closingParen = sig.indexOf(")");
        String paramTypes = sig.substring( 1, closingParen );
        String retType    = sig.substring( closingParen + 1 );
        
        String   ret    = decodeTypeName( retType );
        String[] params = readTypes( paramTypes );
        String[] types  = new String[ params.length + 1 ];
        
        types[0] = ret;
        System.arraycopy( params, 0, types, 1, params.length );
        
        return types;
    }
    
    /**
     * Read a type name from a signature stream
     * @param sig the signature stream
     * @return the next type name - null if no more
     */
    public static String readTypeName( Reader sig ) {
        
        try {
            int c = sig.read();
            
            switch( c ) {
                case -1 : return null;
                
                case 'I': return "int";
                case 'J': return "long";
                case 'V': return "void";
                case 'S': return "short";
                case 'C': return "char";
                case 'B': return "byte";
                case 'Z': return "boolean";
                case 'F': return "float";
                case 'D': return "double";
                
                case '[': return readTypeName(sig) + "[]";  
                    
                case 'L': {
                    StringBuffer buff = new StringBuffer();
                    while( (c = sig.read()) >= 0 ) {
                        if( c == ';' ) break;
                        if( c == '/' ) c = '.';
                        
                        buff.append((char) c);
                    }
                    
                    return buff.toString();
                }
                    
                default: return null;
            }
        } catch( IOException ignored ) {
            return null;
        }
    }
    
    /**
     * Get the value of a UTF8 entry. 
     */
    public String getUTF8Value( int index ) {
        return ((UTF8Entry) getEntry(index)).value;
    }
    
    /**
     * Get a constant value
     * @param index the value's index
     * @return Integer, Long, Float, Double, String or JavaType
     */
    public Object getConstant( int index ) {
        
        Entry e = getEntry(index);
        
        if( e instanceof IntEntry    ) return new Integer(((IntEntry   ) e).value );
        if( e instanceof FloatEntry  ) return new Float  (((FloatEntry ) e).value );
        if( e instanceof DoubleEntry ) return new Double (((DoubleEntry) e).value );
        if( e instanceof LongEntry   ) return new Long   (((LongEntry  ) e).value );
        
        if( e instanceof StringEntry ) return ((StringEntry) e).getUTF8Entry().value;
        if( e instanceof ClassEntry  ) return JavaType.fromName( getClassName( index ));
        
        return null;
    }
}


