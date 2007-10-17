/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * A class constant
 *
 * @author nickmain
 */
public final class ConstantClass extends Constant {  
	private final JavaType classType;

	/*pkg*/ ConstantClass( JavaType classType ) {
		super( ObjectType.CLASS );
		this.classType = classType;
	}

	/** @see org.javaswf.j2avm.model.code.expression.Expression#accept(org.javaswf.j2avm.model.code.expression.ExpressionVisitor) */
	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitConstantClass( classType );
	}
}