package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class Scene extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Scene( String arg1, flash.FlashArray arg2, int arg3 ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native String getName(  );

    @SlotId( -1 ) @Getter
    public native int getNumFrames(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getLabels(  );

}
