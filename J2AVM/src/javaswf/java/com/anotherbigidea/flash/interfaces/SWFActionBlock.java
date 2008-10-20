/****************************************************************
 * Copyright (c) 2003, David N. Main, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the 
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer. 
 * 
 * 2. Redistributions in binary form must reproduce the above 
 * copyright notice, this list of conditions and the following 
 * disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 * 3. The name of the author may not be used to endorse or 
 * promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************/
package com.anotherbigidea.flash.interfaces;

import java.io.IOException;

/**
 * A block of actions.
 * 
 * end() is called at the end of the block.
 * 
 * @author nmain
 */
public interface SWFActionBlock {

    public static enum GetURLMethod { MethodNone, MethodGet, MethodPost };
    
    /**
     * End of actions
     */
    public void end() throws IOException;

    /**
     * Pass through a blob of actions
     */
    public void blob( byte[] blob ) throws IOException;

    /**
     * Unrecognized action code
     * @param data may be null
     */
    public void unknown( int code, byte[] data ) throws IOException;

    /**
     * Target label for a jump - this method call immediately precedes the
     * target action.
     */
    public void jumpLabel( String label ) throws IOException;

    /**
     * Comment Text - useful for debugging purposes
     */
    public void comment( String comment ) throws IOException;

    //--Flash 3 Actions:
    public void gotoFrame( int frameNumber ) throws IOException;
    public void gotoFrame( String label ) throws IOException;
    public void getURL( String url, String target ) throws IOException;
    public void nextFrame() throws IOException;
    public void prevFrame() throws IOException;
    public void play() throws IOException;
    public void stop() throws IOException;
    public void toggleQuality() throws IOException;
    public void stopSounds() throws IOException;
    public void waitForFrame( int frameNumber, String jumpLabel ) throws IOException;
    public void setTarget( String target ) throws IOException;

    //--Flash 4 Actions:
    public void push( String value ) throws IOException;
    public void push( float  value ) throws IOException;
    public void pop() throws IOException;

    public void add() throws IOException;
    public void substract() throws IOException;
    public void multiply() throws IOException;
    public void divide() throws IOException;

    public void equals() throws IOException;
    public void lessThan() throws IOException;

    public void and() throws IOException;
    public void or() throws IOException;
    public void not() throws IOException;

    public void stringEquals() throws IOException;
    public void stringLength() throws IOException;
    public void concat() throws IOException;
    public void substring() throws IOException;
    public void stringLessThan() throws IOException;
    public void stringLengthMB() throws IOException;
    public void substringMB() throws IOException;

    public void toInteger() throws IOException;
    public void charToAscii() throws IOException;
    public void asciiToChar() throws IOException;
    public void charMBToAscii() throws IOException;
    public void asciiToCharMB() throws IOException;

    public void jump( String jumpLabel ) throws IOException;
    public void ifJump( String jumpLabel ) throws IOException;

    public void call() throws IOException;

    public void getVariable() throws IOException;
    public void setVariable() throws IOException;

    public void getURL( GetURLMethod method, boolean loadVars, boolean targetSprite ) throws IOException;

    public void gotoFrame( boolean play ) throws IOException;
    public void setTarget() throws IOException;
    public void getProperty() throws IOException;
    public void setProperty() throws IOException;
    public void cloneSprite() throws IOException;
    public void removeSprite() throws IOException;
    public void startDrag() throws IOException;
    public void endDrag() throws IOException;
    public void waitForFrame( String jumpLabel ) throws IOException;
    public void trace() throws IOException;
    public void getTime() throws IOException;
    public void randomNumber() throws IOException;

    //--Flash 5 Actions
    public void callFunction() throws IOException;
    public void callMethod() throws IOException;
    public void lookupTable( String[] values ) throws IOException;

    public SWFActionBlock startFunction( String name, String[] paramNames ) throws IOException;

    public void defineLocalValue() throws IOException;
    public void defineLocal() throws IOException;

    public void deleteProperty() throws IOException;
    public void deleteScopeProperty() throws IOException;

    public void enumerate() throws IOException;
    public void typedEquals() throws IOException;
    public void getMember() throws IOException;

    public void initArray() throws IOException;
    public void initObject() throws IOException;
    public void newMethod() throws IOException;
    public void newObject() throws IOException;
    public void setMember() throws IOException;
    public void getTargetPath() throws IOException;

    public SWFActionBlock startWith() throws IOException;

    public void convertToNumber() throws IOException;
    public void convertToString() throws IOException;
    public void typeOf() throws IOException;
    public void typedAdd() throws IOException;
    public void typedLessThan() throws IOException;
    public void modulo() throws IOException;

    public void bitAnd() throws IOException;
    public void bitOr() throws IOException;
    public void bitXor() throws IOException;
    public void shiftLeft() throws IOException;
    public void shiftRight() throws IOException;
    public void shiftRightUnsigned() throws IOException;

    public void decrement() throws IOException;
    public void increment() throws IOException;

    public void duplicate() throws IOException;
    public void returnValue() throws IOException;
    public void swap() throws IOException;
    public void storeInRegister( int registerNumber ) throws IOException;

    public void push( double value ) throws IOException;
    public void pushNull() throws IOException;
    public void pushUndefined() throws IOException;
    public void pushRegister( int registerNumber ) throws IOException;
    public void push( boolean value ) throws IOException;
    public void push( int value ) throws IOException;
    public void lookup( int dictionaryIndex ) throws IOException;
    
    //--Flash 6 Actions
    public void instanceOf() throws IOException;
    public void enumerateObject() throws IOException;
    public void strictEquals() throws IOException;
    public void greaterThan() throws IOException;
    public void stringGreaterThan() throws IOException;    
    
    //--Flash 7 Actions
    public SWFActionBlock startFunction2( String name, 
            int numRegistersToAllocate,
            int preloadingFlags,
            String[] paramNames,
            int[] registersForArguments ) throws IOException;
    
    public void _throw() throws IOException;
    public void _extends() throws IOException;
	public void _implements() throws IOException;
	public void cast() throws IOException;
	
	/** @param catchVarName may be null if there is no catch block. */
	public SWFActionBlock.TryCatchFinally _try( String catchVarName ) throws IOException;
	public SWFActionBlock.TryCatchFinally _try( int catchRegisterNumber ) throws IOException;
	
	/**
	 * Interface to pass a try/catch/finally block.
	 * Call sequence is:
	 *   tryBlock()
	 *   optionally catchBlock()
	 *   optionally finallyBlock()
	 *   endTry()
	 * There must be a catchBlock or a finallyBlock, or both.
	 */
	public interface TryCatchFinally {
		public SWFActionBlock tryBlock() throws IOException;
		public SWFActionBlock catchBlock() throws IOException;
		public SWFActionBlock finallyBlock() throws IOException;
		public void endTry() throws IOException;
	}
}
