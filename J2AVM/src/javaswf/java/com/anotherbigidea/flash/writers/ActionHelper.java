
package com.anotherbigidea.flash.writers;

import java.io.IOException;

import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Wrapper around SWFActionBlock that provides helper methods.
 * @author nmain
 */
public class ActionHelper implements SWFActionBlock {
    
    private SWFActionBlock mBlock;
    
    private int registerCount = 0;
    public int getRegisterCount() { return registerCount; }
    
    public ActionHelper( SWFActionBlock block ) {
        mBlock = block;
    }
    
    /** Get the named variable. */
    public void getVariable( String name ) throws IOException {
        push( name );
        getVariable();
    }
    
    /** Call the named method. */
    public void callMethod( String name ) throws IOException {
        push( name );
        callMethod();
    }

    /** Call the named function. */
    public void callFunction( String name ) throws IOException {
        push( name );
        callFunction();
    }
    
    //====================================================================
    public void _extends() throws IOException {
        mBlock._extends();
    }
    public void _implements() throws IOException {
        mBlock._implements();
    }
    public void _throw() throws IOException {
        mBlock._throw();
    }
    public TryCatchFinally _try(int catchRegisterNumber) throws IOException {
        return mBlock._try(catchRegisterNumber);
    }
    public TryCatchFinally _try(String catchVarName) throws IOException {
        return mBlock._try(catchVarName);
    }
    public void add() throws IOException {
        mBlock.add();
    }
    public void and() throws IOException {
        mBlock.and();
    }
    public void asciiToChar() throws IOException {
        mBlock.asciiToChar();
    }
    public void asciiToCharMB() throws IOException {
        mBlock.asciiToCharMB();
    }
    public void bitAnd() throws IOException {
        mBlock.bitAnd();
    }
    public void bitOr() throws IOException {
        mBlock.bitOr();
    }
    public void bitXor() throws IOException {
        mBlock.bitXor();
    }
    public void blob(byte[] blob) throws IOException {
        mBlock.blob(blob);
    }
    public void call() throws IOException {
        mBlock.call();
    }
    public void callFunction() throws IOException {
        mBlock.callFunction();
    }
    public void callMethod() throws IOException {
        mBlock.callMethod();
    }
    public void cast() throws IOException {
        mBlock.cast();
    }
    public void charMBToAscii() throws IOException {
        mBlock.charMBToAscii();
    }
    public void charToAscii() throws IOException {
        mBlock.charToAscii();
    }
    public void cloneSprite() throws IOException {
        mBlock.cloneSprite();
    }
    public void comment(String comment) throws IOException {
        mBlock.comment(comment);
    }
    public void concat() throws IOException {
        mBlock.concat();
    }
    public void convertToNumber() throws IOException {
        mBlock.convertToNumber();
    }
    public void convertToString() throws IOException {
        mBlock.convertToString();
    }
    public void decrement() throws IOException {
        mBlock.decrement();
    }
    public void defineLocal() throws IOException {
        mBlock.defineLocal();
    }
    public void defineLocalValue() throws IOException {
        mBlock.defineLocalValue();
    }
    public void deleteProperty() throws IOException {
        mBlock.deleteProperty();
    }
    public void deleteScopeProperty() throws IOException {
        mBlock.deleteScopeProperty();
    }
    public void divide() throws IOException {
        mBlock.divide();
    }
    public void duplicate() throws IOException {
        mBlock.duplicate();
    }
    public void end() throws IOException {
        mBlock.end();
    }
    public void endDrag() throws IOException {
        mBlock.endDrag();
    }
    public void enumerate() throws IOException {
        mBlock.enumerate();
    }
    public void enumerateObject() throws IOException {
        mBlock.enumerateObject();
    }
    public void equals() throws IOException {
        mBlock.equals();
    }
    public boolean equals(Object arg0) {
        return mBlock.equals(arg0);
    }
    public void getMember() throws IOException {
        mBlock.getMember();
    }
    public void getProperty() throws IOException {
        mBlock.getProperty();
    }
    public void getTargetPath() throws IOException {
        mBlock.getTargetPath();
    }
    public void getTime() throws IOException {
        mBlock.getTime();
    }
    public void getURL(GetURLMethod method, boolean loadVars, boolean targetSprite) throws IOException {
        mBlock.getURL( method, loadVars, targetSprite );
    }
    public void getURL(String url, String target) throws IOException {
        mBlock.getURL(url, target);
    }
    public void getVariable() throws IOException {
        mBlock.getVariable();
    }
    public void gotoFrame(boolean play) throws IOException {
        mBlock.gotoFrame(play);
    }
    public void gotoFrame(int frameNumber) throws IOException {
        mBlock.gotoFrame(frameNumber);
    }
    public void gotoFrame(String label) throws IOException {
        mBlock.gotoFrame(label);
    }
    public void greaterThan() throws IOException {
        mBlock.greaterThan();
    }
    public int hashCode() {
        return mBlock.hashCode();
    }
    public void ifJump(String jumpLabel) throws IOException {
        mBlock.ifJump(jumpLabel);
    }
    public void increment() throws IOException {
        mBlock.increment();
    }
    public void initArray() throws IOException {
        mBlock.initArray();
    }
    public void initObject() throws IOException {
        mBlock.initObject();
    }
    public void instanceOf() throws IOException {
        mBlock.instanceOf();
    }
    public void jump(String jumpLabel) throws IOException {
        mBlock.jump(jumpLabel);
    }
    public void jumpLabel(String label) throws IOException {
        mBlock.jumpLabel(label);
    }
    public void lessThan() throws IOException {
        mBlock.lessThan();
    }
    public void lookup(int dictionaryIndex) throws IOException {
        mBlock.lookup(dictionaryIndex);
    }
    public void lookupTable(String[] values) throws IOException {
        mBlock.lookupTable(values);
    }
    public void modulo() throws IOException {
        mBlock.modulo();
    }
    public void multiply() throws IOException {
        mBlock.multiply();
    }
    public void newMethod() throws IOException {
        mBlock.newMethod();
    }
    public void newObject() throws IOException {
        mBlock.newObject();
    }
    public void nextFrame() throws IOException {
        mBlock.nextFrame();
    }
    public void not() throws IOException {
        mBlock.not();
    }
    public void or() throws IOException {
        mBlock.or();
    }
    public void play() throws IOException {
        mBlock.play();
    }
    public void pop() throws IOException {
        mBlock.pop();
    }
    public void prevFrame() throws IOException {
        mBlock.prevFrame();
    }
    public void push(boolean value) throws IOException {
        mBlock.push(value);
    }
    public void push(double value) throws IOException {
        mBlock.push(value);
    }
    public void push(float value) throws IOException {
        mBlock.push(value);
    }
    public void push(int value) throws IOException {
        mBlock.push(value);
    }
    public void push(String value) throws IOException {
        mBlock.push(value);
    }
    public void pushNull() throws IOException {
        mBlock.pushNull();
    }
    public void pushRegister(int registerNumber) throws IOException {
        mBlock.pushRegister(registerNumber);
        if( registerNumber >= registerCount ) registerCount = registerNumber + 1;
    }
    public void pushUndefined() throws IOException {
        mBlock.pushUndefined();
    }
    public void randomNumber() throws IOException {
        mBlock.randomNumber();
    }
    public void removeSprite() throws IOException {
        mBlock.removeSprite();
    }
    public void returnValue() throws IOException {
        mBlock.returnValue();
    }
    public void setMember() throws IOException {
        mBlock.setMember();
    }
    public void setProperty() throws IOException {
        mBlock.setProperty();
    }
    public void setTarget() throws IOException {
        mBlock.setTarget();
    }
    public void setTarget(String target) throws IOException {
        mBlock.setTarget(target);
    }
    public void setVariable() throws IOException {
        mBlock.setVariable();
    }
    public void shiftLeft() throws IOException {
        mBlock.shiftLeft();
    }
    public void shiftRight() throws IOException {
        mBlock.shiftRight();
    }
    public void shiftRightUnsigned() throws IOException {
        mBlock.shiftRightUnsigned();
    }
    public void startDrag() throws IOException {
        mBlock.startDrag();
    }
    public SWFActionBlock startFunction(String name, String[] paramNames)
            throws IOException {
        return mBlock.startFunction(name, paramNames);
    }
    public SWFActionBlock startFunction2(String name,
            int numRegistersToAllocate, int preloadingFlags,
            String[] paramNames, int[] registersForArguments)
            throws IOException {
        return mBlock.startFunction2(name, numRegistersToAllocate,
                preloadingFlags, paramNames, registersForArguments);
    }
    public SWFActionBlock startWith() throws IOException {
        return mBlock.startWith();
    }
    public void stop() throws IOException {
        mBlock.stop();
    }
    public void stopSounds() throws IOException {
        mBlock.stopSounds();
    }
    public void storeInRegister(int registerNumber) throws IOException {
        mBlock.storeInRegister(registerNumber);
        if( registerNumber >= registerCount ) registerCount = registerNumber + 1;
    }
    public void strictEquals() throws IOException {
        mBlock.strictEquals();
    }
    public void stringEquals() throws IOException {
        mBlock.stringEquals();
    }
    public void stringGreaterThan() throws IOException {
        mBlock.stringGreaterThan();
    }
    public void stringLength() throws IOException {
        mBlock.stringLength();
    }
    public void stringLengthMB() throws IOException {
        mBlock.stringLengthMB();
    }
    public void stringLessThan() throws IOException {
        mBlock.stringLessThan();
    }
    public void substract() throws IOException {
        mBlock.substract();
    }
    public void substring() throws IOException {
        mBlock.substring();
    }
    public void substringMB() throws IOException {
        mBlock.substringMB();
    }
    public void swap() throws IOException {
        mBlock.swap();
    }
    public void toggleQuality() throws IOException {
        mBlock.toggleQuality();
    }
    public void toInteger() throws IOException {
        mBlock.toInteger();
    }
    public String toString() {
        return mBlock.toString();
    }
    public void trace() throws IOException {
        mBlock.trace();
    }
    public void typedAdd() throws IOException {
        mBlock.typedAdd();
    }
    public void typedEquals() throws IOException {
        mBlock.typedEquals();
    }
    public void typedLessThan() throws IOException {
        mBlock.typedLessThan();
    }
    public void typeOf() throws IOException {
        mBlock.typeOf();
    }
    public void unknown(int code, byte[] data) throws IOException {
        mBlock.unknown(code, data);
    }
    public void waitForFrame(int frameNumber, String jumpLabel)
            throws IOException {
        mBlock.waitForFrame(frameNumber, jumpLabel);
    }
    public void waitForFrame(String jumpLabel) throws IOException {
        mBlock.waitForFrame(jumpLabel);
    }
}
