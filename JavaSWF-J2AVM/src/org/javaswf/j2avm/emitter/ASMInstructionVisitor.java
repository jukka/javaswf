package org.javaswf.j2avm.emitter;

import org.javaswf.j2avm.JavaClass;
import org.objectweb.asm.Label;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

/**
 * Visitor for ASM instructions for a single method
 *
 * @author nickmain
 */
public class ASMInstructionVisitor extends EmptyVisitor {

    private AbstractInsnNode instruction; //the instruction being visited
    private Frame            frame; //the frame of the instruction

    private final AVM2Code   avm2Code;
    private final MethodNode methodNode;
    private final JavaClass  javaClass;
    
    /**
     * @param javaClass the class being translated
     * @param methodNode the method being translated
     * @param avm2code the AVM2 code to target
     */
    /*pkg*/ ASMInstructionVisitor( JavaClass javaClass, 
                                   MethodNode methodNode, 
                                   AVM2Code avm2code ) {
        this.avm2Code   = avm2code;
        this.methodNode = methodNode;
        this.javaClass  = javaClass;
    }
    
    /**
     * Visit all the instructions in order
     */
    /*pkg*/ void visitAll() throws AnalyzerException {
        Analyzer anal = new Analyzer( new BasicInterpreter() );
        Frame[] frames = anal.analyze( javaClass.node.name, methodNode );

        visitCode();
        
        InsnList instructions = methodNode.instructions;
        int instructionCount = instructions.size();
        for( int i = 0; i < instructionCount; i++ ) {
            Frame frame = frames[i];
            AbstractInsnNode ain = instructions.get( i );
            visitInstruction( ain, frame );
        }
        
        visitEnd();
    }
    
    /**
     * Visit the given instruction
     *  
     * @param instruction the instruction to visit
     * @param frame the frame prior to the instruction execution
     */
    private void visitInstruction( AbstractInsnNode instruction, Frame frame ) {
        this.instruction = instruction;
        this.frame       = frame;
        instruction.accept( this );
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitCode()
     */
    @Override
    public void visitCode() {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitEnd()
     */
    @Override
    public void visitEnd() {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitFieldInsn(int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitFrame(int, int, java.lang.Object[], int, java.lang.Object[])
     */
    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitIincInsn(int, int)
     */
    @Override
    public void visitIincInsn(int var, int increment) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitInsn(int)
     */
    @Override
    public void visitInsn(int opcode) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitIntInsn(int, int)
     */
    @Override
    public void visitIntInsn(int opcode, int operand) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitJumpInsn(int, org.objectweb.asm.Label)
     */
    @Override
    public void visitJumpInsn(int opcode, Label label) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitLabel(org.objectweb.asm.Label)
     */
    @Override
    public void visitLabel(Label label) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitLdcInsn(java.lang.Object)
     */
    @Override
    public void visitLdcInsn(Object cst) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitLineNumber(int, org.objectweb.asm.Label)
     */
    @Override
    public void visitLineNumber(int line, Label start) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitLookupSwitchInsn(org.objectweb.asm.Label, int[], org.objectweb.asm.Label[])
     */
    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitMethodInsn(int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitMultiANewArrayInsn(java.lang.String, int)
     */
    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitTableSwitchInsn(int, int, org.objectweb.asm.Label, org.objectweb.asm.Label[])
     */
    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitTryCatchBlock(org.objectweb.asm.Label, org.objectweb.asm.Label, org.objectweb.asm.Label, java.lang.String)
     */
    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitTypeInsn(int, java.lang.String)
     */
    @Override
    public void visitTypeInsn(int opcode, String desc) {
        //TODO:
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitVarInsn(int, int)
     */
    @Override
    public void visitVarInsn(int opcode, int var) {
        //TODO:
    }
}