package org.javaswf.j2avm.emitter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.javaswf.j2avm.JavaClass;
import org.javaswf.j2avm.runtime.annotations.Getter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

import static org.javaswf.j2avm.util.ASMUtils.*;
import static org.javaswf.j2avm.emitter.EmitterUtils.*;

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

    //true if the previous instruction was an abrupt control flow such as
    //a goto or throw
    private boolean lastWasAbrupt = false;  
    private boolean abrupt = false; //used to signal abrupt flow
    
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

        abrupt = false;
        
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
        
        lastWasAbrupt = abrupt;
        abrupt = false;
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
        switch( opcode ) {        
            case Opcodes.GETFIELD:
                Type fieldType = Type.getType( desc );
                
                avm2Code.append( Operation.OP_getproperty, nameForField( name ));
                avm2Code.append( Operation.OP_coerce, new AVM2QName( fieldType.getClassName()));
                return;
                
            case Opcodes.PUTFIELD:
                avm2Code.append( Operation.OP_setproperty, nameForField( name ));
                return;
                
                //TODO:
            case Opcodes.GETSTATIC:
            case Opcodes.PUTSTATIC:   
            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }   
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
        if( increment == 1 ) {
            avm2Code.append( Operation.OP_inclocal_i, var ); 
            return;
        }
        
        //simulate the increment by an addition
        avm2Code.loadLocalVar( var );        
        
        if( increment <= 127 || increment >= -128 ) {
            avm2Code.append( Operation.OP_pushbyte, increment );             
        }
        else if( increment <= Short.MAX_VALUE || increment >= Short.MIN_VALUE ) {
            avm2Code.append( Operation.OP_pushshort, increment );
        }
        else {
            avm2Code.append( Operation.OP_pushint, increment );            
        }

        avm2Code.append( Operation.OP_add_i );
        avm2Code.storeLocalVar( var );        
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitInsn(int)
     */
    @Override
    public void visitInsn(int opcode) {
        switch( opcode ) {
            case Opcodes.NOP: avm2Code.append( Operation.OP_nop ); return;
                
            case Opcodes.ACONST_NULL: avm2Code.append( Operation.OP_pushnull ); return;
            
            case Opcodes.ICONST_M1: avm2Code.append( Operation.OP_pushbyte, -1 ); return;
            case Opcodes.ICONST_0:  avm2Code.append( Operation.OP_pushbyte, 0  ); return;
            case Opcodes.ICONST_1:  avm2Code.append( Operation.OP_pushbyte, 1  ); return;
            case Opcodes.ICONST_2:  avm2Code.append( Operation.OP_pushbyte, 2  ); return;
            case Opcodes.ICONST_3:  avm2Code.append( Operation.OP_pushbyte, 3  ); return;
            case Opcodes.ICONST_4:  avm2Code.append( Operation.OP_pushbyte, 4  ); return;
            case Opcodes.ICONST_5:  avm2Code.append( Operation.OP_pushbyte, 5  ); return;
            
            //Longs are treated as doubles (Flash class Number)
            case Opcodes.LCONST_0: avm2Code.append( Operation.OP_pushdouble, 0 ); return;
            case Opcodes.LCONST_1: avm2Code.append( Operation.OP_pushdouble, 1 ); return;
            
            case Opcodes.FCONST_0: avm2Code.append( Operation.OP_pushdouble, 0.0 ); return;
            case Opcodes.FCONST_1: avm2Code.append( Operation.OP_pushdouble, 1.0 ); return;
            case Opcodes.FCONST_2: avm2Code.append( Operation.OP_pushdouble, 2.0 ); return;
            case Opcodes.DCONST_0: avm2Code.append( Operation.OP_pushdouble, 0.0 ); return;
            case Opcodes.DCONST_1: avm2Code.append( Operation.OP_pushdouble, 1.0 ); return;
                
            //TODO: array operations
            case Opcodes.IALOAD:
            case Opcodes.LALOAD:
            case Opcodes.FALOAD:
            case Opcodes.DALOAD:
            case Opcodes.AALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
                throw new RuntimeException( "Unhandled array operation " + opcode );
                
            case Opcodes.POP:  avm2Code.append( Operation.OP_pop ); return;
            case Opcodes.DUP:  avm2Code.append( Operation.OP_dup ); return;
            case Opcodes.SWAP: avm2Code.append( Operation.OP_swap ); return;
            
            //TODO: stack operations (require frame info)
            case Opcodes.POP2:
            case Opcodes.DUP_X1:
            case Opcodes.DUP_X2:
            case Opcodes.DUP2:
            case Opcodes.DUP2_X1:
            case Opcodes.DUP2_X2:
                throw new RuntimeException( "Unhandled stack operation " + opcode );
                
            case Opcodes.IRETURN: //fall-thru
            case Opcodes.LRETURN: //fall-thru
            case Opcodes.FRETURN: //fall-thru
            case Opcodes.DRETURN: //fall-thru
            case Opcodes.ARETURN: avm2Code.append( Operation.OP_returnvalue ); return;
            case Opcodes.RETURN:  avm2Code.append( Operation.OP_returnvoid ); return;                
                
            case Opcodes.I2D: avm2Code.append( Operation.OP_convert_d ); return;
                
            case Opcodes.F2D:
            case Opcodes.L2D:
                //floats and longs are already doubles - do nothing
                return;
            
            case Opcodes.IADD: avm2Code.append( Operation.OP_add_i ); return;
            case Opcodes.IMUL: avm2Code.append( Operation.OP_multiply_i ); return;
            
            case Opcodes.LADD:
            case Opcodes.FADD:
            case Opcodes.DADD:
            case Opcodes.ISUB:
            case Opcodes.LSUB:
            case Opcodes.FSUB:
            case Opcodes.DSUB:
            case Opcodes.LMUL:
            case Opcodes.FMUL:
            case Opcodes.DMUL:
            case Opcodes.IDIV:
            case Opcodes.LDIV:
            case Opcodes.FDIV:
            case Opcodes.DDIV:
            case Opcodes.IREM:
            case Opcodes.LREM:
            case Opcodes.FREM:
            case Opcodes.DREM:
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
            case Opcodes.ISHL:
            case Opcodes.LSHL:
            case Opcodes.ISHR:
            case Opcodes.LSHR:
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
            case Opcodes.IAND:
            case Opcodes.LAND:
            case Opcodes.IOR:
            case Opcodes.LOR:
            case Opcodes.IXOR:
            case Opcodes.LXOR:
            case Opcodes.I2L:
            case Opcodes.I2F:
            case Opcodes.L2I:
            case Opcodes.L2F:
            case Opcodes.F2I:
            case Opcodes.F2L:
            case Opcodes.D2I:
            case Opcodes.D2L:
            case Opcodes.D2F:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
            case Opcodes.LCMP:
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                            
            case Opcodes.ARRAYLENGTH:
            case Opcodes.ATHROW:    //TODO; abrupt = true;
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:        
            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitIntInsn(int, int)
     */
    @Override
    public void visitIntInsn(int opcode, int operand) {
        switch( opcode ) {
        
            case Opcodes.BIPUSH:
                avm2Code.append( Operation.OP_pushbyte, operand );
                return;                
                
            case Opcodes.SIPUSH:
                avm2Code.append( Operation.OP_pushshort, operand );
                return;                
                
                //TODO:
            case Opcodes.NEWARRAY:                
            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitJumpInsn(int, org.objectweb.asm.Label)
     */
    @Override
    public void visitJumpInsn(int opcode, Label label) {
        
        switch( opcode ) {

            case Opcodes.JSR:
                throw new RuntimeException( "JSR instruction is not permitted - please compile for Java 5+" );

            case Opcodes.GOTO: 
                avm2Code.append( Operation.OP_jump, label.hashCode() );
                abrupt = true;
                return;

            case Opcodes.IF_ICMPEQ: avm2Code.append( Operation.OP_ifeq, label.hashCode() ); return;
            case Opcodes.IF_ICMPNE: avm2Code.append( Operation.OP_ifne, label.hashCode() ); return;
            case Opcodes.IF_ICMPLT: avm2Code.append( Operation.OP_iflt, label.hashCode() ); return;
            case Opcodes.IF_ICMPGE: avm2Code.append( Operation.OP_ifge, label.hashCode() ); return;
            case Opcodes.IF_ICMPGT: avm2Code.append( Operation.OP_ifgt, label.hashCode() ); return;
            case Opcodes.IF_ICMPLE: avm2Code.append( Operation.OP_ifle, label.hashCode() ); return;
            case Opcodes.IF_ACMPEQ: avm2Code.append( Operation.OP_ifstricteq, label.hashCode() ); return;
            case Opcodes.IF_ACMPNE: avm2Code.append( Operation.OP_ifstrictne, label.hashCode() ); return;

                
        //TODO:
       
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
        
            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }        
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitLabel(org.objectweb.asm.Label)
     */
    @Override
    public void visitLabel(Label label) {
        
        avm2Code.appendLabel( label.hashCode() );

        //if the previous instruction was abrupt - such as a goto - then we need
        //to insert a label so that the AVM2 verifier do not think that this
        //code is unreachable (see "label" operation in AVM2 spec).
        if( lastWasAbrupt ) {
            avm2Code.append( Operation.OP_label );
        }
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitLdcInsn(java.lang.Object)
     */
    @Override
    public void visitLdcInsn( Object cst ) {
        
        if( cst instanceof Integer ) {
            avm2Code.append( Operation.OP_pushint, cst );
            return;
        }
        if( cst instanceof Float ) {
            avm2Code.append( Operation.OP_pushdouble, cst );
            return;
        }
        if( cst instanceof Long ) {
            avm2Code.append( Operation.OP_pushdouble, cst );
            return;
        }
        if( cst instanceof Double ) {
            avm2Code.append( Operation.OP_pushdouble, cst );
            return;
        }
        if( cst instanceof String ) {
            avm2Code.append( Operation.OP_pushstring, cst );
            return;
        }
        if( cst instanceof Type ) {
            //TODO: how to represent classes on the stack ?
        } 
     
        throw new RuntimeException( "Unhandled LDC type " + cst.getClass().getName() );
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
        
        int argCount = getArgCount( desc );        
        
        switch( opcode ) {
            case Opcodes.INVOKEINTERFACE: //fall-thru
            case Opcodes.INVOKEVIRTUAL: {
                                
                Operation op = isVoidMethod( desc ) ?
                                   Operation.OP_callpropvoid :
                                   Operation.OP_callproperty;
                
                avm2Code.append( op, nameForMethod( name ), argCount );
                                
                return;
            }
                
            case Opcodes.INVOKESPECIAL: {
                if( "<init>".equals( name ) ) { //constructor call
                    
                    String className = externalName( owner );
                    if( className.equals( javaClass.superclass.clazz.getName())) {                    
                        avm2Code.append( Operation.OP_constructsuper, argCount );
                    }
                    else {
                        //call to a constructor that is not the superclass.
                        
                        //TODO: for now skip this and just pop the object
                        avm2Code.append( Operation.OP_pop );                        
                    }
                                        
                } else { //super call or private call
                    
                    Method m = getMethod( javaClass, owner, name, desc ); 
                    if( Modifier.isPrivate( m.getModifiers() ) ) { //private

                        Operation op = isVoidMethod( desc ) ?
                                           Operation.OP_callpropvoid :
                                           Operation.OP_callproperty;
                         
                        avm2Code.append( op, nameForPrivateMethod( name ), argCount );
                        
                    }
                    else { //super call
                        
                    }
                    
                    //TODO:
                }
                return;
            }

            //TODO:
            case Opcodes.INVOKESTATIC:
            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }
        
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
        switch( opcode ) {
            case Opcodes.NEW:                
                //TODO: this code only supports nullary constructors
                Type      newType  = Type.getObjectType( desc );
                AVM2QName typeName = new AVM2QName( newType.getClassName() );

                avm2Code.append( Operation.OP_findpropstrict, typeName );
                avm2Code.append( Operation.OP_constructprop , typeName, 0 );
                avm2Code.append( Operation.OP_coerce        , typeName );
                return;
                
            case Opcodes.ANEWARRAY:
            case Opcodes.CHECKCAST:
            case Opcodes.INSTANCEOF:
            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }
    }

    /**
     * @see org.objectweb.asm.commons.EmptyVisitor#visitVarInsn(int, int)
     */
    @Override
    public void visitVarInsn(int opcode, int var) {
        
        switch( opcode ) {
            case Opcodes.RET:
                throw new RuntimeException( "RET instruction is not permitted - please compile for Java 5+" );

            case Opcodes.ILOAD: 
            case Opcodes.LLOAD: 
            case Opcodes.FLOAD: 
            case Opcodes.DLOAD: 
            case Opcodes.ALOAD: 
                avm2Code.loadLocalVar( var );
                return;

            case Opcodes.LSTORE: 
            case Opcodes.FSTORE: 
            case Opcodes.DSTORE: 
            case Opcodes.ISTORE: 
            case Opcodes.ASTORE:
                avm2Code.storeLocalVar( var );
                return;

            default: throw new RuntimeException( "Unhandled opcode " + opcode );
        }   
    }
}