package org.javaswf.j2avm.model.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.javaswf.j2avm.model.MethodModel;
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
    
    private int valueNameIndex = 0;
    private static String valueNames = "abcdefghijklmnopqrstuvwxyz";
    
    /**
     * Generate the next value name
     */
    String nextValueName() {
    	if( valueNameIndex < valueNames.length() ) {
    		int i = valueNameIndex;
    		valueNameIndex++;
    		return valueNames.substring( i, i+1 );
    	}
    	
    	int suffix = valueNameIndex / valueNames.length();
    	int i      = valueNameIndex % valueNames.length();

    	valueNameIndex++;
		return valueNames.substring( i, i+1 ) + suffix;
    }
    
    /** The exception handlers in order of decreasing precedence */
    private final List<ExceptionHandler> handlers = new ArrayList<ExceptionHandler>();
    
    /**
     * If true then this list has been normalized such that 64 bit types
     * are assumed to be single slots (rather than the double slots that the
     * JVM normally uses).
     */
    /*pkg*/ boolean hasBeenNormalized;
    
    private boolean hasFrames = false;
    
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
    }    
    
    /**
     * Clear all the frames.
     */
    public void clearFrames() {
    	Instruction i = first;
    	while( i != null ) {
    		i.frame = null;
    		i = i.next;
    	}
    	
    	hasFrames = false;
    }
    
    /**
     * Determine the frames for each instruction.
     * Clears any existing frames first.
     */
    public void determineFrames( MethodModel method ) {
    	
    	if( hasFrames ) clearFrames();    	
    	if( isEmpty() ) return;

    	new FrameBuilder( method ).build();
    	
    	hasFrames = true;
        hasBeenNormalized = true;
    }
    
    /**
     * Refactors newObject + invokeSpecial combinations into 
     * newObject(with paramTypes)
     */
    private class NewObjectRefactorer {
    	final Instruction.NewObject newObject;
    	Instruction.InvokeSpecial   invoke;    //the constructor call
    	Instruction.Dup             dup;       //optional dup after the newObject
    	
    	NewObjectRefactorer( Instruction.NewObject newObject ) {
    		this.newObject = newObject;
    	}
    	
    	void refactor() {
    		if( dup != null ) {
				if( dup.skip != 0 || dup.count != 1 ) {
					throw new RuntimeException( "Unhandled NewObject corner case" );
				}
			
				dup.remove();
    		}

    		newObject.paramTypes = invoke.methodDesc.signature.paramTypes;

			newObject.remove();			
			insert( newObject, invoke, invoke.next() );
			invoke.remove();

			//if new object is not used then discard it
    		if( dup == null ) {
    			insert( new Instruction.Pop(1), newObject, newObject.next() );
    		}    		
    	}
    }
    
    /**
     * Determines instruction frames
     */
    private class FrameBuilder {
    	LinkedList<IncomingFrame>    queue    = new LinkedList<IncomingFrame>();
    	LinkedList<ExceptionHandler> handlerQ = new LinkedList<ExceptionHandler>();

    	//map to gather the newObjects to be refactored
    	Map<Instruction.NewObject, NewObjectRefactorer> newObjects = 
    		new HashMap<Instruction.NewObject, NewObjectRefactorer>();
    	
    	FrameBuilder( MethodModel method ) {
        	Frame frame1 = Frame.forMethod( method );
        	queue.add( new IncomingFrame( frame1, first()) );
        	
    		for( ExceptionHandler handler : handlers ) {
    			handlerQ.add( handler );
        	}
    	}
    	
    	/**
    	 * Process instructions and then exception handlers
    	 */
    	public void build() {
    		processQueue();
    		
    		//add the exception handlers and process
    		while( ! handlerQ.isEmpty() ) {
    			ExceptionHandler handler = handlerQ.removeFirst();
    			Frame f = Frame.forHandler( handler );
        		queue.add( new IncomingFrame( f, handler.handler ) );
        		
        		processQueue();
    		}
    		
    		for( NewObjectRefactorer newObj : newObjects.values() ) {
    			newObj.refactor();
    		}
    	}
    	
    	/**
    	 * Process all instructions in the queue
    	 */
    	private void processQueue() {
    		while( ! queue.isEmpty() ) {
        		IncomingFrame infr  = queue.removeFirst();
        		Instruction   insn  = infr.insn;
        		Frame         frame = infr.incoming;
    			
        		//gather new objects and associated dups and constructor calls
        		if( insn instanceof Instruction.NewObject ) {
        			Instruction.NewObject newObj = (Instruction.NewObject) insn;
        			newObjects.put( newObj, new NewObjectRefactorer( newObj ));
        		}
        		else if( insn instanceof Instruction.Dup ) {
        			ValueGenerator vg = frame.stack[0].getValue().creator();
        			
        			if( vg instanceof Instruction.NewObject ) { 
		    			Instruction.NewObject newObj = (Instruction.NewObject) vg;
		    			Instruction.Dup dup = (Instruction.Dup) insn;
		    			newObjects.get( newObj ).dup = dup;
        			}
        		}
        		else if( insn instanceof Instruction.InvokeSpecial ) {
        			Instruction.InvokeSpecial invoke = (Instruction.InvokeSpecial) insn;
        			
        			if( invoke.methodDesc.signature.name.equals( MethodModel.CONSTRUCTOR_NAME ) ) {
        				int argCount = invoke.methodDesc.signature.paramTypes.length;
        				ValueGenerator vg = frame.stack[argCount].getValue().creator();
        				
            			if( vg instanceof Instruction.NewObject ) { 
    		    			Instruction.NewObject newObj = (Instruction.NewObject) vg;
    		    			newObjects.get( newObj ).invoke = invoke;
            			}	
        			}
        		}
        		
        		Frame newFrame = insn.compute( frame );
    			Collection<Instruction> subseqs = insn.subsequents();
    			
    			for( Instruction i : subseqs ) {
					if( i.frame == null ) {						
						if( i instanceof CodeLabel ) {
							CodeLabel label = (CodeLabel) i;
							
							//if the label only has one incoming arc then
							//use the incoming frame as-is, otherwise make a
							//copy to allow merging the values coming in from
							//the other arcs							
							if( label.incomingArcs().size() > 1 ) {
								newFrame = new Frame( newFrame ); //copy
							}							
						}						
						
						queue.add( new IncomingFrame( newFrame, i ) );
					}
					else if( i instanceof CodeLabel ) {
					    
						//merge the incoming values with the existing ones
						Slot[] stack  = i.frame.stack;
						Slot[] locals = i.frame.locals;
						
						Slot[] newStack  = newFrame.stack;
						Slot[] newLocals = newFrame.locals;						
						
						for( int j = 0; j < stack.length; j++ ) {
							Slot s = (newStack.length > j) ? newStack[j] : null;
							
							if( stack[j] != null && stack[j].getValue() != null ) {
								if( s == null || s.getValue() == null ) {
									stack[j].setValue( null ); 
								}
								else {
									stack[j].setValue( 
										Value.merge( stack[j].getValue(), 
												     s.getValue() ));
								}
							}
						}			
						
						for( int j = 0; j < locals.length; j++ ) {
							Slot s = (newLocals.length > j) ? newLocals[j] : null;
							
							if( locals[j] != null && locals[j].getValue() != null ) {
								if( s == null || s.getValue() == null ) {
									locals[j].setValue( null ); 
								}
								else {
									locals[j].setValue( 
										Value.merge( locals[j].getValue(), 
												     s.getValue() ));
								}
							}
						}	
					}
				}
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
