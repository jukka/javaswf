package com.anotherbigidea.flash.avm2.model;

import java.util.ArrayList;
import java.util.List;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.instruction.Instruction;
import com.anotherbigidea.flash.avm2.instruction.InstructionList;
import com.anotherbigidea.flash.avm2.instruction.InstructionTarget;

public class AVM2MethodBody {

    /** The max stack */
    public int maxStack = -1;
    
    /** The max register */
    public int maxRegisters = -1;
    
    /** The scope depth of the method */
    public int scopeDepth = -1;
    
    /** The max scope in the method */
    public int maxScope = -1;

    /** The method this body belongs to */
    public final AVM2Method method;
    
    /** The exception handlers in order of decreasing precedence */
    public final List<AVM2ExceptionHandler> exceptionHandlers = 
        new ArrayList<AVM2ExceptionHandler>();
    
    /** The traits of the activation object (if there is one) */
    public final AVM2Traits activationTraits = new AVM2Traits();
    
    /** The instructions */
    public final InstructionList instructions = new InstructionList();
    
    /*pkg*/ AVM2MethodBody( AVM2Method method ) {
        this.method = method;
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {
        if( maxStack < 0 ) return;
        
        context.mbodies.add( this );
        
        for( AVM2ExceptionHandler h : exceptionHandlers ) {
            h.initPool( context );
        }
        
        activationTraits.initPool( context );
        
        Instruction i = instructions.first();
        while( i != null ) {
            i.initPool( context );            
            i = i.next();
        }
    }
    
    /**
     * Write.
     */
    public void write( ABC.MethodBodies bodies, AVM2ABCFile.WriteContext context ) {
        
        int methodInfo = 0;
        for( int i = 0; i < context.methods.size(); i++ ) {
            if( method == context.methods.get( i ) ) {
                methodInfo = i;
                break;
            }
        }
        
        ABC.MethodBody mb = bodies.methodBody( methodInfo, maxStack, maxRegisters, scopeDepth, maxScope );
        
        ABC.Instructions ins = mb.instructions( -1 /*computed by ABCWriter*/ );
        if( ins != null ) {
            Instruction i = instructions.first();
            while( i != null ) {
                i.write( ins, context );
                i = i.next();
            }
            ins.done();
        }

        ABC.ExceptionHandlers exs = mb.exceptionHandlers( exceptionHandlers.size() );
        if( exs != null ) {
            for( AVM2ExceptionHandler h : exceptionHandlers ) {
                exs.exceptionHandler( h.start, h.end, h.handler, 
                                      (h.exceptionType == null) ? 0 : h.exceptionType.indexIn( context.pool ), 
                                      (h.name == null) ? 0 : h.name.indexIn( context.pool ));
            }
            exs.done();
        }
        
        //TODO:

        ABC.Traits ts = mb.traits( activationTraits.traits.size() );
        if( ts != null ) {
            activationTraits.write( ts, context );
        }
        
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        //if( maxStack == -1 ) return;  //this body does not exist
        
        out.println( "body {" );
        out.indent();
        
        out.println( "max-stack    : " + maxStack );
        out.println( "max-registers: " + maxRegisters );
        out.println( "max-scope    : " + maxScope );
        out.println( "scope-depth  : " + scopeDepth );
        out.println( );        
        
        Instruction in = instructions.first();
        boolean isTarget = false;
        while( in != null ) {
            if( in instanceof InstructionTarget ) {
                isTarget = true;
                out.writeField( ""+((InstructionTarget) in).label, 4, false );
                out.write( ": " );                
            } else {
                if( ! isTarget ) out.print( "      " );
                else isTarget = false;
                
                in.dump( out );
            }
            
            in = in.next();
        }
        
        out.println( );                
        for( AVM2ExceptionHandler handler : exceptionHandlers ) {
            handler.dump( out );
        }
        
        for( AVM2Trait trait : activationTraits.traits ) {
            out.print( "activation " );
            trait.dump( out );
        }
        
        out.unindent();
        out.println( "}" );        
    }
}
