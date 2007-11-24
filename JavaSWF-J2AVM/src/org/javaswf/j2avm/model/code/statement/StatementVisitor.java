package org.javaswf.j2avm.model.code.statement;

import java.util.Set;
import java.util.SortedSet;

import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.code.expression.CaughtExceptionExpression;
import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.SSAValueExpression;

/**
 * Visitor interface for statements
 *
 * @author nickmain
 */
public interface StatementVisitor {

    /**
     * Visit a label.
     */
    public void visitLabel( LabelStatement label );
    
    /**
     * Visit an unconditional branch
     * 
     * @param target the target label
     */
    public void visitUnconditionalBranch( LabelStatement target );
    
    /**
     * Visit a condition branch
     * 
     * @param target the branch target if the condition is true
     * @param condition the condition for the branch
     */
    public void visitConditionalBranch( LabelStatement target, Expression condition );
    
    /**
     * Visit an expression evaluation statement
     * 
     * @param expression the expression to evaluate
     */
    public void visitExpression( Expression expression );
    
    /**
     * Visit an SSA value definition
     * 
     * @param valueName the human-friendly value name
     * @param value the value
     * @param references the references to the value
     */
    public void visitSSAValue( String valueName, Expression value, Set<SSAValueExpression> references );
    
    /**
     * Visit an array element assignment
     * 
     * @param array the array object
     * @param index the element index
     * @param value the value to assign
     */
    public void visitElementAssignment( Expression array, Expression index, Expression value );
    
    /**
     * Visit a field assignment
     * 
     * @param field the field
     * @param instance the instance object
     * @param value the value to assign
     */
    public void visitFieldAssignment( FieldDescriptor field, Expression instance, Expression value );
    
    /**
     * Visit a static field assignment
     * 
     * @param field the field
     * @param value the value to assign
     */
    public void visitStaticFieldAssignment( FieldDescriptor field, Expression value );
    
    /**
     * Visit a method return
     * 
     * @param expression the return value - null if the method is void
     */
    public void visitReturn( Expression expression );
    
    /**
     * Visit a throw statement
     * 
     * @param exception the exception to throw
     */
    public void visitThrow( Expression exception );
    
    /**
     * Visit a switch statement
     * 
     * @param value the value to match on
     * @param defaultTarget the default target
     * @param cases the cases in ascending order of case value
     */
    public void visitSwitch( Expression value, 
    		                 LabelStatement defaultTarget, 
    		                 SortedSet<SwitchCase> cases );
    
    /**
     * Visit the start of a synchronized block
     * 
     * @param object the object to sync on
     */
    public void visitMonitorEnter( Expression object );
    
    /**
     * Visit the end of a synchronized block
     * 
     * @param object the object to sync on
     */
    public void visitMonitorExit( Expression object );    

    /**
     * Visit a Phi statement - the merging of disparate SSA values that come
     * from alternate execution paths.
     * 
     * @param valueName the new value name
     * @param values the alternate values
     */
    public void visitPhi( String valueName, SSAValueExpression... values );
    
    /**
     * Visit the start of a try block
     * 
     * @param exception the exception that is to be caught
     * @param endLabel the end of the try block
     * @param handlerLabel the handler
     */
    public void visitTry( CaughtExceptionExpression exception, LabelStatement endLabel, LabelStatement handlerLabel );
}
