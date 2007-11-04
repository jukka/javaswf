package org.javaswf.j2avm.model.code.instruction;

import java.util.LinkedList;

import org.javaswf.j2avm.model.MethodModel;
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
    private final LinkedList<ConstructedCode> agenda = new LinkedList<ConstructedCode>();
    
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
        while(( s = cursor.next()) != null ) addToAgenda( s );
        
        //process the agenda
        int count = 0;
        int size  = agenda.size();
        while( ! agenda.isEmpty() ) {
        	ConstructedCode cc = agenda.removeFirst();
            cc.construct( this ); 
            
            //if everything in the agenda has been processed and there was no
            //decrease in the agenda size then we're in a loop and need to
            //abort
            if( agenda.size() < size ) {
            	count = 0;
            	size  = agenda.size();            	
            }
            else {
            	count++;
            	if( count == size ) {
            		System.err.println( "maxed out" );
            		return;
            	}
            }
        }
    }
    
    /**
     * Append an item to the processing agenda
     */
    /*pkg*/ void addToAgenda( ConstructedCode cc ) {
    	agenda.addLast( cc );
    }    
}
