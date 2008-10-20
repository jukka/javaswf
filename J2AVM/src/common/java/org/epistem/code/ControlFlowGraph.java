package org.epistem.code;

import java.util.*;

/**
 * A control-flow graph
 *
 * @author nickmain
 */
public class ControlFlowGraph<INSTRUCTION_TYPE,FRAME_TYPE> {

    /**
     * Map from instruction to node
     */
    /*pkg*/ Map<INSTRUCTION_TYPE, InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE>> nodes =
        new HashMap<INSTRUCTION_TYPE, InstructionNode<INSTRUCTION_TYPE,FRAME_TYPE>>();
    
    private final InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> firstNode;
    final InstructionAdaptor<INSTRUCTION_TYPE,FRAME_TYPE> adaptor;
    
    final Set<LocalValue<INSTRUCTION_TYPE>> locals = new HashSet<LocalValue<INSTRUCTION_TYPE>>();
    
    /**
     * @param instruction the initial instruction
     * @param initialFrame the initial incoming frame
     */
    public ControlFlowGraph( InstructionAdaptor<INSTRUCTION_TYPE,FRAME_TYPE> adaptor,
                             INSTRUCTION_TYPE instruction, FRAME_TYPE initialFrame ) {
        this.adaptor = adaptor;
        firstNode = makeNode( instruction );
        makeEdge( null, firstNode, initialFrame );                
        buildGraph( firstNode, initialFrame );
    }
    
    /**
     * Add flow from an instruction to an exception handler that covers it.
     * 
     * @param instruction the covered instruction
     * @param handler the first instruction of the exception handler
     * @param frame the frame for the new edge
     */
    public void addExcepionHandlerFlow( INSTRUCTION_TYPE instruction, 
                                        INSTRUCTION_TYPE handler,
                                        FRAME_TYPE frame ) {
        
        InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> handlerNode = makeNode( handler );
        
        makeEdge( makeNode( instruction ), handlerNode, frame );        
        buildGraph( handlerNode, frame );
    }
    
    /**
     * Perform register allocation
     * 
     * @param reservedRegisters the reserved registers
     * @return the highest allocated register
     */
    public int allocateRegisters( Set<Integer> reservedRegisters ) {
        return new RegisterAllocator<INSTRUCTION_TYPE, FRAME_TYPE>( this ).allocate( reservedRegisters );
    }
    
    private void makeEdge( InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> start,
                           InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> end,
                           FRAME_TYPE frame ) {
        
        if( start != null ) start.outgoingEdges.put( end, frame );
        if( end   != null ) end  .incomingEdges.put( start, frame );      
    }
    
    private InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> makeNode( INSTRUCTION_TYPE instruction ) {
        InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> node = nodes.get( instruction );
        
        if( node == null ) {            
            node = new InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE>( instruction );
            nodes.put( instruction, node );
        }
        
        return node;
    }
    
    /**
     * Build the graph starting with the given node and incoming frame
     */
    private void buildGraph( InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> node,
                             FRAME_TYPE frame ) {

        if( ! makeOutgoingEdges( node, frame )) return;
        
        //--the queue of visits to make
        LinkedList< InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE>> queue = 
            new LinkedList<InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE>>();
        
        queue.addAll( node.outgoingEdges.keySet() );

        while( ! queue.isEmpty()) {
            
            node = queue.removeFirst();
            if( node == null ) continue; //skip terminal edges
            
            adaptor.gatherReferencedLocals( node.instruction, locals );
            
            if( makeOutgoingEdges( node, frame ) ) {
                queue.addAll( node.outgoingEdges.keySet() );
            }
        }
    }
    
    /**
     * Generate the set of outgoing edges for a given node
     * 
     * @return false if the node has already been visited and there are no
     *               new outgoing edges
     */
    private boolean makeOutgoingEdges( InstructionNode<INSTRUCTION_TYPE, FRAME_TYPE> node,
                                       FRAME_TYPE frame ) {
        if( ! node.outgoingEdges.isEmpty() ) return false;
        
        FRAME_TYPE frameOut = adaptor.computeFrame( node.instruction, node.incomingEdges.values() );
        
        Collection<INSTRUCTION_TYPE> follows = adaptor.getFollowOnInstructions( node.instruction );
        
        //if this is a terminal instruction then add an outgoing edge to nowhere
        if( follows.isEmpty() ) {
            makeEdge( node, null, frame );
            return false;
        }
        
        for( INSTRUCTION_TYPE instruction : follows ) {
            makeEdge( node, makeNode( instruction ), frameOut );
        }
        
        return true;
    }
}
