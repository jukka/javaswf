/****************************************************************
 * Copyright (c) 2003, David N. Main, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the 
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer. 
 * 
 * 2. Redistributions in binary form must reproduce the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * 3. The name of the author may not be used to endorse or 
 * promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************/

package org.epistem.java.classfile.bytecode;

import java.io.DataInput;
import java.io.IOException;

import org.epistem.java.classfile.resolved.ConstantPool;
import org.epistem.java.classfile.resolved.ResolvedAttributeReceiver;
import org.objectweb.asm.Opcodes;

/**
 * Parser for the Code attribute. 
 * 
 * Instruction labels are passed for every instruction and are the actual 
 * bytecode offsets.
 * 
 * @author nmain
 */
public class CodeParser {

    private ConstantPool mPool;

    /**
	 * @param pool
	 *            the constant pool used by the code being parsed.
	 */
    public CodeParser(ConstantPool pool) {
        mPool = pool;
    }

    /**
	 * Parse a Code attribute. The CodeParser instance may be reused to parse
	 * more than one code attribute - even concurrently.
	 */
    public void parse(DataInput data, CodeAttribute codeAttr) throws IOException {
        codeAttr.maxStack(data.readUnsignedShort());
        codeAttr.maxLocals(data.readUnsignedShort());

        int codeLength = data.readInt();

        Instructions instrs = codeAttr.instructions(codeLength);
        if (instrs != null) {
            parseInstructions(data, codeLength, instrs);
        } else {
            data.skipBytes(codeLength);
        }

        int exceptionTableCount = data.readUnsignedShort();
        ExceptionTableReceiver exrec =
            codeAttr.exceptionTable(exceptionTableCount);
        if (exrec != null) {
            for (int i = 0; i < exceptionTableCount; i++) {
                exrec.receiveExceptionHandler(
                    data.readUnsignedShort(),
                    data.readUnsignedShort(),
                    data.readUnsignedShort(),
                    mPool.getClassName(data.readUnsignedShort()));
            }
        } else {
            data.skipBytes(exceptionTableCount * 8);
        }

        int attrCount = data.readUnsignedShort();
        ResolvedAttributeReceiver attrrec = codeAttr.attributes(attrCount);

        for (int i = 0; i < attrCount; i++) {
            readAttr(attrrec, data);
        }
    }

    private void readAttr(ResolvedAttributeReceiver atts, DataInput data)  throws IOException {
        int nameIndex = data.readShort();
        int length = data.readInt();

        if (atts == null) {
            data.skipBytes(length);
            return;
        }

        boolean attrRead =
            atts.receiveAttribute(mPool.getUTF8Value(nameIndex), length, data);

        if (!attrRead) {
            data.skipBytes(length);
        }
    }

    private void parseInstructions( DataInput dataIn, int codeLength, Instructions instrs) throws IOException {
        OffsetMaintainingDataInput data = new OffsetMaintainingDataInput( dataIn );

        boolean isWide = false;
        
        while ( data.offset < codeLength) {
            if( ! isWide ) instrs.defineLabel( data.offset );
            int opcode = data.readUnsignedByte();

            switch (opcode) {
                case Opcodes.NOP :         instrs.nop(); break;
                case Opcodes.ACONST_NULL : instrs.aconst_null(); break;
                case Opcodes.ICONST_M1 :   instrs.iconst_m1(); break;
                case Opcodes.ICONST_0 :    instrs.iconst_0(); break;
                case Opcodes.ICONST_1 :    instrs.iconst_1(); break;
                case Opcodes.ICONST_2 :    instrs.iconst_2(); break;
                case Opcodes.ICONST_3 :    instrs.iconst_3(); break;
                case Opcodes.ICONST_4 :    instrs.iconst_4(); break;
                case Opcodes.ICONST_5 :    instrs.iconst_5(); break;
                case Opcodes.LCONST_0 :    instrs.lconst_0(); break;
                case Opcodes.LCONST_1 :    instrs.lconst_1(); break;
                case Opcodes.FCONST_0 :    instrs.fconst_0(); break;
                case Opcodes.FCONST_1 :    instrs.fconst_1(); break;
                case Opcodes.FCONST_2 :    instrs.fconst_2(); break;
                case Opcodes.DCONST_0 :    instrs.dconst_0(); break;
                case Opcodes.DCONST_1 :    instrs.dconst_1(); break;
                case Opcodes.BIPUSH :      instrs.bipush( data.readByte() ); break;
                case Opcodes.SIPUSH :      instrs.sipush( data.readShort() ); break;
                case Opcodes.LDC :         instrs.ldc( data.readUnsignedByte() ); break;
                case Opcodes.LDC_W :       instrs.ldc_w( data.readUnsignedShort() ); break;
                case Opcodes.LDC2_W :      instrs.ldc2_w( data.readUnsignedShort() ); break;
                case Opcodes.ILOAD :   instrs.iload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;  
                case Opcodes.LLOAD :   instrs.lload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.FLOAD :   instrs.fload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.DLOAD :   instrs.dload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.ALOAD :   instrs.aload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.ILOAD_0 : instrs.iload_0(); break;
                case Opcodes.ILOAD_1 : instrs.iload_1(); break;
                case Opcodes.ILOAD_2 : instrs.iload_2(); break;
                case Opcodes.ILOAD_3 : instrs.iload_3(); break;
                case Opcodes.LLOAD_0 : instrs.lload_0(); break;
                case Opcodes.LLOAD_1 : instrs.lload_1(); break;
                case Opcodes.LLOAD_2 : instrs.lload_2(); break;
                case Opcodes.LLOAD_3 : instrs.lload_3(); break;
                case Opcodes.FLOAD_0 : instrs.fload_0(); break;
                case Opcodes.FLOAD_1 : instrs.fload_1(); break;
                case Opcodes.FLOAD_2 : instrs.fload_2(); break;
                case Opcodes.FLOAD_3 : instrs.fload_3(); break;
                case Opcodes.DLOAD_0 : instrs.dload_0(); break;
                case Opcodes.DLOAD_1 : instrs.dload_1(); break;
                case Opcodes.DLOAD_2 : instrs.dload_2(); break;
                case Opcodes.DLOAD_3 : instrs.dload_3(); break;
                case Opcodes.ALOAD_0 : instrs.aload_0(); break;
                case Opcodes.ALOAD_1 : instrs.aload_1(); break;
                case Opcodes.ALOAD_2 : instrs.aload_2(); break;
                case Opcodes.ALOAD_3 : instrs.aload_3(); break;
                case Opcodes.IALOAD :  instrs.iaload(); break;
                case Opcodes.LALOAD :  instrs.laload(); break;
                case Opcodes.FALOAD :  instrs.faload(); break;
                case Opcodes.DALOAD :  instrs.daload(); break;
                case Opcodes.AALOAD :  instrs.aaload(); break;
                case Opcodes.BALOAD :  instrs.baload(); break;
                case Opcodes.CALOAD :  instrs.caload(); break;
                case Opcodes.SALOAD :  instrs.saload(); break;
                case Opcodes.ISTORE :  instrs.istore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.LSTORE :  instrs.lstore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.FSTORE :  instrs.fstore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.DSTORE :  instrs.dstore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.ASTORE :  instrs.astore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.ISTORE_0 : instrs.istore_0(); break;
                case Opcodes.ISTORE_1 : instrs.istore_1(); break;
                case Opcodes.ISTORE_2 : instrs.istore_2(); break;
                case Opcodes.ISTORE_3 : instrs.istore_3(); break;
                case Opcodes.LSTORE_0 : instrs.lstore_0(); break;
                case Opcodes.LSTORE_1 : instrs.lstore_1(); break;
                case Opcodes.LSTORE_2 : instrs.lstore_2(); break;
                case Opcodes.LSTORE_3 : instrs.lstore_3(); break;
                case Opcodes.FSTORE_0 : instrs.fstore_0(); break;
                case Opcodes.FSTORE_1 : instrs.fstore_1(); break;
                case Opcodes.FSTORE_2 : instrs.fstore_2(); break;
                case Opcodes.FSTORE_3 : instrs.fstore_3(); break;
                case Opcodes.DSTORE_0 : instrs.dstore_0(); break;
                case Opcodes.DSTORE_1 : instrs.dstore_1(); break;
                case Opcodes.DSTORE_2 : instrs.dstore_2(); break;
                case Opcodes.DSTORE_3 : instrs.dstore_3(); break;
                case Opcodes.ASTORE_0 : instrs.astore_0(); break;
                case Opcodes.ASTORE_1 : instrs.astore_1(); break;
                case Opcodes.ASTORE_2 : instrs.astore_2(); break;
                case Opcodes.ASTORE_3 : instrs.astore_3(); break;
                case Opcodes.IASTORE :  instrs.iastore(); break;
                case Opcodes.LASTORE :  instrs.lastore(); break;
                case Opcodes.FASTORE :  instrs.fastore(); break;
                case Opcodes.DASTORE :  instrs.dastore(); break;
                case Opcodes.AASTORE :  instrs.aastore(); break;
                case Opcodes.BASTORE :  instrs.bastore(); break;
                case Opcodes.CASTORE :  instrs.castore(); break;
                case Opcodes.SASTORE :  instrs.sastore(); break;
                case Opcodes.POP :      instrs.pop(); break;
                case Opcodes.POP2 :     instrs.pop2(); break;
                case Opcodes.DUP :      instrs.dup(); break;
                case Opcodes.DUP_X1 :   instrs.dup_x1(); break;
                case Opcodes.DUP_X2 :   instrs.dup_x2(); break;
                case Opcodes.DUP2 :     instrs.dup2(); break;
                case Opcodes.DUP2_X1 :  instrs.dup2_x1(); break;
                case Opcodes.DUP2_X2 :  instrs.dup2_x2(); break;
                case Opcodes.SWAP :     instrs.swap(); break;
                case Opcodes.IADD :     instrs.iadd(); break;
                case Opcodes.LADD :     instrs.ladd(); break;
                case Opcodes.FADD :     instrs.fadd(); break;
                case Opcodes.DADD :     instrs.dadd(); break;
                case Opcodes.ISUB :     instrs.isub(); break;
                case Opcodes.LSUB :     instrs.lsub(); break;
                case Opcodes.FSUB :     instrs.fsub(); break;
                case Opcodes.DSUB :     instrs.dsub(); break;
                case Opcodes.IMUL :     instrs.imul(); break;
                case Opcodes.LMUL :     instrs.lmul(); break;
                case Opcodes.FMUL :     instrs.fmul(); break;
                case Opcodes.DMUL :     instrs.dmul(); break;
                case Opcodes.IDIV :     instrs.idiv(); break;
                case Opcodes.LDIV :     instrs.ldiv(); break;
                case Opcodes.FDIV :     instrs.fdiv(); break;
                case Opcodes.DDIV :     instrs.ddiv(); break;
                case Opcodes.IREM :     instrs.irem(); break;
                case Opcodes.LREM :     instrs.lrem(); break;
                case Opcodes.FREM :     instrs.frem(); break;
                case Opcodes.DREM :     instrs.drem(); break;
                case Opcodes.INEG :     instrs.ineg(); break;
                case Opcodes.LNEG :     instrs.lneg(); break;
                case Opcodes.FNEG :     instrs.fneg(); break;
                case Opcodes.DNEG :     instrs.dneg(); break;
                case Opcodes.ISHL :     instrs.ishl(); break;
                case Opcodes.LSHL :     instrs.lshl(); break;
                case Opcodes.ISHR :     instrs.ishr(); break;
                case Opcodes.LSHR :     instrs.lshr(); break;
                case Opcodes.IUSHR :    instrs.iushr(); break;
                case Opcodes.LUSHR :    instrs.lushr(); break;
                case Opcodes.IAND :     instrs.iand(); break;
                case Opcodes.LAND :     instrs.land(); break;
                case Opcodes.IOR :      instrs.ior(); break;
                case Opcodes.LOR :      instrs.lor(); break;
                case Opcodes.IXOR :     instrs.ixor(); break;
                case Opcodes.LXOR :     instrs.lxor(); break;
                case Opcodes.IINC :     instrs.iinc( isWide ? data.readUnsignedShort() : data.readUnsignedByte(), isWide ? data.readShort() : data.readByte()); break;
                case Opcodes.I2L :      instrs.i2l(); break;
                case Opcodes.I2F :      instrs.i2f(); break;
                case Opcodes.I2D :      instrs.i2d(); break;
                case Opcodes.L2I :      instrs.l2i(); break;
                case Opcodes.L2F :      instrs.l2f(); break;
                case Opcodes.L2D :      instrs.l2d(); break;
                case Opcodes.F2I :      instrs.f2i(); break;
                case Opcodes.F2L :      instrs.f2l(); break;
                case Opcodes.F2D :      instrs.f2d(); break;
                case Opcodes.D2I :      instrs.d2i(); break;
                case Opcodes.D2L :      instrs.d2l(); break;
                case Opcodes.D2F :      instrs.d2f(); break;
                case Opcodes.I2B :      instrs.i2b(); break;
                case Opcodes.I2C :      instrs.i2c(); break;
                case Opcodes.I2S :      instrs.i2s(); break;
                case Opcodes.LCMP :     instrs.lcmp(); break;
                case Opcodes.FCMPL :    instrs.fcmpl(); break;
                case Opcodes.FCMPG :    instrs.fcmpg(); break;
                case Opcodes.DCMPL :    instrs.dcmpl(); break;
                case Opcodes.DCMPG :    instrs.dcmpg(); break;
                case Opcodes.IFEQ :     instrs.ifeq( data.offset-1 + data.readShort() ); break;
                case Opcodes.IFNE :     instrs.ifne( data.offset-1 + data.readShort() ); break;
                case Opcodes.IFLT :     instrs.iflt( data.offset-1 + data.readShort() ); break;
                case Opcodes.IFGE :     instrs.ifge( data.offset-1 + data.readShort() ); break;
                case Opcodes.IFGT :     instrs.ifgt( data.offset-1 + data.readShort() ); break;
                case Opcodes.IFLE :     instrs.ifle( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ICMPEQ : instrs.if_icmpeq( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ICMPNE : instrs.if_icmpne( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ICMPLT : instrs.if_icmplt( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ICMPGE : instrs.if_icmpge( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ICMPGT : instrs.if_icmpgt( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ICMPLE : instrs.if_icmple( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ACMPEQ : instrs.if_acmpeq( data.offset-1 + data.readShort() ); break;
                case Opcodes.IF_ACMPNE : instrs.if_acmpne( data.offset-1 + data.readShort() ); break;
                case Opcodes.GOTO :         instrs.goto_( data.offset-1 + data.readShort() ); break;
                case Opcodes.JSR :          instrs.jsr( data.offset-1 + data.readShort() ); break;
                case Opcodes.RET :          instrs.ret( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case Opcodes.TABLESWITCH :  parseTableSwitch( instrs, data ); break;  
                case Opcodes.LOOKUPSWITCH : parseLookupSwitch( instrs, data ); break; 
                case Opcodes.IRETURN :      instrs.ireturn(); break;
                case Opcodes.LRETURN :      instrs.lreturn(); break;
                case Opcodes.FRETURN :      instrs.freturn(); break;
                case Opcodes.DRETURN :      instrs.dreturn(); break;
                case Opcodes.ARETURN :      instrs.areturn(); break;
                case Opcodes.RETURN :       instrs.vreturn(); break;
                case Opcodes.GETSTATIC :    instrs.getstatic( data.readUnsignedShort() ); break;
                case Opcodes.PUTSTATIC :    instrs.putstatic( data.readUnsignedShort() ); break;
                case Opcodes.GETFIELD :     instrs.getfield( data.readUnsignedShort() );  break;
                case Opcodes.PUTFIELD :     instrs.putfield( data.readUnsignedShort() );  break;
                case Opcodes.INVOKEVIRTUAL :   instrs.invokevirtual( data.readUnsignedShort() );  break;
                case Opcodes.INVOKESPECIAL :   instrs.invokespecial( data.readUnsignedShort() );  break;
                case Opcodes.INVOKESTATIC :    instrs.invokestatic( data.readUnsignedShort() );  break;
                case Opcodes.INVOKEINTERFACE : instrs.invokeinterface( data.readUnsignedShort(), data.readUnsignedByte() ); data.readByte(); break;
                case Opcodes.NEW :             instrs.newObject( data.readUnsignedShort() );  break;
                case Opcodes.NEWARRAY :        instrs.newarray( data.readUnsignedByte() ); break;
                case Opcodes.ANEWARRAY :       instrs.anewarray( data.readUnsignedShort() );  break;
                case Opcodes.ARRAYLENGTH :   instrs.arraylength(); break;
                case Opcodes.ATHROW :        instrs.athrow(); break;
                case Opcodes.CHECKCAST :     instrs.checkcast( data.readUnsignedShort() ); break;
                case Opcodes.INSTANCEOF :    instrs.instanceOf( data.readUnsignedShort() ); break;
                case Opcodes.MONITORENTER :  instrs.monitorenter(); break;
                case Opcodes.MONITOREXIT :   instrs.monitorexit(); break;
                case Opcodes.WIDE :          isWide = true; continue;
                case Opcodes.MULTIANEWARRAY : instrs.multianewarray( data.readUnsignedShort(), data.readUnsignedByte() );  break;
                case Opcodes.IFNULL :       instrs.ifnull( data.offset-1 + data.readShort() ); break;
                case Opcodes.IFNONNULL :    instrs.ifnonnull( data.offset-1 + data.readShort() ); break;
                case Opcodes.GOTO_W :       instrs.goto_w( data.offset-1 + data.readInt() ); break;
                case Opcodes.JSR_W :        instrs.jsr_w( data.offset-1 + data.readInt() ); break;
                default: throw new IOException( "Unknown opcode " + opcode );
            }
            
            isWide = false;
        }

        //label just after the end
        instrs.defineLabel(codeLength);
        instrs.done();
    }
  
    private void parseLookupSwitch( Instructions instrs, OffsetMaintainingDataInput data ) throws IOException {        
        int opAddr = data.offset - 1;
        
        //skip padding
        while( data.offset % 4 != 0 ) data.readByte();
        
        int defaultAddr = opAddr + data.readInt();
        int numPairs    = data.readInt();
        
        int[] cases   = new int[ numPairs ];
        int[] targets = new int[ numPairs ];
        for (int i = 0; i < targets.length; i++) {
            cases[i]   = data.readInt();
            targets[i] = opAddr + data.readInt();
        }
        
        instrs.lookupswitch( defaultAddr, cases, targets );
    }
    
    private void parseTableSwitch( Instructions instrs, OffsetMaintainingDataInput data ) throws IOException {        
        int opAddr = data.offset - 1;
        
        //skip padding
        while( data.offset % 4 != 0 ) data.readByte();
        
        int defaultAddr = opAddr + data.readInt();
        int lowIndex    = data.readInt();
        int highIndex   = data.readInt();
        
        int[] targets = new int[ highIndex - lowIndex + 1 ];
        for (int i = 0; i < targets.length; i++) {
            targets[i] = opAddr + data.readInt();
        }
        
        instrs.tableswitch( defaultAddr, lowIndex, targets );
    }
    
    private static class OffsetMaintainingDataInput implements DataInput {
        private DataInput mIn;
        public int offset = 0;
        
        OffsetMaintainingDataInput( DataInput in ) {
            mIn = in;
        }
        
        public boolean readBoolean() throws IOException { throw new IOException( "Unimplemented method." ); }
        public void readFully(byte[] arg0) throws IOException { throw new IOException( "Unimplemented method." ); }
        public void readFully(byte[] arg0, int arg1, int arg2) throws IOException { throw new IOException( "Unimplemented method." ); }
        public String readUTF() throws IOException { throw new IOException( "Unimplemented method." ); }
        public String readLine() throws IOException { throw new IOException( "Unimplemented method." ); }
        
        public byte readByte() throws IOException {
            offset++;
            return mIn.readByte();
        }

        public char readChar() throws IOException {
            offset += 2;
            return mIn.readChar();
        }

        public double readDouble() throws IOException {
            offset += 8;
            return mIn.readDouble();
        }

        public float readFloat() throws IOException {
            offset += 4;
            return mIn.readFloat();
        }

        public int readInt() throws IOException {
            offset += 4;
            return mIn.readInt();
        }

        public long readLong() throws IOException {
            offset += 8;
            return mIn.readLong();
        }

        public short readShort() throws IOException {
            offset += 2;
            return mIn.readShort();
        }

        public int readUnsignedByte() throws IOException {
            offset++;
            return mIn.readUnsignedByte();
        }

        public int readUnsignedShort() throws IOException {
            offset += 2;
            return mIn.readUnsignedShort();
        }
        
        public int skipBytes(int num) throws IOException {
            offset += num;
            return mIn.skipBytes(num);
        }
    }
}
