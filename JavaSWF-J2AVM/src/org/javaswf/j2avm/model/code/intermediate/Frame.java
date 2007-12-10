package org.javaswf.j2avm.model.code.intermediate;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.MethodModel;

import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.intermediate.Slot;
import org.javaswf.j2avm.model.code.statement.StaticSingleAssignmentStatement;
import org.javaswf.j2avm.model.code.statement.TryStatement;
import org.javaswf.j2avm.model.flags.MethodFlag;
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
    public final Stack<Slot> stack = new Stack<Slot>();
    
    /** The local vars, some slots may be null */
    public final SortedMap<Integer, Slot> locals = new TreeMap<Integer, Slot>();
        
    /**
     * Create an empty frame.
     */
    public Frame() {
    	//nada
    }
 
    /**
     * Create a copy of another frame - new stack and locals, but containing the
     * same slot instances as the original
     */
    public Frame( Frame toCopy ) {
    	stack .addAll( toCopy.stack  );
    	locals.putAll( toCopy.locals );
    }
    
    /**
     * Create a new frame that has all the same slots as this frame plus the
     * extra value on the top of the stack
     */
    public Frame push( SlotValue value ) {
    	return push( new Slot( value ) );
    }

    /**
     * Create a new frame that has all the same slots as this frame plus the
     * extra slot on the top of the stack
     */
    public Frame push( Slot s ) {
    	return popPush( 0, s );
    }
   
    /**
     * Create a new frame that has all the same slots as this frame but with
     * an item popped off the stack and stored at the given local var index.
     */
    public Frame popStore( int index ) {
    	Frame f = new Frame( this );
    	
    	Slot s = f.stack.pop();
    	f.locals.put( index, s );
    	
        return f;
    }
    
    /**
     * Create a new frame that has all the same slots as this one except that
     * the given number of slots are popped from the stack and the new value
     * is pushed.
     */
    public Frame popPush( int popCount, SlotValue value ) {    	
    	return popPush( popCount, new Slot( value ));
    }

    /**
     * Create a new frame that has all the same slots as this one except that
     * the given number of slots are popped from the stack and the given slots
     * are pushed (in order, with top at the front).
     */
    public Frame popPush( int popCount, Slot...values ) {
    	Frame f = new Frame( this );
    	for( int i = 0; i < popCount; i++ ) {
      	  	f.stack.pop();			
		}

    	for( int i = values.length - 1; i >= 0; i-- ) {
			f.stack.push( values[i] );
		}
    	
        return f;
    }
    
    /**
     * Create a new frame that has all the same slots as this one except that
     * the given number of slots are popped from the stack.
     */
    public Frame pop( int popCount ) {    	
    	return popPush( popCount );    	
    }

    /**
     * Create a new frame that has all the same slots as this one except that
     * the top two stack slots are swapped
     */
    public Frame swap() {    	
    	Frame f = new Frame( this );
    	
    	Slot s = f.stack.get( 0 );
    	f.stack.set( 0,  f.stack.get( 1 ));
    	f.stack.set( 1, s );
    	return f;    	
    }
    
    /**
     * Dump the frame
     */
    public void dump( IndentingPrintWriter ipw ) {
   	 	ipw.print( "[" );
    	
   	 	boolean first = true;
    	for( Slot slot : locals.values()) {
    		if( first ) first = false;
    		else ipw.print( "|" );
    		
			if     ( slot            == null ) ipw.print( "_" );
			else if( slot.getValue() == null ) ipw.print( "-" );
			else                               ipw.print( slot.getValue() );
		}
    	
   	    ipw.print( "] " );
   	    
   	    first = true;
   	    for( Slot slot : stack ) {
    		if( first ) first = false;
    		else ipw.print( "|" );

			if     ( slot            == null ) ipw.print( "_" );
			else if( slot.getValue() == null ) ipw.print( "-" );
			else                               ipw.print( slot.getValue() );
		}
     	
     	ipw.println();
    }
    
    @Override
    public String toString() {
        StringWriter sw = new StringWriter();         
        IndentingPrintWriter ipw = new IndentingPrintWriter( sw );
        dump( ipw );
        return sw.toString();
    }
        
    /**
     * Make a frame for the start of the given method
     */
    public static Frame forMethod( MethodModel method ) {
    	Frame frame = new Frame();
        frame.addParams( method );
        return frame;
    }
    
    private void addParams( MethodModel method ) {
    	//add "this" as first local var for instance method
        if( ! method.flags.contains( MethodFlag.MethodIsStatic ) ) {
            locals.put( 0, new Slot( ExpressionBuilder.this_( method.owner.type )));
    	}

        ValueType[] paramTypes = method.signature.paramTypes;

        int index = locals.size();
        for( int i = 0; i < paramTypes.length; i++ ) {
            locals.put( index, new Slot( ExpressionBuilder.argument( paramTypes[i], index )));
            
            index += (PrimitiveType.is64Bit( paramTypes[i] ) ) ? 2 : 1;
        }
    }
    
	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !( obj instanceof Frame )) return false;
		
		Frame f = (Frame) obj;
		
		return stack.equals( f.stack ) && locals.equals( f.locals );
	}
}
