package org.javaswf.j2avm.model.code.instruction;

/**
 * A slot contains a reference to a value and the same slot may appear in
 * multiple frames.  The slot is the indirection that allows a value change to 
 * automatically propagate through frames.
 *  
 * @author nickmain
 */
public class Slot {
	private Value value;
	
	public Slot( Value value ) { this.value = value; }

	/**
	 * @return the value
	 */
	public Value getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue( Value value ) {
		this.value = value;
	}
}
