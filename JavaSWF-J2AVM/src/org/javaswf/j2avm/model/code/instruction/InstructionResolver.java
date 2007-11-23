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
package org.javaswf.j2avm.model.code.instruction;

import static org.javaswf.j2avm.model.code.expression.BinaryOperation.*;
import static org.javaswf.j2avm.model.code.expression.Condition.*;
import static org.javaswf.j2avm.model.code.expression.ExpressionBuilder.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.code.expression.Expression;
import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.statement.LabelStatement;
import org.javaswf.j2avm.model.code.statement.Statement;
import org.javaswf.j2avm.model.code.statement.StatementCursor;
import org.javaswf.j2avm.model.code.statement.StatementList;
import org.javaswf.j2avm.model.code.statement.Statements;
import org.javaswf.j2avm.model.code.statement.StaticSingleAssignmentStatement;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.ValueType;
import org.javaswf.j2avm.model.types.VoidType;

/**
 * Translator from Instructions to Expressions and Statements.
 * 
 * @author nmain
 */
public final class InstructionResolver implements Instructions {

	private int currentOffset;
	private final StatementList list;		
	private final StatementCursor cursor;
	private final Statements statements;	
    private ConstantPool cpool;
    
    private final Map<Integer, Statement> statementByOffset = 
    	new HashMap<Integer, Statement>();

    public InstructionResolver( ConstantPool pool, StatementList list ) {
        cpool = pool;
        this.list  = list;
        cursor     = list.cursorAtStart();
        statements = cursor.insert();
        
        statements.setListener( new Statements.Listener() {
			public void statementInserted( Statement s ) {
				statementByOffset.put( currentOffset, s );			
			}
        });
    }

    /**
     * Position all the labels before the statement at the corresponding
     * offsets.  Call this after all the instructions have been parsed
     */
    public void positionLabels() {
    	for( Iterator<LabelStatement> i = list.labels(); i.hasNext(); ) {
			LabelStatement label =  i.next();
			
			Integer offset = (Integer) label.name;
			Statement s = statementByOffset.get( offset );
			
			list.label( s, label.name );
		}
    }
    
    public void done() {
    	//nada
    }
    
    public void defineLabel( int label ) { 
    	currentOffset = label;
    }
    
	private void insert( Expression e ) {
		IntermediateStatement s = new UnassembledExpressionStatement( e );
		s.append( statements );		
	}
	
    public void aaload()  { insert( element( null, null ) ); }
    public void aastore() { statements.assign( (Expression)null, null, null ); }
    public void baload()  { insert( element( null, null ) ); }
    public void bastore() { statements.assign( (Expression)null, null, null ); }
    public void caload()  { insert( element( null, null ) ); }
    public void castore() { statements.assign( (Expression)null, null, null ); }
    public void daload()  { insert( element( null, null ) ); }
    public void dastore() { statements.assign( (Expression)null, null, null ); }
    public void faload()  { insert( element( null, null ) ); }
    public void fastore() { statements.assign( (Expression)null, null, null ); }
    public void iaload()  { insert( element( null, null ) ); }
    public void iastore() { statements.assign( (Expression)null, null, null ); }
    public void laload()  { insert( element( null, null ) ); }
    public void lastore() { statements.assign( (Expression)null, null, null ); }
    public void saload()  { insert( element( null, null ) ); }
    public void sastore() { statements.assign( (Expression)null, null, null ); }
    
    public void aconst_null() { insert( constantNull() ); }

    public void aload_0() { aload( 0 ); }
    public void aload_1() { aload( 1 ); }
    public void aload_2() { aload( 2 ); }
    public void aload_3() { aload( 3 ); }
    public void aload(int localVar) { pushVar( localVar ); }
    public void dload_0() { dload( 0 ); }
    public void dload_1() { dload( 1 ); }
    public void dload_2() { dload( 2 ); }
    public void dload_3() { dload( 3 );  }
    public void dload(int localVar) { pushVar( localVar ); }
    public void fload_0() { fload( 0 ); }
    public void fload_1() { fload( 1 ); }
    public void fload_2() { fload( 2 ); }
    public void fload_3() { fload( 3 );  }
    public void fload(int localVar) { pushVar( localVar ); }
    public void iload_0() { iload( 0 ); }
    public void iload_1() { iload( 1 ); }
    public void iload_2() { iload( 2 ); }
    public void iload_3() { iload( 3 );  }
    public void iload(int localVar) { pushVar( localVar ); }
    public void lload_0() { lload( 0 ); }
    public void lload_1() { lload( 1 ); }
    public void lload_2() { lload( 2 ); }
    public void lload_3() { lload( 3 );  }
    public void lload(int localVar) { pushVar( localVar ); }

    private LocalVarAccessStatement pushVar( int localVar ) { 
    	LocalVarAccessStatement s = new LocalVarAccessStatement( localVar ); 
    	s.append( statements ); 
    	return s;
    }
    
    public void arraylength() { insert( arrayLength( null ) ); }

    public void newarray(int primitiveType) { 
    	PrimitiveType pt = PrimitiveArrayType.valueToType.get( primitiveType ).primitiveType;    	
    	insert( newArray( new ArrayType( pt, 1 ), (Expression) null ) );
    }

    public void anewarray(int classIndex) { 
    	ObjectType ot = new ObjectType( cpool.getClassName( classIndex ) ); 
    	insert( newArray( new ArrayType( ot, 1 ), (Expression) null ) );
    }

    public void multianewarray(int arrayIndex, int dimensionCount) {
    	ArrayType at = ArrayType.fromName( cpool.getClassName( arrayIndex ) ); 
    	insert( newArray( at, new Expression[ dimensionCount ] ) );
    }
    
    public void newObject(int classIndex) {
    	ObjectType ot = new ObjectType( cpool.getClassName( classIndex ) );     	
    	insert( ExpressionBuilder.newObject( ot ));
    }

    public void areturn() { statements.returnValue( null ); }
    public void dreturn() { statements.returnValue( null ); }
    public void freturn() { statements.returnValue( null ); }
    public void ireturn() { statements.returnValue( null ); }
    public void lreturn() { statements.returnValue( null ); }
    public void vreturn() { statements.voidReturn(); }
    public void ret(int localVar) { throw new RuntimeException( "ret instruction is not supported." ); }    

    public void astore_0() { astore( 0 ); }
    public void astore_1() { astore( 1 ); }
    public void astore_2() { astore( 2 ); }
    public void astore_3() { astore( 3 ); }
    public void astore(int localVar) { store( localVar ); }
    public void dstore_0() { dstore( 0 ); }
    public void dstore_1() { dstore( 1 ); }
    public void dstore_2() { dstore( 2 ); }
    public void dstore_3() { dstore( 3 ); }
    public void dstore(int localVar) { store( localVar ); }
    public void fstore_0() { fstore( 0 ); }
    public void fstore_1() { fstore( 1 ); }
    public void fstore_2() { fstore( 2 ); }
    public void fstore_3() { fstore( 3 ); }
    public void fstore(int localVar) { store( localVar ); }
    public void istore_0() { istore( 0 ); }
    public void istore_1() { istore( 1 ); }
    public void istore_2() { istore( 2 ); }
    public void istore_3() { istore( 3 ); }
    public void istore(int localVar) { store( localVar ); }
    public void lstore_0() { lstore( 0 ); }
    public void lstore_1() { lstore( 1 ); }
    public void lstore_2() { lstore( 2 ); }
    public void lstore_3() { lstore( 3 ); }
    public void lstore(int localVar) { store( localVar ); }

    private void store( int localVar ) {
    	new LocalVarAssignmentStatement( localVar, null );
    }
    
    public void dconst_0()  { insert( constantDouble( 0.0 ) ); }
    public void dconst_1()  { insert( constantDouble( 1.0 ) ); }
    public void fconst_0()  { insert( constantFloat( 0f ) ); }
    public void fconst_1()  { insert( constantFloat( 1f ) ); }
    public void fconst_2()  { insert( constantFloat( 2f ) ); }
    public void iconst_0()  { insert( constantInt( 0 ) ); }
    public void iconst_1()  { insert( constantInt( 1 ) ); }
    public void iconst_2()  { insert( constantInt( 2 ) ); }
    public void iconst_3()  { insert( constantInt( 3 ) ); }
    public void iconst_4()  { insert( constantInt( 4 ) ); }
    public void iconst_5()  { insert( constantInt( 5 ) ); }
    public void iconst_m1() { insert( constantInt( -1 ) ); }
    public void bipush(int value) { insert( constantInt( value ) ); }
    public void sipush(int value) { insert( constantInt( value ) ); }
    public void lconst_0() { insert( constantLong( 0L ) ); }
    public void lconst_1() { insert( constantLong( 1L ) ); }

    public void ldc_w(int constantIndex) { ldc( constantIndex ); }
    public void ldc2_w(int constantIndex) { ldc( constantIndex ); }
    public void ldc(int constantIndex) {
        ConstantPool.Entry entry = cpool.getEntry( constantIndex );
		if( entry instanceof ConstantPool.DoubleEntry ) {
			insert( constantDouble( ((ConstantPool.DoubleEntry)entry).value ));
		} 
		else if( entry instanceof ConstantPool.FloatEntry ) {
			insert( constantFloat( ((ConstantPool.FloatEntry)entry).value ));
		} 
		else if( entry instanceof ConstantPool.IntEntry ) {
			insert( constantInt( ((ConstantPool.IntEntry)entry).value ));
		} 
		else if( entry instanceof ConstantPool.LongEntry ) {
			insert( constantLong( ((ConstantPool.LongEntry)entry).value ));
		} 
		else if( entry instanceof ConstantPool.StringEntry ) {
			insert( constantString( ((ConstantPool.StringEntry)entry).getUTF8Entry().value ));
		}
		else if( entry instanceof ConstantPool.ClassEntry ) {
			insert( constantClass( ValueType.fromName( ((ConstantPool.ClassEntry)entry).getUTF8Entry().value) ));
		}		
    }

    public void athrow() { statements.throwException( null ); }

    public void checkcast (int classIndex) { insert( cast( ObjectOrArrayType.fromName( cpool.getClassName( classIndex )), null ) ); }
    public void instanceOf(int classIndex) { insert( ExpressionBuilder.instanceOf( ObjectOrArrayType.fromName( cpool.getClassName( classIndex )), null ) ); }
    
    public void d2f() { conv( PrimitiveType.FLOAT ); }
    public void d2i() { conv( PrimitiveType.INT ); }
    public void d2l() { conv( PrimitiveType.LONG ); }
    public void i2b() { conv( PrimitiveType.BYTE ); }
    public void i2c() { conv( PrimitiveType.CHAR ); }
    public void i2d() { conv( PrimitiveType.DOUBLE ); }
    public void i2f() { conv( PrimitiveType.FLOAT ); }
    public void i2l() { conv( PrimitiveType.LONG ); }
    public void i2s() { conv( PrimitiveType.SHORT ); }
    public void l2d() { conv( PrimitiveType.DOUBLE ); }
    public void l2f() { conv( PrimitiveType.FLOAT ); }
    public void l2i() { conv( PrimitiveType.INT ); }
    public void f2d() { conv( PrimitiveType.DOUBLE ); }
    public void f2i() { conv( PrimitiveType.INT ); }
    public void f2l() { conv( PrimitiveType.LONG ); }
    
    private void conv( PrimitiveType type ) {
    	insert( convert( type, null ) );
    }
    
    public void monitorenter() { statements.monitorEnter( null ); }
    public void monitorexit()  { statements.monitorExit( null ); }
    
    public void getfield(int fieldIndex) { 
		insert( field( cpool.getField( fieldIndex ), null ) ); 
	}
    
	public void getstatic(int fieldIndex){ 
		insert( staticField( cpool.getField( fieldIndex ) ) ); 
	}
    
	public void putfield(int fieldIndex) { 
		statements.assign( cpool.getField( fieldIndex ), null, null );
	}
    
	public void putstatic(int fieldIndex){ 
		statements.assign( cpool.getField( fieldIndex ), null );
	}
    
	private void invoke( Expression e, MethodDescriptor md ) {
	    if( md.type == VoidType.VOID ) {
	        statements.expression( e );
	    }
	    else {
	        insert( e );
	    }	    
	}
	
    public void invokeinterface(int methodIndex, int count) {
    	MethodDescriptor md = cpool.getMethod( methodIndex );    	    	
    	invoke( call( md, null, new Expression[ md.signature.paramTypes.length ] ), md );
    }

    public void invokespecial(int methodIndex) {
    	MethodDescriptor md = cpool.getMethod( methodIndex );    	
    	invoke( specialCall( md, null, new Expression[ md.signature.paramTypes.length ] ), md );
    }

    public void invokestatic(int methodIndex) {
    	MethodDescriptor md = cpool.getMethod( methodIndex );    	
    	invoke( staticCall( md, new Expression[ md.signature.paramTypes.length ] ), md );
    }

    public void invokevirtual(int methodIndex) {
    	MethodDescriptor md = cpool.getMethod( methodIndex );    	
    	invoke( call( md, null, new Expression[ md.signature.paramTypes.length ] ), md );
    }

    public void goto_(int label)  { statements.branch( label ); }
    public void goto_w(int label) { goto_( label ); }
    public void jsr_w(int label)  { throw new RuntimeException( "JSR is unsupported - please recompile for >= Java 5" ); }
    public void jsr(int label)    { jsr_w( label ); }

    public void tableswitch( int defaultLabel, int lowIndex, int[] jumpTable) {
        int[] caseValues = new int[ jumpTable.length ];
        int value = lowIndex;  
        for (int i = 0; i < caseValues.length; i++) {
            caseValues[i] = value++;
        }

		lookupswitch( defaultLabel, caseValues, jumpTable );
    }

    public void lookupswitch(int defaultLabel, int[] caseValues, int[] caseTargets) {

    	Statements.Cases cases = statements.switchBranch( null );
    	
		for (int i = 0; i < caseValues.length; i++) {
			cases.switchCase( caseValues[i], caseTargets[i] );
        }

		cases.defaultCase( defaultLabel );
    }

    public void if_acmpeq(int label) { statements.conditionalBranch( label, condition( IF_EQUAL           , null, null ) ); }
    public void if_acmpne(int label) { statements.conditionalBranch( label, condition( IF_NOT_EQUAL       , null, null ) ); }
    public void if_icmpeq(int label) { statements.conditionalBranch( label, condition( IF_EQUAL           , null, null ) ); }
    public void if_icmpge(int label) { statements.conditionalBranch( label, condition( IF_GREATER_OR_EQUAL, null, null ) ); }
    public void if_icmpgt(int label) { statements.conditionalBranch( label, condition( IF_GREATER_THAN    , null, null ) ); }
    public void if_icmple(int label) { statements.conditionalBranch( label, condition( IF_LESS_OR_EQUAL   , null, null ) ); }
    public void if_icmplt(int label) { statements.conditionalBranch( label, condition( IF_LESS_THAN       , null, null ) ); }
    public void if_icmpne(int label) { statements.conditionalBranch( label, condition( IF_NOT_EQUAL       , null, null ) ); }
    public void ifeq(int label)      { statements.conditionalBranch( label, condition( IF_EQUAL           , null, constantInt( 0 ) ) ); }
    public void ifge(int label)      { statements.conditionalBranch( label, condition( IF_GREATER_OR_EQUAL, null, constantInt( 0 ) ) ); }
    public void ifgt(int label)      { statements.conditionalBranch( label, condition( IF_GREATER_THAN    , null, constantInt( 0 ) ) ); }
    public void ifle(int label)      { statements.conditionalBranch( label, condition( IF_LESS_OR_EQUAL   , null, constantInt( 0 ) ) ); }
    public void iflt(int label)      { statements.conditionalBranch( label, condition( IF_LESS_THAN       , null, constantInt( 0 ) ) ); }
    public void ifne(int label)      { statements.conditionalBranch( label, condition( IF_NOT_EQUAL       , null, constantInt( 0 ) ) ); }
    public void ifnonnull(int label) { statements.conditionalBranch( label, condition( IF_NOT_EQUAL       , null, constantNull() ) ); }
    public void ifnull(int label)    { statements.conditionalBranch( label, condition( IF_EQUAL           , null, constantNull() ) ); }

    public void nop() { statements.expression( constantNull() ); }

    public void iinc(int localVar, int increment) {
    	StaticSingleAssignmentStatement var = pushVar( localVar );
    	new LocalVarAssignmentStatement( 
    			localVar, 
    			ExpressionBuilder.binaryOp( 
    				ADD, 
    				ExpressionBuilder.value( var ), 
    				ExpressionBuilder.constantInt( increment ) ) );
    }

    public void dup_x1()  { new StackOperationStatement( 1, 1 ).append( statements ); }
    public void dup_x2()  { new StackOperationStatement( 1, 2 ).append( statements ); }
    public void dup()     { new StackOperationStatement( 1, 0 ).append( statements ); }
    public void dup2_x1() { new StackOperationStatement( 2, 1 ).append( statements ); }
    public void dup2_x2() { new StackOperationStatement( 2, 2 ).append( statements ); }
    public void dup2()    { new StackOperationStatement( 2, 0 ).append( statements ); }
    public void swap()    { new StackOperationStatement( 0, 0 ).append( statements ); }

    public void pop()     { new PopStatement( 1 ).append( statements ); }
    public void pop2()    { new PopStatement( 1 ).append( statements ); }
    
    public void dneg() { insert( negate( null ) ); }
    public void fneg() { insert( negate( null ) ); }
    public void ineg() { insert( negate( null ) ); }
    public void lneg() { insert( negate( null ) ); }

    public void dadd() { insert( binaryOp( ADD, null, null )); }
    public void fadd() { insert( binaryOp( ADD, null, null )); }
    public void iadd() { insert( binaryOp( ADD, null, null )); }
    public void ladd() { insert( binaryOp( ADD, null, null )); }

    public void ddiv() { insert( binaryOp( DIVIDE, null, null )); }
    public void fdiv() { insert( binaryOp( DIVIDE, null, null )); }
    public void idiv() { insert( binaryOp( DIVIDE, null, null )); }
    public void ldiv() { insert( binaryOp( DIVIDE, null, null )); }

    public void dmul() { insert( binaryOp( MULTIPLY, null, null )); }
    public void fmul() { insert( binaryOp( MULTIPLY, null, null )); }
    public void imul() { insert( binaryOp( MULTIPLY, null, null )); }
    public void lmul() { insert( binaryOp( MULTIPLY, null, null )); }

    public void drem() { insert( binaryOp( REMAINDER, null, null )); }
    public void frem() { insert( binaryOp( REMAINDER, null, null )); }
    public void irem() { insert( binaryOp( REMAINDER, null, null )); }
    public void lrem() { insert( binaryOp( REMAINDER, null, null )); }

    public void dsub() { insert( binaryOp( SUBTRACT, null, null )); }
    public void isub() { insert( binaryOp( SUBTRACT, null, null )); }
    public void fsub() { insert( binaryOp( SUBTRACT, null, null )); }
    public void lsub() { insert( binaryOp( SUBTRACT, null, null )); }

    public void dcmpg() { insert( binaryOp( COMPARE_G, null, null )); }
    public void fcmpg() { insert( binaryOp( COMPARE_G, null, null )); }

    public void dcmpl() { insert( binaryOp( COMPARE_L, null, null )); }
    public void fcmpl() { insert( binaryOp( COMPARE_L, null, null )); }

    public void iand() { insert( binaryOp( AND, null, null )); }
    public void land() { insert( binaryOp( AND, null, null )); }

    public void ior() { insert( binaryOp( OR, null, null )); }
    public void lor() { insert( binaryOp( OR, null, null )); }

    public void ishl() { insert( binaryOp( SHIFT_LEFT, null, null )); }
    public void lshl() { insert( binaryOp( SHIFT_LEFT, null, null )); }

    public void ishr() { insert( binaryOp( SHIFT_RIGHT_SIGNED, null, null )); }
    public void lshr() { insert( binaryOp( SHIFT_RIGHT_SIGNED, null, null )); }

    public void iushr() { insert( binaryOp( SHIFT_RIGHT_UNSIGNED, null, null )); }
    public void lushr() { insert( binaryOp( SHIFT_RIGHT_UNSIGNED, null, null )); }

    public void ixor() { insert( binaryOp( XOR, null, null )); }
    public void lxor() { insert( binaryOp( XOR, null, null )); }

    public void lcmp() { insert( binaryOp( COMPARE, null, null )); }
}
