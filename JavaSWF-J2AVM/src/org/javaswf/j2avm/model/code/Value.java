package org.javaswf.j2avm.model.code;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * Represents a computed value.
 * 
 * The value is represented as its type, not the actual value.
 * 
 * Value equality is Object equality - each instance is unique.
 *  
 * @author nickmain
 */
public class Value {

	/**
	 * The instruction, method or exception handler that created this value.
	 */
	public final ValueGenerator creator;
	
	/**
	 * The type of the value
	 */
	private ValueType type;
	
	/**
	 * The name of the value
	 */
	private final String name;
	
	/**
	 * @param creator the item that created the value
	 * @param type the type of the value
	 */
	/*pkg*/ Value( ValueGenerator creator, ValueType type, String name ) {
		this.creator = creator;
		this.type    = type;
		this.name    = name;
	}	
	
	/**
	 * Get the type of this value - null if the type is unknown.
	 */
	public ValueType type() {
		return type;
	}
	
	@Override
	public String toString() {		
		return name + ":" + type.abbreviation;
	}
}
