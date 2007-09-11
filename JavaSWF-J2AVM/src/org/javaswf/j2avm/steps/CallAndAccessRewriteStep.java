package org.javaswf.j2avm.steps;

import static org.javaswf.j2avm.util.ASMUtils.externalName;
import static org.javaswf.j2avm.util.ASMUtils.getMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.javaswf.j2avm.JavaClass;
import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

/**
 * A translation step that passes method call, field access and new object
 * instructions to a number of rewriters for potential alteration or 
 * substitution.
 *
 * @author nickmain
 */
public class CallAndAccessRewriteStep implements TranslationStep {

    protected JavaClass javaClass;
    protected TranslationContext context;
    
    private final CallAndAccessRewriter[] rewriters;
    private boolean abortVisit;
    private AbstractInsnNode instruction; //the instruction being visited
    private InsnList         instructions; //the current instruction list
    
    /**
     * @param rewriters the rewriters to delegate to
     */
    public CallAndAccessRewriteStep( CallAndAccessRewriter...rewriters ) {
        this.rewriters = rewriters;
    }
    
    /**
     * @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.JavaClass, org.javaswf.j2avm.TranslationContext)
     */
    public boolean process(JavaClass javaClass, TranslationContext context) {
        
        this.context   = context;
        this.javaClass = javaClass;
        
        for( CallAndAccessRewriter rewriter : rewriters ) {
            rewriter.init( this, javaClass, context );
        }
        
        MethodVisitor visitor = new InstructionVisitor();
        
        //pass each method to the visitor
        @SuppressWarnings("unchecked")
        List<MethodNode> methods = (List<MethodNode>) javaClass.node.methods;
        for( MethodNode method : methods ) {
            visitInstructions( method, visitor );
        }
        
        return true;
    }

    /**
     * Visit the instructions of the given method
     */
    private void visitInstructions( MethodNode method, MethodVisitor visitor ) {
        instructions = method.instructions;
        
        instruction = instructions.getFirst();
        AbstractInsnNode nextInsn = (instruction != null) ? 
                                        instruction.getNext() :
                                        null;
                
        while( instruction != null ) {
            abortVisit = false;
            
            instruction.accept( visitor );

            //note: visit proceeds with the previously known next instruction.
            //      This means that any instructions inserted before or after
            //      the current instruction will be skipped.
            instruction = nextInsn;
            nextInsn    = (instruction != null) ? 
                              instruction.getNext() :
                              null;            
        }
    }
    
    /**
     * Callback from the CallAndAccessRewriter to indicate that the current
     * operation has been replaced and the visit should be cut short 
     */
    /*pkg*/ void cancelVisit() {
        abortVisit = true;
    }
    
    private class InstructionVisitor extends EmptyVisitor {

        /** @see org.objectweb.asm.commons.EmptyVisitor#visitFieldInsn(int, java.lang.String, java.lang.String, java.lang.String) */
        @Override
        public void visitFieldInsn( int opcode, String owner, String name, String desc ) {
            String className = externalName( owner );
            Type   fieldType = Type.getType( desc );
            
            switch( opcode ) {
                case Opcodes.GETSTATIC:
                    for( CallAndAccessRewriter rewriter : rewriters ) {
                        rewriter.setInstruction( instructions, instruction );
                        rewriter.rewriteStaticGet( className, name, fieldType );
                        if( abortVisit ) return;
                    }
                    return;
                    
                case Opcodes.PUTSTATIC:
                    for( CallAndAccessRewriter rewriter : rewriters ) {
                        rewriter.setInstruction( instructions, instruction );
                        rewriter.rewriteStaticSet( className, name, fieldType );
                        if( abortVisit ) return;
                    }
                    return;
                    
                case Opcodes.GETFIELD:
                    for( CallAndAccessRewriter rewriter : rewriters ) {
                        rewriter.setInstruction( instructions, instruction );
                        rewriter.rewriteFieldGet( className, name, fieldType );
                        if( abortVisit ) return;
                    }
                    return;
                    
                case Opcodes.PUTFIELD:
                    for( CallAndAccessRewriter rewriter : rewriters ) {
                        rewriter.setInstruction( instructions, instruction );
                        rewriter.rewriteFieldSet( className, name, fieldType );
                        if( abortVisit ) return;
                    }
                    return;
                    
                default: return;
            }
        }

        /** @see org.objectweb.asm.commons.EmptyVisitor#visitMethodInsn(int, java.lang.String, java.lang.String, java.lang.String) */
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc ) {
            String className  = externalName( owner );
            Type   returnType = Type.getReturnType( desc );
            Type[] paramTypes = Type.getArgumentTypes( desc );
            
            switch( opcode ) {
                case Opcodes.INVOKEINTERFACE: //fall-thru
                case Opcodes.INVOKEVIRTUAL:
                    for( CallAndAccessRewriter rewriter : rewriters ) {
                        rewriter.setInstruction( instructions, instruction );
                        rewriter.rewriteVirtualCall( className, name, 
                                                     returnType, paramTypes );
                        if( abortVisit ) return;
                    }
                    return;

                case Opcodes.INVOKESTATIC: 
                    for( CallAndAccessRewriter rewriter : rewriters ) {
                        rewriter.setInstruction( instructions, instruction );
                        rewriter.rewriteStaticCall( className, name, 
                                                    returnType, paramTypes );
                        if( abortVisit ) return;
                    }
                    return;

                case Opcodes.INVOKESPECIAL:
                    if( name.equals( "<init>" ) ) { //constructor call
                        for( CallAndAccessRewriter rewriter : rewriters ) {
                            rewriter.setInstruction( instructions, instruction );
                            rewriter.rewriteConstructorCall( className, paramTypes );
                            if( abortVisit ) return;
                        }
                        return;
                    }

                    //is this a private call ?
                    Method meth = getMethod( javaClass, owner, name, desc );
                    if( Modifier.isPrivate( meth.getModifiers() )) {
                        for( CallAndAccessRewriter rewriter : rewriters ) {
                            rewriter.setInstruction( instructions, instruction );
                            rewriter.rewritePrivateCall( className, name, 
                                                         returnType, paramTypes );
                            if( abortVisit ) return;
                        }
                        return;                        
                    }
                    
                    //must be a super call
                    for( CallAndAccessRewriter rewriter : rewriters ) {
                        rewriter.setInstruction( instructions, instruction );
                        rewriter.rewriteSuperCall( className, name, 
                                                   returnType, paramTypes );
                        if( abortVisit ) return;
                    }
                    return;                        
                    
                    
                default: return;
            }
        }

        /** @see org.objectweb.asm.commons.EmptyVisitor#visitTypeInsn(int, java.lang.String) */
        @Override
        public void visitTypeInsn( int opcode, String desc ) {
            
            String className = externalName( desc );
            
            if( opcode == Opcodes.NEW ) {
                for( CallAndAccessRewriter rewriter : rewriters ) {
                    rewriter.setInstruction( instructions, instruction );
                    rewriter.rewriteNew( className );
                    if( abortVisit ) return;
                }
            }
        }
    }    
}
