package org.javaswf.j2avm.model.code.statement;

import java.util.SortedSet;

import org.javaswf.j2avm.model.code.expression.Expression;

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
    public void visitBranch( LabelStatement target );
    
    /**
     * Visit a condition branch
     * 
     * @param target the branch target if the condition is true
     * @param condition the condition for the branch
     */
    public void visitBranch( LabelStatement target, Expression condition );
    
    /**
     * Visit an expression evaluation statement
     * 
     * @param expression the expression to evaluate
     */
    public void visitExpression( Expression expression );
    
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
     * Visit an increment statement
     * 
     * @param varIndex the variable to increment
     * @param increment the increment value
     */
    public void visitIncrement( int varIndex, Expression increment );
}
