package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class FrameLabel extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FrameLabel( String arg1, int arg2 ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native String getName(  );

    @SlotId( -1 ) @Getter
    public native int getFrame(  );

}
