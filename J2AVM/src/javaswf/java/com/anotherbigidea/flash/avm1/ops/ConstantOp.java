package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.AVM1ValueProducer;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * An operation that pushes a constant
 *
 * @author nickmain
 */
public abstract class ConstantOp extends AVM1Operation implements AVM1ValueProducer {
    
    public static final class IntValue extends ConstantOp {
        public final int value;        
        public IntValue( int value ) { this.value = value; }
        @Override public void write(SWFActionBlock block) throws IOException { block.push( value ); }        

        public boolean isNonZero() { return value != 0; }
        public int intValue() { return value; }
        
        public void accept(AVM1OpVisitor visitor) { visitor.visitIntValue( this ); }
        
        public String toString() { return "" + value; }
    }

    public static final class BooleanValue extends ConstantOp {
        public final boolean value;        
        public BooleanValue( boolean value ) { this.value = value; }
        @Override public void write(SWFActionBlock block) throws IOException { block.push( value ); }
        
        public boolean isNonZero() { return value; }
        public int intValue() { return value ? 1 : 0; }
        
        public void accept(AVM1OpVisitor visitor) { visitor.visitBooleanValue( this ); }
        
        public String toString() { return "" + value; }
}

    public static final class FloatValue extends ConstantOp {
        public final float value;        
        public FloatValue( float value ) { this.value = value; }
        @Override public void write(SWFActionBlock block) throws IOException { block.push( value ); }        

        public boolean isNonZero() { return value != 0.0f; }
        public int intValue() { return (int) value; }
        
        public void accept(AVM1OpVisitor visitor) { visitor.visitFloatValue( this ); }
        
        public String toString() { return "" + value; }
    }

    public static final class DoubleValue extends ConstantOp {
        public final double value;        
        public DoubleValue( double value ) { this.value = value; }
        @Override public void write(SWFActionBlock block) throws IOException { block.push( value ); }    
        
        public boolean isNonZero() { return value != 0.0; }
        public int intValue() { return (int) value; }
        
        public void accept(AVM1OpVisitor visitor) { visitor.visitDoubleValue( this ); }
        
        public String toString() { return "" + value; }
    }

    public static final class NullValue extends ConstantOp {
        public NullValue( ) { }
        @Override public void write(SWFActionBlock block) throws IOException { block.pushNull(); }        

        public boolean isNonZero() { return false; }
        
        public void accept(AVM1OpVisitor visitor) { visitor.visitNullValue( this ); }
        
        public String toString() { return "<null>"; }
    }

    public static final class UndefinedValue extends ConstantOp {
        public UndefinedValue( ) { }
        @Override public void write(SWFActionBlock block) throws IOException { block.pushUndefined(); }
        
        public boolean isNonZero() { return false; }
        
        public void accept(AVM1OpVisitor visitor) { visitor.visitUndefinedValue( this ); }
        
        public String toString() { return "<undefined>"; }
    }

    public static final class StringValue extends ConstantOp {
        public final String value;        
        public StringValue( String value ) { this.value = value; }
        @Override public void write(SWFActionBlock block) throws IOException { block.push( value ); }        

        public int intValue() { return Integer.parseInt( value ); }
        
        public void accept(AVM1OpVisitor visitor) { visitor.visitStringValue( this ); }
        
        public String toString() { return "\"" + value + "\""; }
    }
}
