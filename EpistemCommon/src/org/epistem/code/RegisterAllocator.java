package org.epistem.code;

import java.util.*;

/**
 * Allocates registers to local values
 *
 * @author nickmain
 */
public class RegisterAllocator<INSTRUCTION_TYPE,FRAME_TYPE> {

    
    private final ControlFlowGraph<INSTRUCTION_TYPE, FRAME_TYPE> cfg;
    
    private final Map<InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE>, 
                      Set<InterferenceNode<INSTRUCTION_TYPE>>> aliveValues =
                          new HashMap<InstructionNode<INSTRUCTION_TYPE,FRAME_TYPE>, Set<InterferenceNode<INSTRUCTION_TYPE>>>();
    
    public RegisterAllocator( ControlFlowGraph<INSTRUCTION_TYPE, FRAME_TYPE> cfg ) {
        this.cfg = cfg;
    }
    
    /**
     * Perform the allocation
     * 
     * @param reservedRegisters the registers that are reserved
     * @return maximum register that was allocated
     */
    public int allocate( Set<Integer> reservedRegisters ) {        
        Set<InterferenceNode<INSTRUCTION_TYPE>> inodes = buildInterferenceGraph();

        int maxReg = 0;
        
        for( InterferenceNode<INSTRUCTION_TYPE> inode : inodes ) {
            LocalValue<INSTRUCTION_TYPE> value = inode.local;
            if( value.allocatedRegister >= 0 ) continue; //already allocated

            //gather regs already taken
            Set<Integer> takenRegs = new HashSet<Integer>();
            takenRegs.addAll( reservedRegisters );
            
            for( InterferenceNode<INSTRUCTION_TYPE> otherINode : inode.concurrentValues ) {
                int otherReg = otherINode.local.allocatedRegister;
                if( otherReg >= 0 ) {
                    takenRegs.add( otherReg );
                }
            }
            
            //look for the first free register
            int reg = 0;
            while( true ) {
                if( takenRegs.contains( reg ) ) {
                    reg++;
                    continue;
                }
                break;
            }
            
            value.allocatedRegister = reg;
            maxReg = Math.max( reg, maxReg );
        }
        
        return maxReg;
    }
    
    /**
     * Build the value interference graph
     * 
     * @return null if there are no locals
     */
    private Set<InterferenceNode<INSTRUCTION_TYPE>> buildInterferenceGraph() {
        Collection<LocalValue<INSTRUCTION_TYPE>> locals = cfg.locals;
        
        Set<InterferenceNode<INSTRUCTION_TYPE>> inodes = new HashSet<InterferenceNode<INSTRUCTION_TYPE>>();
        
        for( LocalValue<INSTRUCTION_TYPE> val : locals ) {
            InterferenceNode<INSTRUCTION_TYPE> inode = new InterferenceNode<INSTRUCTION_TYPE>( val );
            inodes.add( inode );
            findValueLifespan( inode );
        }
        
        //--cross link the InterferenceNodes
        for( Set<InterferenceNode<INSTRUCTION_TYPE>> values : aliveValues.values() ) {
            for( InterferenceNode<INSTRUCTION_TYPE> valA : values ) {
                for( InterferenceNode<INSTRUCTION_TYPE> valB : values ) {
                    if( valA == valB ) continue;
                    
                    valA.concurrentValues.add( valB );
                }                
            }
        }
        
        //--notify the adaptor of lifespan ends
        for( InterferenceNode<INSTRUCTION_TYPE> inode : inodes ) {
            notifyOfLifespanEnds( inode );
        }
        
        return inodes;
    }
    
    private void notifyOfLifespanEnds( InterferenceNode<INSTRUCTION_TYPE> inode ) {

        for( INSTRUCTION_TYPE instruction : inode.local.getters ) {
            InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> node = cfg.nodes.get( instruction );
  
            //check each downstream instruction to see if the value is alive there
            boolean isAlive = false;
            for( InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> next : node.outgoingEdges.keySet() ) {
                Set<InterferenceNode<INSTRUCTION_TYPE>> values = aliveValues.get( next );
                                
                isAlive |=  (values != null) && values.contains( inode );
            }
            
            if( ! isAlive ) {
                cfg.adaptor.endOfLocalLifespan( instruction, inode.local );
            }            
        }
    }
    
    /**
     * Find the lifespan of the given local value.  Start with each getter
     * and assume that the value must alive on all the incoming edges. Chase
     * those edges to find a setter.
     */
    private void findValueLifespan( InterferenceNode<INSTRUCTION_TYPE> inode ) {
        
        for( INSTRUCTION_TYPE instruction : inode.local.getters ) {
            InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> node = cfg.nodes.get( instruction );
            
            LinkedList<InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE>> queue = 
                new LinkedList<InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE>>(); 
            
            addValueAliveInstruction( node, inode );
            queue.add( node );
            
            //process queue until setters are reached
            while( ! queue.isEmpty() ) {
                
                node = queue.removeFirst();
                
                //register all incoming edges as places where the value is alive
                for( InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> inNode : node.incomingEdges.keySet()){
                    if( inNode == null ) continue;
                    if( addValueAliveInstruction( inNode, inode )) continue; //continue if already registered - avoid loops
                    
                    //enqueue the upstream instruction to be chased unless it is a setter of the value
                    if( inode.local.setters.contains( inNode.instruction ) ) continue;
                    queue.add( inNode );
                }
            }
        }
    }
 
    /**
     * Register that a given local value is alive at the given instruction
     * 
     * @return true if the value was already registered
     */
    private boolean addValueAliveInstruction( InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> node,
                                              InterferenceNode<INSTRUCTION_TYPE> inode ) {
        
        Set<InterferenceNode<INSTRUCTION_TYPE>> values = aliveValues.get( node );
        if( values == null ) {
            values = new HashSet<InterferenceNode<INSTRUCTION_TYPE>>();
            aliveValues.put( node, values );
        }
        
        if( values.contains( inode )) return true;
        
        values.add( inode );        
        return false;
    }
}
