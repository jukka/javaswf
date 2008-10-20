package adobe.utils;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class ProductManager extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ProductManager( String arg1 ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native boolean launch(  );

    @SlotId( -1 ) 
    public native boolean download( String arg1 );

    @SlotId( -1 ) 
    public native boolean download(  );

    @SlotId( -1 ) @Getter
    public native boolean getInstalled(  );

    @SlotId( -1 ) @Getter
    public native String getInstalledVersion(  );

    @SlotId( -1 ) @Getter
    public native boolean getRunning(  );

}
