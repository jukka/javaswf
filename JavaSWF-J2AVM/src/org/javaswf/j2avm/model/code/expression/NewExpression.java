/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.ValueType;

class NewExpression extends Expression {  ObjectType type; ValueType[] paramTypes; Expression[] args; }