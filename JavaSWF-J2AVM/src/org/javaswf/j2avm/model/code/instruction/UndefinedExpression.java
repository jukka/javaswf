package org.javaswf.j2avm.model.code.instruction;

import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionVisitor;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * A placeholder expression.
 *
 * @author nickmain
 */
public class UndefinedExpression extends Expression {

	@Override
	public void accept( ExpressionVisitor visitor ) {
		visitor.visitVariable( "<UNDEFINED>", ObjectType.OBJECT );
	}

	@Override
	public ValueType type() {
		return ObjectType.OBJECT;
	}
}
