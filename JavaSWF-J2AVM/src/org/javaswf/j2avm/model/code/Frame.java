package org.javaswf.j2avm.model.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
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
    public final Slot[] stack;
    
    /** The local vars, some slots may be null */
    public final Slot[] locals;
    
    /**
     * Copy an existing frame - create all new slots but keep the referenced
     * values.
     */
    public Frame( Frame toCopy ) {
    	this( toCopy.stack .length, toCopy.locals.length );
    	
    	for( int i = 0; i < stack.length; i++ ) {
			stack[i] = new Slot( toCopy.stack[i].getValue() );
		}

    	for( int i = 0; i < locals.length; i++ ) {
			locals[i] = new Slot( toCopy.locals[i].getValue() );
		}
    }
    
    /**
     * Create an empty frame.
     */
    public Frame() {
    	this( 0, 0 );
    }

    /**
     * Create a frame with the given stack size and max locals
     */
    public Frame( int stackSize, int maxLocals ) {
    	stack  = new Slot[ stackSize ];
    	locals = new Slot[ maxLocals ];    	
    }
 
    /**
     * Create a new frame that has all the same slots as this frame plus the
     * extra value on the top of the stack
     */
    public Frame push( Value value ) {
    	return push( new Slot( value ) );
    }

    /**
     * Create a new frame that has all the same slots as this frame plus the
     * extra slot on the top of the stack
     */
    public Frame push( Slot s ) {
    	Frame f = new Frame( stack.length + 1, locals.length );
    	System.arraycopy( stack , 0, f.stack , 1, stack.length  );
    	System.arraycopy( locals, 0, f.locals, 0, locals.length );
    	f.stack[0] = s;
    	return f;
    }

    /**
     * Create a new frame that has all the same slots as this one except that
     * the given number of slots are popped from the stack and the new value
     * is pushed.
     */
    public Frame popPush( int popCount, Value value ) {    	
    	Frame f = new Frame( stack.length + 1 - popCount, locals.length );
    	System.arraycopy( stack , 0, f.stack , 1, stack.length - popCount  );
    	System.arraycopy( locals, 0, f.locals, 0, locals.length );
    	f.stack[0] = new Slot( value );
    	return f;    	
    }

    /**
     * Create a new frame that has all the same slots as this one except that
     * the given number of slots are popped from the stack.
     */
    public Frame pop( int popCount ) {    	
    	Frame f = new Frame( stack.length - popCount, locals.length );
    	System.arraycopy( stack , 0, f.stack , 0, stack.length - popCount  );
    	System.arraycopy( locals, 0, f.locals, 0, locals.length );
    	return f;    	
    }

    /**
     * Create a new frame that has all the same slots as this one except that
     * the top two stack slots are swapped
     */
    public Frame swap() {    	
    	Frame f = new Frame( stack.length, locals.length );
    	System.arraycopy( stack , 0, f.stack , 0, stack.length  );
    	System.arraycopy( locals, 0, f.locals, 0, locals.length );
    	Slot s = f.stack[0];
    	f.stack[0] = f.stack[1];
    	f.stack[1] = s;
    	return f;    	
    }
    
    /**
     * Dump the frame
     */
    public void dump( IndentingPrintWriter ipw ) {
   	 	ipw.print( "[" );
    	
   	 	boolean first = false;
    	for( Slot slot : locals ) {
    		if( first ) first = false;
    		else ipw.print( "|" );
    		
			if     ( slot            == null ) ipw.print( "-" );
			else if( slot.getValue() == null ) ipw.print( "-" );
			else                               ipw.print( slot.getValue() );
		}
    	
   	    ipw.print( "] " );
   	    
   	    first = false;
   	    for( Slot slot : stack ) {
    		if( first ) first = false;
    		else ipw.print( "|" );

			if     ( slot            == null ) ipw.print( "-" );
			else if( slot.getValue() == null ) ipw.print( "-" );
			else                               ipw.print( slot.getValue() );
		}
     	
     	ipw.println();
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
    	int maxLocals = 0; 
    	while((i = cursor.forward()) != handler.end ) {
    		Frame iframe = i.frame();
    		frames.add( iframe );
    		maxLocals = Math.max( maxLocals, iframe.locals.length );
    	}
    	
    	Frame f = new Frame( 1, maxLocals );
    	Value exception = new Value( handler, handler.exceptionType, 
    			                    "catch_" + handler.handler.label );
    	f.stack[0] = new Slot(exception);
    	
    	//merge the values in each slot
    	slotLoop:
    	for( int j = 0; j < maxLocals; j++ ) {
    		Value[] vals = new Value[ frames.size() ];
    		
        	for( Frame frame : frames ) {
        		Slot s = ( frame.locals.length > j ) ? 
        				      frame.locals[j] :
        				      null;
        				      
                //if any frame has no value in this slot then leave the slot
        	    //empty and go to the next slot
        	    if( s == null || s.getValue() == null ) continue slotLoop;
        	}
        	
        	Value merged = Value.merge( vals );
        	f.locals[j] = new Slot(merged);
		}
    	
        return f;
    }
    
    /**
     * Make a frame for the beginning of an instance method or constructor
     */
    private static Frame instanceMethod( MethodModel method ) {

    	Frame f = new Frame();

    	CodeAttribute code = (CodeAttribute) method.attributes.get( AttributeName.Code );
    	if( code != null ) {    	
	    	f.locals[0] = new Slot( new Value( method, method.owner.type, "this" ));
	    	setLocals( method, f, method.signature.paramTypes, 1 );
    	}
    	
        return f;        
    }

    /**
     * Make a frame for the start of the given method
     */
    public static Frame forMethod( MethodModel method ) {
    	if( method.flags.contains( MethodFlag.MethodIsStatic ) ) {
    		return staticMethod( method );
    	}
    	
    	return instanceMethod( method );
    }
    
    /**
     * Make a frame for the beginning of a static method.
     */
    private static Frame staticMethod( MethodModel method ) {
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
			f.locals[local] = new Slot( params[i] );
			
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
		
		return Arrays.equals( stack , f.stack  ) 
		    && Arrays.equals( locals, f.locals );
	}
}
