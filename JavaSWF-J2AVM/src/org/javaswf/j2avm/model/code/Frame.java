package org.javaswf.j2avm.model.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
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
    
    /**
     * Create an empty frame.
     * 
     * @param maxLocals the max locals that can be stored
     */
    public Frame( int maxLocals ) {
        this.stack  = new LinkedList<ValueType>();
        this.locals = new ValueType[ maxLocals ];    	
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
     * Peek at a stack slot
     * @param index zero is stack top
     */
    public ValueType peek( int index ) {
    	return stack.get( index );
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
     * Merge the locals from another frame into this one.  The resulting locals
     * represent what can be known about the type in each slot.  Null slots
     * denote unknown, conflicting or invalid types.
     * 
     * @return true if any slots changed
     */
    public boolean mergeLocals( Frame f ) {
    	ValueType[] locals2 = f.locals;
    	
    	boolean changed = false;
    	
    	for( int i = 0; i < locals.length; i++ ) {
			ValueType otherType = (i < locals2.length) ? locals2[i] : null;
			ValueType thisType = locals[i];
			
			if( otherType == null || thisType == null ) {
				locals[i] = null;
				continue;
			}
			
			if( otherType.equals( thisType )) continue;
			
			if( otherType instanceof ObjectOrArrayType
			 && thisType  instanceof ObjectOrArrayType ) {
				
				changed = changed || (! locals[i].equals( ObjectType.OBJECT )); 				
				locals[i] = ObjectType.OBJECT;
			}
			
			changed = changed || (locals[i] != null); 
			locals[i] = null;			
		}
    	
    	return changed;
    }
    
    /**
     * Dump the frame
     */
    public void dump( IndentingPrintWriter ipw ) {
   	 	ipw.print( "[" );
    	
    	for( ValueType slot : locals ) {
			if( slot == null ) ipw.print( "-" );
			else               ipw.print( slot.abbreviation );
		}
    	
   	    ipw.print( "] " );
   	    
     	for( ValueType slot : stack ) {
			if( slot == null ) ipw.print( "-" );
			else               ipw.print( slot.abbreviation );
		}
     	
     	ipw.println();
    }
    
    /**
     * Determine the set of local vars that are common to the given frames.
     * This can be used to determine the local variable state that can be
     * accessed in an exception handler - by finding the local variable types
     * that are common to all the instructions in the try-block.
     * The resulting frame consists of the merged locals and an empty stack
     */
    public static Frame merge( Collection<Frame> frames ) {
    	
    	Frame frame = null;
    	
    	for( Frame f : frames ) {
    		if( frame == null ) {
    			frame = new Frame( f );
    			frame.stack.clear();
    		} 
    		else {
    		    frame.mergeLocals( f );	
    		}
    	}
        
        return frame;
    }
    
    /**
     * Make a frame for the start of an exception handler
     */
    public static Frame forHandler( InstructionList.ExceptionHandler handler ) {
    	
    	Collection<Frame> frames = new ArrayList<Frame>();
    	InstructionCursor cursor = handler.start.cursor();
    	
    	Instruction i;
    	while((i = cursor.forward()) != handler.end ) {
    		frames.add( i.frameBefore );  //frameAfter is not relevant ??    		
    	}
    	
    	Frame f = merge( frames );
        f.stack.add( ObjectType.THROWABLE );
        return f;
    }
    
    /**
     * Make a frame for the beginning of an instance method or constructor
     */
    public static Frame instanceMethod( MethodModel method ) {
    	    	
    	CodeAttribute code = (CodeAttribute) method.attributes.get( AttributeName.Code );
    	if( code == null ) return new Frame(0);
    	
    	Frame f = new Frame( code.maxLocals );
    	f.setLocal( 0, ObjectType.OBJECT );
    	int local = 1;
    	
    	setLocals( f, method.signature.paramTypes, local );    	
        return f;        
    }

    /**
     * Make a frame for the beginning of a static method.
     */
    public static Frame staticMethod( MethodModel method ) {
    	CodeAttribute code = (CodeAttribute) method.attributes.get( AttributeName.Code );
    	if( code == null ) return new Frame(0);
    	
    	Frame f = new Frame( code.maxLocals );    	
    	setLocals( f, method.signature.paramTypes, 0 );
        return f;        
    }

    private static void setLocals( Frame f, ValueType[] paramTypes, int local ) {
    	for( int i = 0; i < paramTypes.length; i++ ) {
			f.setLocal( local, paramTypes[i] );
			
			if( paramTypes[i] == PrimitiveType.LONG 
			 || paramTypes[i] == PrimitiveType.DOUBLE ) {
				local += 2;
			}
			else {
				local++;
			}
		}    	
    }
}
