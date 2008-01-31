package babelswf;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.epistem.code.LocalValue;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.avm1.ops.*;
import com.anotherbigidea.flash.avm1.ops.ConstantOp.*;
import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.instruction.Instruction;
import com.anotherbigidea.flash.avm2.model.*;

public class OperationVisitor implements AVM1OpVisitor {

    private static final boolean TRACE = AVMTranslator.TRACE;
    
    private final Timeline   timeline;
    private final AVM2Class  avmClass;
    private final AVM2Code   code;
    private final LocalValue<Instruction> execEnv;
    
    //exception handler translations...
    private final Map<Try, AVM2Code.ExceptionHandler> handlers = 
        new HashMap<Try, AVM2Code.ExceptionHandler>();
    
    /**
     * Mappings between AVM1 local values and AVM2
     */
    private final Map<LocalValue<AVM1Operation>, LocalValue<Instruction>> valueMappings =
        new HashMap<LocalValue<AVM1Operation>, LocalValue<Instruction>>();
    
    public OperationVisitor( Timeline timeline, AVM2Code code ) {
        this.timeline  = timeline;
        this.code      = code;
        this.avmClass  = timeline.movieClip().avm2Class;
        this.execEnv   = code.newLocal();        
    }

    /**
     * Push the execution context onto the stack
     */
//    private void pushExecContext() {
//        code.getLocal( execEnv );        
//    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitAnonymousFunction(com.anotherbigidea.flash.avm1.ops.AnonymousFunction) */
    public void visitAnonymousFunction(AnonymousFunction op) {        
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.newFunction( makeFunction( op ) );
        code.callProperty( BabelSWFRuntime.RT_NEWFUNC_METHOD, 1 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitBinaryOp(com.anotherbigidea.flash.avm1.ops.BinaryOp) */
    public void visitBinaryOp(BinaryOp op) {
        
        op.visitAggregated( this ); //push the args
        
        switch( op.type ) {
            case BinOp_Add:
            case BinOp_TypedAdd:
                code.add();
                code.coerceToAny();
                break;
                
            case BinOp_Subtract:
//                code.convertToDouble();
//                code.swap();
//                code.convertToDouble();
//                code.swap();
                code.subtract();
                code.coerceToAny();
                break;
                
            case BinOp_Multiply:
//                code.convertToDouble();
//                code.swap();
//                code.convertToDouble();
//                code.swap();
                code.multiply();
                code.coerceToAny();
                break;
                
            case BinOp_Divide:
//                code.convertToDouble();
//                code.swap();
//                code.convertToDouble();
//                code.swap();
                code.divide();
                code.coerceToAny();
                break;
                
            case BinOp_Modulo:
//                code.convertToDouble();
//                code.swap();
//                code.convertToDouble();
//                code.swap();
                code.modulo();
                code.coerceToAny();
                break;
    
            case BinOp_Equals:
            case BinOp_TypedEquals:
                code.equals();
                code.coerceToAny();
                break;
                
            case BinOp_StrictEquals:
                code.strictEquals();
                code.coerceToAny();
                break;
                
            case BinOp_LessThan:
            case BinOp_TypedLessThan:
//                code.convertToDouble();
//                code.swap();
//                code.convertToDouble();
//                code.swap();
                code.lessThan();
                code.coerceToAny();
                break;
                
            case BinOp_GreaterThan:
//                code.convertToDouble();
//                code.swap();
//                code.convertToDouble();
//                code.swap();
              
//                code.swap();
//                code.traceValue( "A = " );
//                code.swap();
//                code.traceValue( "B = " );
                code.greaterThan();
                code.coerceToAny();
//                code.traceValue( "GreaterThan = " );
                break;
    
            case BinOp_And:
                code.and();
                code.coerceToAny();
                break;
                
            case BinOp_Or:
                code.or();
                code.coerceToAny();
                break;
    
            case BinOp_BitAnd:
            case BinOp_BitOr:
            case BinOp_BitXor:
            case BinOp_ShiftLeft:
            case BinOp_ShiftRight:
            case BinOp_ShiftRightUnsigned:
            case BinOp_InstanceOf:
            case BinOp_Cast:
    
            case BinOp_Concat:
            case BinOp_StringEquals:
            case BinOp_StringLessThan:
            case BinOp_StringGreaterThan:
                //FIXME
                throw new RuntimeException("UNIMPLEMENTED AVM1 BINARY OPERATION"); 
        }
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitBooleanValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.BooleanValue) */
    public void visitBooleanValue(BooleanValue op) {
        code.pushBoolean( op.value );        
        code.coerceToAny();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallFrame(com.anotherbigidea.flash.avm1.ops.CallFrame) */
    public void visitCallFrame(CallFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallFunction(com.anotherbigidea.flash.avm1.ops.CallFunction) */
    public void visitCallFunction(CallFunction op) {

        // ***** BIG ASSUMPTION - that the arg count is a constant
        int argCount = op.numArgs.intValue();

        op.visitAggregated( this );

        LocalValue<Instruction> name = code.newLocal();
        code.setLocal( name );
        
        code.pop(); //arg count
        
        //--pack the args into an array
        code.newArray( argCount );

        //--set up for call to runtime
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();

        //--find the function
        code.getLocal( name );
        code.findProperty( AVM2LateMultiname.EMPTY_PACKAGE );        
        code.dup(); //the object        
        code.getLocal( name );
        code.getProperty( AVM2LateMultiname.EMPTY_PACKAGE ); 
        //stack is now func|obj|args|runtime|...

        code.getLocal( name ); //for debug purposes only
        code.callProperty( BabelSWFRuntime.RT_CALLFUNC_METHOD, 4 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallMethod(com.anotherbigidea.flash.avm1.ops.CallMethod) */
    public void visitCallMethod(CallMethod op) {
        
        // ***** BIG ASSUMPTION - that the arg count is a constant
        int argCount = op.numArgs.intValue();

        op.visitAggregated( this );

        LocalValue<Instruction> name = code.newLocal();
        code.setLocal( name );

        LocalValue<Instruction> object = code.newLocal();
        code.setLocal( object );

        code.pop(); //arg count
        
        //--pack the args into an array
        code.newArray( argCount );

        //--set up for call to runtime
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();
        
        //--execute the call
        code.getLocal( object );
        code.getLocal( name );
        code.callProperty( BabelSWFRuntime.RT_CALLMETH_METHOD, 3 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCloneSprite(com.anotherbigidea.flash.avm1.ops.CloneSprite) */
    public void visitCloneSprite(CloneSprite op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDecrement(com.anotherbigidea.flash.avm1.ops.Decrement) */
    public void visitDecrement(Decrement op) {
        op.visitAggregated( this );
        code.convertToDouble();
        code.decrement();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDefineLocal(com.anotherbigidea.flash.avm1.ops.DefineLocal) */
    public void visitDefineLocal(DefineLocal op) {
        op.visitAggregated( this );
        code.getLocal( code.thisValue );        
        code.swap();
        code.pushUndefined();
        code.setProperty( AVM2LateMultiname.EMPTY_PACKAGE );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDefineLocalValue(com.anotherbigidea.flash.avm1.ops.DefineLocalValue) */
    public void visitDefineLocalValue(DefineLocalValue op) {
        LocalValue<Instruction> value = code.newLocal(); 
        
        op.visitAggregated( this );
        code.setLocal( value );         
        code.getLocal( code.thisValue );        
        code.swap();
        code.getLocal( value );
        code.setProperty( AVM2LateMultiname.EMPTY_PACKAGE );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDeleteProperty(com.anotherbigidea.flash.avm1.ops.DeleteProperty) */
    public void visitDeleteProperty(DeleteProperty op) {

        LocalValue<Instruction> temp = code.newLocal(); 
        
        op.visitAggregated( this );
        code.setLocal( temp ); //save the name

        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();
        
        code.getLocal( temp );
        code.callProperty( BabelSWFRuntime.RT_DELMEMBER_METHOD, 2 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDeleteScopeProperty(com.anotherbigidea.flash.avm1.ops.DeleteScopeProperty) */
    public void visitDeleteScopeProperty(DeleteScopeProperty op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDoubleValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.DoubleValue) */
    public void visitDoubleValue(DoubleValue op) {
        code.pushDouble( op.value );        
        code.coerceToAny();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDuplicate(com.anotherbigidea.flash.avm1.ops.Duplicate) */
    public void visitDuplicate(Duplicate op) {
        code.dup();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitEndDrag(com.anotherbigidea.flash.avm1.ops.EndDrag) */
    public void visitEndDrag(EndDrag op) {
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.callPropVoid( BabelSWFRuntime.RT_ENDDRAG_METHOD, 0 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitEnumerate(com.anotherbigidea.flash.avm1.ops.Enumerate) */
    public void visitEnumerate(Enumerate op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitExtends(com.anotherbigidea.flash.avm1.ops.Extends) */
    public void visitExtends(Extends op) {
        
        LocalValue<Instruction> superclass = code.newLocal(); 
        
        op.visitAggregated( this );
        code.setLocal( superclass );  
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();
        code.getLocal( superclass );
        code.callPropVoid( BabelSWFRuntime.RT_EXTENDS_METHOD, 2 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitFloatValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.FloatValue) */
    public void visitFloatValue(FloatValue op) {
        code.pushDouble( op.value );        
        code.coerceToAny();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitFunction(com.anotherbigidea.flash.avm1.ops.Function) */
    public void visitFunction(Function op) {                
        code.getLocal( code.thisValue );
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.newFunction( makeFunction( op ) );
        code.callProperty( BabelSWFRuntime.RT_NEWFUNC_METHOD, 1 );
        code.setProperty( op.name );
    }

    private AVM2Method makeFunction( Function op ) {

        boolean needsArgs =    op.argumentsRegister != null
                          || ! op.flags.contains( Function.PreloadingFlag.SUPPRESS_ARGUMENTS );
        
        Set<MethodInfoFlags> flags = needsArgs ? EnumSet.of( MethodInfoFlags.NeedArguments ) : null;
        AVM2Method function =  avmClass.abcFile.addFunctionClosure( null, flags );
        
        for( String paramName : op.paramNames ) {
            function.addParameter( paramName, null, null );
        }
        
        AVM2MethodBody body = function.methodBody;
        body.scopeDepth = code.scopeDepth() + 1;

        AVM2Code funcCode = new AVM2Code( body, null, op.paramRegisters.length, needsArgs );
        if( op.name != null && op.name.length() > 0 ) {
            if( TRACE ) funcCode.trace( "BabelSWF >>> Entering function " + op.name );
        }
        else {
            if( TRACE ) funcCode.trace( "BabelSWF >>> Entering anon function" );
        }
        
        //create the activation object - in thisReg and on scope stack
        funcCode.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        funcCode.getLocal( funcCode.thisValue );
        funcCode.callProperty( BabelSWFRuntime.RT_NEWACTIVN_METHOD, 1 );
        funcCode.dup();
        funcCode.setLocal( funcCode.thisValue );
        funcCode.pushWith();
        
        //set up args
        if( ! op.flags.contains( Function.PreloadingFlag.SUPPRESS_ARGUMENTS ) ) {
            funcCode.getLocal( funcCode.thisValue );
            funcCode.getLocal( funcCode.argumentsValue );
            funcCode.setProperty( "arguments" );
        }
        
        OperationVisitor visitor = new OperationVisitor( timeline, funcCode );
        
        //--set up the special registers
        setupFunctionRegister( visitor, funcCode, op.thisRegister, "this" );
        setupFunctionRegister( visitor, funcCode, op.argumentsRegister, "arguments" );
        setupFunctionRegister( visitor, funcCode, op.superRegister, "super" );
        setupFunctionRegister( visitor, funcCode, op.parentRegister, "_parent" );
        setupFunctionRegister( visitor, funcCode, op.globalRegister, "_global" );
        setupFunctionRegister( visitor, funcCode, op.rootRegister, "_root" );
        
        //make the parameter mappings
        for( int i = 0; i < op.paramRegisters.length; i++ ) {
            if( op.paramRegisters[i] != null ) {
                visitor.valueMappings.put( op.paramRegisters[i].localValue, funcCode.paramValues[i] );
            }
            
            //set the param as a named var
            String paramName = op.paramNames[i];
            if( paramName != null && paramName.length() > 0 ) {                
                funcCode.getLocal( funcCode.thisValue ); //activation
                funcCode.pushString( paramName );
                funcCode.getLocal( funcCode.paramValues[i] );
                funcCode.setProperty( AVM2LateMultiname.EMPTY_PACKAGE );
            }            
        }

        op.body.accept( visitor );

//        funcCode.dump( IndentingPrintWriter.SYSERR );
//        IndentingPrintWriter.SYSERR.flush();

        //make sure there is a return at the end
        funcCode.pushUndefined();
        funcCode.returnValue();
        
        funcCode.analyze();
        
        return function;
    }
    
    private void setupFunctionRegister( OperationVisitor visitor, 
                                        AVM2Code         funcCode, 
                                        StoreInRegister  store, 
                                        String           name ) {
        if( store != null ) {
            LocalValue<Instruction> register = funcCode.newLocal();
            visitor.valueMappings.put( store.localValue, register );
            
            funcCode.findProperty( name );
            funcCode.getProperty( name );
            funcCode.setLocal( register );
        }
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetMember(com.anotherbigidea.flash.avm1.ops.GetMember) */
    public void visitGetMember(GetMember op) {
        LocalValue<Instruction> temp = code.newLocal(); 
        
        op.visitAggregated( this );
        code.setLocal( temp ); //save the name

        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();
        
        code.getLocal( temp );
        code.callProperty( BabelSWFRuntime.RT_GETMEMBER_METHOD, 2 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetProperty(com.anotherbigidea.flash.avm1.ops.GetProperty) */
    public void visitGetProperty(GetProperty op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetTargetPath(com.anotherbigidea.flash.avm1.ops.GetTargetPath) */
    public void visitGetTargetPath(GetTargetPath op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetTime(com.anotherbigidea.flash.avm1.ops.GetTime) */
    public void visitGetTime(GetTime op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetURL(com.anotherbigidea.flash.avm1.ops.GetURL) */
    public void visitGetURL(GetURL op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetVariable(com.anotherbigidea.flash.avm1.ops.GetVariable) */
    public void visitGetVariable(GetVariable op) {
        op.visitAggregated( this );

        //code.traceTop( "Before get var --> " );
        
        getVar(); 
        
        //code.traceTop( "After get var --> " );
    }

    /**
     * Get a variable - name is already on the stack
     */
    private void getVar() {
        code.dup(); //the name
        
        code.findProperty( AVM2LateMultiname.EMPTY_PACKAGE );        
        code.swap(); //object then name
        code.getProperty( AVM2LateMultiname.EMPTY_PACKAGE );        
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGotoFrame(com.anotherbigidea.flash.avm1.ops.GotoFrame) */
    public void visitGotoFrame(GotoFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIfJump(com.anotherbigidea.flash.avm1.ops.IfJump) */
    public void visitIfJump(IfJump op) {
        op.visitAggregated( this );
        code.iftrue( op.jumpLabel );
        if( TRACE ) code.trace( "@--did not jump to " + op.jumpLabel );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitImplements(com.anotherbigidea.flash.avm1.ops.Implements) */
    public void visitImplements(Implements op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIncrement(com.anotherbigidea.flash.avm1.ops.Increment) */
    public void visitIncrement(Increment op) {
        op.visitAggregated( this );
        code.convertToDouble();
        code.increment();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitInitArray(com.anotherbigidea.flash.avm1.ops.InitArray) */
    public void visitInitArray(InitArray op) {
        op.visitElements( this );
        
        //===ASSUMES THAT THE ARRAY LENGTH IS A CONSTANT
        int length = op.length.intValue();
        
        code.newArray( length );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitInitObject(com.anotherbigidea.flash.avm1.ops.InitObject) */
    public void visitInitObject(InitObject op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIntValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.IntValue) */
    public void visitIntValue(IntValue op) {
        code.pushInt( op.value );
        code.coerceToAny();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitJump(com.anotherbigidea.flash.avm1.ops.Jump) */
    public void visitJump(Jump op) {
        code.jump( op.jumpLabel );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitJumpLabel(com.anotherbigidea.flash.avm1.ops.JumpLabel) */
    public void visitJumpLabel(JumpLabel op) {
        code.target( op.label );
        code.label();
        if( TRACE ) code.trace( "@label " + op.label );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNewMethod(com.anotherbigidea.flash.avm1.ops.NewMethod) */
    public void visitNewMethod(NewMethod op) {
        // ***** BIG ASSUMPTION - that the arg count is a constant
        int argCount = op.numArgs.intValue();

        op.visitAggregated( this );

        LocalValue<Instruction> name = code.newLocal();
        code.setLocal( name );

        LocalValue<Instruction> object = code.newLocal();
        code.setLocal( object );

        code.pop(); //arg count
        
        //--pack the args into an array
        code.newArray( argCount );

        //--set up for call to runtime
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();
        
        //--execute the call
        code.getLocal( object );
        code.getLocal( name );
        code.callProperty( BabelSWFRuntime.RT_NEWMETHOD_METHOD, 3 );      
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNewObject(com.anotherbigidea.flash.avm1.ops.NewObject) */
    public void visitNewObject(NewObject op) {

        // ***** BIG ASSUMPTION - that the arg count is a constant
        int argCount = op.numArgs.intValue();

        op.visitAggregated( this );

        LocalValue<Instruction> name = code.newLocal();
        code.setLocal( name );
        
        code.pop(); //arg count
        
        //--pack the args into an array
        code.newArray( argCount );

        //--set up for call to runtime
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();

        //--find the function
        code.getLocal( name );
        code.findProperty( AVM2LateMultiname.EMPTY_PACKAGE );        
        code.dup(); //the object        
        code.getLocal( name );
        code.getProperty( AVM2LateMultiname.EMPTY_PACKAGE ); 
        //stack is now func|obj|args|runtime|...

        code.getLocal( name ); //for debug purposes only
        code.callProperty( BabelSWFRuntime.RT_NEWOBJECT_METHOD, 4 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNextFrame(com.anotherbigidea.flash.avm1.ops.NextFrame) */
    public void visitNextFrame(NextFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNullValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.NullValue) */
    public void visitNullValue(NullValue op) {
        code.pushNull();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPlay(com.anotherbigidea.flash.avm1.ops.Play) */
    public void visitPlay(Play op) {
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.callPropVoid( BabelSWFRuntime.RT_PLAY_METHOD, 0 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPop(com.anotherbigidea.flash.avm1.ops.Pop) */
    public void visitPop(Pop op) {
        
        op.visitAggregated( this );
        
        //do not pop if prev was a store register since that will already have
        //anticipated this pop and optimized
        if( op.prev() != null && op.prev() instanceof StoreInRegister ) return;

        code.pop();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPrevFrame(com.anotherbigidea.flash.avm1.ops.PrevFrame) */
    public void visitPrevFrame(PrevFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPushRegister(com.anotherbigidea.flash.avm1.ops.PushRegister) */
    public void visitPushRegister(PushRegister op) {
        
        LocalValue<AVM1Operation> val1 = op.localValue;
        LocalValue<Instruction>   val2 = valueMappings.get( val1 );
        
        if( val2 == null ) {
            val2 = code.newLocal();
            valueMappings.put( val1, val2 );
        }
        
        code.getLocal( val2 );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitRandomNumber(com.anotherbigidea.flash.avm1.ops.RandomNumber) */
    public void visitRandomNumber(RandomNumber op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitRemoveSprite(com.anotherbigidea.flash.avm1.ops.RemoveSprite) */
    public void visitRemoveSprite(RemoveSprite op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitReturnValue(com.anotherbigidea.flash.avm1.ops.ReturnValue) */
    public void visitReturnValue(ReturnValue op) {
        op.visitAggregated( this );        
        code.returnValue();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetMember(com.anotherbigidea.flash.avm1.ops.SetMember) */
    public void visitSetMember(SetMember op) {
        LocalValue<Instruction> tempVal  = code.newLocal(); 
        LocalValue<Instruction> tempName = code.newLocal(); 
        
        op.visitAggregated( this );
        code.setLocal( tempVal  ); //save the value        
        code.setLocal( tempName ); //save the name        
        
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();

        code.getLocal( tempName );
        code.getLocal( tempVal  );
        code.callPropVoid( BabelSWFRuntime.RT_SETMEMBER_METHOD, 3 );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetProperty(com.anotherbigidea.flash.avm1.ops.SetProperty) */
    public void visitSetProperty(SetProperty op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetTarget(com.anotherbigidea.flash.avm1.ops.SetTarget) */
    public void visitSetTarget(SetTarget op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetVariable(com.anotherbigidea.flash.avm1.ops.SetVariable) */
    public void visitSetVariable(SetVariable op) {
        LocalValue<Instruction> value = code.newLocal(); 
        
        op.visitAggregated( this );
        code.setLocal( value );    
        
        //stack top = name
        code.dup();
        code.findProperty( AVM2LateMultiname.EMPTY_PACKAGE );
        
        //stack top = obj with prop or global
        code.dup();
        code.getGlobalScope();
        String isNotGlobal = code.newLabel();
        code.ifstrictne( isNotGlobal );
        
        //stack top is global - use activation obj instead
        code.pop();
        code.getLocal( code.thisValue );        
        
        code.target( isNotGlobal );
        code.label();
        
        //stack top is the object - swap with the name
        code.swap();
        
        code.getLocal( value );
        code.setProperty( AVM2LateMultiname.EMPTY_PACKAGE );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStackValue(com.anotherbigidea.flash.avm1.ops.StackValue) */
    public void visitStackValue(StackValue op) {
        //nada - the value is already on the stack
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStartDrag(com.anotherbigidea.flash.avm1.ops.StartDrag) */
    public void visitStartDrag(StartDrag op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStop(com.anotherbigidea.flash.avm1.ops.Stop) */
    public void visitStop(Stop op) {
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.callPropVoid( BabelSWFRuntime.RT_STOP_METHOD, 0 );

    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStopSounds(com.anotherbigidea.flash.avm1.ops.StopSounds) */
    public void visitStopSounds(StopSounds op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStoreInRegister(com.anotherbigidea.flash.avm1.ops.StoreInRegister) */
    public void visitStoreInRegister(StoreInRegister op) {
        
        op.visitAggregated( this );
        
        if( op.next() != null && op.next() instanceof Pop ) {
            //do not duplicate the value since it will be popped anyway
        }
        else {
            code.dup();
        }

        LocalValue<AVM1Operation> val1 = op.localValue;
        LocalValue<Instruction>   val2 = valueMappings.get( val1 );
        
        if( val2 == null ) {
            val2 = code.newLocal();
            valueMappings.put( val1, val2 );
        }
        
        code.setLocal( val2 ); 
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStringValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.StringValue) */
    public void visitStringValue(StringValue op) {
        code.pushString( op.value );        
        code.coerceToAny();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSubstring(com.anotherbigidea.flash.avm1.ops.Substring) */
    public void visitSubstring(Substring op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSwap(com.anotherbigidea.flash.avm1.ops.Swap) */
    public void visitSwap(Swap op) {
        code.swap();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitThrowException(com.anotherbigidea.flash.avm1.ops.ThrowException) */
    public void visitThrowException(ThrowException op) {
        op.visitAggregated( this );
        code.throwException();        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitToggleQuality(com.anotherbigidea.flash.avm1.ops.ToggleQuality) */
    public void visitToggleQuality(ToggleQuality op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTrace(com.anotherbigidea.flash.avm1.ops.Trace) */
    public void visitTrace(Trace op) {
        op.visitAggregated( this );
        
        code.getLex( BabelSWFRuntime.AVM1_RUNTIME_CLASS );
        code.swap();
        code.callPropVoid( BabelSWFRuntime.RT_TRACE_METHOD, 1 );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTry(com.anotherbigidea.flash.avm1.ops.Try) */
    public void visitTry(Try op) {        
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
//        AVM2Code.ExceptionHandler handler = code.tryStart();
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTryCatch(com.anotherbigidea.flash.avm1.ops.TryCatch) */
    public void visitTryCatch( TryCatch op ) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTryEnd(com.anotherbigidea.flash.avm1.ops.TryEnd) */
    public void visitTryEnd( TryEnd op ) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTryFinally(com.anotherbigidea.flash.avm1.ops.TryFinally) */
    public void visitTryFinally( TryFinally op ) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitUnaryOp(com.anotherbigidea.flash.avm1.ops.UnaryOp) */
    public void visitUnaryOp(UnaryOp op) {
        
        op.visitAggregated( this );
        
        switch( op.type ) {
        
            case UnOp_Not:
                code.not();
                code.coerceToAny();
                break;
                
            case UnOp_StringLength:
            case UnOp_StringLengthMB:
            
            case UnOp_CharToAscii:
            case UnOp_AsciiToChar:
            case UnOp_CharMBToAscii:
            case UnOp_AsciiToCharMB:
    
            case UnOp_ToInteger:
            case UnOp_ConvertToNumber:
            case UnOp_ConvertToString:
            
            case UnOp_TypeOf:
                //FIXME
                throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION"); 
        }
   }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitUndefinedValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.UndefinedValue) */
    public void visitUndefinedValue(UndefinedValue op) {
        code.pushUndefined();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitWaitForFrame(com.anotherbigidea.flash.avm1.ops.WaitForFrame) */
    public void visitWaitForFrame(WaitForFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitWith(com.anotherbigidea.flash.avm1.ops.With) */
    public void visitWith(With op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitWithEnd(com.anotherbigidea.flash.avm1.ops.WithEnd) */
    public void visitWithEnd( WithEnd op ) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
    }
}
