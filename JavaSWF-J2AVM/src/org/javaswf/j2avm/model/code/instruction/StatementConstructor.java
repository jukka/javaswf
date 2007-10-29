package org.javaswf.j2avm.model.code.instruction;

import java.util.LinkedList;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionPrinter;
import org.javaswf.j2avm.model.code.expression.VirtualCallExpression;
import org.javaswf.j2avm.model.code.statement.ExpressionStatement;
import org.javaswf.j2avm.model.code.statement.LabelStatement;
import org.javaswf.j2avm.model.code.statement.Statement;
import org.javaswf.j2avm.model.code.statement.StatementCursor;
import org.javaswf.j2avm.model.code.statement.StatementList;

/**
 * Constructs expressions and statements from the intermediate forms that
 * result after instruction parsing
 *
 * @author nickmain
 */
public class StatementConstructor {

    private final StatementList list;
    private final MethodModel   method;
    
    //queue of items to be resolved
    private final LinkedList<Statement> agenda = new LinkedList<Statement>();
    
    /**
     * @param list the statements to process in-place
     * @param method the method the code belongs to
     */
    public StatementConstructor( StatementList list, MethodModel method ) {
        this.method = method;
        this.list   = list;
    }
    
    /**
     * Perform the construction process
     */
    public void perform() {
        
        //add all statements to the agenda
        StatementCursor cursor = list.cursorAtStart();
        Statement s;
        while(( s = cursor.next()) != null ) {
            
            //skip pseudo-statements and labels
            if( s instanceof IntermediateStatement || s instanceof LabelStatement ) continue;
            
            if( ! s.children().isComplete() ) {
                agenda.addLast( s );
            }
        }
        
        while( ! agenda.isEmpty() ) processAgenda();
    }
    
    //process the first item on the agenda
    private void processAgenda() {
        Statement s = agenda.removeFirst();
    
        while( ! s.children().isComplete() ) {
            
            //is the previous expression complete ?
            Statement prev = s.previous();
            if( prev == null ) throw new RuntimeException( "Incomplete statement and no prior expression" );
            
            if( prev instanceof UnassembledExpressionStatement ) {
                Expression prevEx = ((UnassembledExpressionStatement) prev).wrappedExpression();
                
                //consume the expression if it is complete
                if( prevEx.isComplete() ) {
                    s.children().setLastUndefinedChild( prevEx );
                    prev.remove();
                }
                
                //if expression is not complete - add it to the agenda along with 
                //the current statement
                else {
                    agenda.addLast( prev );
                    agenda.addLast( s );
                    return;
                }
            }
            else if( prev instanceof  )
            
            else {
                return;
                //FIXME:
            }
        }
        
    }
}
