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

package org.javaswf.j2avm.model.code.instruction;

import java.io.DataInput;
import java.io.IOException;



/**
 * Parser for instruction. 
 * 
 * Instruction labels are passed for every instruction and are the actual 
 * bytecode offsets.
 * 
 * @author nmain
 */
public class InstructionParser {

    /**
     * Parse bytecode instructions
     * 
     * @param dataIn the bytecode data 
     * @param codeLength the bytecode data length
     * @param instrs the instruction receiver
     */
    public void parseInstructions( DataInput dataIn, int codeLength, Instructions instrs) throws IOException {
        OffsetMaintainingDataInput data = new OffsetMaintainingDataInput( dataIn );

        boolean isWide = false;
        
        while ( data.offset < codeLength) {
            if( ! isWide ) instrs.defineLabel( data.offset );
            int opcode = data.readUnsignedByte();

            Operation op = Operation.fromOpcode( opcode );
            
            switch ( op ) {
                case NOP :         instrs.nop(); break;
                case ACONST_NULL : instrs.aconst_null(); break;
                case ICONST_M1 :   instrs.iconst_m1(); break;
                case ICONST_0 :    instrs.iconst_0(); break;
                case ICONST_1 :    instrs.iconst_1(); break;
                case ICONST_2 :    instrs.iconst_2(); break;
                case ICONST_3 :    instrs.iconst_3(); break;
                case ICONST_4 :    instrs.iconst_4(); break;
                case ICONST_5 :    instrs.iconst_5(); break;
                case LCONST_0 :    instrs.lconst_0(); break;
                case LCONST_1 :    instrs.lconst_1(); break;
                case FCONST_0 :    instrs.fconst_0(); break;
                case FCONST_1 :    instrs.fconst_1(); break;
                case FCONST_2 :    instrs.fconst_2(); break;
                case DCONST_0 :    instrs.dconst_0(); break;
                case DCONST_1 :    instrs.dconst_1(); break;
                case BIPUSH :      instrs.bipush( data.readByte() ); break;
                case SIPUSH :      instrs.sipush( data.readShort() ); break;
                case LDC :         instrs.ldc( data.readUnsignedByte() ); break;
                case LDC_W :       instrs.ldc_w( data.readUnsignedShort() ); break;
                case LDC2_W :      instrs.ldc2_w( data.readUnsignedShort() ); break;
                case ILOAD :   instrs.iload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;  
                case LLOAD :   instrs.lload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case FLOAD :   instrs.fload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case DLOAD :   instrs.dload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case ALOAD :   instrs.aload( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case ILOAD_0 : instrs.iload_0(); break;
                case ILOAD_1 : instrs.iload_1(); break;
                case ILOAD_2 : instrs.iload_2(); break;
                case ILOAD_3 : instrs.iload_3(); break;
                case LLOAD_0 : instrs.lload_0(); break;
                case LLOAD_1 : instrs.lload_1(); break;
                case LLOAD_2 : instrs.lload_2(); break;
                case LLOAD_3 : instrs.lload_3(); break;
                case FLOAD_0 : instrs.fload_0(); break;
                case FLOAD_1 : instrs.fload_1(); break;
                case FLOAD_2 : instrs.fload_2(); break;
                case FLOAD_3 : instrs.fload_3(); break;
                case DLOAD_0 : instrs.dload_0(); break;
                case DLOAD_1 : instrs.dload_1(); break;
                case DLOAD_2 : instrs.dload_2(); break;
                case DLOAD_3 : instrs.dload_3(); break;
                case ALOAD_0 : instrs.aload_0(); break;
                case ALOAD_1 : instrs.aload_1(); break;
                case ALOAD_2 : instrs.aload_2(); break;
                case ALOAD_3 : instrs.aload_3(); break;
                case IALOAD :  instrs.iaload(); break;
                case LALOAD :  instrs.laload(); break;
                case FALOAD :  instrs.faload(); break;
                case DALOAD :  instrs.daload(); break;
                case AALOAD :  instrs.aaload(); break;
                case BALOAD :  instrs.baload(); break;
                case CALOAD :  instrs.caload(); break;
                case SALOAD :  instrs.saload(); break;
                case ISTORE :  instrs.istore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case LSTORE :  instrs.lstore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case FSTORE :  instrs.fstore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case DSTORE :  instrs.dstore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case ASTORE :  instrs.astore( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case ISTORE_0 : instrs.istore_0(); break;
                case ISTORE_1 : instrs.istore_1(); break;
                case ISTORE_2 : instrs.istore_2(); break;
                case ISTORE_3 : instrs.istore_3(); break;
                case LSTORE_0 : instrs.lstore_0(); break;
                case LSTORE_1 : instrs.lstore_1(); break;
                case LSTORE_2 : instrs.lstore_2(); break;
                case LSTORE_3 : instrs.lstore_3(); break;
                case FSTORE_0 : instrs.fstore_0(); break;
                case FSTORE_1 : instrs.fstore_1(); break;
                case FSTORE_2 : instrs.fstore_2(); break;
                case FSTORE_3 : instrs.fstore_3(); break;
                case DSTORE_0 : instrs.dstore_0(); break;
                case DSTORE_1 : instrs.dstore_1(); break;
                case DSTORE_2 : instrs.dstore_2(); break;
                case DSTORE_3 : instrs.dstore_3(); break;
                case ASTORE_0 : instrs.astore_0(); break;
                case ASTORE_1 : instrs.astore_1(); break;
                case ASTORE_2 : instrs.astore_2(); break;
                case ASTORE_3 : instrs.astore_3(); break;
                case IASTORE :  instrs.iastore(); break;
                case LASTORE :  instrs.lastore(); break;
                case FASTORE :  instrs.fastore(); break;
                case DASTORE :  instrs.dastore(); break;
                case AASTORE :  instrs.aastore(); break;
                case BASTORE :  instrs.bastore(); break;
                case CASTORE :  instrs.castore(); break;
                case SASTORE :  instrs.sastore(); break;
                case POP :      instrs.pop(); break;
                case POP2 :     instrs.pop2(); break;
                case DUP :      instrs.dup(); break;
                case DUP_X1 :   instrs.dup_x1(); break;
                case DUP_X2 :   instrs.dup_x2(); break;
                case DUP2 :     instrs.dup2(); break;
                case DUP2_X1 :  instrs.dup2_x1(); break;
                case DUP2_X2 :  instrs.dup2_x2(); break;
                case SWAP :     instrs.swap(); break;
                case IADD :     instrs.iadd(); break;
                case LADD :     instrs.ladd(); break;
                case FADD :     instrs.fadd(); break;
                case DADD :     instrs.dadd(); break;
                case ISUB :     instrs.isub(); break;
                case LSUB :     instrs.lsub(); break;
                case FSUB :     instrs.fsub(); break;
                case DSUB :     instrs.dsub(); break;
                case IMUL :     instrs.imul(); break;
                case LMUL :     instrs.lmul(); break;
                case FMUL :     instrs.fmul(); break;
                case DMUL :     instrs.dmul(); break;
                case IDIV :     instrs.idiv(); break;
                case LDIV :     instrs.ldiv(); break;
                case FDIV :     instrs.fdiv(); break;
                case DDIV :     instrs.ddiv(); break;
                case IREM :     instrs.irem(); break;
                case LREM :     instrs.lrem(); break;
                case FREM :     instrs.frem(); break;
                case DREM :     instrs.drem(); break;
                case INEG :     instrs.ineg(); break;
                case LNEG :     instrs.lneg(); break;
                case FNEG :     instrs.fneg(); break;
                case DNEG :     instrs.dneg(); break;
                case ISHL :     instrs.ishl(); break;
                case LSHL :     instrs.lshl(); break;
                case ISHR :     instrs.ishr(); break;
                case LSHR :     instrs.lshr(); break;
                case IUSHR :    instrs.iushr(); break;
                case LUSHR :    instrs.lushr(); break;
                case IAND :     instrs.iand(); break;
                case LAND :     instrs.land(); break;
                case IOR :      instrs.ior(); break;
                case LOR :      instrs.lor(); break;
                case IXOR :     instrs.ixor(); break;
                case LXOR :     instrs.lxor(); break;
                case IINC :     instrs.iinc( isWide ? data.readUnsignedShort() : data.readUnsignedByte(), isWide ? data.readShort() : data.readByte()); break;
                case I2L :      instrs.i2l(); break;
                case I2F :      instrs.i2f(); break;
                case I2D :      instrs.i2d(); break;
                case L2I :      instrs.l2i(); break;
                case L2F :      instrs.l2f(); break;
                case L2D :      instrs.l2d(); break;
                case F2I :      instrs.f2i(); break;
                case F2L :      instrs.f2l(); break;
                case F2D :      instrs.f2d(); break;
                case D2I :      instrs.d2i(); break;
                case D2L :      instrs.d2l(); break;
                case D2F :      instrs.d2f(); break;
                case I2B :      instrs.i2b(); break;
                case I2C :      instrs.i2c(); break;
                case I2S :      instrs.i2s(); break;
                case LCMP :     instrs.lcmp(); break;
                case FCMPL :    instrs.fcmpl(); break;
                case FCMPG :    instrs.fcmpg(); break;
                case DCMPL :    instrs.dcmpl(); break;
                case DCMPG :    instrs.dcmpg(); break;
                case IFEQ :     instrs.ifeq( data.offset-1 + data.readShort() ); break;
                case IFNE :     instrs.ifne( data.offset-1 + data.readShort() ); break;
                case IFLT :     instrs.iflt( data.offset-1 + data.readShort() ); break;
                case IFGE :     instrs.ifge( data.offset-1 + data.readShort() ); break;
                case IFGT :     instrs.ifgt( data.offset-1 + data.readShort() ); break;
                case IFLE :     instrs.ifle( data.offset-1 + data.readShort() ); break;
                case IF_ICMPEQ : instrs.if_icmpeq( data.offset-1 + data.readShort() ); break;
                case IF_ICMPNE : instrs.if_icmpne( data.offset-1 + data.readShort() ); break;
                case IF_ICMPLT : instrs.if_icmplt( data.offset-1 + data.readShort() ); break;
                case IF_ICMPGE : instrs.if_icmpge( data.offset-1 + data.readShort() ); break;
                case IF_ICMPGT : instrs.if_icmpgt( data.offset-1 + data.readShort() ); break;
                case IF_ICMPLE : instrs.if_icmple( data.offset-1 + data.readShort() ); break;
                case IF_ACMPEQ : instrs.if_acmpeq( data.offset-1 + data.readShort() ); break;
                case IF_ACMPNE : instrs.if_acmpne( data.offset-1 + data.readShort() ); break;
                case GOTO :         instrs.goto_( data.offset-1 + data.readShort() ); break;
                case JSR :          instrs.jsr( data.offset-1 + data.readShort() ); break;
                case RET :          instrs.ret( isWide ? data.readUnsignedShort() : data.readUnsignedByte() ); break;
                case TABLESWITCH :  parseTableSwitch( instrs, data ); break;  
                case LOOKUPSWITCH : parseLookupSwitch( instrs, data ); break; 
                case IRETURN :      instrs.ireturn(); break;
                case LRETURN :      instrs.lreturn(); break;
                case FRETURN :      instrs.freturn(); break;
                case DRETURN :      instrs.dreturn(); break;
                case ARETURN :      instrs.areturn(); break;
                case RETURN :       instrs.vreturn(); break;
                case GETSTATIC :    instrs.getstatic( data.readUnsignedShort() ); break;
                case PUTSTATIC :    instrs.putstatic( data.readUnsignedShort() ); break;
                case GETFIELD :     instrs.getfield( data.readUnsignedShort() );  break;
                case PUTFIELD :     instrs.putfield( data.readUnsignedShort() );  break;
                case INVOKEVIRTUAL :   instrs.invokevirtual( data.readUnsignedShort() );  break;
                case INVOKESPECIAL :   instrs.invokespecial( data.readUnsignedShort() );  break;
                case INVOKESTATIC :    instrs.invokestatic( data.readUnsignedShort() );  break;
                case INVOKEINTERFACE : instrs.invokeinterface( data.readUnsignedShort(), data.readUnsignedByte() ); data.readByte(); break;
                case NEW :             instrs.newObject( data.readUnsignedShort() );  break;
                case NEWARRAY :        instrs.newarray( data.readUnsignedByte() ); break;
                case ANEWARRAY :       instrs.anewarray( data.readUnsignedShort() );  break;
                case ARRAYLENGTH :   instrs.arraylength(); break;
                case ATHROW :        instrs.athrow(); break;
                case CHECKCAST :     instrs.checkcast( data.readUnsignedShort() ); break;
                case INSTANCEOF :    instrs.instanceOf( data.readUnsignedShort() ); break;
                case MONITORENTER :  instrs.monitorenter(); break;
                case MONITOREXIT :   instrs.monitorexit(); break;
                case WIDE :          isWide = true; continue;
                case MULTIANEWARRAY : instrs.multianewarray( data.readUnsignedShort(), data.readUnsignedByte() );  break;
                case IFNULL :       instrs.ifnull( data.offset-1 + data.readShort() ); break;
                case IFNONNULL :    instrs.ifnonnull( data.offset-1 + data.readShort() ); break;
                case GOTO_W :       instrs.goto_w( data.offset-1 + data.readInt() ); break;
                case JSR_W :        instrs.jsr_w( data.offset-1 + data.readInt() ); break;
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
