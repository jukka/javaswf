package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class ContextMenuEvent extends flash.events.Event   {

    @SlotId( 0 )
    public static final String MENU_ITEM_SELECT = "menuItemSelect";	

    @SlotId( 1 )
    public static final String MENU_SELECT = "menuSelect";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuEvent( String arg1, boolean arg2, boolean arg3, flash.display.InteractiveObject arg4, flash.display.InteractiveObject arg5 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuEvent( String arg1, boolean arg2, boolean arg3, flash.display.InteractiveObject arg4 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuEvent( String arg1, boolean arg2, boolean arg3 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuEvent( String arg1, boolean arg2 ) { 
		super( null, false, false );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuEvent( String arg1 ) { 
		super( null, false, false );
	}




    @SlotId( -1 ) @Setter
    public native void setContextMenuOwner( flash.display.InteractiveObject arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.InteractiveObject getMouseTarget(  );

    @SlotId( -1 ) @Getter
    public native flash.display.InteractiveObject getContextMenuOwner(  );

    @SlotId( -1 ) @Setter
    public native void setMouseTarget( flash.display.InteractiveObject arg1 );

}
