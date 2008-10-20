package org.epistem.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Wrapper around OutStream that converts IOExceptions to RuntimeExceptions
 *
 * @author nickmain
 */
public class OutStreamWrapper {

    /** The wrapped OutStream */
    public final OutStream out;

    /**
     * @param out the stream to wrap
     */
    public OutStreamWrapper( OutStream out ) {
        this.out = out;
    } 
    
    /** @see com.anotherbigidea.io.OutStream#close() */
    public void close() { try { 
        out.close();
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#flush() */
    public void flush() { try { 
        out.flush();
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#flushBits() */
    public void flushBits() { try { 
        out.flushBits();
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.CountingOutputStream#getCount() */
    public int getCount() {
        return out.getCount();
    } 

    /** @see com.anotherbigidea.io.CountingOutputStream#setCount(int) */
    public void setCount(int count) {
        out.setCount(count);
    } 

    /** @see com.anotherbigidea.io.OutputStreamWrapper#setOutputStream(java.io.OutputStream) */
    public void setOutputStream(OutputStream out) {
        this.out.setOutputStream(out);
    }

    /** @see com.anotherbigidea.io.OutStream#write(byte[], int, int) */
    public void write(byte[] bytes, int start, int length) { try { 
        out.write(bytes, start, length);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#write(byte[]) */
    public void write(byte[] bytes) { try { 
        out.write(bytes);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#write(int) */
    public void write(int b) { try { 
        out.write(b);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeCompressed() */
    public void writeCompressed() {
        out.writeCompressed();
    } 

    /** @see com.anotherbigidea.io.OutStream#writeDouble(double) */
    public void writeDouble(double value) { try { 
        out.writeDouble(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeDoubleLE(double) */
    public void writeDoubleLE(double value) { try { 
        out.writeDoubleLE(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeFloat(float) */
    public void writeFloat(float value) { try { 
        out.writeFloat(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeSBits(int, int) */
    public void writeSBits(int numBits, int value) { try { 
        out.writeSBits(numBits, value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeSI16(short) */
    public void writeSI16(short value) { try { 
        out.writeSI16(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeSI32(int) */
    public void writeSI32(int value) { try { 
        out.writeSI32(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeString(byte[]) */
    public void writeString(byte[] string) { try { 
        out.writeString(string);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeString(java.lang.String, java.lang.String) */
    public int writeString(String s, String encoding) { try { 
        return out.writeString(s, encoding);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeUBits(int, long) */
    public void writeUBits(int numBits, long value) { try { 
        out.writeUBits(numBits, value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeUI16(int) */
    public void writeUI16(int value) { try { 
        out.writeUI16(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeUI32(long) */
    public void writeUI32(long value) { try { 
        out.writeUI32(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeUI8(int) */
    public void writeUI8(int value) { try { 
        out.writeUI8(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeVS24(int) */
    public void writeVS24(int value) { try { 
        out.writeVS24(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeVS32(int) */
    public void writeVS32(int value) { try { 
        out.writeVS32(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeVU30(int) */
    public void writeVU30(int value) { try { 
        out.writeVU30(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeVU30String(java.lang.String) */
    public void writeVU30String(String s) { try { 
        out.writeVU30String(s);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }

    /** @see com.anotherbigidea.io.OutStream#writeVU32(long) */
    public void writeVU32(long value) { try { 
        out.writeVU32(value);
    } catch(IOException ioe){ throw new RuntimeException(ioe);} }
}
