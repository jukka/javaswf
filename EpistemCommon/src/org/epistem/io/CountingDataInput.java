package org.epistem.io;

import java.io.DataInput;
import java.io.IOException;

/**
 * DataInput that keeps a count of the bytes read.
 * 
 * readUTF and readLine are unimplemented.
 *
 * @author nickmain
 */
public class CountingDataInput implements DataInput {

    private final DataInput in;
    
    /**
     * The count - can be modified
     */
    public int count = 0;
    
    /**
     * @param in the wrapped input
     */
    public CountingDataInput( DataInput in ) {
        this.in = in;
    }

    /** @see java.io.DataInput#readBoolean() */
    public boolean readBoolean() throws IOException {
        count++;
        return in.readBoolean();
    }

    /** @see java.io.DataInput#readByte() */
    public byte readByte() throws IOException {
        count++;
        return in.readByte();
    }

    /** @see java.io.DataInput#readChar() */
    public char readChar() throws IOException {
        count += 2;
        return in.readChar();
    }

    /** @see java.io.DataInput#readDouble() */
    public double readDouble() throws IOException {
        count += 8;
        return in.readDouble();
    }

    /** @see java.io.DataInput#readFloat() */
    public float readFloat() throws IOException {
        count += 4;
        return in.readFloat();
    }

    /** @see java.io.DataInput#readFully(byte[], int, int) */
    public void readFully(byte[] b, int off, int len) throws IOException {
        count += len;
        in.readFully(b, off, len);
    }

    /** @see java.io.DataInput#readFully(byte[]) */
    public void readFully(byte[] b) throws IOException {
        count += b.length;
        in.readFully(b);
    }

    /** @see java.io.DataInput#readInt() */
    public int readInt() throws IOException {
        count += 4;
        return in.readInt();
    }

    /** @see java.io.DataInput#readLine() */
    public String readLine() throws IOException {
        throw new IOException( "readLine is not implemented" );
    }

    /** @see java.io.DataInput#readLong() */
    public long readLong() throws IOException {
        count += 8;
        return in.readLong();
    }

    /** @see java.io.DataInput#readShort() */
    public short readShort() throws IOException {
        count += 2;
        return in.readShort();
    }

    /** @see java.io.DataInput#readUnsignedByte() */
    public int readUnsignedByte() throws IOException {
        count++;
        return in.readUnsignedByte();
    }

    /** @see java.io.DataInput#readUnsignedShort() */
    public int readUnsignedShort() throws IOException {
        count += 2;
        return in.readUnsignedShort();
    }

    /** @see java.io.DataInput#readUTF() */
    public String readUTF() throws IOException {
        throw new IOException( "readUTF is not implemented" );
    }

    /** @see java.io.DataInput#skipBytes(int) */
    public int skipBytes(int n) throws IOException {
        int read = in.skipBytes(n);
        count += read;
        return read;
    }
}
