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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.epistem.io.OutStream;

import com.anotherbigidea.flash.SWFActionCodes;
import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;

/**
 * Implementation of SWFActionBlock that builds a byte array of the actions
 * it is passed.
 * 
 * @author nmain
 */
public class ActionBlockWriter implements SWFActionBlock, SWFActionCodes {

    /** Base for different types of fix-ups. */
    interface Fixer {
        public void fix( byte[] actionData );
    }
        
    private class OffsetFixer implements Fixer {
        
        private int mBaseOffset;
        private int mOffsetAddr;
        private String mTargetLabel;
        
        OffsetFixer( int baseOffset, int offsetAddr, String targetLabel ) {
            mBaseOffset  = baseOffset;
            mOffsetAddr  = offsetAddr;
            mTargetLabel = targetLabel;
        }
        
        public void fix( byte[] actionData ) {
            LabelInfo info = (LabelInfo) mLabelToInfo.get( mTargetLabel );
            int offset = info.offset - mBaseOffset;
            actionData[ mOffsetAddr   ] = (byte)(offset & 0xff);
            actionData[ mOffsetAddr+1 ] = (byte)((offset >> 8) & 0xff);
        }
    }
    
    private class SkipActionFixer implements Fixer {
        
        private int mSkipCountAddr;
        private int mBaseActionCount;
        private String mTargetLabel;
        
        SkipActionFixer( int baseActionCount, int skipCountAddr, String targetLabel ) {
            mBaseActionCount = baseActionCount;
            mSkipCountAddr   = skipCountAddr;
            mTargetLabel     = targetLabel;
        }
        
        public void fix( byte[] actionData ) {
            LabelInfo info = (LabelInfo) mLabelToInfo.get( mTargetLabel );
            int actionsToSkip = info.actionNumber - mBaseActionCount;
            actionData[ mSkipCountAddr ] = (byte) actionsToSkip;
        }        
    }
    
    private class BlockLengthFixer implements Fixer {
        
        private int mBlockLengthAddr;
        private int mBlockLength;
        
        BlockLengthFixer( int blockLengthAddr ) {
            mBlockLengthAddr = blockLengthAddr;
        }
        
        public void setBlockLength( int length ) {
            mBlockLength = length;
        }
        
        public void fix( byte[] actionData ) {
            actionData[ mBlockLengthAddr   ] = (byte)(mBlockLength & 0xff);
            actionData[ mBlockLengthAddr+1 ] = (byte)((mBlockLength >> 8) & 0xff);            
        }        
    }
    
    private class TryFlagsFixer implements Fixer {
        
        private int mFlagsAddr;
        private int mFlags = 0;
        
        TryFlagsFixer( int flagsAddr ) {
            mFlagsAddr = flagsAddr;
        }
        
        public void hasCatchBlock()   { mFlags |= SWFActionCodes.TRY_HAS_CATCH; }
        public void hasFinallyBlock() { mFlags |= SWFActionCodes.TRY_HAS_FINALLY; }
        public void catchInRegister() { mFlags |= SWFActionCodes.TRY_CATCH_IN_REGISTER; }
        
        public void fix( byte[] actionData ) {
            actionData[ mFlagsAddr ] = (byte)(mFlags & 0xff);
        }        
    }
    
    /** Info about a label. */
    private static class LabelInfo {
        public final String label;
        public final int    offset;
        public final int    actionNumber;
        
        public LabelInfo( String label, int offset, int actionNumber ) {
            this.label  = label;
            this.offset = offset;
            this.actionNumber = actionNumber;
        }
    }
    
    interface PushValue { public abstract void write( OutStream out ) throws IOException; }

    class PushUndefined  implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_UNDEFINED ); } }
    class PushStringVal  implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_STRING ); out.writeString( value, mStringEncoding ); } private String value; PushStringVal( String v ) { value = v; } }
    class PushBooleanVal implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_BOOLEAN ); out.writeUI8( value ? 1 : 0 ); } private boolean value; PushBooleanVal( boolean v ) { value = v; } }
    class PushIntegerVal implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_INTEGER ); out.writeSI32( value ); } private int value; PushIntegerVal( int v ) { value = v; } }
    class PushLookup     implements PushValue { public void write( OutStream out ) throws IOException { if( idx <=255 ) { out.writeUI8( PUSHTYPE_LOOKUP8 ); out.writeUI8( idx ); } else { out.writeUI8( PUSHTYPE_LOOKUP16 ); out.writeUI16( idx ); } } private int idx; PushLookup( int v ) { idx = v; } }
    class PushRegister   implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_REGISTER ); out.writeUI8( value ); } private int value; PushRegister( int v ) { value = v; } }
    class PushFloatVal   implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_FLOAT ); out.writeFloat( value ); } private float value; PushFloatVal( float v ) { value = v; } }
    class PushDoubleVal  implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_DOUBLE ); out.writeDouble( value ); } private double value; PushDoubleVal( double v ) { value = v; } }
    class PushNull       implements PushValue { public void write( OutStream out ) throws IOException { out.writeUI8( PUSHTYPE_NULL ); } }
    
    protected static class SubBlockWriter extends ActionBlockWriter {
        private BlockLengthFixer mLengthFixer;
        private int mInitOffset;
        
        SubBlockWriter( BlockLengthFixer fixer,
                        OutStream out,
		                ByteArrayOutputStream byteOut,
		                List  fixers,
		                Map   labelToInfo,
		                int[] actionCount,
		                int   flashVersion ) {
            super( out, byteOut, fixers, labelToInfo, actionCount, flashVersion );
            mLengthFixer = fixer;
            mInitOffset  = out.getCount();
        }
        
        public void end() throws IOException {
            int length = mOut.getCount() - mInitOffset;
            mLengthFixer.setBlockLength( length );
        }
    }
    
    protected OutStream             mOut;
    protected ByteArrayOutputStream mByteOut;
    protected List                  mFixers;
    protected Map                   mLabelToInfo;
    protected int[]                 mActionCount;
    protected List                  mPushValues = new ArrayList();
    protected int                 mFlashVersion;
    protected String              mStringEncoding;
    
    public ActionBlockWriter( int flashVersion ) {
        mByteOut = new ByteArrayOutputStream();
        mOut     = new OutStream( mByteOut );
        mFixers  = new ArrayList();
        mLabelToInfo = new HashMap();
        mActionCount = new int[] {0};
		setFlashVersion( flashVersion );
    }
    
    protected ActionBlockWriter( OutStream out,
            				     ByteArrayOutputStream byteOut,
            				     List  fixers,
            				     Map   labelToInfo,
            				     int[] actionCount,
 								 int   flashVersion ) {
        mByteOut      = byteOut;
        mOut          = out;
        mFixers       = fixers;
        mLabelToInfo  = labelToInfo;        
        mActionCount  = actionCount;
		setFlashVersion( flashVersion );
    }
    
	private void setFlashVersion( int flashVersion ) {
        mFlashVersion = flashVersion;
        mStringEncoding = ( flashVersion >= SWFConstants.FLASH_MX_VERSION ) ?
							 SWFConstants.STRING_ENCODING_MX :
							 SWFConstants.STRING_ENCODING_PRE_MX;	
	} 

	/** Close out the action data, fix up all labels and blocks and return the data. */
	public byte[] getActionData() throws IOException {
	    mOut.flush();
	    mOut.close();
	    byte[] data = mByteOut.toByteArray();
	    
	    for (Iterator i = mFixers.iterator(); i.hasNext();) {
            Fixer fixer = (Fixer) i.next();
            fixer.fix( data );
        }
	    
	    return data;
	}
	
    protected void writeCode( int code ) throws IOException {
        if( mPushValues.size() > 0 && code != PUSH ) flushPushValues();
        mOut.writeUI8( code );
        mActionCount[0]++;
    }
 
    public void end() throws IOException {
        writeCode( 0 );
    }
    
    /**
     * Pass through a blob of actions
     */
    public void blob( byte[] blob ) throws IOException
    {
        mOut.write( blob );
    }
    

    public void comment( String comment ) throws IOException
    {
        //ignore comments
    }    
 
    public void unknown( int code, byte[] data ) throws IOException
    {
        writeCode( code );
        
        int length = (data != null) ? data.length : 0;
        
        if( code >= 0x80 || length > 0 )
        {
            mOut.writeUI16( length );
        }
        
        if( length > 0 ) mOut.write( data );
    }
 
    public void initArray() throws IOException 
    {
        writeCode( INIT_ARRAY );
    }    
 
    public void jumpLabel( String label ) throws IOException {
        if( mPushValues.size() > 0 ) flushPushValues();
        
        int offset = (int) mOut.getCount();
        LabelInfo info = new LabelInfo( label, offset, mActionCount[0] );
        
        mLabelToInfo.put( label, info );
    }    
 
    public void gotoFrame( int frameNumber ) throws IOException
    {
        writeCode( GOTO_FRAME );
        mOut.writeUI16( 2 );
        mOut.writeUI16( frameNumber );
    }
 
    public void gotoFrame( String label ) throws IOException
    {
        writeCode( GOTO_LABEL );
        mOut.writeUI16  ( OutStream.getStringLength( label ) );
        mOut.writeString( label, mStringEncoding );
    }
 
    public void getURL( String url, String target ) throws IOException
    {
        writeCode( GET_URL );
        mOut.writeUI16  ( OutStream.getStringLength(url) + OutStream.getStringLength(target) );
        mOut.writeString( url, mStringEncoding );
        mOut.writeString( target, mStringEncoding );
    }
 
    public void nextFrame() throws IOException
    {
        writeCode( NEXT_FRAME );
    }
 
    public void prevFrame() throws IOException
    {
        writeCode( PREVIOUS_FRAME );
    }
 
    public void play() throws IOException
    {
        writeCode( PLAY );
    }
  
    public void stop() throws IOException
    {
        writeCode( STOP );
    }
 
    public void toggleQuality() throws IOException
    {
        writeCode( TOGGLE_QUALITY );
    }
 
    public void stopSounds() throws IOException
    {
        writeCode( STOP_SOUNDS );
    }
 
    public void setTarget( String target ) throws IOException
    {
        writeCode( SET_TARGET );
        mOut.writeUI16  ( OutStream.getStringLength( target ) );
        mOut.writeString( target, mStringEncoding );
    }

    protected void writeJump( String label, int code ) throws IOException {
        writeCode( code );
        mOut.writeUI16( 2 );
        
        int offsetAddr = mOut.getCount();
        mOut.writeUI16( 0 );   //will be fixed up later
        
        mFixers.add( new OffsetFixer( mOut.getCount(), offsetAddr, label ) );
    }
 
    public void jump( String jumpLabel ) throws IOException
    {
        writeJump( jumpLabel, JUMP );
    }
 
    public void ifJump( String jumpLabel ) throws IOException
    {
        writeJump( jumpLabel, IF );
    }
 
    public void waitForFrame( int frameNumber, String jumpLabel ) throws IOException
    {
        writeCode( WAIT_FOR_FRAME );
        mOut.writeUI16( 3 );
        mOut.writeUI16( frameNumber );

        int addr = (int) mOut.getCount();
        mOut.writeUI8 ( 0 ); //will be fixed up later
        
        int thisCount = mActionCount[0];
        
        SkipActionFixer fixer = new SkipActionFixer( thisCount, addr, jumpLabel );
        
        mFixers.add( fixer );
    }
 
    public void waitForFrame( String jumpLabel ) throws IOException
    {
        writeCode( WAIT_FOR_FRAME_2 );
        mOut.writeUI16( 1 );

        int addr = (int) mOut.getCount();
        mOut.writeUI8 ( 0 ); //will be fixed up later
        
        int thisCount = mActionCount[0];
        
        SkipActionFixer fixer = new SkipActionFixer( thisCount, addr, jumpLabel );
        
        mFixers.add( fixer );
    }
 
    public void pop() throws IOException
    {
        writeCode( POP );
    }
 
    public void add() throws IOException
    {
        writeCode( ADD );
    }
 
    public void substract() throws IOException
    {
        writeCode( SUBTRACT );
    }
 
    public void multiply() throws IOException
    {
        writeCode( MULTIPLY );
    }
 
    public void divide() throws IOException
    {
        writeCode( DIVIDE );
    }
 
    public void equals() throws IOException
    {
        writeCode( EQUALS );
    }
 
    public void lessThan() throws IOException
    {
        writeCode( LESS );
    }
 
    public void and() throws IOException
    {
        writeCode( AND );
    }
 
    public void or() throws IOException
    {
        writeCode( OR );
    }
 
    public void not() throws IOException
    {
        writeCode( NOT );
    }
 
    public void stringEquals() throws IOException
    {
        writeCode( STRING_EQUALS );
    }
 
    public void stringLength() throws IOException
    {
        writeCode( STRING_LENGTH );
    }
 
    public void concat() throws IOException
    {
        writeCode( STRING_ADD );
    }
 
    public void substring() throws IOException
    {
        writeCode( STRING_EXTRACT );
    }
 
    public void stringLessThan() throws IOException
    {
        writeCode( STRING_LESS );
    }
 
    public void stringLengthMB() throws IOException
    {
        writeCode( MB_STRING_LENGTH );
    }
 
    public void substringMB() throws IOException
    {
        writeCode( MB_STRING_EXTRACT );
    }    
 
    public void toInteger() throws IOException
    {
        writeCode( TO_INTEGER );
    }
 
    public void charToAscii() throws IOException
    {
        writeCode( CHAR_TO_ASCII );
    }
 
    public void asciiToChar() throws IOException
    {
        writeCode( ASCII_TO_CHAR );
    }
 
    public void charMBToAscii() throws IOException
    {
        writeCode( MB_CHAR_TO_ASCII );
    }
 
    public void asciiToCharMB() throws IOException
    {
        writeCode( MB_ASCII_TO_CHAR );
    }
 
    public void call() throws IOException
    {
        writeCode( CALL );
        mOut.writeUI16( 0 );   //SWF File Format anomaly
    }
 
    public void getVariable() throws IOException
    {
        writeCode( GET_VARIABLE );
    }
 
    public void setVariable() throws IOException
    {
        writeCode( SET_VARIABLE );
    }
 
    /** @see com.anotherbigidea.flash.interfaces.SWFActionBlock#getURL(com.anotherbigidea.flash.interfaces.SWFActionBlock.GetURLMethod, boolean, boolean) */
    public void getURL(GetURLMethod method, boolean loadVars, boolean targetSprite) throws IOException {
        writeCode( GET_URL_2 );
        mOut.writeUI16( 1 );

        int flags = 0;

        if( loadVars     ) flags |= 0x80;
        if( targetSprite ) flags |= 0x40;
        
        switch( method ) {
            case MethodGet:  flags |= 0x01; break;
            case MethodPost: flags |= 0x02; break;
        }
        
        mOut.writeUI8( flags );                
    }

    public void gotoFrame( boolean play ) throws IOException
    {
        writeCode( GOTO_FRAME_2 );
        mOut.writeUI16( 1 );
        mOut.writeUI8( play ? 1 : 0 );
    }
  
    public void setTarget() throws IOException
    {
        writeCode( SET_TARGET_2 );
    }
 
    public void getProperty() throws IOException
    {
        writeCode( GET_PROPERTY );
    }
 
    public void setProperty() throws IOException
    {
        writeCode( SET_PROPERTY );
    }
 
    public void cloneSprite() throws IOException
    {
        writeCode( CLONE_SPRITE );
    }
 
    public void removeSprite() throws IOException
    {
        writeCode( REMOVE_SPRITE );
    }
 
    public void startDrag() throws IOException
    {
        writeCode( START_DRAG );
    }
   
    public void endDrag() throws IOException
    {
        writeCode( END_DRAG );
    }     
 
    public void trace() throws IOException
    {
        writeCode( TRACE );
    }     
 
    public void getTime() throws IOException
    {
        writeCode( GET_TIME );
    }     
 
    public void randomNumber() throws IOException
    {
        writeCode( RANDOM_NUMBER );
    }         
 
    public void lookupTable( String[] values ) throws IOException
    {
        writeCode( LOOKUP_TABLE );
        
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutStream bout = new OutStream( baout );
        
        bout.writeUI16( values.length );
        
        for( int i = 0; i < values.length; i++ )
        {
            bout.writeString( values[i], mStringEncoding );
        }

        bout.flush();
        byte[] data = baout.toByteArray();
        mOut.writeUI16( data.length );
        mOut.write( data );        
    }
   
    public void callFunction() throws IOException
    {
        writeCode( CALL_FUNCTION );
    }         
                                      
    public void callMethod() throws IOException
    {
        writeCode( CALL_METHOD );
    }         
   
    public SWFActionBlock startFunction( String name, String[] paramNames ) throws IOException
    {
        writeCode( DEFINE_FUNCTION );        
        
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutStream bout = new OutStream( baout );
        
        bout.writeString( name, mStringEncoding );
        bout.writeUI16( paramNames.length );
        
        for( int i = 0; i < paramNames.length; i++ )
        {
            bout.writeString( paramNames[i], mStringEncoding );
        }
        
        bout.flush();
        byte[] data = baout.toByteArray();
        mOut.writeUI16( data.length + 2 );
        mOut.write( data );
        
        int lengthAddr = mOut.getCount();
        mOut.writeUI16( 0 );  //code size - will be fixed up later
        
        BlockLengthFixer fixer = new BlockLengthFixer( lengthAddr );        
        mFixers.add( fixer );
        
        SWFActionBlock block = new SubBlockWriter( fixer, mOut, mByteOut, mFixers, mLabelToInfo, mActionCount, mFlashVersion );
        return block;
    }    
     
    public SWFActionBlock startFunction2( String name, 
								          int numRegistersToAllocate,
								          int preloadingFlags,
								          String[] paramNames,
								          int[] registersForArguments ) throws IOException {

        writeCode( DEFINE_FUNCTION_2 );        
        
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutStream bout = new OutStream( baout );
        
        bout.writeString( name, mStringEncoding );
        bout.writeUI16( paramNames.length );
        bout.writeUI8( numRegistersToAllocate );
        bout.writeUI16( preloadingFlags );
        
        for( int i = 0; i < paramNames.length; i++ )
        {
            bout.writeUI8( registersForArguments[i] );
            bout.writeString( paramNames[i], mStringEncoding );
        }

        bout.flush();
        byte[] data = baout.toByteArray();
        mOut.writeUI16( data.length + 2 );
        mOut.write( data );
        
        int lengthAddr = mOut.getCount();
        mOut.writeUI16( 0 );  //code size - will be fixed up later
        
        BlockLengthFixer fixer = new BlockLengthFixer( lengthAddr );        
        mFixers.add( fixer );
        
        SWFActionBlock block = new SubBlockWriter( fixer, mOut, mByteOut, mFixers, mLabelToInfo, mActionCount, mFlashVersion );
        return block;							          
    }  
    
    public TryCatchFinally _try( String varName ) throws IOException {
        return _try( varName, 0 );
    }

    public TryCatchFinally _try( int regNum ) throws IOException {
        return _try( null, regNum );
    }    
    
    private TryCatchFinally _try( String varName, int regNum ) throws IOException {
        writeCode( TRY );

        int actionLengthAddr = mOut.getCount();
        mOut.writeUI16( 0 );  //will be fixed up
        BlockLengthFixer lengthFixer = new BlockLengthFixer( actionLengthAddr );
        mFixers.add( lengthFixer );
        
        int flagsAddr = mOut.getCount();
        mOut.writeUI8( 0 );   //flags will be fixed up
        final TryFlagsFixer flagsFixer = new TryFlagsFixer( flagsAddr );        
        mFixers.add( flagsFixer );
        
        int tryBlockSizeAddr = mOut.getCount();
        mOut.writeUI16( 0 ); //will be fixed up
        final BlockLengthFixer tryBlockFixer = new BlockLengthFixer( tryBlockSizeAddr );
        mFixers.add( tryBlockFixer );
        
        int catchBlockSizeAddr = mOut.getCount();
        mOut.writeUI16( 0 ); //will be fixed up
        final BlockLengthFixer catchBlockFixer = new BlockLengthFixer( catchBlockSizeAddr );
        mFixers.add( catchBlockFixer );
        
        int finallyBlockSizeAddr = mOut.getCount();
        mOut.writeUI16( 0 ); //will be fixed up
        final BlockLengthFixer finallyBlockFixer = new BlockLengthFixer( finallyBlockSizeAddr );
        mFixers.add( finallyBlockFixer );        

        if( varName != null ) {
            mOut.writeString( varName, mStringEncoding );
        } else {
            mOut.writeUI8( regNum );
            flagsFixer.catchInRegister();
        }
        
        lengthFixer.setBlockLength( mOut.getCount() - flagsAddr );
        
        return new TryCatchFinally() {
                        
            public SWFActionBlock tryBlock() throws IOException {
                return new SubBlockWriter( tryBlockFixer, mOut, mByteOut, mFixers, mLabelToInfo, mActionCount, mFlashVersion );
            }
            
            public SWFActionBlock catchBlock() throws IOException {
                flagsFixer.hasCatchBlock();
                return new SubBlockWriter( catchBlockFixer, mOut, mByteOut, mFixers, mLabelToInfo, mActionCount, mFlashVersion );
            }
                        
            public SWFActionBlock finallyBlock() throws IOException {
                flagsFixer.hasFinallyBlock();
                return new SubBlockWriter( finallyBlockFixer, mOut, mByteOut, mFixers, mLabelToInfo, mActionCount, mFlashVersion );
            }
            
            public void endTry() throws IOException {
                //nada
            }
        };
    }    
    
    public void defineLocalValue() throws IOException
    {
        writeCode( DEFINE_LOCAL_VAL );
    }
     
    public void defineLocal() throws IOException
    {
        writeCode( DEFINE_LOCAL );
    }   
  
    public void deleteProperty() throws IOException
    {
        writeCode( DEL_VAR );
    }   
 
    public void deleteScopeProperty() throws IOException
    {
        writeCode( DEL_SCOPE_VAR );
    }   
     
    public void enumerate() throws IOException
    {
        writeCode( ENUMERATE );
    }   
 
    public void typedEquals() throws IOException
    {
        writeCode( TYPED_EQUALS );
    }   
 
    public void getMember() throws IOException
    {
        writeCode( GET_MEMBER );
    }   
        
    public void initObject() throws IOException
    {
        writeCode( INIT_OBJECT );
    }   
     
    public void newMethod() throws IOException
    {
        writeCode( CALL_NEW_METHOD );
    }   
    
    public void newObject() throws IOException
    {
        writeCode( NEW_OBJECT );
    }   
   
    public void setMember() throws IOException
    {
        writeCode( SET_MEMBER );
    }   
    
    public void getTargetPath() throws IOException
    {
        writeCode( GET_TARGET_PATH );
    }   
 
    public SWFActionBlock startWith() throws IOException
    {
        writeCode( WITH );
        mOut.writeUI16( 2 );
        int blockSizeAddr = mOut.getCount();
        mOut.writeUI16( 0 );  //codeSize - will be fixed up later

        BlockLengthFixer fixer = new BlockLengthFixer( blockSizeAddr );
        mFixers.add( fixer );
        
        SWFActionBlock block = new SubBlockWriter( fixer, mOut, mByteOut, mFixers, mLabelToInfo, mActionCount,mFlashVersion );
        return block;
    }   
 
    public void duplicate() throws IOException
    {
        writeCode( DUPLICATE );
    }   
 
    public void returnValue() throws IOException
    {
        writeCode( RETURN );
    }   
 
    public void swap() throws IOException
    {
        writeCode( SWAP );
    }   
 
    public void storeInRegister( int registerNumber ) throws IOException
    {
        writeCode( REGISTER );
        mOut.writeUI16( 1 );
        mOut.writeUI8( registerNumber );
    }   
 
    public void convertToNumber() throws IOException
    {
        writeCode( CONVERT_TO_NUMBER );
    }   
 
    public void convertToString() throws IOException
    {
        writeCode( CONVERT_TO_STRING );
    }   
 
    public void typeOf() throws IOException
    {
        writeCode( TYPEOF );
    }   
 
    public void typedAdd() throws IOException
    {
        writeCode( TYPED_ADD );
    }   
 
    public void typedLessThan() throws IOException
    {
        writeCode( TYPED_LESS_THAN );
    }   

    public void modulo() throws IOException
    {
        writeCode( MODULO );
    }   
 
    public void bitAnd() throws IOException
    {
        writeCode( BIT_AND );
    }   
 
    public void bitOr() throws IOException
    {
        writeCode( BIT_OR );
    }   
 
    public void bitXor() throws IOException
    {
        writeCode( BIT_XOR );
    }   
 
    public void shiftLeft() throws IOException
    {
        writeCode( SHIFT_LEFT );
    }   
  
    public void shiftRight() throws IOException
    {
        writeCode( SHIFT_RIGHT );
    }   
 
    public void shiftRightUnsigned() throws IOException
    {
        writeCode( SHIFT_UNSIGNED );
    }   
 
    public void decrement() throws IOException
    {
        writeCode( DECREMENT );
    }   
 
    public void increment() throws IOException
    {
        writeCode( INCREMENT );
    }   
    
    protected void flushPushValues() throws IOException {
		writeCode( PUSH );

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutStream bout = new OutStream( baout );
        
        for( Iterator e = mPushValues.iterator(); e.hasNext(); )
        {
            PushValue pv = (PushValue) e.next();
			pv.write( bout );
        }

        mPushValues.clear();
        
        bout.flush();
        byte[] data = baout.toByteArray();
        mOut.writeUI16( data.length );
        mOut.write( data );
    }
    
	private void push( PushValue value ) throws IOException {
        mPushValues.add( value );
        if( mFlashVersion < 5 ) flushPushValues();
	} 

    public void push( String value ) throws IOException {
		push( new PushStringVal( value ) );  
    }
    
    public void push( float  value ) throws IOException {
		push( new PushFloatVal( value ) );  
    }
    
    public void push( double value ) throws IOException {
		push( new PushDoubleVal( value ) );  
    }
    
    public void pushNull() throws IOException {
		push( new PushNull() );  
    }

    public void pushUndefined() throws IOException {
		push( new PushUndefined() );  
    }

    public void pushRegister( int registerNumber ) throws IOException {
		push( new PushRegister( registerNumber ) );  
    }
    
    public void push( boolean value ) throws IOException {
		push( new PushBooleanVal( value ) );  
    }
    
    public void push( int value ) throws IOException {
		push( new PushIntegerVal( value ) );  
    }

    public void lookup( int dictionaryIndex ) throws IOException {
		push( new PushLookup( dictionaryIndex ) );  
    }    
    
    /**
     * @see com.anotherbigidea.flash.interfaces.SWFActions#enumerateObject()
     */
    public void enumerateObject() throws IOException {
        writeCode( ENUMERATE_OBJECT );
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFActions#greaterThan()
     */
    public void greaterThan() throws IOException {
        writeCode( GREATER );
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFActions#instanceOf()
     */
    public void instanceOf() throws IOException {
        writeCode( INSTANCE_OF );
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFActions#strictEquals()
     */
    public void strictEquals() throws IOException {
        writeCode( STRICT_EQUALS );
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFActions#stringGreaterThan()
     */
    public void stringGreaterThan() throws IOException {
        writeCode( STRING_GREATER );
    }
    
    
    public void _extends() throws IOException {
        writeCode( EXTENDS );
    }

    public void _implements() throws IOException {
        writeCode( IMPLEMENTS_OP );
    }

    public void _throw() throws IOException {
        writeCode( THROW );
    }

    public void cast() throws IOException {
        writeCode( CAST_OP );
    }
}
