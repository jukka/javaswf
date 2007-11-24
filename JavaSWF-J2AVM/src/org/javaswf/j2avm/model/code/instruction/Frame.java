package org.javaswf.j2avm.model.code.instruction;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.code.instruction.Instruction;
import org.javaswf.j2avm.model.code.intermediate.Slot;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.parser.ExceptionHandler;
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
    		Slot s = toCopy.locals[i];
			locals[i] =  (s!=null) ? new Slot( s.getValue() ) : null;
		}
    }
    
    /**
     * Create an empty frame.
     */
    public Frame() {
    	this( 0, 0 );
    }

    /**
     * Create an empty stack with the given locals.
     */
    private Frame( Slot[] locals ) {
        this.stack  = new Slot[ 0 ];
        this.locals = locals;     
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
    	return popPush( 0, s );
    }

    /**
     * Create a new frame that has all the same slots as this frame but with
     * an item popped off the stack and the given value stored at the given
     * local var index.
     */
    public Frame popStore( Value value, int index ) {
        return popStore( new Slot( value ), index );
    }
    
    /**
     * Create a new frame that has all the same slots as this frame but with
     * an item popped off the stack and the given slot stored at the given
     * local var index.
     */
    public Frame popStore( Slot s, int index ) {
        int maxVars = Math.max( locals.length, index + 1 );
        
        Frame f = new Frame( stack.length - 1, maxVars );
        System.arraycopy( stack , 1, f.stack , 0, stack.length - 1 );
        System.arraycopy( locals, 0, f.locals, 0, locals.length );
        f.locals[index] = s;
        return f;
    }
    
    /**
     * Create a new frame that has all the same slots as this one except that
     * the given number of slots are popped from the stack and the new value
     * is pushed.
     */
    public Frame popPush( int popCount, Value value ) {    	
    	return popPush( popCount, new Slot( value ));
    }

    /**
     * Create a new frame that has all the same slots as this one except that
     * the given number of slots are popped from the stack and the given slots
     * are pushed (in order, with top at the front).
     */
    public Frame popPush( int popCount, Slot...values ) {
        int pushCount = values.length;
        int delta     = pushCount - popCount;    //change in stack size
        int keepCount = stack.length - popCount; //num slots to keep
        
        Frame f = new Frame( stack.length + delta, locals.length );
        System.arraycopy( stack , popCount, f.stack , pushCount, keepCount );
        System.arraycopy( values, 0       , f.stack , 0        , pushCount );
        System.arraycopy( locals, 0       , f.locals, 0        , locals.length );
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
    	
   	 	boolean first = true;
    	for( Slot slot : locals ) {
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
    		
    		int k = 0;
        	for( Frame frame : frames ) {
        		Slot s = ( frame.locals.length > j ) ? 
        				      frame.locals[j] :
        				      null;
        				      
                //if any frame has no value in this slot then leave the slot
        	    //empty and go to the next slot
        	    if( s == null || s.getValue() == null ) continue slotLoop;
        	    
        	    vals[k++] = s.getValue();
        	}
        	
        	Value merged = Value.merge( vals );
        	f.locals[j] = new Slot(merged);
		}
    	
        return f;
    }
    
    /**
     * Make a frame for the start of the given method
     */
    public static Frame forMethod( MethodModel method ) {
        List<Slot> locals = makeLocals( method );       

        //add "this" as first local var for instance method
        if( ! method.flags.contains( MethodFlag.MethodIsStatic ) ) {
            locals.add( 0, new Slot( new Value( method, method.owner.type, "this" )));
    	}
    	
        return new Frame( locals.toArray( new Slot[ locals.size() ] ) );        
    }
    
    private static List<Slot> makeLocals( MethodModel method ) {
        ValueType[] paramTypes = method.signature.paramTypes;
        List<Slot> slots = new ArrayList<Slot>();

        for( int i = 0; i < paramTypes.length; i++ ) {
            Value v = new Value( method, paramTypes[i], "p" + (i+1) );
            slots.add( new Slot( v ) );
            if( PrimitiveType.is64Bit( paramTypes[i] ) ) {
                slots.add( null );
            }
        }
        
        return slots;
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
