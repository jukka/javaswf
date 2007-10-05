package org.javaswf.j2avm.model.code;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.types.JavaType;
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

    /** The stack - top at front */
    public final Value[] stack;
    
    /** The local vars, some slots may be null */
    public final Value[] locals;
    
    /**
     * Copy an existing frame
     */
    public Frame( Frame toCopy ) {
    	stack = toCopy.
    	
    	stack .addAll( toCopy.stack  );
    	locals.addAll( toCopy.locals );
    }
    
    /**
     * Create an empty frame.
     */
    public Frame() {
    	stack = locals = new Value[0];
    }

    /**
     * Push a value.  Pushes 64 bit types as two slots.
     */
    public void push( Value value ) {
    	ValueType type = value.type();
    	
        if( type instanceof PrimitiveType && ((PrimitiveType) type).is64Bit() ) {
            stack.addFirst( null );
        }
        
        stack.addFirst( value );
    }
    
    /**
     * Pops a value.  64 bit types will pop two slots.
     */
    public Value pop() {
        Value     value = stack.removeFirst();
        ValueType type  = value.type();
        
        if( type instanceof PrimitiveType && ((PrimitiveType) type).is64Bit() ) {
            stack.removeFirst();
        }

        return value;
    }
    
    /**
     * Peek at a stack slot
     * @param index zero is stack top
     */
    public Value peek( int index ) {
    	return stack.get( index );
    }
    
    /**
     * Set a local variable.  Sets two slots for a 64 bit type.
     * 
     * @param index the local var index
     * @param value the value to set
     */
    public void setLocal( int index, Value value ) {

    	ValueType type = value.type();
    	int newSize = index + 1;

    	if( type instanceof PrimitiveType && ((PrimitiveType) type).is64Bit() ) {
            newSize++;
        }

    	while( locals.size() < newSize ) locals.add( null ); 
    		
    	locals.set( index, value );
    }
    
    /**
     * Get a local variable.
     * 
     * @param index the local var index
     * @return null if the given local is not valid or is not set.
     */
    public Value getLocal( int index ) {
        if( locals.size() < index+1 ) return null;
    	return locals.get( index );
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
        Value top = stack.removeFirst();
        Value snd = stack.removeFirst();
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
        Value top = stack.removeFirst();
        Value snd = (count==2) ? stack.removeFirst() : null;

        Value skip1 = (skip > 0) ? stack.removeFirst() : null;
        Value skip2 = (skip > 1) ? stack.removeFirst() : null;

        if( snd != null ) stack.addFirst( snd );
        stack.addFirst( top );

        if( skip2 != null ) stack.addFirst( skip2 );
        if( skip1 != null ) stack.addFirst( skip1 );

        if( snd != null ) stack.addFirst( snd );
        stack.addFirst( top );
    }
    
    /**
     * Merge the values from another frame into this one.
     * 
     * @return true if any slots changed
     */
    public boolean merge( Frame f ) {
    	
    	boolean stackChanged  = merge( stack,  f.stack );
    	boolean localsChanged = merge( locals, f.locals );
    	
    	return stackChanged || localsChanged;
    }
    
    /**
     * Dump the frame
     */
    public void dump( IndentingPrintWriter ipw ) {
   	 	ipw.print( "[" );
    	
   	 	boolean first = false;
    	for( Value slot : locals ) {
    		if( first ) first = false;
    		else ipw.print( "|" );
    		
			if( slot == null ) ipw.print( "-" );
			else               ipw.print( slot );
		}
    	
   	    ipw.print( "] " );
   	    
   	    first = false;
   	    for( Value slot : stack ) {
    		if( first ) first = false;
    		else ipw.print( "|" );

    		if( slot == null ) ipw.print( "-" );
			else               ipw.print( slot );
		}
     	
     	ipw.println();
    }
        
    /**
     * Merge the src values into the dest.
     * 
     * @return true if any slots were altered
     */
    private static boolean merge( List<Value> dest, List<Value> src ) {
    	boolean altered = false;
    	
    	int size = Math.max( dest.size(), src.size() );
    	for( int i = 0; i < size; i++ ) {
			Value a = (i < dest.size()) ? dest.get( i ) : null;
			Value b = (i < src .size()) ? src .get( i ) : null;
			
			Value merged = merge( a, b );
			
			if( merged != a ) {
				altered = true;
				set( dest, i, merged );				
			}
		}
    	
    	return altered;
    }
    
    private static Value merge( Value a, Value b ) {
    	if( a == null || b == null ) return null;
    	if( a == b ) return a;
    	
    	ValueType common = (ValueType) JavaType.common( a.type(), b.type() );
    	if( common == null ) return null;
    	
    	ValueGenerator gen = (a.creator == b.creator) ? a.creator : null;

    	return new Value( gen, common, "*" );
    }
    
    private static void set( List<Value> list, int index, Value value ) {
    	int newSize = index + 1;
    	while( list.size() < newSize ) list.add( null );
    	list.set( index, value );
    }
    
    /**
     * Make a frame for the start of an exception handler
     */
    public static Frame forHandler( ExceptionHandler handler ) {
    	
    	//gather and merge all the frames from within the try-block
    	//in order to determine the common local variables
    	List<Frame> frames = new ArrayList<Frame>();
    	InstructionCursor cursor = handler.start.cursor();
    	
    	Instruction i;
    	while((i = cursor.forward()) != handler.end ) {
    		frames.add( i.frameBefore );  //frameAfter is not relevant ??    		
    	}
    	
    	Frame f = new Frame( frames.get( 0 ) );
    	for( Frame frame : frames ) {
			f.merge( frame );
		}
    	
    	f.stack.clear();
        f.stack.add( new Value( handler, handler.exceptionType, "catch_" + handler.handler.label ));
        return f;
    }
    
    /**
     * Make a frame for the beginning of an instance method or constructor
     */
    public static Frame instanceMethod( MethodModel method, ObjectType owner ) {

    	Frame f = new Frame();

    	CodeAttribute code = (CodeAttribute) method.attributes.get( AttributeName.Code );
    	if( code != null ) {    	
	    	f.setLocal( 0, new Value( method, owner, "this" ) );
	    	setLocals( method, f, method.signature.paramTypes, 1 );
    	}
    	
        return f;        
    }

    /**
     * Make a frame for the beginning of a static method.
     */
    public static Frame staticMethod( MethodModel method ) {
    	Frame f = new Frame();    	

    	CodeAttribute code = (CodeAttribute) method.attributes.get( AttributeName.Code );
    	if( code != null ) setLocals( method, f, method.signature.paramTypes, 0 );
        return f;        
    }

    private static void setLocals( MethodModel method, Frame f, ValueType[] paramTypes, int local ) {
    	Value[] params = new Value[ paramTypes.length ];
    	for( int i = 0; i < params.length; i++ ) {
			params[i] = new Value( method, paramTypes[i], "p" + (i+1) );
		}
    	
    	for( int i = 0; i < params.length; i++ ) {
			f.setLocal( local, params[i] );
			
			if( paramTypes[i] == PrimitiveType.LONG 
			 || paramTypes[i] == PrimitiveType.DOUBLE ) {
				local += 2;
			}
			else {
				local++;
			}
		}    	
    }

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !( obj instanceof Frame )) return false;
		
		Frame f = (Frame) obj;
		
		int stackSize = stack.size();
		if( f.stack.size() != stack.size() ) return false;
	
		for( int i = 0; i < stackSize; i++ ) {
			if( ! stack.get( i ).equals( obj )  )
		}
	}
}
