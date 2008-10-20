package flash.system;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class System extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public System(  ) { 
		super(  );
	}




    @SlotId( 2 ) @Getter
    public static final native flash.system.IME getIme(  );

    @SlotId( 5 ) @Getter
    public static final native boolean getUseCodePage(  );

    @SlotId( 3 ) 
    public static final native void setClipboard( String arg1 );

    @SlotId( 4 ) @Getter
    public static final native int getTotalMemory(  );

    @SlotId( 6 ) @Setter
    public static final native void setUseCodePage( boolean arg1 );

    @SlotId( 7 ) @Getter
    public static final native String getVmVersion(  );

}
