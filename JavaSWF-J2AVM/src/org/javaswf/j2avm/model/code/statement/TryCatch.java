package org.javaswf.j2avm.model.code.statement;

import java.util.Set;

import org.javaswf.j2avm.model.types.ObjectType;

/**
 * An exception handler.
 * 
 * @author nickmain
 */
public class TryCatch implements LabelTargetter {

	/** The start of the try block */
	public final LabelStatement tryStart;
	
	/** The end of the try block */
	public final LabelStatement tryEnd;
	
	/** The start of the handler */
	public final LabelStatement handlerStart;
	
	/** The type of exception to catch */
	public final ObjectType exceptionType;
	
    TryCatch( LabelStatement tryStart, 
    		  LabelStatement tryEnd, 
    		  LabelStatement handlerStart, 
    		  ObjectType     exceptionType ) {
    	this.tryStart      = tryStart;
    	this.tryEnd        = tryEnd;
    	this.handlerStart  = handlerStart;
    	this.exceptionType = exceptionType;

		tryStart    .targetters.add( this );
		tryEnd      .targetters.add( this );
		handlerStart.targetters.add( this );		
    }

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#release() */
	public void release() {
		tryStart    .targetters.remove( this );
		tryEnd      .targetters.remove( this );
		handlerStart.targetters.remove( this );		
	}

	/** @see org.javaswf.j2avm.model.code.statement.LabelTargetter#targets(java.util.Set) */
	public void targets( Set<LabelStatement> labels ) {
		labels.add( tryStart );
		labels.add( tryEnd );
		labels.add( handlerStart );		
	}
}
