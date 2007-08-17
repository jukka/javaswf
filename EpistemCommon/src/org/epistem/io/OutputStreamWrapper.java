package org.epistem.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Base class for output stream wrappers
 * 
 * @author nick
 */
public abstract class OutputStreamWrapper extends CountingOutputStream {
	
	private OutputStream mOut;

	protected OutputStreamWrapper( OutputStream out ) {
		mOut = out;
	}
	
	/**
	 * Change the wrapped output stream
	 */
	public void setOutputStream( OutputStream out ) {
		mOut = out;
	}    

	/**
	 * Get the wrapped output stream
	 */
	protected OutputStream getOutputStream() {
		return mOut;
	}

	/**
	 * @see java.io.OutputStream#write(int)
	 */
	public void write( int b ) throws IOException {
		mOut.write( b );
		mCount++;
	}

	/**
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException {
		mOut.close();
	}

	/**
	 * @see java.io.OutputStream#flush()
	 */
	public void flush() throws IOException {
		mOut.flush();
	}

	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write( byte[] b, int start, int len ) throws IOException {
		mOut.write( b, start, len );
		mCount += len;
	}

	/**
	 * @see java.io.OutputStream#write(byte[])
	 */
	public void write( byte[] b ) throws IOException {
		mOut.write( b );
		mCount += b.length;
	}
}
