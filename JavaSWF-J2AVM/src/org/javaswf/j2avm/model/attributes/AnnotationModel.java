package org.javaswf.j2avm.model.attributes;

import java.util.HashMap;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A class, member or parameter annotation
 *
 * @author nickmain
 */
public class AnnotationModel {

    /** Base for annotation values */
    public static abstract class Value {
        
        /** Dump for debug purposes */
        public abstract void dump( IndentingPrintWriter out );
    }
    
    public static class IntegerValue extends Value {
        public final int value;
        
        public IntegerValue( int value ) {
            this.value = value;
        }
        
        @Override public String toString() {
            return Integer.toString( value );
        }
        
        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.print( value );
        }
    }

    public static class LongValue extends Value {
        public final long value;
        
        public LongValue( long value ) {
            this.value = value;
        }
        
        @Override public String toString() {
            return Long.toString( value ) + "L";
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.print( value );
        }
    }

    public static class FloatValue extends Value {
        public final float value;
        
        public FloatValue( float value ) {
            this.value = value;
        }
        
        @Override public String toString() {
            return Float.toString( value ) + "f";
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.print( value );
        }
    }

    public static class DoubleValue extends Value {
        public final double value;
        
        public DoubleValue( double value ) {
            this.value = value;
        }
        
        @Override public String toString() {
            return Double.toString( value );
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.print( value );
        }
    }

    public static class StringValue extends Value {
        public final String value;
        
        public StringValue( String value ) {
            this.value = value;
        }
        
        @Override public String toString() {
            return "\"" + IndentingPrintWriter.escapeString(value) + "\"";
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.writeDoubleQuotedString( value );
        }
    }

    public static class AnnotationValue extends Value {
        public final AnnotationModel value;
        
        public AnnotationValue( AnnotationModel value ) {
            this.value = value;
        }
        
        @Override public String toString() {
            return "Annotation " + value.type;
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            value.dump( out );
        }
    }
    
    public static class ClassValue extends Value {
        public final JavaType value;
        
        public ClassValue( JavaType value ) {
            this.value = value;
        }
        
        @Override public String toString() {
            return "Class " + value.name;
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.print( value );
        }
    }
    
    public static class ArrayValue extends Value {
        public final Value[] values;
        
        public ArrayValue( Value[] values ) {
            this.values = values;
        }
        
        @Override public String toString() {
            return "Value Array";
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.println( "[" );
            out.indent();
            
            for ( Value value : values ) {
                value.dump( out );
                out.println();                
            }
            
            out.unindent();
            out.print( "]" );
        }
    }
    
    /**
     * An enum value.
     */
    public static class EnumValue extends Value {
        /** The type of the enum interface */
        public final ObjectType type;
        
        /** The enum string name */
        public final String enumName;
        
        public EnumValue( ObjectType type, String enumName ) {
            this.type     = type;
            this.enumName = enumName;
        }
        
        @Override public String toString() {
            return type.name + "." + enumName;
        }

        /** Dump for debug purposes */
        public void dump( IndentingPrintWriter out ) {
            out.print( toString() );
        }
    }
    
    /**
     * Map of values in this annotation, keyed by String name.
     */
    public final Map<String, Value> values = new HashMap<String, Value>();
    
    /**
     * The annotation class type.
     */
    public final ObjectType type;
    
    /**
     * @param type the annotation class
     * @param visible true if this annotation is visible at runtime.
     */
    public AnnotationModel( ObjectType type ) {
        this.type = type;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof AnnotationModel )) return false;
        return ((AnnotationModel) obj).type.equals( type );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return type.hashCode();
    }        
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        
        out.println( "@" + type + " {");
        out.indent();
        
        for( String name : values.keySet() ) {
            
            Value val = values.get( name );
            
            out.print( name );
            out.print( " = " );
            val.dump( out );
            out.println();
        }
        
        out.unindent();
        out.println( "}" );
    }
}

