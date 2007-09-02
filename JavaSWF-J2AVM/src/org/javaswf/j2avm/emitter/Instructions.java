package org.javaswf.j2avm.emitter;

import org.objectweb.asm.Label;

/**
 * Interface for passing bytecode instructions.
 * 
 * done() is called when all instructions have been passed.
 * 
 * @author nmain
 */
public interface Instructions {

    /**
     * Called when done passing instructions through this instance of the
     * interface.
     */
    public void done();
    
    /**
     * A label that can be used as the target of branches.
     */
    public void label( Label label );
    
    public void nop();
    public void aconst_null();
    public void iconst_m1();
    public void iconst_0();
    public void iconst_1();
    public void iconst_2();
    public void iconst_3();
    public void iconst_4();
    public void iconst_5();
    public void lconst_0();
    public void lconst_1();
    public void fconst_0();
    public void fconst_1();
    public void fconst_2();
    public void dconst_0();
    public void dconst_1();
    public void bipush( int value );
    public void sipush( int value );
    public void ldc( int constantIndex );
    public void ldc_w( int constantIndex );
    public void ldc2_w( int constantIndex );
    public void iload( int localVar );
    public void lload( int localVar );
    public void fload( int localVar );
    public void dload( int localVar );
    public void aload( int localVar );
    public void iload_0();
    public void iload_1();
    public void iload_2();
    public void iload_3();
    public void lload_0();
    public void lload_1();
    public void lload_2();
    public void lload_3();
    public void fload_0();
    public void fload_1();
    public void fload_2();
    public void fload_3();
    public void dload_0();
    public void dload_1();
    public void dload_2();
    public void dload_3();
    public void aload_0();
    public void aload_1();
    public void aload_2();
    public void aload_3();
    public void iaload();
    public void laload();
    public void faload();
    public void daload();
    public void aaload();
    public void baload();
    public void caload();
    public void saload();
    public void istore( int localVar );
    public void lstore( int localVar );
    public void fstore( int localVar );
    public void dstore( int localVar );
    public void astore( int localVar );
    public void istore_0();
    public void istore_1();
    public void istore_2();
    public void istore_3();
    public void lstore_0();
    public void lstore_1();
    public void lstore_2();
    public void lstore_3();
    public void fstore_0();
    public void fstore_1();
    public void fstore_2();
    public void fstore_3();
    public void dstore_0();
    public void dstore_1();
    public void dstore_2();
    public void dstore_3();
    public void astore_0();
    public void astore_1();
    public void astore_2();
    public void astore_3();
    public void iastore();
    public void lastore();
    public void fastore();
    public void dastore();
    public void aastore();
    public void bastore();
    public void castore();
    public void sastore();
    public void pop();
    public void pop2();
    public void dup();
    public void dup_x1();
    public void dup_x2();
    public void dup2();
    public void dup2_x1();
    public void dup2_x2();
    public void swap();
    public void iadd();
    public void ladd();
    public void fadd();
    public void dadd();
    public void isub();
    public void lsub();
    public void fsub();
    public void dsub();
    public void imul();
    public void lmul();
    public void fmul();
    public void dmul();
    public void idiv();
    public void ldiv();
    public void fdiv();
    public void ddiv();
    public void irem();
    public void lrem();
    public void frem();
    public void drem();
    public void ineg();
    public void lneg();
    public void fneg();
    public void dneg();
    public void ishl();
    public void lshl();
    public void ishr();
    public void lshr();
    public void iushr();
    public void lushr();
    public void iand();
    public void land();
    public void ior();
    public void lor();
    public void ixor();
    public void lxor();
    public void iinc( int localVar, int increment );
    public void i2l();
    public void i2f();
    public void i2d();
    public void l2i();
    public void l2f();
    public void l2d();
    public void f2i();
    public void f2l();
    public void f2d();
    public void d2i();
    public void d2l();
    public void d2f();
    public void i2b();
    public void i2c();
    public void i2s();
    public void lcmp();
    public void fcmpl();
    public void fcmpg();
    public void dcmpl();
    public void dcmpg();
    public void ifeq( int label );
    public void ifne( int label );
    public void iflt( int label );
    public void ifge( int label );
    public void ifgt( int label );
    public void ifle( int label );
    public void if_icmpeq( int label );
    public void if_icmpne( int label );
    public void if_icmplt( int label );
    public void if_icmpge( int label );
    public void if_icmpgt( int label );
    public void if_icmple( int label );
    public void if_acmpeq( int label );
    public void if_acmpne( int label );
    public void goto_( int label );
    public void jsr( int label );
    public void ret( int localVar );
    public void tableswitch( int defaultLabel, int lowIndex, int[] jumpTable );
    public void lookupswitch( int defaultLabel, int[] caseValues, int[] caseTargets );
    public void ireturn();
    public void lreturn();
    public void freturn();
    public void dreturn();
    public void areturn();
    public void vreturn();
    public void getstatic( int fieldIndex );
    public void putstatic( int fieldIndex );
    public void getfield( int fieldIndex );
    public void putfield( int fieldIndex );
    public void invokevirtual( int methodIndex );
    public void invokespecial( int methodIndex );
    public void invokestatic( int methodIndex );
    public void invokeinterface( int methodIndex, int count );
    public void newObject( int classIndex );
    public void newarray( int primitiveType );
    public void anewarray( int classIndex );
    public void arraylength();
    public void athrow();
    public void checkcast( int classIndex );
    public void instanceOf( int classIndex );
    public void monitorenter();
    public void monitorexit();
    //public void wide(); -- NOT TO BE IMPLEMENTED
    public void multianewarray( int arrayIndex, int dimensionCount );
    public void ifnull( int label );
    public void ifnonnull( int label );
    public void goto_w( int label );
    public void jsr_w( int label );
}
