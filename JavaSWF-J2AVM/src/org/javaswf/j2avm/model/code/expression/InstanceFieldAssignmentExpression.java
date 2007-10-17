/**
 * 
 */
package org.javaswf.j2avm.model.code.expression;

import org.javaswf.j2avm.model.FieldDescriptor;

class InstanceFieldAssignmentExpression extends Expression {  FieldDescriptor field; Expression instance; Expression value; }