package org.javaswf.j2avm.model.code.intermediate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
    
    //frame before each statement
    private Map<Statement, Frame> frames = new HashMap<Statement, Frame>();
    
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
     * Perform the construction process.
     */
    public void perform() {

    	// Build stack frames and local vars for each statement
    	// Statements consume the previous 
    	
  
    }
    
    /**
     * Get or determine the frame just before the given statement
     * @param s the statement
     */
    public Frame frameForStatement( Statement s ) {
    	Frame f = frames.get( s );
    	if( f == null ) {
    		f = determineFrame( s );
    		//is already in the cache
    	}
    	
    	return f;
    }

    /**
     * Determine the frame for the given statement
     */
    private Frame determineFrame( Statement s ) {
    	agenda.add( s );
    	processAgenda();
    	return frames.get( s );
    }

    /**
     * Process the agenda to determine frames for all the statements it
     * contains.
     */
    private void processAgenda() {
    	while( ! agenda.isEmpty()) {
    		Statement s = agenda.removeFirst();
    		if( frames.containsKey( s ) ) continue; //already has a frame
    		
    		//TODO:
    	}
    }
}
