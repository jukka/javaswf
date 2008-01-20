package com.anotherbigidea.flash.avm1;

import static com.anotherbigidea.flash.avm1.ops.BinaryOpType.*;
import static com.anotherbigidea.flash.avm1.ops.UnaryOpType.*;

import java.io.IOException;

import com.anotherbigidea.flash.avm1.ops.*;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * SWFActionBlock implementation that builds an AVM1ActionBlock
 *
 * @author nickmain
 */
public class AVM1BlockBuilder implements SWFActionBlock {

    private final AVM1ActionBlock block;
    
    private String[] lookupTable;
    
    public AVM1BlockBuilder( AVM1ActionBlock block ) {
        this.block = block;
    }

    private AVM1BlockBuilder( AVM1ActionBlock block, String[] lookupTable ) {
        this.block = block;
        this.lookupTable = lookupTable;
    }
    
    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_extends() */
    public void _extends() throws IOException {
        block.append( new Extends() );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_implements() */
    public void _implements() throws IOException {
        block.append( new Implements() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_throw() */
    public void _throw() throws IOException {
        block.append( new ThrowException() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_try(int) */
    public TryCatchFinally _try(int catchRegisterNumber) throws IOException {
        return _try( catchRegisterNumber, null );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#_try(java.lang.String) */
    public TryCatchFinally _try(String catchVarName) throws IOException {
        return _try( -1, catchVarName );
    }
           
    private TryCatchFinally _try( final int catchRegisterNumber,
                                  final String catchVarName ) {
        
        final Try tryOp = new Try();
        block.append( tryOp );
        
        return new TryCatchFinally() {

            public SWFActionBlock tryBlock() { 
                return new AVM1BlockBuilder( block, lookupTable ); 
            }
            
            public SWFActionBlock catchBlock() {
                tryOp.tryCatch = (catchVarName != null) ?
                                     new TryCatch( tryOp, catchVarName ) :
                                     new TryCatch( tryOp, catchRegisterNumber );
                
                block.append( tryOp.tryCatch );
                return new AVM1BlockBuilder( block, lookupTable ); 
            }
            
            public SWFActionBlock finallyBlock() { 
                tryOp.tryFinally = new TryFinally( tryOp );
                block.append( tryOp.tryFinally );                
                return new AVM1BlockBuilder( block, lookupTable ); 
            }
            
            public void endTry() {
                tryOp.tryEnd = new TryEnd( tryOp );
                block.append( tryOp.tryEnd );                
            }
        };
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startWith() */
    public SWFActionBlock startWith() throws IOException {

        final With with = new With();
        block.append( with );
        
        return new AVM1BlockBuilder( block, lookupTable ) {
            @Override public void end() throws IOException {
                with.withEnd = new WithEnd( with );
                block.append( with.withEnd );
            }
        };        
    }
    
    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#add() */
    public void add() throws IOException {
        block.append( new BinaryOp( BinOp_Add ) );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#and() */
    public void and() throws IOException {
        block.append( new BinaryOp( BinOp_And ) );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#asciiToChar() */
    public void asciiToChar() throws IOException {
        block.append( new UnaryOp( UnOp_AsciiToChar ) );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#asciiToCharMB() */
    public void asciiToCharMB() throws IOException {
        block.append( new UnaryOp( UnOp_AsciiToCharMB ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#bitAnd() */
    public void bitAnd() throws IOException {
        block.append( new BinaryOp( BinOp_BitAnd ) );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#bitOr() */
    public void bitOr() throws IOException {
        block.append( new BinaryOp( BinOp_BitOr ) );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#bitXor() */
    public void bitXor() throws IOException {
        block.append( new BinaryOp( BinOp_BitXor ) );                
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#blob(byte[]) */
    public void blob(byte[] blob) throws IOException {
        throw new IOException( "blob not supported" );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#call() */
    public void call() throws IOException {
        block.append( new CallFrame() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#callFunction() */
    public void callFunction() throws IOException {
        block.append( new CallFunction() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#callMethod() */
    public void callMethod() throws IOException {
        block.append( new CallMethod() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#cast() */
    public void cast() throws IOException {
        block.append( new BinaryOp( BinOp_Cast ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#charMBToAscii() */
    public void charMBToAscii() throws IOException {
        block.append( new UnaryOp( UnOp_CharMBToAscii ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#charToAscii() */
    public void charToAscii() throws IOException {
        block.append( new UnaryOp( UnOp_CharToAscii ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#cloneSprite() */
    public void cloneSprite() throws IOException {
        block.append( new CloneSprite() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#comment(java.lang.String) */
    public void comment(String comment) throws IOException {
        // nothing        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#concat() */
    public void concat() throws IOException {
        block.append( new BinaryOp( BinOp_Concat ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#convertToNumber() */
    public void convertToNumber() throws IOException {
        block.append( new UnaryOp( UnOp_ConvertToNumber ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#convertToString() */
    public void convertToString() throws IOException {
        block.append( new UnaryOp( UnOp_ConvertToString ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#decrement() */
    public void decrement() throws IOException {
        block.append( new Decrement() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#defineLocal() */
    public void defineLocal() throws IOException {
        block.append( new DefineLocal() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#defineLocalValue() */
    public void defineLocalValue() throws IOException {
        block.append( new DefineLocalValue() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#deleteProperty() */
    public void deleteProperty() throws IOException {
        block.append( new DeleteProperty() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#deleteScopeProperty() */
    public void deleteScopeProperty() throws IOException {
        block.append( new DeleteScopeProperty() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#divide() */
    public void divide() throws IOException {
        block.append( new BinaryOp( BinOp_Divide ) );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#duplicate() */
    public void duplicate() throws IOException {
        block.append( new Duplicate() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#end() */
    public void end() throws IOException {
        block.complete();
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#endDrag() */
    public void endDrag() throws IOException {
        block.append( new EndDrag() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#enumerate() */
    public void enumerate() throws IOException {
        block.append( new Enumerate( true ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#enumerateObject() */
    public void enumerateObject() throws IOException {
        block.append( new Enumerate( false ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#equals() */
    public void equals() throws IOException {
        block.append( new BinaryOp( BinOp_Equals ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getMember() */
    public void getMember() throws IOException {
        block.append( new GetMember() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getProperty() */
    public void getProperty() throws IOException {
        block.append( new GetProperty() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getTargetPath() */
    public void getTargetPath() throws IOException {
        block.append( new GetTargetPath() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getTime() */
    public void getTime() throws IOException {
        block.append( new GetTime() );
    }
    
    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getURL(com.anotherbigidea.flash.interfaces.SWFActionBlock.GetURLMethod, boolean, boolean) */
    public void getURL(GetURLMethod method, boolean loadVars, boolean targetSprite) throws IOException {
        block.append( new GetURL( targetSprite, loadVars, method ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getURL(java.lang.String, java.lang.String) */
    public void getURL(String url, String target) throws IOException {
        block.append( new GetURL( url, target ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getVariable() */
    public void getVariable() throws IOException {
        block.append( new GetVariable() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#gotoFrame(boolean) */
    public void gotoFrame(boolean play) throws IOException {
        block.append( GotoFrame.andPlay( play ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#gotoFrame(int) */
    public void gotoFrame(int frameNumber) throws IOException {
        block.append( GotoFrame.number( frameNumber ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#gotoFrame(java.lang.String) */
    public void gotoFrame(String label) throws IOException {
        block.append( GotoFrame.label( label ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#greaterThan() */
    public void greaterThan() throws IOException {
        block.append( new BinaryOp( BinOp_GreaterThan ) );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#ifJump(java.lang.String) */
    public void ifJump(String jumpLabel) throws IOException {
        block.append( new IfJump( jumpLabel ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#increment() */
    public void increment() throws IOException {
        block.append( new Increment() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#initArray() */
    public void initArray() throws IOException {
        block.append( new InitArray() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#initObject() */
    public void initObject() throws IOException {
        block.append( new InitObject() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#instanceOf() */
    public void instanceOf() throws IOException {
        block.append( new BinaryOp( BinOp_InstanceOf ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#jump(java.lang.String) */
    public void jump(String jumpLabel) throws IOException {
        block.append( new Jump( jumpLabel ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#jumpLabel(java.lang.String) */
    public void jumpLabel(String label) throws IOException {
        AVM1Operation op = new JumpLabel( label );
        block.append( op );
        
        //if the previous was a function then this label should also go at
        //the end of the body
        System.err.println( label + " ---> " + op.prev );
        if( op.prev != null && op.prev instanceof Function ) {
            ((Function) op.prev).body.append( new JumpLabel( label ) );            
        }
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#lessThan() */
    public void lessThan() throws IOException {
        block.append( new BinaryOp( BinOp_LessThan ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#lookup(int) */
    public void lookup(int dictionaryIndex) throws IOException {
        block.append( new ConstantOp.StringValue( lookupTable[ dictionaryIndex ] ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#lookupTable(java.lang.String[]) */
    public void lookupTable(String[] values) throws IOException {
        lookupTable = values;
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#modulo() */
    public void modulo() throws IOException {
        block.append( new BinaryOp( BinOp_Modulo ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#multiply() */
    public void multiply() throws IOException {
        block.append( new BinaryOp( BinOp_Multiply ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#newMethod() */
    public void newMethod() throws IOException {
        block.append( new NewMethod() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#newObject() */
    public void newObject() throws IOException {
        block.append( new NewObject() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#nextFrame() */
    public void nextFrame() throws IOException {
        block.append( new NextFrame() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#not() */
    public void not() throws IOException {
        block.append( new UnaryOp( UnOp_Not ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#or() */
    public void or() throws IOException {
        block.append( new BinaryOp( BinOp_Or ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#play() */
    public void play() throws IOException {
        block.append( new Play() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pop() */
    public void pop() throws IOException {
        block.append( new Pop() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#prevFrame() */
    public void prevFrame() throws IOException {
        block.append( new PrevFrame() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(boolean) */
    public void push(boolean value) throws IOException {
        block.append( new ConstantOp.BooleanValue( value ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(double) */
    public void push(double value) throws IOException {
        block.append( new ConstantOp.DoubleValue( value ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(float) */
    public void push(float value) throws IOException {
        block.append( new ConstantOp.FloatValue( value ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(int) */
    public void push(int value) throws IOException {
        block.append( new ConstantOp.IntValue( value ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#push(java.lang.String) */
    public void push(String value) throws IOException {
        block.append( new ConstantOp.StringValue( value ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pushNull() */
    public void pushNull() throws IOException {
        block.append( new ConstantOp.NullValue() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pushRegister(int) */
    public void pushRegister(int registerNumber) throws IOException {
        block.append( new PushRegister( registerNumber ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#pushUndefined() */
    public void pushUndefined() throws IOException {
        block.append( new ConstantOp.UndefinedValue() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#randomNumber() */
    public void randomNumber() throws IOException {
        block.append( new RandomNumber() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#removeSprite() */
    public void removeSprite() throws IOException {
        block.append( new RemoveSprite() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#returnValue() */
    public void returnValue() throws IOException {
        block.append( new ReturnValue() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setMember() */
    public void setMember() throws IOException {
        block.append( new SetMember() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setProperty() */
    public void setProperty() throws IOException {
        block.append( new SetProperty() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setTarget() */
    public void setTarget() throws IOException {
        block.append( new SetTarget() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setTarget(java.lang.String) */
    public void setTarget(String target) throws IOException {
        block.append( new SetTarget( target ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#setVariable() */
    public void setVariable() throws IOException {
        block.append( new SetVariable() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#shiftLeft() */
    public void shiftLeft() throws IOException {
        block.append( new BinaryOp( BinOp_ShiftLeft ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#shiftRight() */
    public void shiftRight() throws IOException {
        block.append( new BinaryOp( BinOp_ShiftRight ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#shiftRightUnsigned() */
    public void shiftRightUnsigned() throws IOException {
        block.append( new BinaryOp( BinOp_ShiftRightUnsigned ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startDrag() */
    public void startDrag() throws IOException {
        block.append( new StartDrag() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startFunction(java.lang.String, java.lang.String[]) */
    public SWFActionBlock startFunction(String name, String[] paramNames) throws IOException {
        return startFunction2( name, 4, 0, paramNames, null );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#startFunction2(java.lang.String, int, int, java.lang.String[], int[]) */
    public SWFActionBlock startFunction2( String name,
                                          int numRegistersToAllocate, int preloadingFlags,
                                          String[] paramNames, int[] registersForArguments)
        throws IOException {
        
        Function func = ( name.length() == 0 ) ? 
            new AnonymousFunction( numRegistersToAllocate, paramNames, registersForArguments, preloadingFlags ) :
            new Function( name, numRegistersToAllocate, paramNames, registersForArguments, preloadingFlags );
        
        block.append( func );
        
        return new AVM1BlockBuilder( func.body, lookupTable );
    }


    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stop() */
    public void stop() throws IOException {
        block.append( new Stop() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stopSounds() */
    public void stopSounds() throws IOException {
        block.append( new StopSounds() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#storeInRegister(int) */
    public void storeInRegister(int registerNumber) throws IOException {
        block.append( new StoreInRegister( registerNumber ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#strictEquals() */
    public void strictEquals() throws IOException {
        block.append( new BinaryOp( BinOp_StrictEquals ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringEquals() */
    public void stringEquals() throws IOException {
        block.append( new BinaryOp( BinOp_StringEquals ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringGreaterThan() */
    public void stringGreaterThan() throws IOException {
        block.append( new BinaryOp( BinOp_StringGreaterThan ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringLength() */
    public void stringLength() throws IOException {
        block.append( new UnaryOp( UnOp_StringLength ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringLengthMB() */
    public void stringLengthMB() throws IOException {
        block.append( new UnaryOp( UnOp_StringLengthMB ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#stringLessThan() */
    public void stringLessThan() throws IOException {
        block.append( new BinaryOp( BinOp_StringLessThan ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#substract() */
    public void substract() throws IOException {
        block.append( new BinaryOp( BinOp_Subtract ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#substring() */
    public void substring() throws IOException {
        block.append( new Substring( false ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#substringMB() */
    public void substringMB() throws IOException {
        block.append( new Substring( true ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#swap() */
    public void swap() throws IOException {
        block.append( new Swap() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#toggleQuality() */
    public void toggleQuality() throws IOException {
        block.append( new ToggleQuality() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#toInteger() */
    public void toInteger() throws IOException {
        block.append( new UnaryOp( UnOp_ToInteger ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#trace() */
    public void trace() throws IOException {
        block.append( new Trace() );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typedAdd() */
    public void typedAdd() throws IOException {
        block.append( new BinaryOp( BinOp_TypedAdd ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typedEquals() */
    public void typedEquals() throws IOException {
        block.append( new BinaryOp( BinOp_TypedEquals ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typedLessThan() */
    public void typedLessThan() throws IOException {
        block.append( new BinaryOp( BinOp_TypedLessThan ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#typeOf() */
    public void typeOf() throws IOException {
        block.append( new UnaryOp( UnOp_TypeOf ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#unknown(int, byte[]) */
    public void unknown(int code, byte[] data) throws IOException {
        throw new IOException( "Unknown AVM1 opcode " + code );        
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#waitForFrame(int, java.lang.String) */
    public void waitForFrame(int frameNumber, String jumpLabel) throws IOException {
        block.append( new WaitForFrame( frameNumber, jumpLabel ) );
    }

    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#waitForFrame(java.lang.String) */
    public void waitForFrame(String jumpLabel) throws IOException {
        block.append( new WaitForFrame( jumpLabel ) );
    }
}
