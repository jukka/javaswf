package flash.ui;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class ContextMenu extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenu(  ) { 
		super( null );
	}




    @SlotId( -1 ) @Getter
    public native flash.ui.ContextMenuBuiltInItems getBuiltInItems(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getCustomItems(  );

    @SlotId( -1 ) @Setter
    public native void setBuiltInItems( flash.ui.ContextMenuBuiltInItems arg1 );

    @SlotId( -1 ) @Setter
    public native void setCustomItems( flash.FlashArray arg1 );

    @SlotId( -1 ) 
    public native void hideBuiltInItems(  );

}
