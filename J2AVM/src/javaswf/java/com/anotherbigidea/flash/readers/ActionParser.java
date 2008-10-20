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
package com.anotherbigidea.flash.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.epistem.io.InStream;

import com.anotherbigidea.flash.SWFActionCodes;
import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActionBlock.GetURLMethod;

/**
 * Parse action bytes and drive a SWFActions interface
 */
public class ActionParser implements SWFActionCodes 
{
    protected SWFActionBlock mBlock;
    protected int            mFlashVersion;
    protected int            mStartOffset;
    
    public ActionParser( SWFActionBlock actions, int flashVersion )
    {
        mBlock = actions;
        mFlashVersion = flashVersion;
    }
    
    public synchronized void parse( byte[] bytes ) throws IOException
    {
        parse( new InStream(bytes) );
    }
    
    public synchronized void parse( InStream in ) throws IOException
    {
        mStartOffset = (int) in.getBytesRead();
        BlockParser parser = new BlockParser( mBlock, -1, in, mFlashVersion );
        parser.parse();
    }
        
    private static class WaitForFrameTarget {
        public int    actionsToSkip;
        public String targetLabel;
        public WaitForFrameTarget( int skip, String label ) {
            actionsToSkip = skip;
            targetLabel   = label;
        }
    }
    
    private class BlockParser implements SWFActionCodes {
        
        private SWFActionBlock mBlock;
        private int            mBlockSize;
        private InStream       mIn;
        private int            mInitBytesRead;
        private int            mFlashVersion;
        protected String mStringEncoding = SWFConstants.STRING_ENCODING_MX;
        private int            mWaitForFrameIndex = 0;
        private List<WaitForFrameTarget> mWaitForFrameTargets;
              
        /** @return the target label for the Wait-for-frame action. */
        private String newWaitForFrameTarget( int actionsToSkip ) {
            if( mWaitForFrameTargets == null ) mWaitForFrameTargets = new ArrayList<WaitForFrameTarget>();
            WaitForFrameTarget target = new WaitForFrameTarget( actionsToSkip, "w" + (mWaitForFrameIndex++) );
            mWaitForFrameTargets.add( target );
            return target.targetLabel;
        }
        
        BlockParser( SWFActionBlock block, int blockSize, InStream in, int flashVersion ) {
            mBlock     = block;
            mBlockSize = blockSize;
            mIn        = in;
            mInitBytesRead = (int) in.getBytesRead();
            mFlashVersion = flashVersion;
            
            if( flashVersion < SWFConstants.FLASH_MX_VERSION ) {
                mStringEncoding = SWFConstants.STRING_ENCODING_PRE_MX;
            }
        }
        
        private String makeLabel( int offset ) {
        	String label = Integer.toString( offset + mInitBytesRead - mStartOffset );
        	if( label.length() < 4 ) label = "0000".substring(0,4-label.length()) + label;
        	return label;
        }
        
        private void applyWaitForFrameTargets() throws IOException {
            if( mWaitForFrameTargets == null || mWaitForFrameTargets.isEmpty() ) return;
            
            for( Iterator<WaitForFrameTarget> i = mWaitForFrameTargets.iterator(); i.hasNext();) {
                WaitForFrameTarget target = i.next();
                
                if( target.actionsToSkip == 0 ) {
                    mBlock.jumpLabel( target.targetLabel );
                    i.remove();
                    continue;
                }
            
                target.actionsToSkip--;
            }
        }
        
        void parse() throws IOException {
            int offset = 0;
            boolean foundEndAction = false;
            
            end: while( ( offset = ((int) mIn.getBytesRead()) - mInitBytesRead ) < mBlockSize 
            		  || mBlockSize < 0 //denotes termination by end rather than size
					  ) {
                int code = mIn.readUI8();                
                int dataLength = (code >= 0x80) ? mIn.readUI16() : 0;                
                
                applyWaitForFrameTargets();
                mBlock.jumpLabel( makeLabel(offset) );
                                
                switch( code )
                {
	                case 0: mBlock.end(); foundEndAction = true; break end;
	                
	                //--Flash 3
	                case GOTO_FRAME    : mBlock.gotoFrame( mIn.readUI16() ); break;
	                case GET_URL       : mBlock.getURL( mIn.readString(mStringEncoding), mIn.readString(mStringEncoding) ); break;
	                case NEXT_FRAME    : mBlock.nextFrame(); break;
	                case PREVIOUS_FRAME: mBlock.prevFrame(); break;
	                case PLAY          : mBlock.play(); break;
	                case STOP          : mBlock.stop(); break;
	                case TOGGLE_QUALITY: mBlock.toggleQuality(); break;
	                case STOP_SOUNDS   : mBlock.stopSounds(); break;
	                case WAIT_FOR_FRAME: mBlock.waitForFrame( mIn.readUI16(), newWaitForFrameTarget( mIn.readUI8() ) ); break; 
	                case SET_TARGET    : mBlock.setTarget( mIn.readString(mStringEncoding) ); break;
	                case GOTO_LABEL    : mBlock.gotoFrame( mIn.readString(mStringEncoding) ); break;
	                
	                //--Flash 4
	                case IF              : mBlock.ifJump( makeLabel( offset + 5 + mIn.readSI16() ) ); break;
	                case JUMP            : mBlock.jump( makeLabel( offset + 5 + mIn.readSI16() ) ); break;
	                case WAIT_FOR_FRAME_2: mBlock.waitForFrame( newWaitForFrameTarget( mIn.readUI8() )); break;
	                case POP             : mBlock.pop(); break;
	                case PUSH            : parsePush( dataLength, mIn ); break;
	                case ADD             : mBlock.add(); break;
	                case SUBTRACT        : mBlock.substract(); break;
	                case MULTIPLY        : mBlock.multiply(); break;
	                case DIVIDE          : mBlock.divide(); break;
	                
	                case EQUALS          : mBlock.equals(); break;
	                case LESS            : mBlock.lessThan(); break;
	                case AND             : mBlock.and(); break;
	                case OR              : mBlock.or(); break;
	                case NOT             : mBlock.not(); break;
	                
	                case STRING_EQUALS     : mBlock.stringEquals(); break;
	                case STRING_LENGTH     : mBlock.stringLength(); break;
	                case STRING_ADD        : mBlock.concat(); break;
	                case STRING_EXTRACT    : mBlock.substring(); break;
	                case STRING_LESS       : mBlock.stringLessThan(); break;                                                        
	                case MB_STRING_EXTRACT : mBlock.substringMB(); break;
	                case MB_STRING_LENGTH  : mBlock.stringLengthMB(); break;
	
	                case TO_INTEGER       : mBlock.toInteger(); break;
	                case CHAR_TO_ASCII    : mBlock.charToAscii(); break;
	                case ASCII_TO_CHAR    : mBlock.asciiToChar(); break;
	                case MB_CHAR_TO_ASCII : mBlock.charMBToAscii(); break;
	                case MB_ASCII_TO_CHAR : mBlock.asciiToCharMB(); break;
	                
	                case CALL             : mBlock.call(); break;
	                case GET_VARIABLE     : mBlock.getVariable(); break;
	                case SET_VARIABLE     : mBlock.setVariable(); break;
	                
	                case GET_URL_2        : parseGetURL2( mIn.readUI8() ); break;
	                
	                case GOTO_FRAME_2  : mBlock.gotoFrame( mIn.readUI8() != 0 ); break;
	                case SET_TARGET_2  : mBlock.setTarget(); break;
	                case GET_PROPERTY  : mBlock.getProperty(); break;
	                case SET_PROPERTY  : mBlock.setProperty(); break;
	                case CLONE_SPRITE  : mBlock.cloneSprite(); break;
	                case REMOVE_SPRITE : mBlock.removeSprite(); break;
	                case START_DRAG    : mBlock.startDrag(); break;
	                case END_DRAG      : mBlock.endDrag(); break;
	                case TRACE         : mBlock.trace(); break;
	                case GET_TIME      : mBlock.getTime(); break;
	                case RANDOM_NUMBER : mBlock.randomNumber(); break;
	
	                //--Flash 5                         
	                case INIT_ARRAY         : mBlock.initArray(); break;
	                case LOOKUP_TABLE       : parseLookupTable( mIn ); break;
	                case CALL_FUNCTION      : mBlock.callFunction(); break;
	                case CALL_METHOD        : mBlock.callMethod(); break;
	                case DEFINE_FUNCTION    : parseDefineFunction(mIn); break;
	                case DEFINE_LOCAL_VAL   : mBlock.defineLocalValue(); break;
	                case DEFINE_LOCAL       : mBlock.defineLocal(); break;
	                case DEL_VAR            : mBlock.deleteProperty(); break;
	                case DEL_SCOPE_VAR      : mBlock.deleteScopeProperty(); break;
	                case ENUMERATE          : mBlock.enumerate(); break;
	                case TYPED_EQUALS       : mBlock.typedEquals(); break;
	                case GET_MEMBER         : mBlock.getMember(); break;
	                case INIT_OBJECT        : mBlock.initObject(); break;
	                case CALL_NEW_METHOD    : mBlock.newMethod(); break;
	                case NEW_OBJECT         : mBlock.newObject(); break;
	                case SET_MEMBER         : mBlock.setMember(); break;
	                case GET_TARGET_PATH    : mBlock.getTargetPath(); break;
	                case WITH               : parseWith( mIn ); break;
	                case DUPLICATE          : mBlock.duplicate(); break;
	                case RETURN             : mBlock.returnValue(); break;
	                case SWAP               : mBlock.swap(); break;
	                case REGISTER           : mBlock.storeInRegister( mIn.readUI8() ); break;
	                case MODULO             : mBlock.modulo(); break;
	                case TYPEOF             : mBlock.typeOf(); break;
	                case TYPED_ADD          : mBlock.typedAdd(); break;
	                case TYPED_LESS_THAN    : mBlock.typedLessThan(); break;
	                case CONVERT_TO_NUMBER  : mBlock.convertToNumber(); break;
	                case CONVERT_TO_STRING  : mBlock.convertToString(); break;
	                case INCREMENT          : mBlock.increment(); break;
	                case DECREMENT          : mBlock.decrement(); break;
	                case BIT_AND            : mBlock.bitAnd(); break;
	                case BIT_OR             : mBlock.bitOr(); break;
	                case BIT_XOR            : mBlock.bitXor(); break;
	                case SHIFT_LEFT         : mBlock.shiftLeft(); break;
	                case SHIFT_RIGHT        : mBlock.shiftRight(); break;
	                case SHIFT_UNSIGNED     : mBlock.shiftRightUnsigned(); break;
	
	                //--Flash 6
	                case INSTANCE_OF        : mBlock.instanceOf(); break;
	                case ENUMERATE_OBJECT   : mBlock.enumerateObject(); break;
	                case GREATER            : mBlock.greaterThan(); break;
	                case STRICT_EQUALS      : mBlock.strictEquals(); break;
	                case STRING_GREATER     : mBlock.stringGreaterThan(); break;
	
	                //--Flash 7
	                case DEFINE_FUNCTION_2  : parseDefineFunction2(mIn); break;
	                case THROW              : mBlock._throw(); break;
	                case CAST_OP            : mBlock.cast(); break;
	                case EXTENDS            : mBlock._extends(); break;
	                case IMPLEMENTS_OP      : mBlock._implements(); break;
	                case TRY                : parseTry( mIn ); break;
	                
	                default: mBlock.unknown( code, mIn.read( dataLength ) ); break;
                }                            
            }
            
            if( ! foundEndAction ) {
            	applyWaitForFrameTargets();            	
                mBlock.end();
            }
        }

        private void parseTry( InStream in ) throws IOException 
        {           
            int flags       = in.readUI8();
            int trySize     = in.readUI16();
            int catchSize   = in.readUI16();
            int finallySize = in.readUI16();
            
            String catchVar = null;
            int    catchReg = -1;
            //if( (flags & TRY_HAS_CATCH) != 0 ) {
                if( (flags & TRY_CATCH_IN_REGISTER) != 0 ) {
                    catchReg = in.readUI8();
                } else {
                    catchVar = in.readString( mStringEncoding );
                }
            //}

            SWFActionBlock.TryCatchFinally tcf = (catchReg >= 0) ?  mBlock._try( catchReg ) : mBlock._try( catchVar );
                
            SWFActionBlock block = tcf.tryBlock();
            
            BlockParser parser = new BlockParser( block, trySize, in, mFlashVersion );
            parser.parse();
            
            if( (flags & TRY_HAS_CATCH) != 0 ) {
                block = tcf.catchBlock();                
                parser = new BlockParser( block, catchSize, in, mFlashVersion );
                parser.parse();
            }
            
            if( (flags & TRY_HAS_FINALLY) != 0 ) {
                block = tcf.finallyBlock();     
	            parser = new BlockParser( block, finallySize, in, mFlashVersion );
	            parser.parse();
            }
            
            tcf.endTry();
        }  
        
        private void parseWith( InStream in ) throws IOException 
        {
            int codesize = in.readUI16();

            SWFActionBlock withBlock = mBlock.startWith( );
            BlockParser parser = new BlockParser( withBlock, codesize, in, mFlashVersion );
            parser.parse();
        }    
        
        private void parseDefineFunction( InStream in ) throws IOException 
        {
            String name = in.readString(mStringEncoding);
            int paramCount = in.readUI16();
            
            String[] params = new String[ paramCount ];
            for( int i = 0; i < params.length; i++ )
            {
                params[i] = in.readString(mStringEncoding);
            }
            
            int codesize = in.readUI16();

            //System.out.println( "codesize=" + codesize ); System.out.flush();

            SWFActionBlock functionBlock = mBlock.startFunction( name, params );
            BlockParser parser = new BlockParser( functionBlock, codesize, in, mFlashVersion );
            parser.parse();
        }

        private void parseDefineFunction2( InStream in ) throws IOException 
        {
            String name = in.readString(mStringEncoding);
            int paramCount = in.readUI16();
            int numRegs    = in.readUI8();
            int flags      = in.readUI16(); 
            
            String[] params  = new String[ paramCount ];
            int[]    argRegs = new int[ paramCount ];
            for( int i = 0; i < params.length; i++ )
            {
                argRegs[i] = in.readUI8();
                params [i] = in.readString(mStringEncoding);
            }
            
            int codesize = in.readUI16();

            //System.out.println( "codesize=" + codesize ); System.out.flush();

            SWFActionBlock functionBlock = mBlock.startFunction2( name, numRegs, flags, params, argRegs );
            BlockParser parser = new BlockParser( functionBlock, codesize, in, mFlashVersion );
            parser.parse();
        }
        
        private void parseLookupTable( InStream in ) throws IOException 
        {
            String[] strings = new String[ in.readUI16() ];
            
            for( int i = 0; i < strings.length; i++ )
            {
                strings[i] = in.readString(mStringEncoding);
            }
            
            mBlock.lookupTable( strings );
        }
        
        private void parseGetURL2( int flags ) throws IOException
        {
            GetURLMethod method;
            boolean loadVars     = (flags & 0x80) != 0;
            boolean targetSprite = (flags & 0x40) != 0;
            
            switch( flags & 0x03 ) {
                case 1:  method = GetURLMethod.MethodGet; break;
                case 2:  method = GetURLMethod.MethodPost; break;
                default: method = GetURLMethod.MethodNone; break;
            }
            
            mBlock.getURL( method, loadVars, targetSprite );
        }        
        
        private void parsePush( int length, InStream in ) throws IOException 
        {
            int initCount = (int) in.getBytesRead();
            
            while( in.getBytesRead() - initCount < length )
            {
                int pushType = in.readUI8();
                
                switch( pushType )
                {
	                case PUSHTYPE_STRING   : mBlock.push( in.readString(mStringEncoding) ); break;
	                case PUSHTYPE_FLOAT    : mBlock.push( in.readFloat() ); break;
	                case PUSHTYPE_NULL     : mBlock.pushNull(); break;
	                case PUSHTYPE_UNDEFINED: mBlock.pushUndefined(); break;
	                case PUSHTYPE_REGISTER : mBlock.pushRegister( in.readUI8() ); break;
	                case PUSHTYPE_BOOLEAN  : mBlock.push( (in.readUI8() != 0) ? true : false ); break;
	                case PUSHTYPE_DOUBLE   : mBlock.push( in.readDouble() ); break;
	                case PUSHTYPE_INTEGER  : mBlock.push( in.readSI32() ); break;
	                case PUSHTYPE_LOOKUP8  : mBlock.lookup( in.readUI8() ); break;
	                case PUSHTYPE_LOOKUP16 : mBlock.lookup( in.readUI16() ); break;
	                default:
                }
            }
        }
    }
}
