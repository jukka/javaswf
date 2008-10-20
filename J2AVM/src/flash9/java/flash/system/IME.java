package flash.system;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class IME extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public IME(  ) { 
		super( null );
	}




    @SlotId( 7 ) 
    public static final native void setCompositionString( String arg1 );

    @SlotId( 3 ) @Getter
    public static final native boolean getEnabled(  );

    @SlotId( 6 ) @Setter
    public static final native void setConversionMode( String arg1 );

    @SlotId( 2 ) @Setter
    public static final native void setConstructOK( boolean arg1 );

    @SlotId( 8 ) 
    public static final native void doConversion(  );

    @SlotId( 5 ) @Getter
    public static final native String getConversionMode(  );

    @SlotId( 4 ) @Setter
    public static final native void setEnabled( boolean arg1 );

}
