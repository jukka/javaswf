package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.PrimitiveType;

/**
 * The types of binary operation
 *  
 * @author nickmain
 */
public enum BinaryOperation {

	ADD,
	SUBTRACT,
	MULTIPLY,
	DIVIDE,
	REMAINDER,
	SHIFT_LEFT,
	SHIFT_RIGHT_SIGNED,
	SHIFT_RIGHT_UNSIGNED,
	AND,
	OR,
	XOR,
	COMPARE,
	COMPARE_G,
	COMPARE_L;
	
	/**
	 * Get the result type for this operation.
	 * 
	 * @param operand the type of the left operand
	 * @return the result type
	 */
	public PrimitiveType type( PrimitiveType operand ) {
	    switch( this ) {
	        case ADD:
	        case SUBTRACT:
	        case MULTIPLY:
	        case DIVIDE:
	        case REMAINDER:
	        case SHIFT_LEFT:
	        case SHIFT_RIGHT_SIGNED:
	        case SHIFT_RIGHT_UNSIGNED:
	        case AND:
	        case OR:
	        case XOR:
	            if( operand.isIntType ) return PrimitiveType.INT;
	            return operand;
	            
	        case COMPARE:
	        case COMPARE_G:
	        case COMPARE_L:
	            return PrimitiveType.INT;
	            
	        default: return null;
	    }
	}
}
