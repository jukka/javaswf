package babelswf;

import static babelswf.BabelSWFRuntime.*;

import java.io.IOException;
import java.util.logging.Logger;

import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * A translator from AVM1 bytecode to AVM2 bytecode
 *
 * @author nickmain
 */
public class AVM1BytecodeTranslator implements SWFActionBlock {

	public static final Logger log = Logger.getLogger( AVMTranslator.class.getName() );
	
	private final AVM2Code code;
	
	private String[] lookupTable;
	
	public AVM1BytecodeTranslator( AVM2Code code ) {
		this.code = code;		
		code.trace( "** AVM1BytecodeTranslator **" );
	}
	
	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_extends() */
	public void _extends() throws IOException {
	    RT_Extends.generate( code );		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_implements() */
	public void _implements() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_throw() */
	public void _throw() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_try(int) */
	public TryCatchFinally _try( int catchRegisterNumber ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_try(java.lang.String) */
	public TryCatchFinally _try( String catchVarName ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#add() */
	public void add() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#and() */
	public void and() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#asciiToChar() */
	public void asciiToChar() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#asciiToCharMB() */
	public void asciiToCharMB() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#bitAnd() */
	public void bitAnd() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#bitOr() */
	public void bitOr() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#bitXor() */
	public void bitXor() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#blob(byte[]) */
	public void blob( byte[] blob ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#call() */
	public void call() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#callFunction() */
	public void callFunction() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#callMethod() */
	public void callMethod() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#cast() */
	public void cast() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#charMBToAscii() */
	public void charMBToAscii() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#charToAscii() */
	public void charToAscii() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#cloneSprite() */
	public void cloneSprite() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#comment(java.lang.String) */
	public void comment( String comment ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#concat() */
	public void concat() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#convertToNumber() */
	public void convertToNumber() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#convertToString() */
	public void convertToString() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#decrement() */
	public void decrement() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#defineLocal() */
	public void defineLocal() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#defineLocalValue() */
	public void defineLocalValue() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#deleteProperty() */
	public void deleteProperty() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#deleteScopeProperty() */
	public void deleteScopeProperty() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#divide() */
	public void divide() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#duplicate() */
	public void duplicate() throws IOException {
		code.dup();		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#end() */
	public void end() throws IOException {
		code.returnVoid();
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#endDrag() */
	public void endDrag() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#enumerate() */
	public void enumerate() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#enumerateObject() */
	public void enumerateObject() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#equals() */
	public void equals() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getMember() */
	public void getMember() throws IOException {
		RT_GetMember.generate( code );
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getProperty() */
	public void getProperty() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getTargetPath() */
	public void getTargetPath() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getTime() */
	public void getTime() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getURL(int, int) */
	public void getURL( int sendVars, int loadMode ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getURL(java.lang.String, java.lang.String) */
	public void getURL( String url, String target ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getVariable() */
	public void getVariable() throws IOException {
		RT_GetVariable.generate( code );
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#gotoFrame(boolean) */
	public void gotoFrame( boolean play ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#gotoFrame(int) */
	public void gotoFrame( int frameNumber ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#gotoFrame(java.lang.String) */
	public void gotoFrame( String label ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#greaterThan() */
	public void greaterThan() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#ifJump(java.lang.String) */
	public void ifJump( String jumpLabel ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#increment() */
	public void increment() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#initArray() */
	public void initArray() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#initObject() */
	public void initObject() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#instanceOf() */
	public void instanceOf() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#jump(java.lang.String) */
	public void jump( String jumpLabel ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#jumpLabel(java.lang.String) */
	public void jumpLabel( String label ) throws IOException {
		//throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		//FIXME
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#lessThan() */
	public void lessThan() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#lookup(int) */
	public void lookup( int dictionaryIndex ) throws IOException {
		push( lookupTable[ dictionaryIndex ] );		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#lookupTable(java.lang.String[]) */
	public void lookupTable( String[] values ) throws IOException {
		lookupTable = values;		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#modulo() */
	public void modulo() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#multiply() */
	public void multiply() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#newMethod() */
	public void newMethod() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#newObject() */
	public void newObject() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#nextFrame() */
	public void nextFrame() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#not() */
	public void not() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#or() */
	public void or() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#play() */
	public void play() throws IOException {
        RT_Play.generate( code );
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pop() */
	public void pop() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#prevFrame() */
	public void prevFrame() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(boolean) */
	public void push( boolean value ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(double) */
	public void push( double value ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(float) */
	public void push( float value ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(int) */
	public void push( int value ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(java.lang.String) */
	public void push( String value ) throws IOException {
		code.pushString( value );
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pushNull() */
	public void pushNull() throws IOException {
		code.pushNull();		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pushRegister(int) */
	public void pushRegister( int registerNumber ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pushUndefined() */
	public void pushUndefined() throws IOException {
		code.pushUndefined();		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#randomNumber() */
	public void randomNumber() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#removeSprite() */
	public void removeSprite() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#returnValue() */
	public void returnValue() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setMember() */
	public void setMember() throws IOException {
		RT_SetMember.generate( code );		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setProperty() */
	public void setProperty() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setTarget() */
	public void setTarget() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setTarget(java.lang.String) */
	public void setTarget( String target ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setVariable() */
	public void setVariable() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#shiftLeft() */
	public void shiftLeft() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#shiftRight() */
	public void shiftRight() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#shiftRightUnsigned() */
	public void shiftRightUnsigned() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startDrag() */
	public void startDrag() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startFunction(java.lang.String, java.lang.String[]) */
	public SWFActionBlock startFunction( String name, String[] paramNames )
			throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startFunction2(java.lang.String, int, int, java.lang.String[], int[]) */
	public SWFActionBlock startFunction2( String name,
			int numRegistersToAllocate, int preloadingFlags,
			String[] paramNames, int[] registersForArguments )
			throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startWith() */
	public SWFActionBlock startWith() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stop() */
	public void stop() throws IOException {
		RT_Stop.generate( code );
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stopSounds() */
	public void stopSounds() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#storeInRegister(int) */
	public void storeInRegister( int registerNumber ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#strictEquals() */
	public void strictEquals() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringEquals() */
	public void stringEquals() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringGreaterThan() */
	public void stringGreaterThan() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringLength() */
	public void stringLength() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringLengthMB() */
	public void stringLengthMB() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringLessThan() */
	public void stringLessThan() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#substract() */
	public void substract() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#substring() */
	public void substring() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#substringMB() */
	public void substringMB() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#swap() */
	public void swap() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#toggleQuality() */
	public void toggleQuality() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#toInteger() */
	public void toInteger() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#trace() */
	public void trace() throws IOException {
		RT_Trace.generate( code );		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typedAdd() */
	public void typedAdd() throws IOException {
	    code.add();		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typedEquals() */
	public void typedEquals() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typedLessThan() */
	public void typedLessThan() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typeOf() */
	public void typeOf() throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#unknown(int, byte[]) */
	public void unknown( int code, byte[] data ) throws IOException {
		throw new RuntimeException( "Unknown AVM1 bytecode " + code );		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#waitForFrame(int, java.lang.String) */
	public void waitForFrame( int frameNumber, String jumpLabel ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}

	/** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#waitForFrame(java.lang.String) */
	public void waitForFrame( String jumpLabel ) throws IOException {
		throw new RuntimeException( "UNIMPLEMENTED AVM1 OPERATION" ); // FIXME
		
	}
}
