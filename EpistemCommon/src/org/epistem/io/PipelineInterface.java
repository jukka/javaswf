// Created on May 14, 2005

package org.epistem.io;

/**
 * A Pipeline interface is one which is used to pass structured data from
 * one stage of a processing pipeline to another by way of a sequence of
 * method calls.  Calling the done() method signals completion.
 * 
 * The methods of a pipeline interface should be void or return another
 * pipeline interface.  Method names should not be overloaded.
 *
 * Parameters should be primitive types, enums, strings and arrays of those.
 * More complex structures should be passed via returned PipelineInterfaces.
 *
 * @author nick
 */
public interface PipelineInterface {

	/**
	 * Signals the end of passing data through this interface.
	 * Once end() is called no more method calls should be made to this
	 * instance.
	 */
	public void done();
}
