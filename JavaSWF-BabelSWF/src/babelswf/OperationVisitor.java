package babelswf;

import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.ops.*;
import com.anotherbigidea.flash.avm1.ops.ConstantOp.*;
import com.anotherbigidea.flash.avm2.model.AVM2Code;

public class OperationVisitor implements AVM1OpVisitor {

    private final AVM2Code code;
    
    public OperationVisitor( AVM2Code code ) {
        this.code = code;
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitAnonymousFunction(com.anotherbigidea.flash.avm1.ops.AnonymousFunction) */
    public void visitAnonymousFunction(AnonymousFunction op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitBinaryOp(com.anotherbigidea.flash.avm1.ops.BinaryOp) */
    public void visitBinaryOp(BinaryOp op) {
        
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitBooleanValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.BooleanValue) */
    public void visitBooleanValue(BooleanValue op) {
        code.pushBoolean( op.value );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallFrame(com.anotherbigidea.flash.avm1.ops.CallFrame) */
    public void visitCallFrame(CallFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallFunction(com.anotherbigidea.flash.avm1.ops.CallFunction) */
    public void visitCallFunction(CallFunction op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallMethod(com.anotherbigidea.flash.avm1.ops.CallMethod) */
    public void visitCallMethod(CallMethod op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCloneSprite(com.anotherbigidea.flash.avm1.ops.CloneSprite) */
    public void visitCloneSprite(CloneSprite op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDecrement(com.anotherbigidea.flash.avm1.ops.Decrement) */
    public void visitDecrement(Decrement op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDefineLocal(com.anotherbigidea.flash.avm1.ops.DefineLocal) */
    public void visitDefineLocal(DefineLocal op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDefineLocalValue(com.anotherbigidea.flash.avm1.ops.DefineLocalValue) */
    public void visitDefineLocalValue(DefineLocalValue op) {
        
        
        
        
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDeleteProperty(com.anotherbigidea.flash.avm1.ops.DeleteProperty) */
    public void visitDeleteProperty(DeleteProperty op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDeleteScopeProperty(com.anotherbigidea.flash.avm1.ops.DeleteScopeProperty) */
    public void visitDeleteScopeProperty(DeleteScopeProperty op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDoubleValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.DoubleValue) */
    public void visitDoubleValue(DoubleValue op) {
        code.pushDouble( op.value );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDuplicate(com.anotherbigidea.flash.avm1.ops.Duplicate) */
    public void visitDuplicate(Duplicate op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitEndDrag(com.anotherbigidea.flash.avm1.ops.EndDrag) */
    public void visitEndDrag(EndDrag op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitEnumerate(com.anotherbigidea.flash.avm1.ops.Enumerate) */
    public void visitEnumerate(Enumerate op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitExtends(com.anotherbigidea.flash.avm1.ops.Extends) */
    public void visitExtends(Extends op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitFloatValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.FloatValue) */
    public void visitFloatValue(FloatValue op) {
        code.pushDouble( op.value );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitFunction(com.anotherbigidea.flash.avm1.ops.Function) */
    public void visitFunction(Function op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetMember(com.anotherbigidea.flash.avm1.ops.GetMember) */
    public void visitGetMember(GetMember op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
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
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGotoFrame(com.anotherbigidea.flash.avm1.ops.GotoFrame) */
    public void visitGotoFrame(GotoFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIfJump(com.anotherbigidea.flash.avm1.ops.IfJump) */
    public void visitIfJump(IfJump op) {
        code.iftrue( op.jumpLabel );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitImplements(com.anotherbigidea.flash.avm1.ops.Implements) */
    public void visitImplements(Implements op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIncrement(com.anotherbigidea.flash.avm1.ops.Increment) */
    public void visitIncrement(Increment op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitInitArray(com.anotherbigidea.flash.avm1.ops.InitArray) */
    public void visitInitArray(InitArray op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitInitObject(com.anotherbigidea.flash.avm1.ops.InitObject) */
    public void visitInitObject(InitObject op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIntValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.IntValue) */
    public void visitIntValue(IntValue op) {
        code.pushInt( op.value );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitJump(com.anotherbigidea.flash.avm1.ops.Jump) */
    public void visitJump(Jump op) {
        code.jump( op.jumpLabel );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitJumpLabel(com.anotherbigidea.flash.avm1.ops.JumpLabel) */
    public void visitJumpLabel(JumpLabel op) {
        code.target( op.label );
        code.label();
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNewMethod(com.anotherbigidea.flash.avm1.ops.NewMethod) */
    public void visitNewMethod(NewMethod op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNewObject(com.anotherbigidea.flash.avm1.ops.NewObject) */
    public void visitNewObject(NewObject op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
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
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPop(com.anotherbigidea.flash.avm1.ops.Pop) */
    public void visitPop(Pop op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPrevFrame(com.anotherbigidea.flash.avm1.ops.PrevFrame) */
    public void visitPrevFrame(PrevFrame op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPushRegister(com.anotherbigidea.flash.avm1.ops.PushRegister) */
    public void visitPushRegister(PushRegister op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
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
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetMember(com.anotherbigidea.flash.avm1.ops.SetMember) */
    public void visitSetMember(SetMember op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
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
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStackValue(com.anotherbigidea.flash.avm1.ops.StackValue) */
    public void visitStackValue(StackValue op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStartDrag(com.anotherbigidea.flash.avm1.ops.StartDrag) */
    public void visitStartDrag(StartDrag op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStop(com.anotherbigidea.flash.avm1.ops.Stop) */
    public void visitStop(Stop op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStopSounds(com.anotherbigidea.flash.avm1.ops.StopSounds) */
    public void visitStopSounds(StopSounds op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStoreInRegister(com.anotherbigidea.flash.avm1.ops.StoreInRegister) */
    public void visitStoreInRegister(StoreInRegister op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStringValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.StringValue) */
    public void visitStringValue(StringValue op) {
        code.pushString( op.value );        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSubstring(com.anotherbigidea.flash.avm1.ops.Substring) */
    public void visitSubstring(Substring op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSwap(com.anotherbigidea.flash.avm1.ops.Swap) */
    public void visitSwap(Swap op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitThrowException(com.anotherbigidea.flash.avm1.ops.ThrowException) */
    public void visitThrowException(ThrowException op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitToggleQuality(com.anotherbigidea.flash.avm1.ops.ToggleQuality) */
    public void visitToggleQuality(ToggleQuality op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTrace(com.anotherbigidea.flash.avm1.ops.Trace) */
    public void visitTrace(Trace op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTry(com.anotherbigidea.flash.avm1.ops.Try) */
    public void visitTry(Try op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitUnaryOp(com.anotherbigidea.flash.avm1.ops.UnaryOp) */
    public void visitUnaryOp(UnaryOp op) {
        throw new RuntimeException("UNIMPLEMENTED AVM1 OPERATION");  // TODO
        
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

    
    
}
