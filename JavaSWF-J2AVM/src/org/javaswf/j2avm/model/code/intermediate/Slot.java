package org.javaswf.j2avm.model.code.intermediate;


/**
 * A slot contains a reference to a value and the same slot may appear in
 * multiple frames.  The slot is the indirection that allows a value change to 
 * automatically propagate through frames.
 *  
 * @author nickmain
 */
public class Slot {
	private SlotValue value; //either StaticSingleAssignmentStatement or Constant
	
	public Slot( SlotValue value ) { this.value = value; }

	/**
	 * @return the value
	 */
	public SlotValue getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue( SlotValue value ) {
		this.value = value;
	}
}
