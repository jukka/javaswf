package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ValueType;

/**
 * Base for constant expressions
 *
 * @author nickmain
 */
public abstract class Constant extends Expression {

	private final ValueType type;
	
	protected Constant( ValueType type ) {		
		this.type = type;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public ValueType type() {		
		return type;
	}
}
