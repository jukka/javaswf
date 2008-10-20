package flash.ui;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class ContextMenuItem extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuItem( String arg1, boolean arg2, boolean arg3, boolean arg4 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuItem( String arg1, boolean arg2, boolean arg3 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuItem( String arg1, boolean arg2 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ContextMenuItem( String arg1 ) { 
		super( null );
	}




    @SlotId( -1 ) @Setter
    public native void setEnabled( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setCaption( String arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getEnabled(  );

    @SlotId( -1 ) @Getter
    public native boolean getSeparatorBefore(  );

    @SlotId( -1 ) @Getter
    public native boolean getVisible(  );

    @SlotId( -1 ) @Getter
    public native String getCaption(  );

    @SlotId( -1 ) @Setter
    public native void setSeparatorBefore( boolean arg1 );

    @SlotId( -1 ) @Setter
    public native void setVisible( boolean arg1 );

}
