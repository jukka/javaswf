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
package com.anotherbigidea.flash.writers;

import java.io.IOException;

import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Pass-thru implementation of SWFActionBlock.
 * 
 * @author nmain
 */
public class SWFActionBlockImpl implements SWFActionBlock {
    
    protected SWFActionBlock acts;
    
    /**
     * @param acts may be null
     */
    public SWFActionBlockImpl( SWFActionBlock acts ){
        this.acts = acts;
    }

    public SWFActionBlockImpl(){
        this( null );
    }
    
    /**
     * Set the pass-through target
     * @param acts may be null
     */
    public void setSWFActionBlock( SWFActionBlock acts ){
        this.acts = acts;
    }
    
    public void end() throws IOException
    {
        if( acts != null ) acts.end();
    }
    
    public void blob( byte[] blob ) throws IOException
    {
        if( acts != null ) acts.blob( blob );
    }    
    
    public void unknown( int code, byte[] data ) throws IOException
    {
        if( acts != null ) acts.unknown( code, data );
    }
    
    public void initArray() throws IOException 
    {
        if( acts != null ) acts.initArray();
    }    
    
    public void jumpLabel( String label ) throws IOException
    {
        if( acts != null ) acts.jumpLabel( label );
    }    
    
    public void gotoFrame( int frameNumber ) throws IOException
    {
        if( acts != null ) acts.gotoFrame( frameNumber );
    }
    
    public void gotoFrame( String label ) throws IOException
    {
        if( acts != null ) acts.gotoFrame( label );
    }
    
    public void getURL( String url, String target ) throws IOException
    {
        if( acts != null ) acts.getURL( url, target );
    }
    
    public void nextFrame() throws IOException
    {
        if( acts != null ) acts.nextFrame();
    }
    
    public void prevFrame() throws IOException
    {
        if( acts != null ) acts.prevFrame();
    }
    
    public void play() throws IOException
    {
        if( acts != null ) acts.play();
    }
    
    public void stop() throws IOException
    {
        if( acts != null ) acts.stop();
    }
    
    public void toggleQuality() throws IOException
    {
        if( acts != null ) acts.toggleQuality();
    }
    
    public void stopSounds() throws IOException
    {
        if( acts != null ) acts.stopSounds();
    }
    
    public void setTarget( String target ) throws IOException
    {
        if( acts != null ) acts.setTarget( target );
    }
    
    public void jump( String jumpLabel ) throws IOException
    {
        if( acts != null ) acts.jump( jumpLabel );
    }
    
    public void ifJump( String jumpLabel ) throws IOException
    {
        if( acts != null ) acts.ifJump( jumpLabel );
    }
    
    public void waitForFrame( int frameNumber, String jumpLabel ) throws IOException
    {
        if( acts != null ) acts.waitForFrame( frameNumber, jumpLabel );
    }
    
    public void waitForFrame( String jumpLabel ) throws IOException
    {
        if( acts != null ) acts.waitForFrame( jumpLabel );
    }
    
    public void pop() throws IOException
    {
        if( acts != null ) acts.pop();
    }
    
    public void push( String value ) throws IOException
    {
        if( acts != null ) acts.push( value );
    }
    
    public void push( float  value ) throws IOException
    {
        if( acts != null ) acts.push( value );
    }
    
    public void push( double value ) throws IOException
    {
        if( acts != null ) acts.push( value );
    }
    
    public void pushNull() throws IOException
    {
        if( acts != null ) acts.pushNull();
    }
    
    public void pushRegister( int registerNumber ) throws IOException
    {
        if( acts != null ) acts.pushRegister( registerNumber );
    }
    
    public void push( boolean value ) throws IOException
    {
        if( acts != null ) acts.push( value );
    }
    
    public void push( int value ) throws IOException
    {
        if( acts != null ) acts.push( value );
    }
    
    public void lookup( int dictionaryIndex ) throws IOException
    {
        if( acts != null ) acts.lookup( dictionaryIndex );
    }
    
    public void add() throws IOException
    {
        if( acts != null ) acts.add();
    }
    
    public void substract() throws IOException
    {
        if( acts != null ) acts.substract();
    }
    
    public void multiply() throws IOException
    {
        if( acts != null ) acts.multiply();
    }
    
    public void divide() throws IOException
    {
        if( acts != null ) acts.divide();
    }
    
    public void equals() throws IOException
    {
        if( acts != null ) acts.equals();
    }
    
    public void lessThan() throws IOException
    {
        if( acts != null ) acts.lessThan();
    }
    
    public void and() throws IOException
    {
        if( acts != null ) acts.and();
    }
    
    public void or() throws IOException
    {
        if( acts != null ) acts.or();
    }
    
    public void not() throws IOException
    {
        if( acts != null ) acts.not();
    }
    
    public void stringEquals() throws IOException
    {
        if( acts != null ) acts.stringEquals();
    }
    
    public void stringLength() throws IOException
    {
        if( acts != null ) acts.stringLength();
    }
    
    public void concat() throws IOException
    {
        if( acts != null ) acts.concat();
    }
    
    public void substring() throws IOException
    {
        if( acts != null ) acts.substring();
    }
    
    public void stringLessThan() throws IOException
    {
        if( acts != null ) acts.stringLessThan();
    }
    
    public void stringLengthMB() throws IOException
    {
        if( acts != null ) acts.stringLengthMB();
    }
    
    public void substringMB() throws IOException
    {
        if( acts != null ) acts.substringMB();
    }
    
    public void toInteger() throws IOException
    {
        if( acts != null ) acts.toInteger();
    }
    
    public void charToAscii() throws IOException
    {
        if( acts != null ) acts.charToAscii();
    }
    
    public void asciiToChar() throws IOException
    {
        if( acts != null ) acts.asciiToChar();
    }
    
    public void charMBToAscii() throws IOException
    {
        if( acts != null ) acts.charMBToAscii();
    }
    
    public void asciiToCharMB() throws IOException
    {
        if( acts != null ) acts.asciiToCharMB();
    }
    
    public void call() throws IOException
    {
        if( acts != null ) acts.call();
    }
    
    public void getVariable() throws IOException
    {
        if( acts != null ) acts.getVariable();
    }
    
    public void setVariable() throws IOException
    {
        if( acts != null ) acts.setVariable();
    }
    
    public void getURL(GetURLMethod method, boolean loadVars, boolean targetSprite) throws IOException {
        if( acts != null ) acts.getURL( method, loadVars, targetSprite );
    }

    public void gotoFrame( boolean play ) throws IOException
    {
        if( acts != null ) acts.gotoFrame( play );
    }
    
    public void setTarget() throws IOException
    {
        if( acts != null ) acts.setTarget();
    }
    
    public void getProperty() throws IOException
    {
        if( acts != null ) acts.getProperty();
    }
    
    public void setProperty() throws IOException
    {
        if( acts != null ) acts.setProperty();
    }
    
    public void cloneSprite() throws IOException
    {
        if( acts != null ) acts.cloneSprite();
    }
    
    public void removeSprite() throws IOException
    {
        if( acts != null ) acts.removeSprite();
    }
    
    public void startDrag() throws IOException
    {
        if( acts != null ) acts.startDrag();
    }
    
    public void endDrag() throws IOException
    {
        if( acts != null ) acts.endDrag();
    }
    
    public void trace() throws IOException
    {
        if( acts != null ) acts.trace();
    }
    
    public void getTime() throws IOException
    {
        if( acts != null ) acts.getTime();
    }
    
    public void randomNumber() throws IOException
    {
        if( acts != null ) acts.randomNumber();
    }    
    
    public void lookupTable( String[] values ) throws IOException
    {
        if( acts != null ) acts.lookupTable( values );
    }
    
    public void callFunction() throws IOException
    {
        if( acts != null ) acts.callFunction();
    }    
    
    public void callMethod() throws IOException
    {
        if( acts != null ) acts.callMethod();
    }        
    
    public SWFActionBlock startFunction( String name, String[] paramNames ) throws IOException
    {
        if( acts != null ) return acts.startFunction( name, paramNames );
        return null;
    }    
    
    public void comment( String comment ) throws IOException
    {
        if( acts != null ) acts.comment( comment );
    }
    
    public void defineLocalValue() throws IOException
    {
        if( acts != null ) acts.defineLocalValue();
    }    
    
    public void defineLocal() throws IOException
    {
        if( acts != null ) acts.defineLocal();
    }      
    
    public void deleteProperty() throws IOException
    {
        if( acts != null ) acts.deleteProperty();
    }    
    
    public void deleteScopeProperty() throws IOException
    {
        if( acts != null ) acts.deleteScopeProperty();
    }    
    
    public void enumerate() throws IOException
    {
        if( acts != null ) acts.enumerate();
    }    
    
    public void typedEquals() throws IOException
    {
        if( acts != null ) acts.typedEquals();
    }    
    
    public void getMember() throws IOException
    {
        if( acts != null ) acts.getMember();
    }    
    
    public void initObject() throws IOException
    {
        if( acts != null ) acts.initObject();
    }    
    
    public void newMethod() throws IOException
    {
        if( acts != null ) acts.newMethod();
    }    
    
    public void newObject() throws IOException
    {
        if( acts != null ) acts.newObject();
    }    
    
    public void setMember() throws IOException
    {
        if( acts != null ) acts.setMember();
    }    
    
    public void getTargetPath() throws IOException
    {
        if( acts != null ) acts.getTargetPath();
    }   
    
    public SWFActionBlock startWith() throws IOException
    {       
        if( acts != null ) return acts.startWith();
        return null;
    }   
    
    public void duplicate() throws IOException
    {
        if( acts != null ) acts.duplicate();
    }   
    
    public void returnValue() throws IOException
    {
        if( acts != null ) acts.returnValue();
    }   
    
    public void swap() throws IOException
    {
        if( acts != null ) acts.swap();
    }   
    
    public void storeInRegister( int registerNumber ) throws IOException
    {
        if( acts != null ) acts.storeInRegister( registerNumber );
    }   
    
    public void convertToNumber() throws IOException
    {
        if( acts != null ) acts.convertToNumber();
    }   
    
    public void convertToString() throws IOException
    {
        if( acts != null ) acts.convertToString();
    }   
    
    public void typeOf() throws IOException
    {
        if( acts != null ) acts.typeOf();
    }   
    
    public void typedAdd() throws IOException
    {
        if( acts != null ) acts.typedAdd();
    }   
    
    public void typedLessThan() throws IOException
    {
        if( acts != null ) acts.typedLessThan();
    }   
    
    public void modulo() throws IOException
    {
        if( acts != null ) acts.modulo();
    }   
    
    public void bitAnd() throws IOException
    {
        if( acts != null ) acts.bitAnd();
    }   
    
    public void bitOr() throws IOException
    {
        if( acts != null ) acts.bitOr();
    }   
    
    public void bitXor() throws IOException
    {
        if( acts != null ) acts.bitXor();
    }   
    
    public void shiftLeft() throws IOException
    {
        if( acts != null ) acts.shiftLeft();
    }   
    
    public void shiftRight() throws IOException
    {
        if( acts != null ) acts.shiftRight();
    }   
    
    public void shiftRightUnsigned() throws IOException
    {
        if( acts != null ) acts.shiftRightUnsigned();
    }   
    
    public void decrement() throws IOException
    {
        if( acts != null ) acts.decrement();
    }   
    
    public void increment() throws IOException
    {
        if( acts != null ) acts.increment();
    }   

    public void enumerateObject() throws IOException {
        if( acts != null ) acts.enumerateObject();
    }

    public void greaterThan() throws IOException {
        if( acts != null ) acts.greaterThan();
    }

    public void instanceOf() throws IOException {
        if( acts != null ) acts.instanceOf();
    }

    public void strictEquals() throws IOException {
        if( acts != null ) acts.strictEquals();
    }

    public void stringGreaterThan() throws IOException {
        if( acts != null ) acts.stringGreaterThan();
    }
    
    public void pushUndefined() throws IOException {
        if( acts != null ) acts.pushUndefined();
    }

    public SWFActionBlock startFunction2(String name, int numRegistersToAllocate, int preloadingFlags, String[] paramNames, int[] registersForArguments) throws IOException {
        if( acts != null ) return acts.startFunction2( name, numRegistersToAllocate, preloadingFlags, paramNames, registersForArguments );
        return null;
    }
    
    
    public void _extends() throws IOException {
        if( acts != null ) acts._extends();
    }

    public void _implements() throws IOException {
        if( acts != null ) acts._implements();
    }

    public void _throw() throws IOException {
        if( acts != null ) acts._throw();
    }

    public TryCatchFinally _try( String catchVar ) throws IOException {
        if( acts != null ) return acts._try( catchVar );
        return null;
    }

    public TryCatchFinally _try( int catchReg ) throws IOException {
        if( acts != null ) return acts._try( catchReg );
        return null;
    }
    
    public void cast() throws IOException {
        if( acts != null ) acts.cast();
    }
}
