package org.epistem.io;

import java.io.OutputStream;

/**
 * An OutputStream that provides a count of the bytes written.
 * 
 * @author nick
 */
public abstract class CountingOutputStream extends OutputStream {

	protected int mCount = 0;

	/**
	 * @return the bytes-written counter
	 */
	public int getCount() {
		return mCount;
	}

	/**
	 * Set the counter.
	 */
	public void setCount( int count ) {
		mCount = count;
	}
}
