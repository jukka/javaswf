/****************************************************************
 * Copyright (c) 2001, David N. Main, All rights reserved.
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
import java.io.PrintWriter;

import com.anotherbigidea.flash.SWFActionCodes;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;

/**
 * A writer that implements the SWFActions interface and writes
 * actions to a text format
 */
public class ActionTextWriter implements SWFActions, SWFActionBlock, SWFActionCodes 
{
    protected PrintWriter printer;
    protected int blockLevel = 0;
    protected String indent = "";
    protected String label = null;
    
    public static final String LABEL_ZONE = "      ";
    
    public ActionTextWriter( PrintWriter printer )
    {
        this.printer = printer;
    }

    public void indent() {
        indent += "    ";
    }
    
    public void unindent() {
        if( indent.length() < 4 ) indent = "";
        else indent = indent.substring( 0, indent.length() - 4 );
    }

    public void println( String msg ) {
        printer.println( "  " + msg );        
    }
    
    protected void print( String mnemonic, String[] args )
    {
        printer.print( "  " );
        if( label != null ) {
            printer.print( label + ":" + ((label.length() < 5) ?  LABEL_ZONE.substring( label.length() + 1 ) : " "));
            label = null;
        } else {
            printer.print( LABEL_ZONE );
        }
        
        printer.print( indent );
    	
    	if( mnemonic.equals( "{" ) ||  mnemonic.equals( "}" ) 
    	        || mnemonic.startsWith( "catch" )
    	        || mnemonic.startsWith( "finally" )) {
    		printer.println( mnemonic );
    		return;
    	}
        
        writePaddedString( mnemonic + " ", 15 );
        
        if( args != null )
        {
            for( int i = 0; i < args.length; i++ )
            {
                if( i > 0 ) printer.print( ", " );
                printer.print( args[i] );
            }
        }
        
        printer.println();
    }
    
    protected void writePaddedString( String s, int length )
    {
        int pad = length - s.length();
        
        printer.print( s );
        while( pad > 0 )
        {
            printer.print( " " );
            pad--;
        }
    }

    public SWFActionBlock start( int conditions ) throws IOException
    {
        print( "conditions", new String[] { Integer.toBinaryString( conditions ) } );
        printer.flush();
        return this;
    }
    
	public SWFActionBlock start( int conditions, int keycode ) throws IOException
	{
		print( "conditions", new String[] { Integer.toBinaryString( conditions ), "keycode = " + keycode } );
		printer.flush();
		return this;
	}    

	public void done() throws IOException
	{
	    printer.flush();
	}
	
    public void end() throws IOException
    {    	
    	if( blockLevel > 0 ) {
            unindent();
            print( "}", null );
            blockLevel--;
        } else {
            print( "end", null );            
        }
    }
 
    public void blob( byte[] blob ) throws IOException
    {
        print( "(blob)", null );
        printer.println();
    }    
    
    public void unknown( int code, byte[] data ) throws IOException
    {
        print( "unknown code =", new String[] { Integer.toString( code ) } );
    }
    
    public void initArray() throws IOException 
    {
        print( "initArray", null );
    }    
        
    public void jumpLabel( String label ) throws IOException
    {
        if( this.label != null ) print( "", null );
        this.label = label;
    }    

    public void jumpLabelOnOwnLine( String label ) throws IOException
    {
        print( "---- " + label + " ----", null );
    }    
    
    public void gotoFrame( int frameNumber ) throws IOException
    {
        print( "gotoFrame", new String[] { Integer.toString( frameNumber ) } );
    }
    
    public void gotoFrame( String label ) throws IOException
    {
        print( "gotoFrame", new String[] { "\"" + label + "\"" } );
    }
    
    public void getURL( String url, String target ) throws IOException
    {
        print( "getURL", new String[] { "\"" + url + "\"", "\"" + target + "\"" } );
    }
    
    public void nextFrame() throws IOException
    {
        print( "nextFrame", null );
    }
    
    public void prevFrame() throws IOException
    {
        print( "previousFrame", null );
    }
    
    public void play() throws IOException
    {
        print( "play", null );
    }
    
    public void stop() throws IOException
    {
        print( "stop", null );
    }
    
    public void toggleQuality() throws IOException
    {
        print( "toggleQuality", null );
    }
    
    public void stopSounds() throws IOException
    {
        print( "stopSounds", null );
    }
    
    public void setTarget( String target ) throws IOException
    {
        print( "setTarget", new String[] { "\"" + target + "\"" } );
    }
    
    public void jump( String jumpLabel ) throws IOException
    {
        print( "jump", new String[] { "\"" + jumpLabel + "\"" } );
    }
    
    public void ifJump( String jumpLabel ) throws IOException
    {
        print( "ifJump", new String[] { "\"" + jumpLabel + "\"" } );
    }
    
    public void waitForFrame( int frameNumber, String jumpLabel ) throws IOException
    {
        print( "waitForFrame", new String[] { Integer.toString( frameNumber ), 
                                              "\"" + jumpLabel + "\"" } );        
    }
    
    public void waitForFrame( String jumpLabel ) throws IOException
    {
        print( "waitForFrame", new String[] { "\"" + jumpLabel + "\"" } );       
    }
    
    public void pop() throws IOException
    {
        print( "pop", null );
    }
    
    public void push( String value ) throws IOException
    {
        print( "push", new String[] { "\"" + value + "\"" } );
    }
    
    public void push( float  value ) throws IOException
    {
        print( "push", new String[] { "float " + value } );
    }
    
    public void push( double value ) throws IOException
    {
        print( "push", new String[] { "double " + value } );
    }
    
    public void pushNull() throws IOException
    {
        print( "push", new String[] { "null" } );
    }
    
	public void pushUndefined() throws IOException
	{
		print( "push", new String[] { "undefined" } );
	}
	    
    public void pushRegister( int registerNumber ) throws IOException
    {
        print( "push", new String[] { "register( " + registerNumber + " )" } );
    }
    
    public void push( boolean value ) throws IOException
    {
        print( "push", new String[] { value ? "true" : "false" } );
    }
    
    public void push( int value ) throws IOException
    {
        print( "push", new String[] { "" + value } );
    }
    
    public void lookup( int dictionaryIndex ) throws IOException
    {
        print( "push", new String[] { "lookup( " + dictionaryIndex + " )" } );
    }
    
    public void add() throws IOException
    {
        print( "add", null );
    }
    
    public void substract() throws IOException
    {
        print( "substract", null );
    }
    
    public void multiply() throws IOException
    {
        print( "multiply", null );
    }
    
    public void divide() throws IOException
    {
        print( "divide", null );
    }
    
    public void equals() throws IOException
    {
        print( "equals", null );
    }
    
    public void lessThan() throws IOException
    {
        print( "lessThan", null );
    }
    
    public void and() throws IOException
    {
        print( "and", null );
    }
    
    public void or() throws IOException
    {
        print( "or", null );
    }
    
    public void not() throws IOException
    {
        print( "not", null );
    }
    
    public void stringEquals() throws IOException
    {
        print( "stringEquals", null );
    }
    
    public void stringLength() throws IOException
    {
        print( "stringLength", null );
    }
    
    public void concat() throws IOException
    {
        print( "concat", null );
    }
    
    public void substring() throws IOException
    {
        print( "substring", null );
    }
    
    public void stringLessThan() throws IOException
    {
        print( "stringLessThan", null );
    }
    
    public void stringLengthMB() throws IOException
    {
        print( "stringLengthMB", null );
    }
    
    public void substringMB() throws IOException
    {
        print( "substringMB", null );
    }
        
    public void toInteger() throws IOException
    {
        print( "toInteger", null );
    }
        
    public void charToAscii() throws IOException
    {
        print( "charToAscii", null );
    }
        
    public void asciiToChar() throws IOException
    {
        print( "asciiToChar", null );
    }
        
    public void charMBToAscii() throws IOException
    {
        print( "charMBToAscii", null );
    }
        
    public void asciiToCharMB() throws IOException
    {
        print( "asciiToCharMB", null );
    }
        
    public void call() throws IOException
    {
        print( "call", null );
    }
    
    public void getVariable() throws IOException
    {
        print( "getVariable", null );
    }
    
    public void setVariable() throws IOException
    {
        print( "setVariable", null );
    }
    
    public void getURL(GetURLMethod method, boolean loadVars, boolean targetSprite) throws IOException {
        String sendVars = null;
        
        switch( method ) {
            case MethodGet:
                sendVars = "send vars via GET";
                break;
            
            case MethodPost:
                sendVars = "send vars via POST";
                break;
            
            case MethodNone:
            default:
                sendVars = "no send";
                break;
        }
                
        print( "getURL",  new String[] { sendVars, loadVars ? "load-vars" : "load-movie", targetSprite ? "into sprite" : "into frame" } );        
    }

    public void gotoFrame( boolean play ) throws IOException
    {
        print( "gotoFrame", new String[] { play ? "and play" : "and stop" } );
    }
    
    public void setTarget() throws IOException
    {
        print( "setTarget", null );
    }
    
    public void getProperty() throws IOException
    {
        print( "getProperty", null );
    }
    
    public void setProperty() throws IOException
    {
        print( "setProperty", null );
    }
    
    public void cloneSprite() throws IOException
    {
        print( "cloneSprite", null );
    }
    
    public void removeSprite() throws IOException
    {
        print( "removeSprite", null );
    }
    
    public void startDrag() throws IOException
    {
        print( "startDrag", null );
    }
    
    public void endDrag() throws IOException
    {
        print( "endDrag", null );
    }
    
    public void trace() throws IOException
    {
        print( "trace", null );
    }
    
    public void getTime() throws IOException
    {
        print( "getTime", null );
    }
    
    public void randomNumber() throws IOException
    {
        print( "randomNumber", null );
    }    
    
    public void lookupTable( String[] values ) throws IOException
    {
        print( "lookupTable", null );
        
        for( int i = 0; i < values.length; i++ )
        {
            printer.print( indent + "        " );
            writePaddedString( Integer.toString( i ) + ":", 5  );
            printer.println( "\"" + values[i] + "\"" );
        }
    }
    
    public void callFunction() throws IOException
    {
        print( "callFunction", null );
    }    
    
    public void callMethod() throws IOException
    {
        print( "callMethod", null );
    }        
    
    public SWFActionBlock startFunction( String name, String[] paramNames ) throws IOException
    {
        String args = name + "(";
        
        if( paramNames != null)
        {
            for( int i = 0; i < paramNames.length; i++ )
            {
                if( i > 0 ) args += ",";
                args += " " + paramNames[i];
            }

            if( paramNames.length > 0 ) args += " ";
        }
        
        args += ")";
        
        printer.println();
        print( "defineFunction", new String[] { args } );
        print( "{", null );

        indent();
        blockLevel++;
        return this;
    }    
    
    public SWFActionBlock startFunction2( String name, 
					            int numRegistersToAllocate,
					            int preloadingFlags,
					            String[] paramNames,
					            int[] registersForArguments ) throws IOException {
        String args = name + "(";
        
        if( paramNames != null)
        {
            for( int i = 0; i < paramNames.length; i++ )
            {
                if( i > 0 ) args += ",";
                args += " " + paramNames[i];
                
                if( registersForArguments[i] != 0 ) {
                    args += " [reg " + registersForArguments[i] + "]";  
                }
            }

            if( paramNames.length > 0 ) args += " ";
        }
        
        args += ") num registers = ";
        args += numRegistersToAllocate;        
        
        printer.println();
        print( "defineFunction", new String[] { args } );

        int reg = 1;
        if( (preloadingFlags & START_FUNCTION2_PRELOAD_THIS     ) != 0 ) print( "    Preload this      [reg " + (reg++) + "]", null );
        if( (preloadingFlags & START_FUNCTION2_PRELOAD_ARGUMENTS) != 0 ) print( "    Preload arguments [reg " + (reg++) + "]", null );
        if( (preloadingFlags & START_FUNCTION2_PRELOAD_SUPER    ) != 0 ) print( "    Preload super     [reg " + (reg++) + "]", null );
        if( (preloadingFlags & START_FUNCTION2_PRELOAD_ROOT     ) != 0 ) print( "    Preload root      [reg " + (reg++) + "]", null );
        if( (preloadingFlags & START_FUNCTION2_PRELOAD_PARENT   ) != 0 ) print( "    Preload parent    [reg " + (reg++) + "]", null );
        if( (preloadingFlags & START_FUNCTION2_PRELOAD_GLOBAL   ) != 0 ) print( "    Preload global    [reg " + (reg++) + "]", null );        
        
        if( (preloadingFlags & START_FUNCTION2_SUPRESS_SUPER    ) != 0 ) print( "    Supress super", null );
        if( (preloadingFlags & START_FUNCTION2_SUPRESS_ARGUMENTS) != 0 ) print( "    Supress arguments", null );
        if( (preloadingFlags & START_FUNCTION2_SUPRESS_THIS     ) != 0 ) print( "    Supress this", null );
        
        print( "{", null );

        indent();
        blockLevel++;
        return this;
    }
 
    public void comment( String comment ) throws IOException
    {
        printer.println( indent + "    // " + comment );
    }
     
    public void defineLocalValue() throws IOException
    {
        print( "defineLocalValue", null );
    }    
    
    public void defineLocal() throws IOException
    {
        print( "defineLocal", null );
    }      
    
    public void deleteProperty() throws IOException
    {
        print( "deleteProperty", null );
    }    
    
    public void deleteScopeProperty() throws IOException
    {
        print( "deleteScopeProperty", null );
    }    
    
    public void enumerate() throws IOException
    {
        print( "enumerate", null );
    }    
    
    public void typedEquals() throws IOException
    {
        print( "typedEquals", null );
    }    
    
    public void getMember() throws IOException
    {
        print( "getMember", null );
    }    
    
    public void initObject() throws IOException
    {
        print( "initObject", null );
    }    
    
    public void newMethod() throws IOException
    {
        print( "newMethod", null );
    }    
    
    public void newObject() throws IOException
    {
        print( "newObject", null );
    }    
    
    public void setMember() throws IOException
    {
        print( "setMember", null );
    }    
    
    public void getTargetPath() throws IOException
    {
        print( "getTargetPath", null );
    }   
    
    public SWFActionBlock startWith() throws IOException
    {       
        printer.println();
        print( "with", null );
        print( "{", null );

        indent();
        blockLevel++;
        return this;
    }   
         
    public void duplicate() throws IOException
    {
        print( "duplicate", null );
    }   
    
    public void returnValue() throws IOException
    {
        print( "return", null );
    }   
    
    public void swap() throws IOException
    {
        print( "swap", null );
    }   
    
    public void storeInRegister( int registerNumber ) throws IOException
    {
        print( "register", new String[] { Integer.toString( registerNumber ) } );
    }   
    
    public void convertToNumber() throws IOException
    {
        print( "convertToNumber", null );
    }   
    
    public void convertToString() throws IOException
    {
        print( "convertToString", null );
    }   
    
    public void typeOf() throws IOException
    {
        print( "typeOf", null );
    }   
    
    public void typedAdd() throws IOException
    {
        print( "typedAdd", null );
    }   
    
    public void typedLessThan() throws IOException
    {
        print( "typedLessThan", null );
    }   
    
    public void modulo() throws IOException
    {
        print( "modulo", null );
    }   
    
    public void bitAnd() throws IOException
    {
        print( "bitAnd", null );
    }   
    
    public void bitOr() throws IOException
    {
        print( "bitOr", null );
    }   
    
    public void bitXor() throws IOException
    {
        print( "bitXor", null );
    }   
    
    public void shiftLeft() throws IOException
    {
        print( "shiftLeft", null );
    }   
    
    public void shiftRight() throws IOException
    {
        print( "shiftRight", null );
    }   
    
    public void shiftRightUnsigned() throws IOException
    {
        print( "shiftRightUnsigned", null );
    }   
    
    public void decrement() throws IOException
    {
        print( "decrement", null );
    }   
    
    public void increment() throws IOException
    {
        print( "increment", null );
    }  
    
	public void enumerateObject() throws IOException {
		print( "enumerateObject", null );
	}

	public void greaterThan() throws IOException {
		print( "greaterThan", null );
	}

	public void instanceOf() throws IOException {
		print( "instanceOf", null );
	}

	public void strictEquals() throws IOException {
		print( "strictEquals", null );
	}

	public void stringGreaterThan() throws IOException {
		print( "greaterThan", null );
	}
	
	public void _extends() throws IOException {
		print( "extends", null );
	}

	public void _implements() throws IOException {
		print( "implements", null );
	}

	public void _throw() throws IOException {
		print( "throw", null );
	}

	public void cast() throws IOException {
		print( "cast", null );
	}

	public SWFActionBlock.TryCatchFinally _try( String var ) throws IOException {
	    return new TryImpl( var );
	}

	public SWFActionBlock.TryCatchFinally _try( int reg ) throws IOException {
	    return new TryImpl( "register " + reg );
	}	
	
	private class TryImpl implements SWFActionBlock.TryCatchFinally {
	    
	    private String mCatchName;
	    
	    TryImpl( String catchName ) {
	        mCatchName = catchName; 
	    }
	    
        public SWFActionBlock catchBlock() throws IOException {
            print( "catch( " + mCatchName + " ) {", null );

            indent();
            blockLevel++;
            return ActionTextWriter.this;
        }

        public void endTry() throws IOException {
            //nada
        }

        public SWFActionBlock finallyBlock() throws IOException {
            print( "finally {", null );

            indent();
            blockLevel++;
            return ActionTextWriter.this;
        }

        public SWFActionBlock tryBlock() throws IOException {
            print( "try {", null );

            indent();
            blockLevel++;
            return ActionTextWriter.this;
        }
	}
}

