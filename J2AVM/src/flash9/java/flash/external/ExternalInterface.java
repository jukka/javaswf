package flash.external;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class ExternalInterface extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ExternalInterface(  ) { 
		super(  );
	}




    @SlotId( 3 ) 
    public static final native void addCallback( String arg1, flash.FlashFunction arg2 );

    @SlotId( 2 ) @Getter
    public static final native boolean getAvailable(  );

    @SlotId( 7 ) @Getter
    public static final native String getObjectID(  );

    @SlotId( 4 ) 
    public static final native Object call( String arg1 );

}
