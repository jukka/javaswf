package com.anotherbigidea.flash.avm1;

import com.anotherbigidea.flash.avm1.ops.*;
import com.anotherbigidea.flash.avm1.ops.ConstantOp.*;

/**
 * Visitor interface for operations
 *
 * @author nickmain
 */
public interface AVM1OpVisitor {

    public void visitAnonymousFunction( AnonymousFunction op );
    public void visitBinaryOp( BinaryOp op );
    public void visitCallFrame( CallFrame op );
    public void visitCallFunction( CallFunction op );
    public void visitCallMethod( CallMethod op );
    public void visitCloneSprite( CloneSprite op );
    public void visitDecrement( Decrement op );
    public void visitDefineLocal( DefineLocal op );
    public void visitDefineLocalValue( DefineLocalValue op );
    public void visitDeleteProperty( DeleteProperty op );
    public void visitDeleteScopeProperty( DeleteScopeProperty op );
    public void visitDuplicate( Duplicate op );
    public void visitEndDrag( EndDrag op );
    public void visitEnumerate( Enumerate op );
    public void visitExtends( Extends op );
    public void visitFunction( Function op );
    public void visitGetMember( GetMember op );
    public void visitGetProperty( GetProperty op );
    public void visitGetTargetPath( GetTargetPath op );
    public void visitGetTime( GetTime op );
    public void visitGetURL( GetURL op );
    public void visitGetVariable( GetVariable op );
    public void visitGotoFrame( GotoFrame op );
    public void visitIfJump( IfJump op );
    public void visitImplements( Implements op );
    public void visitIncrement( Increment op );
    public void visitInitArray( InitArray op );
    public void visitInitObject( InitObject op );
    public void visitJump( Jump op );
    public void visitJumpLabel( JumpLabel op );
    public void visitNewMethod( NewMethod op );
    public void visitNewObject( NewObject op );
    public void visitNextFrame( NextFrame op );
    public void visitPlay( Play op );
    public void visitPop( Pop op );
    public void visitPrevFrame( PrevFrame op );
    public void visitPushRegister( PushRegister op );
    public void visitRandomNumber( RandomNumber op );
    public void visitRemoveSprite( RemoveSprite op );
    public void visitReturnValue( ReturnValue op );
    public void visitSetMember( SetMember op );
    public void visitSetProperty( SetProperty op );
    public void visitSetTarget( SetTarget op );
    public void visitSetVariable( SetVariable op );
    public void visitStackValue( StackValue op );
    public void visitStartDrag( StartDrag op );
    public void visitStop( Stop op );
    public void visitStopSounds( StopSounds op );
    public void visitStoreInRegister( StoreInRegister op );
    public void visitSubstring( Substring op );
    public void visitSwap( Swap op );
    public void visitThrowException( ThrowException op );
    public void visitToggleQuality( ToggleQuality op );
    public void visitTrace( Trace op );
    
    public void visitTry( Try op );
    public void visitTryCatch( TryCatch op );
    public void visitTryFinally( TryFinally op );
    public void visitTryEnd( TryEnd op );
    
    public void visitUnaryOp( UnaryOp op );
    public void visitWaitForFrame( WaitForFrame op );
    public void visitWith( With op );
    public void visitWithEnd( WithEnd op );
    
    public void visitIntValue       ( IntValue op );
    public void visitBooleanValue   ( BooleanValue op );
    public void visitFloatValue     ( FloatValue op );
    public void visitDoubleValue    ( DoubleValue op );
    public void visitNullValue      ( NullValue op );
    public void visitUndefinedValue ( UndefinedValue op );
    public void visitStringValue    ( StringValue op );
    
    /**
     * Helper implementation with optional delegation
     */
    public static class Impl implements AVM1OpVisitor {
        private final AVM1OpVisitor visitor;
        
        /**
         * @param visitor delegee
         */
        public Impl( AVM1OpVisitor visitor ) {
            this.visitor = visitor;
        }

        /**
         * No delegation
         */
        public Impl() {
            visitor = null;
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitAnonymousFunction(com.anotherbigidea.flash.avm1.ops.AnonymousFunction) */
        public void visitAnonymousFunction(AnonymousFunction op) {
            if( visitor != null ) visitor.visitAnonymousFunction( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitBinaryOp(com.anotherbigidea.flash.avm1.ops.BinaryOp) */
        public void visitBinaryOp(BinaryOp op) {
            if( visitor != null ) visitor.visitBinaryOp( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitBooleanValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.BooleanValue) */
        public void visitBooleanValue(BooleanValue op) {
            if( visitor != null ) visitor.visitBooleanValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallFrame(com.anotherbigidea.flash.avm1.ops.CallFrame) */
        public void visitCallFrame(CallFrame op) {
            if( visitor != null ) visitor.visitCallFrame( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallFunction(com.anotherbigidea.flash.avm1.ops.CallFunction) */
        public void visitCallFunction(CallFunction op) {
            if( visitor != null ) visitor.visitCallFunction( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCallMethod(com.anotherbigidea.flash.avm1.ops.CallMethod) */
        public void visitCallMethod(CallMethod op) {
            if( visitor != null ) visitor.visitCallMethod( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitCloneSprite(com.anotherbigidea.flash.avm1.ops.CloneSprite) */
        public void visitCloneSprite(CloneSprite op) {
            if( visitor != null ) visitor.visitCloneSprite( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDecrement(com.anotherbigidea.flash.avm1.ops.Decrement) */
        public void visitDecrement(Decrement op) {
            if( visitor != null ) visitor.visitDecrement( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDefineLocal(com.anotherbigidea.flash.avm1.ops.DefineLocal) */
        public void visitDefineLocal(DefineLocal op) {
            if( visitor != null ) visitor.visitDefineLocal( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDefineLocalValue(com.anotherbigidea.flash.avm1.ops.DefineLocalValue) */
        public void visitDefineLocalValue(DefineLocalValue op) {
            if( visitor != null ) visitor.visitDefineLocalValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDeleteProperty(com.anotherbigidea.flash.avm1.ops.DeleteProperty) */
        public void visitDeleteProperty(DeleteProperty op) {
            if( visitor != null ) visitor.visitDeleteProperty( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDeleteScopeProperty(com.anotherbigidea.flash.avm1.ops.DeleteScopeProperty) */
        public void visitDeleteScopeProperty(DeleteScopeProperty op) {
            if( visitor != null ) visitor.visitDeleteScopeProperty( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDoubleValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.DoubleValue) */
        public void visitDoubleValue(DoubleValue op) {
            if( visitor != null ) visitor.visitDoubleValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitDuplicate(com.anotherbigidea.flash.avm1.ops.Duplicate) */
        public void visitDuplicate(Duplicate op) {
            if( visitor != null ) visitor.visitDuplicate( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitEndDrag(com.anotherbigidea.flash.avm1.ops.EndDrag) */
        public void visitEndDrag(EndDrag op) {
            if( visitor != null ) visitor.visitEndDrag( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitEnumerate(com.anotherbigidea.flash.avm1.ops.Enumerate) */
        public void visitEnumerate(Enumerate op) {
            if( visitor != null ) visitor.visitEnumerate( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitExtends(com.anotherbigidea.flash.avm1.ops.Extends) */
        public void visitExtends(Extends op) {
            if( visitor != null ) visitor.visitExtends( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitFloatValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.FloatValue) */
        public void visitFloatValue(FloatValue op) {
            if( visitor != null ) visitor.visitFloatValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitFunction(com.anotherbigidea.flash.avm1.ops.Function) */
        public void visitFunction(Function op) {
            if( visitor != null ) visitor.visitFunction( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetMember(com.anotherbigidea.flash.avm1.ops.GetMember) */
        public void visitGetMember(GetMember op) {
            if( visitor != null ) visitor.visitGetMember( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetProperty(com.anotherbigidea.flash.avm1.ops.GetProperty) */
        public void visitGetProperty(GetProperty op) {
            if( visitor != null ) visitor.visitGetProperty( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetTargetPath(com.anotherbigidea.flash.avm1.ops.GetTargetPath) */
        public void visitGetTargetPath(GetTargetPath op) {
            if( visitor != null ) visitor.visitGetTargetPath( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetTime(com.anotherbigidea.flash.avm1.ops.GetTime) */
        public void visitGetTime(GetTime op) {
            if( visitor != null ) visitor.visitGetTime( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetURL(com.anotherbigidea.flash.avm1.ops.GetURL) */
        public void visitGetURL(GetURL op) {
            if( visitor != null ) visitor.visitGetURL( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGetVariable(com.anotherbigidea.flash.avm1.ops.GetVariable) */
        public void visitGetVariable(GetVariable op) {
            if( visitor != null ) visitor.visitGetVariable( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitGotoFrame(com.anotherbigidea.flash.avm1.ops.GotoFrame) */
        public void visitGotoFrame(GotoFrame op) {
            if( visitor != null ) visitor.visitGotoFrame( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIfJump(com.anotherbigidea.flash.avm1.ops.IfJump) */
        public void visitIfJump(IfJump op) {
            if( visitor != null ) visitor.visitIfJump( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitImplements(com.anotherbigidea.flash.avm1.ops.Implements) */
        public void visitImplements(Implements op) {
            if( visitor != null ) visitor.visitImplements( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIncrement(com.anotherbigidea.flash.avm1.ops.Increment) */
        public void visitIncrement(Increment op) {
            if( visitor != null ) visitor.visitIncrement( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitInitArray(com.anotherbigidea.flash.avm1.ops.InitArray) */
        public void visitInitArray(InitArray op) {
            if( visitor != null ) visitor.visitInitArray( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitInitObject(com.anotherbigidea.flash.avm1.ops.InitObject) */
        public void visitInitObject(InitObject op) {
            if( visitor != null ) visitor.visitInitObject( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitIntValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.IntValue) */
        public void visitIntValue(IntValue op) {
            if( visitor != null ) visitor.visitIntValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitJump(com.anotherbigidea.flash.avm1.ops.Jump) */
        public void visitJump(Jump op) {
            if( visitor != null ) visitor.visitJump( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitJumpLabel(com.anotherbigidea.flash.avm1.ops.JumpLabel) */
        public void visitJumpLabel(JumpLabel op) {
            if( visitor != null ) visitor.visitJumpLabel( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNewMethod(com.anotherbigidea.flash.avm1.ops.NewMethod) */
        public void visitNewMethod(NewMethod op) {
            if( visitor != null ) visitor.visitNewMethod( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNewObject(com.anotherbigidea.flash.avm1.ops.NewObject) */
        public void visitNewObject(NewObject op) {
            if( visitor != null ) visitor.visitNewObject( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNextFrame(com.anotherbigidea.flash.avm1.ops.NextFrame) */
        public void visitNextFrame(NextFrame op) {
            if( visitor != null ) visitor.visitNextFrame( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitNullValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.NullValue) */
        public void visitNullValue(NullValue op) {
            if( visitor != null ) visitor.visitNullValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPlay(com.anotherbigidea.flash.avm1.ops.Play) */
        public void visitPlay(Play op) {
            if( visitor != null ) visitor.visitPlay( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPop(com.anotherbigidea.flash.avm1.ops.Pop) */
        public void visitPop(Pop op) {
            if( visitor != null ) visitor.visitPop( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPrevFrame(com.anotherbigidea.flash.avm1.ops.PrevFrame) */
        public void visitPrevFrame(PrevFrame op) {
            if( visitor != null ) visitor.visitPrevFrame( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitPushRegister(com.anotherbigidea.flash.avm1.ops.PushRegister) */
        public void visitPushRegister(PushRegister op) {
            if( visitor != null ) visitor.visitPushRegister( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitRandomNumber(com.anotherbigidea.flash.avm1.ops.RandomNumber) */
        public void visitRandomNumber(RandomNumber op) {
            if( visitor != null ) visitor.visitRandomNumber( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitRemoveSprite(com.anotherbigidea.flash.avm1.ops.RemoveSprite) */
        public void visitRemoveSprite(RemoveSprite op) {
            if( visitor != null ) visitor.visitRemoveSprite( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitReturnValue(com.anotherbigidea.flash.avm1.ops.ReturnValue) */
        public void visitReturnValue(ReturnValue op) {
            if( visitor != null ) visitor.visitReturnValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetMember(com.anotherbigidea.flash.avm1.ops.SetMember) */
        public void visitSetMember(SetMember op) {
            if( visitor != null ) visitor.visitSetMember( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetProperty(com.anotherbigidea.flash.avm1.ops.SetProperty) */
        public void visitSetProperty(SetProperty op) {
            if( visitor != null ) visitor.visitSetProperty( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetTarget(com.anotherbigidea.flash.avm1.ops.SetTarget) */
        public void visitSetTarget(SetTarget op) {
            if( visitor != null ) visitor.visitSetTarget( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSetVariable(com.anotherbigidea.flash.avm1.ops.SetVariable) */
        public void visitSetVariable(SetVariable op) {
            if( visitor != null ) visitor.visitSetVariable( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStackValue(com.anotherbigidea.flash.avm1.ops.StackValue) */
        public void visitStackValue(StackValue op) {
            if( visitor != null ) visitor.visitStackValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStartDrag(com.anotherbigidea.flash.avm1.ops.StartDrag) */
        public void visitStartDrag(StartDrag op) {
            if( visitor != null ) visitor.visitStartDrag( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStop(com.anotherbigidea.flash.avm1.ops.Stop) */
        public void visitStop(Stop op) {
            if( visitor != null ) visitor.visitStop( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStopSounds(com.anotherbigidea.flash.avm1.ops.StopSounds) */
        public void visitStopSounds(StopSounds op) {
            if( visitor != null ) visitor.visitStopSounds( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStoreInRegister(com.anotherbigidea.flash.avm1.ops.StoreInRegister) */
        public void visitStoreInRegister(StoreInRegister op) {
            if( visitor != null ) visitor.visitStoreInRegister( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitStringValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.StringValue) */
        public void visitStringValue(StringValue op) {
            if( visitor != null ) visitor.visitStringValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSubstring(com.anotherbigidea.flash.avm1.ops.Substring) */
        public void visitSubstring(Substring op) {
            if( visitor != null ) visitor.visitSubstring( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitSwap(com.anotherbigidea.flash.avm1.ops.Swap) */
        public void visitSwap(Swap op) {
            if( visitor != null ) visitor.visitSwap( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitThrowException(com.anotherbigidea.flash.avm1.ops.ThrowException) */
        public void visitThrowException(ThrowException op) {
            if( visitor != null ) visitor.visitThrowException( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitToggleQuality(com.anotherbigidea.flash.avm1.ops.ToggleQuality) */
        public void visitToggleQuality(ToggleQuality op) {
            if( visitor != null ) visitor.visitToggleQuality( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTrace(com.anotherbigidea.flash.avm1.ops.Trace) */
        public void visitTrace(Trace op) {
            if( visitor != null ) visitor.visitTrace( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTry(com.anotherbigidea.flash.avm1.ops.Try) */
        public void visitTry(Try op) {
            if( visitor != null ) visitor.visitTry( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitUnaryOp(com.anotherbigidea.flash.avm1.ops.UnaryOp) */
        public void visitUnaryOp(UnaryOp op) {
            if( visitor != null ) visitor.visitUnaryOp( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitUndefinedValue(com.anotherbigidea.flash.avm1.ops.ConstantOp.UndefinedValue) */
        public void visitUndefinedValue(UndefinedValue op) {
            if( visitor != null ) visitor.visitUndefinedValue( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitWaitForFrame(com.anotherbigidea.flash.avm1.ops.WaitForFrame) */
        public void visitWaitForFrame(WaitForFrame op) {
            if( visitor != null ) visitor.visitWaitForFrame( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitWith(com.anotherbigidea.flash.avm1.ops.With) */
        public void visitWith(With op) {
            if( visitor != null ) visitor.visitWith( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitWithEnd(com.anotherbigidea.flash.avm1.ops.WithEnd) */
        public void visitWithEnd( WithEnd op ) {
            if( visitor != null ) visitor.visitWithEnd( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTryCatch(com.anotherbigidea.flash.avm1.ops.TryCatch) */
        public void visitTryCatch( TryCatch op ) {
            if( visitor != null ) visitor.visitTryCatch( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTryEnd(com.anotherbigidea.flash.avm1.ops.TryEnd) */
        public void visitTryEnd( TryEnd op ) {
            if( visitor != null ) visitor.visitTryEnd( op );
        }

        /** @see com.anotherbigidea.flash.avm1.AVM1OpVisitor#visitTryFinally(com.anotherbigidea.flash.avm1.ops.TryFinally) */
        public void visitTryFinally( TryFinally op ) {
            if( visitor != null ) visitor.visitTryFinally( op );
        }        
        
        
    }
}
