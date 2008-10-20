package com.anotherbigidea.flash.avm2.instruction;

import java.util.*;

import org.epistem.code.ControlFlowGraph;
import org.epistem.code.InstructionAdaptor;
import org.epistem.code.LocalValue;
import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.model.AVM2ExceptionHandler;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;

/**
 * Walks all instructions in a list in order to determine the max values for
 * stack, registers and scope,  Also determines register use and allocates
 * register numbers to instances of RegisterAllocation.
 *
 * @author nickmain
 */
public class AVM2CodeAnalyzer {
    
    private int maxStack = 0;
    private int maxScope = 0;
    
    private ControlFlowGraph<Instruction,Frame> cfGraph;
    
    private Map<LocalValue<Instruction>,Set<Instruction>> valueLifespanEnds =
        new HashMap<LocalValue<Instruction>,Set<Instruction>>();
    
    /**
     * The frame state at a given point
     */
    public static class Frame {
        final int scopeStack;
        final int stack;
       // final Set<RegisterAllocation> allocations;
        Frame( int scopeStack, int stack /*, Set<RegisterAllocation> allocations */ ) {
            this.scopeStack  = scopeStack;
            this.stack       = stack;
           // this.allocations = allocations;
        }
    }
       
    private final AVM2MethodBody body;
    
    /**
     * @param body the method body to walk
     */
    public AVM2CodeAnalyzer( AVM2MethodBody body ) {
        this.body = body;
    }
    
    /**
     * Execute the walk and update the max values of the body 
     */
    public void analyze( Set<Integer> reservedRegisters ) {
        
        cfGraph = buildGraph();
        
        body.maxScope = body.scopeDepth + maxScope;
        body.maxStack = maxStack;
        
        //make sure max regs reflects the reserved regs
        for( int i : reservedRegisters ) {
            body.maxRegisters = Math.max( body.maxRegisters, i + 1 );
        }
        
        body.maxRegisters = Math.max( body.maxRegisters, 1 + cfGraph.allocateRegisters( reservedRegisters ));
        killValues();
    }
    
    /**
     * Insert kill instructions after the final instruction in local value
     * lifespans to make the AVM2 verifier less cranky about conflicting
     * types in registers.
     */
    private void killValues() {
        for( LocalValue<Instruction> value : valueLifespanEnds.keySet() ) {
            for( Instruction death : valueLifespanEnds.get( value ) ) {
                body.instructions.insertAfter( new Instruction( Operation.OP_kill, value ), death );
            }
        }
    }
    
    private ControlFlowGraph<Instruction,Frame> buildGraph() {
        
        ControlFlowGraph<Instruction,Frame> cfg = 
            new ControlFlowGraph<Instruction, Frame>( 
                    new Adaptor(),
                    body.instructions.first(), 
                    new Frame(0,0) );
     
        //--add the exception handlers
        for( AVM2ExceptionHandler handler : body.exceptionHandlers ) {
            
            InstructionTarget tryStart     = body.instructions.targets.get( handler.start );
            InstructionTarget tryEnd       = body.instructions.targets.get( handler.end );
            InstructionTarget handlerStart = body.instructions.targets.get( handler.handler );
            
            for( Instruction i = tryStart; i != tryEnd; i = i.next() ) {
                Frame frame = new Frame( 0, 1 );
                cfg.addExcepionHandlerFlow( i, handlerStart, frame );                
            }
        }
        
        return cfg;
    }
    
    private class Adaptor implements InstructionAdaptor<Instruction,Frame> {

        /** @see org.epistem.code.InstructionAdaptor#computeFrame(java.lang.Object, java.util.Collection) */
        public Frame computeFrame( Instruction instruction, Collection<Frame> incomingFrames ) {

            int maxFrameScope = 0;
            int maxFrameStack = 0;
            
            for( Frame f : incomingFrames ) {
                maxFrameScope = Math.max( maxFrameScope, f.scopeStack );
                maxFrameStack = Math.max( maxFrameStack, f.stack );
            }
            
            Frame out = new Frame(
                maxFrameScope + instruction.getScopePushCount() - instruction.getScopePopCount(),
                maxFrameStack + instruction.getPushCount() - instruction.getPopCount()
            );
            
            maxScope = Math.max( maxScope, out.scopeStack );
            maxStack = Math.max( maxStack, out.stack );
            
            return out;
        }

        /** @see org.epistem.code.InstructionAdaptor#getFollowOnInstructions(java.lang.Object) */
        public Collection<Instruction> getFollowOnInstructions( Instruction instruction ) {
            return instruction.getFollowOnInstructions();
        }

        /** @see org.epistem.code.InstructionAdaptor#gatherReferencedLocals(java.lang.Object, java.util.Collection) */
        public void gatherReferencedLocals( Instruction instruction, Collection<LocalValue<Instruction>> locals ) {
            //instruction.dump( IndentingPrintWriter.SYSERR ); IndentingPrintWriter.SYSERR.flush();
            instruction.gatherReferencedLocals( locals );            
        }

        /** @see org.epistem.code.InstructionAdaptor#endOfLocalLifespan(java.lang.Object, org.epistem.code.LocalValue) */
        public void endOfLocalLifespan(Instruction instruction, LocalValue<Instruction> local) {
            
            //save the value life ends for later kill insertion
            Set<Instruction> lifeEnds = valueLifespanEnds.get( local );
            if( lifeEnds == null ) {
                lifeEnds = new HashSet<Instruction>();
                valueLifespanEnds.put( local, lifeEnds );
            }
            
            lifeEnds.add( instruction );
        }        
    }
}
