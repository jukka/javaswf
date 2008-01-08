package org.epistem.code;

import java.util.HashMap;
import java.util.Map;

/**
 * A node in a control-flow graph
 *
 * @author nickmain
 */
public class InstructionNode<INSTRUCTION_TYPE,FRAME_TYPE> {

    public final INSTRUCTION_TYPE instruction;
    
    /*pkg*/ final Map<InstructionNode<INSTRUCTION_TYPE,FRAME_TYPE>,FRAME_TYPE> incomingEdges = new HashMap<InstructionNode<INSTRUCTION_TYPE,FRAME_TYPE>,FRAME_TYPE>();
    /*pkg*/ final Map<InstructionNode<INSTRUCTION_TYPE,FRAME_TYPE>,FRAME_TYPE> outgoingEdges = new HashMap<InstructionNode<INSTRUCTION_TYPE,FRAME_TYPE>,FRAME_TYPE>();
    
    public InstructionNode( INSTRUCTION_TYPE instruction ) {
        this.instruction = instruction;
    }
}
