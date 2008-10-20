package flash.accessibility;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class Accessibility extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Accessibility(  ) { 
		super(  );
	}




    @SlotId( 3 ) 
    public static final native void sendEvent( flash.display.DisplayObject arg1, int arg2, int arg3, boolean arg4 );

    @SlotId( 3 ) 
    public static final native void sendEvent( flash.display.DisplayObject arg1, int arg2, int arg3 );

    @SlotId( 4 ) 
    public static final native void updateProperties(  );

    @SlotId( 2 ) @Getter
    public static final native boolean getActive(  );

}
