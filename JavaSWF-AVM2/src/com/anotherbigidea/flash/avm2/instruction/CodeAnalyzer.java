package com.anotherbigidea.flash.avm2.instruction;

import java.util.*;

import com.anotherbigidea.flash.avm2.model.AVM2ExceptionHandler;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;

/**
 * Walks all instructions in a list in order to determine the max values for
 * stack, registers and scope,  Also determines register use and allocates
 * register numbers to instances of RegisterAllocation.
 *
 * @author nickmain
 */
public class CodeAnalyzer {
    
    private int maxReg   = 0;
    private int maxStack = 0;
    private int maxScope = 0;
    
    /**
     * Visited instructions - map to the incoming frame
     */
    private final Map<Instruction,Frame> visited = new HashMap<Instruction, Frame>();
 
    /**
     * The visit queue
     */
    private final LinkedList<PendingVisit> queue = new LinkedList<PendingVisit>();
    
    /**
     * The frame size at a given point
     */
    private static class Frame {
        final int scopeStack;
        final int stack;
        final LocalRegisterState locals;
        Frame( int scopeStack, int stack, LocalRegisterState locals ) {
            this.scopeStack = scopeStack;
            this.stack      = stack;
            this.locals     = locals;
        }
    }
    
    /**
     * A pending visit to an instruction
     */
    private static class PendingVisit {
        final Instruction        instruction;
        final Frame              incomingFrame;
       public PendingVisit( Instruction instruction, Frame incomingFrame ) {
            this.incomingFrame = incomingFrame;
            this.instruction   = instruction;
        }
    }
    
    /**
     * Holds the state of the local registers at any point in time.
     */
    private static class LocalRegisterState {
        
        /**
         * true indicates that a register is in use, false that it is
         * free.
         */
        boolean[] registerAllocations;
        
        /**
         * Create an initial state where the first count registers are in use
         */
        public LocalRegisterState( int count ) {
            registerAllocations = new boolean[ count ];
            Arrays.fill( registerAllocations, true );
        }

        /**
         * Create a new register state by mutating an existing one
         * @param state the existing state
         * @param index the register to change
         * @param use whether to free (false) or allocate (true)
         */
        public LocalRegisterState( LocalRegisterState state, 
                                   int index, boolean use ) {
            registerAllocations = 
                new boolean[ 
                       Math.max( state.registerAllocations.length, 
                                 index + 1 ) ];
            
            System.arraycopy( state, 0, 
                              registerAllocations, 0, registerAllocations.length );
            
            registerAllocations[ index ] = use;
        }
    }
    
    private final AVM2MethodBody body;
    
    /**
     * @param body the method body to walk
     */
    public CodeAnalyzer( AVM2MethodBody body ) {
        this.body = body;
    }
    
    /**
     * Execute the walk and update the max values of the body 
     */
    public void analyze() {
        InstructionList list = body.instructions;
        if( list.size() == 0 ) return;
        
        //seed the queue
        queue.add( new PendingVisit( list.first(), new Frame( 0, 0 ) ) );
        
        boolean exceptionHandlersProcessed = false;
        
        while( true ) {
            while( ! queue.isEmpty() ) {
                PendingVisit visit = queue.removeFirst();
                
                Instruction ins = visit.instruction;
                if( visited.contains( ins ) ) continue; //skip already visited
                visited.add( ins );
                
                Frame in = visit.incomingFrame;
                
                //bump up the max register used
                int[] regs = ins.registersUsed();
                for( int reg : regs ) {
                    maxReg = Math.max( maxReg, reg );
                }
                
                //compute outgoing frame
                Frame out = new Frame(
                    in.scopeStack + ins.getScopePushCount() - ins.getScopePopCount(),
                    in.stack + ins.getPushCount() - ins.getPopCount()
                );
                
                //bump up the values
                maxScope = Math.max( maxScope, out.scopeStack );
                maxStack = Math.max( maxStack, out.stack );

//                System.out.print( ins.operation );
//                System.out.print( " in : " + in.stack + " " + in.scopeStack );
//                System.out.print( " out: " + out.stack + " " + out.scopeStack );                
//                System.out.println();
                
                //generate downstream visits
                for( Instruction i : ins.getFollowOnInstructions() ) {
                    if( i == null ) System.err.println( "NULL INSTRUCTION for " + ins.operation );
                    queue.add( new PendingVisit( i, out ) );
                }
            }
            
            if( exceptionHandlersProcessed ) break;
            
            //make sure that exception handlers are also included (since they
            //may not be reachable via normal execution flow)
            for( AVM2ExceptionHandler handler : body.exceptionHandlers ) {
                InstructionTarget target = list.targets.get( handler.handler );
                
                queue.add( new PendingVisit( target, new Frame( 0, 1 ) ) );
            }        
                
            exceptionHandlersProcessed = true;
        }
        
        //copy the results into the method body
        body.maxRegisters = Math.max( body.maxRegisters, maxReg + 1 );
        body.maxStack     = Math.max( body.maxStack, maxStack );
        body.maxScope     = Math.max( body.maxScope, body.scopeDepth + maxScope );
    }
}
