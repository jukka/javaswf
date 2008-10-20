package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public interface IEventDispatcher   {



    @SlotId( -1 ) 
    public boolean dispatchEvent( flash.events.Event arg1 );

    @SlotId( -1 ) 
    public boolean hasEventListener( String arg1 );

    @SlotId( -1 ) 
    public boolean willTrigger( String arg1 );

    @SlotId( -1 ) 
    public void removeEventListener( String arg1, flash.FlashFunction arg2, boolean arg3 );

    @SlotId( -1 ) 
    public void removeEventListener( String arg1, flash.FlashFunction arg2 );

    @SlotId( -1 ) 
    public void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3, int arg4, boolean arg5 );

    @SlotId( -1 ) 
    public void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3, int arg4 );

    @SlotId( -1 ) 
    public void addEventListener( String arg1, flash.FlashFunction arg2, boolean arg3 );

    @SlotId( -1 ) 
    public void addEventListener( String arg1, flash.FlashFunction arg2 );

}
