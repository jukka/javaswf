/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.types.ArrayType;

class NewArrayExpression extends Expression {  ArrayType type; Expression[] dimSizes; Expression[] initialValues; }