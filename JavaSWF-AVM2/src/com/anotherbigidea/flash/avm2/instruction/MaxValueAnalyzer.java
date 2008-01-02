package com.anotherbigidea.flash.avm2.instruction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.anotherbigidea.flash.avm2.model.AVM2ExceptionHandler;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;

/**
 * Walks all instructions in a list in order to determine the max values for
 * stack, registers and scope,
 *
 * @author nickmain
 */
public class MaxValueAnalyzer {
    
    private int maxReg   = 0;
    private int maxStack = 0;
    private int maxScope = 0;
    
    /**
     * Visited instructions
     */
    private final Set<Instruction> visited = new HashSet<Instruction>();
 
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
        Frame( int scopeStack, int stack ) {
            this.scopeStack = scopeStack;
            this.stack      = stack;
        }
    }
    
    /**
     * A pending visit to an instruction
     */
    private static class PendingVisit {
        final Instruction instruction;
        final Frame       incomingFrame;
        public PendingVisit( Instruction instruction, Frame incomingFrame ) {
            this.incomingFrame = incomingFrame;
            this.instruction   = instruction;
        }
    }
    
    private final AVM2MethodBody body;
    
    /**
     * @param body the method body to walk
     */
    public MaxValueAnalyzer( AVM2MethodBody body ) {
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
