package org.javaswf.j2avm.model.code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.types.ObjectType;

public class InstructionList implements Iterable<Instruction> {

    /**
     * Get the number of instructions in the list
     */
    public int size() {
        return count;
    }

    /**
     * Remove the given instruction from the list
     */
    public void remove( Instruction insn ) {
        Instruction next = insn.next;
        Instruction prev = insn.prev;
        
        if( first == insn ) first = next;
        if( last  == insn ) last  = prev;
        
        if( next != null ) next.prev = prev;
        if( prev != null ) prev.next = next;
        
        count--;
        
        insn.list = null;
        
        if( insn instanceof LabelTargetter ) {
            ((LabelTargetter) insn).release();
        }
    }
  
    /**
     * Get a cursor that is positioned at the start of the list
     */
    public InstructionCursor cursorAtStart() {
        return new InstructionCursor( this, first );
    }
    
    /**
     * Get a cursor that is positioned at the end of the list
     */
    public InstructionCursor cursorAtEnd() {
        return new InstructionCursor( this, null );
    }
    
    /**
     * Get a cursor that is positioned before the given instruction
     */
    public InstructionCursor cursorBefore( Instruction insn ) {
     	if( insn.list != this ) {
     		throw new IllegalArgumentException( "Instruction is not part of this list" );
     	}
    	
        return new InstructionCursor( this, insn );
    }
    
    /**
     * Get a cursor that is positioned after the given instruction
     */
    public InstructionCursor cursorAfter( Instruction insn ) {
     	if( insn.list != this ) {
     		throw new IllegalArgumentException( "Instruction is not part of this list" );
     	}
    	
        return new InstructionCursor( this, insn.next );
    }
    
    /**
     * Label the given instruction by inserting a CodeLabel before it.
     * 
     * @param label the label text - this should be unique within the list
     * @param insn the instruction to label
     * @return the new label (or the given instruction if it is a label)
     */
    public CodeLabel label( Instruction insn, String label ) {
     	if( insn.list != this ) {
     		throw new IllegalArgumentException( "Instruction is not part of this list" );
     	}

     	if( insn instanceof CodeLabel ) return (CodeLabel) insn;
     	
     	CodeLabel cl = new CodeLabel( label );
    	insert( cl, insn.prev, insn );
    	return cl;
    }
    
    /**
     * Get an iterator over the instructions
     */
    public Iterator<Instruction> iterator() {
		return new Iterator<Instruction>(){
			
			private Instruction next = first;
			private Instruction curr = null;
			
			/** @see java.util.Iterator#hasNext() */
			public boolean hasNext() {
				return next != null ;
			}

			/** @see java.util.Iterator#next() */
			public Instruction next() {
				if( next == null ) throw new NoSuchElementException();
				curr = next;
				next = curr.next;
				return curr;
			}

			/** @see java.util.Iterator#remove() */
			public void remove() {
				if( curr == null ) throw new IllegalStateException();
				curr.remove();
				curr = null;
			}
		};
	}

    /**
     * Get the first instruction in the list.
     * 
     * @return null if the list is empty
     */
    public Instruction first() { return first; }
    
    /**
     * Get the last instruction in the list.
     * 
     * @return null if the list is empty
     */
    public Instruction last() { return last; }
    
    /**
     * Whether this list is empty
     */
    public boolean isEmpty() { return count == 0; }
    
    /**
     * Whether there are any exception handlers
     */
    public boolean hasExceptionHandlers() {
    	return ! handlers.isEmpty();
    }
    
    /**
     * Append a new exception handler
     * 
     * @return the new handler
     */
    public final ExceptionHandler addHandler( CodeLabel start, 
                                              CodeLabel end, 
                                              CodeLabel handler, 
                                              ObjectType exceptionType ) {
        ExceptionHandler eh = 
            new ExceptionHandler( start, end, handler, exceptionType );
        
        handlers.add( eh );
        return eh;
    }
    
    /**
     * Get an iterator over the exception handlers in order of decreasing
     * importance. The remove operation is supported.
     */
    public final Iterator<ExceptionHandler> handlers() {
        return new Iterator<ExceptionHandler>() {            
            private final Iterator<ExceptionHandler> it = handlers.iterator();
            private ExceptionHandler handler;            
            
            /** @see java.util.Iterator#hasNext() */
            public boolean hasNext() {
                return it.hasNext();
            }

            /** @see java.util.Iterator#next() */
            public ExceptionHandler next() {
                handler = it.next();
                return handler;
            }

            /** @see java.util.Iterator#remove() */
            public void remove() {
                it.remove();
                handler.release();
            }
        };
    }
    
	private Instruction first;
    private Instruction last;
    private int count;
    
    /** The exception handlers in order of decreasing precedence */
    private final List<ExceptionHandler> handlers = new ArrayList<ExceptionHandler>();
    
    /**
     * If true then this list has been normalized such that 64 bit types
     * are assumed to be single slots (rather than the double slots that the
     * JVM normally uses).
     */
    /*pkg*/ boolean hasBeenNormalized;
    private boolean needsNormalization; //if not normalized this flag indicates dup/pop insertions
    
    /*pkg*/ void insert( Instruction newInsn, 
                         Instruction prevInsn, Instruction nextInsn ) {
        newInsn.next = nextInsn;
        newInsn.prev = prevInsn;
        newInsn.list = this;
        
        if( prevInsn == null ) {
            first = newInsn;
        } else {
            prevInsn.next = newInsn;
        }
        
        if( nextInsn == null ) {
            last = newInsn;
        } else {
            nextInsn.prev = newInsn;
        }
        
        count++;
        
        if( (! hasBeenNormalized) && newInsn.mayInvolve64BitSlots() ) {
        	needsNormalization = true;
        }
    }    
    
    /**
     * Normalize the list - convert dup and pop instructions to assume that
     * 64 bit types only occupy a single stack slot rather than 2.
     */
    public void normalize( ObjectType owner, MethodModel method ) {
    	if( hasBeenNormalized ) return;
    	
    	if( ! needsNormalization ) {
        	hasBeenNormalized = true;
        	return;
    	}
    	
    	determineFrames( owner, method );
    	
    	Instruction i = first;
    	while( i != null ) {
    		i.normalize();
    		i = i.next;
    	}

    	hasBeenNormalized = true;    	
    }
    
    /**
     * Determine the frames for each instruction.
     */
    public void determineFrames( ObjectType owner, MethodModel method ) {
    	
    	LinkedList<IncomingFrame> queue = new LinkedList<IncomingFrame>();
    	Frame frame1 = (method.flags.contains( MethodFlag.MethodIsStatic )) ?
    			           Frame.staticMethod( method ) :
    			           Frame.instanceMethod( method, owner );
    	queue.add( new IncomingFrame( frame1, first()) );

    	boolean processedHandlers = false;
    	
    	while( ! queue.isEmpty() ) {
        	while( ! queue.isEmpty() ) {
        		
        		IncomingFrame infr = queue.removeFirst();
        		Instruction   insn = infr.insn;
        		
        		//merge the incoming frame and if it caused a change in the
        		//outgoing frame then enqueue downstream instructions
        		if( infr.insn.mergeIncomingFrame( infr.incoming ) ) {
        			Frame out = insn.frameAfter();
        			
        			if( insn.flowsToNext() ) {
        				queue.add( new IncomingFrame( out, insn.next() ));
        			}
        			
        			if( insn instanceof LabelTargetter ) {
        				LabelTargetter targetter = (LabelTargetter) insn;
        				
        				Set<CodeLabel> labels = new HashSet<CodeLabel>();
        				targetter.targets( labels );
        				
        				for( CodeLabel target : labels ) {
        					queue.add( new IncomingFrame( out, target ));
        				}
        			}
        		}        		
        	}
        	
        	//process the exception handlers
        	if( ! processedHandlers ) {
        		for( Iterator<ExceptionHandler> i = handlers(); i.hasNext(); ) {
					ExceptionHandler handler = i.next();
					
					Frame f = Frame.forHandler( handler );
	        		queue.add( new IncomingFrame( f, handler.handler ) );
	        	}
        		
        		processedHandlers = true;
        	}
    	}
    }
    
    private class IncomingFrame {
    	Frame       incoming;
    	Instruction insn;    	
    	
    	IncomingFrame( Frame incoming, Instruction insn ) {
    		this.incoming = incoming;
    		this.insn     = insn;
    	}
    }
}
