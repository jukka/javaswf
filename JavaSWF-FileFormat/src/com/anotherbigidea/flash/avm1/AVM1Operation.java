package com.anotherbigidea.flash.avm1;

import java.io.IOException;

import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * An AVM1 Operation
 *
 * @author nickmain
 */
public abstract class AVM1Operation {

    /*pkg*/ AVM1Operation prev;
    /*pkg*/ AVM1Operation next;
    /*pkg*/ AVM1ActionBlock block;

    /**
     * Get the operation after this one
     * 
     * @return null if this is the last operation or it is not in a block, or
     *              has been aggregated
     */
    public final AVM1Operation next() {
        return next;
    }

    /**
     * Get the operation before this one
     * 
     * @return null if this is the first operation or it is not in a block, or
     *              has been aggregated
     */
    public final AVM1Operation prev() {
        return prev;
    }

    /**
     * Remove this operation from the block it is in
     */
    public final void remove() {
        if( block != null ) block.remove( this );
    }
    
    /**
     * Write the operation to a block
     */
    public abstract void write( SWFActionBlock block ) throws IOException;

    /**
     * Whether this operation evaluates to a non-zero value
     */
    public boolean isNonZero() {
        return true;
    }
    
    /**
     * Get the int value
     */
    public int intValue() {
        return 0;
    }
    
    /*
s
    //--Flash 5 Actions

    public SWFActionBlock startFunction( String name, String[] paramNames ) throws IOException;

    public void deleteProperty() throws IOException;
    public void deleteThreadVars() throws IOException;

    public void enumerate() throws IOException;
    public void getMember() throws IOException;

    public void setMember() throws IOException;
    public void getTargetPath() throws IOException;

    public SWFActionBlock startWith() throws IOException;


    public void decrement() throws IOException;
    public void increment() throws IOException;

    public void duplicate() throws IOException;
    public void returnValue() throws IOException;
    public void swap() throws IOException;
    public void storeInRegister( int registerNumber ) throws IOException;

    public void pushRegister( int registerNumber ) throws IOException;
    
    //--Flash 6 Actions
    public void enumerateObject() throws IOException;
    
    //--Flash 7 Actions
    public SWFActionBlock startFunction2( String name, 
            int numRegistersToAllocate,
            int preloadingFlags,
            String[] paramNames,
            int[] registersForArguments ) throws IOException;
    
    public void _throw() throws IOException;
    public void _extends() throws IOException;
    public void _implements() throws IOException;
    
    */
}
