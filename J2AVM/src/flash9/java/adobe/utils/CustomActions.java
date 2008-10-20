package adobe.utils;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class CustomActions extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public CustomActions(  ) { 
		super(  );
	}




    @SlotId( 2 ) 
    public static final native void installActions( String arg1, String arg2 );

    @SlotId( 3 ) 
    public static final native void uninstallActions( String arg1 );

    @SlotId( 4 ) @Getter
    public static final native flash.FlashArray getActionsList(  );

    @SlotId( 5 ) 
    public static final native String getActions( String arg1 );

}
