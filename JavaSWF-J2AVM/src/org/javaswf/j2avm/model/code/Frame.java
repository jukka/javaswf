package org.javaswf.j2avm.model.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * The state of the stack and local variables at a particular point of 
 * execution.
 *
 * @author nickmain
 */
public class Frame {

    private static final LinkedList<ValueType> EMPTY = new LinkedList<ValueType>();
    
    /** The stack - top at index 0 */
    private final LinkedList<ValueType> stack;
    
    /** The local vars, some slots may be null */
    private final ValueType[] locals;
    
    /**
     * Copy an existing frame
     */
    public Frame( Frame toCopy ) {
        this.stack  = new LinkedList<ValueType>( toCopy.stack );
        this.locals = new ValueType[ toCopy.locals.length ];
        System.arraycopy( toCopy.locals, 0, locals, 0, locals.length );
    }
    
    private Frame( LinkedList<ValueType> stack, ValueType[] locals ) {
        this.locals = locals;
        this.stack  = stack;
    }

    /**
     * Push a type.  Pushes 64 bit types as two slots.
     */
    public void push( ValueType type ) {
        if( type instanceof PrimitiveType && ((PrimitiveType) type).is64Bit() ) {
            stack.addFirst( null );
        }
        
        stack.addFirst( type );
    }
    
    /**
     * Pops a type.  64 bit types will pop two slots.
     */
    public ValueType pop() {
        ValueType type = stack.removeFirst();
        
        if( type instanceof PrimitiveType && ((PrimitiveType) type).is64Bit() ) {
            stack.removeFirst();
        }

        return type;
    }
    
    /**
     * Set a local variable.  Sets two slots for a 64 bit type.
     * 
     * @param index the local var index
     * @param type the type to set
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public void setLocal( int index, ValueType type ) {

        locals[ index ] = type;
        
        if( type instanceof PrimitiveType && ((PrimitiveType) type).is64Bit() ) {
            locals[ index + 1 ] = null;
        }
    }
    
    /**
     * Get a local variable.
     * 
     * @param index the local var index
     * @return null if the given local is not valid or is not set.
     * 
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public ValueType getLocal( int index ) {
        return locals[ index ];
    }    
    
    /**
     * Get the max locals count.
     */
    public int maxLocals() {
        return locals.length;
    }
    
    /**
     * Get the current size of the stack
     */
    public int stackSize() {
        return stack.size();
    }
    
    /**
     * Pop a given number of slots (ignores size of types)
     * 
     * @param count the number of slots to pop
     */
    public void pop( int count ) {
        while( count-- > 0 ) stack.removeFirst();
    }

    /**
     * Swap the top two slots (which should not be 64 bit types)
     */
    public void swap() {
        ValueType top = stack.removeFirst();
        ValueType snd = stack.removeFirst();
        stack.addFirst( top );
        stack.addFirst( snd );
    }
    
    /**
     * Duplicate the given number of slots and insert them after skipping
     * over the given number of slots. Type size is ignored.
     * 
     * @param count slots to duplicate - 1 or 2
     * @param skip slots to skip over - 0, 1 or 2
     */
    public void dup( int count, int skip ) {
        ValueType top = stack.removeFirst();
        ValueType snd = (count==2) ? stack.removeFirst() : null;

        ValueType skip1 = (skip > 0) ? stack.removeFirst() : null;
        ValueType skip2 = (skip > 1) ? stack.removeFirst() : null;

        if( snd != null ) stack.addFirst( snd );
        stack.addFirst( top );

        if( skip2 != null ) stack.addFirst( skip2 );
        if( skip1 != null ) stack.addFirst( skip1 );

        if( snd != null ) stack.addFirst( snd );
        stack.addFirst( top );
    }
    
    /**
     * Determine the set of local vars that are common to the given frames.
     * This can be used to determine the local variable state that can be
     * accessed in an exception handler - by finding the local variable types
     * that are common to all the instructions in the try-block.
     * The resulting frame consists of the merged locals and an empty stack
     */
    public static Frame merge( Collection<Frame> frames ) {
        ArrayList<ValueType> slots = new ArrayList<ValueType>();
        
        
        
        return slots;
    }
    
    /**
     * Make a frame for the start of an exception handler
     */
    public static Frame forHandler( CodeAttribute.ExceptionHandler handler ) {
        LinkedList<ValueType> stack = new LinkedList<ValueType>();
        stack.add( exceptionType );
        return new Frame( stack, locals );
    }
    
    /**
     * Make a frame for the beginning of an instance method or constructor
     *  
     * @param desc the method
     */
    public static Frame instanceMethod( MethodModel method ) {
        ArrayList<ValueType> vars = expandTypes( desc.signature.paramTypes );
        vars.add( 0, desc.owner );
        return new Frame( EMPTY, vars.toArray( new ValueType[ vars.size() ] ) );        
    }

    /**
     * Make a frame for the beginning of a static method.
     *  
     * @param desc the method
     */
    public static Frame staticMethod( MethodModel method ) {
        ArrayList<ValueType> vars = expandTypes( desc.signature.paramTypes );
        return new Frame( EMPTY, vars.toArray( new ValueType[ vars.size() ] ) );        
    }
    
    /**
     * Expand 64 bit types into double slots.
     */
    private static ArrayList<ValueType> expandTypes( ValueType[] types ) {
        ArrayList<ValueType> lvt = new ArrayList<ValueType>();
        for( ValueType vt : types ) {
            lvt.add( vt );
            
            if( vt == PrimitiveType.LONG || vt == PrimitiveType.DOUBLE ) {
                lvt.add( null );
            }
        }
        
        return lvt;
    }
}
