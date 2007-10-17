package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.types.ValueType;
import org.javaswf.j2avm.model.types.VoidType;

/**
 * Base for method call expressions.
 *
 * @author nickmain
 */
public abstract class MethodCallExpression extends Expression {

	protected final MethodDescriptor method;
	
	protected MethodCallExpression( MethodDescriptor method, Expression...args ) {
		super( args );
		this.method = method;
	}	
	
	protected MethodCallExpression( MethodDescriptor method, Expression instance, Expression...args ) {
		super( merge( instance, args ) );
		this.method = method;
	}	
	
	/** @see org.javaswf.j2avm.model.code.expression.Expression#type() */
	@Override
	public final ValueType type() {
		if( method.type == VoidType.VOID ) return null;		
		return (ValueType) method.type;
	}
	
	private static Expression[] merge( Expression e, Expression[] ee ) {
		Expression[] merged = new Expression[ ee.length + 1 ];
		merged[0] = e;
		System.arraycopy( ee, 0, merged, 1, ee.length );
		return merged;
	}
}
