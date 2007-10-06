package org.javaswf.j2avm.model.code;

import org.javaswf.j2avm.model.types.JavaType;
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
	 * May be null if the creator is not known or the value is merged from
	 * other values with different creators.
	 */
	public final ValueGenerator creator;
	
	/**
	 * The type of the value.
	 */
	public final ValueType type;
	
	/**
	 * The name of the value
	 */
	public final String name;
	
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
	 * Get the type of this value - null if unknown.
	 */
	public ValueType type() {
		return type;
	}
	
	/**
	 * Make a copy
	 */
	public Value copy() {
		return new Value( creator, type, name );
	}
	
	/**
	 * Get the value creator
	 */
	public ValueGenerator creator() { return creator; }	
	
	@Override
	public String toString() {		
		String abbrev = (type != null) ? type.abbreviation : "?";
		return name + ":" + abbrev;
	}
	
	/**
	 * Merge values.  
	 * If they are the same then return that.
	 * If any is null then return null.
	 * Otherwise return a value with the common type - or null if there
	 * is no common type.
	 */
	public static Value merge( Value...values ) {
		if( values.length == 0 ) return null;
		
		Value a = null;
		boolean allSame = true;
		for( Value value : values ) {
			if( value == null ) return null;
			if( a == null ) a = value;
			if( a != value ) allSame = false;			
		}
		if( allSame ) return a;
		
		ValueGenerator gen    = a.creator;
		ValueType      common = a.type;
		String         name   = a.name;
		for( int i = 1; i < values.length; i++ ) {
			Value b = values[i];
			common = (ValueType) JavaType.common( common, b.type );
			
			if( b.creator != gen ) gen = null;
			name += "+" + b.name;
		}
		
		return new Value( gen, common, name );
	}
}
