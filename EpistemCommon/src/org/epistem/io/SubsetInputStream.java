package org.epistem.io;

import java.io.*;

/**
 * An input stream that is a view onto a subset of another input stream.
 */
public class SubsetInputStream extends InputStream {
	protected InputStream in;
	protected int length;
	protected int read;

	public SubsetInputStream(InputStream in, int length) {
		this.in = in;
		this.length = length;
		read = 0;
	}

	/** 
	 * The number of bytes read from the input stream
	 */
	public int getRead() {
		return read;
	}

	/**
	 * Force the subset to be fully consumed
	 */
	public void consume() throws IOException {
		if (read < length) {
			in.skip(length - read);
		}

		read = length;
	}

	/**
	 * Does not close the underlying stream - calls consume()
	 */
	public void close() throws IOException {
		consume();
	}

	public int read(byte[] b, int off, int len) throws IOException {
		if (read + len > length) len = length - read;

		int count = in.read(b, off, len);

		if (count < 0) { //end of input
			read = length;
			return -1;
		}

		read += count;

		return count;
	}

	public long skip(long n) throws IOException {
		if (n > length - read) n = length - read;

		long skipped = in.skip(n);
		read += (int) skipped;

		return skipped;
	}

	public int read() throws IOException {
		if (read >= length) return -1;
		read++;
		return in.read();
	}
}
