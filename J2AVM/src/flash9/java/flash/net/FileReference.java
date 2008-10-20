package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class FileReference extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FileReference(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native boolean browse( flash.FlashArray arg1 );

    @SlotId( -1 ) 
    public native boolean browse(  );

    @SlotId( -1 ) @Getter
    public native String getCreator(  );

    @SlotId( -1 ) @Getter
    public native int getSize(  );

    @SlotId( -1 ) @Getter
    public native String getType(  );

    @SlotId( -1 ) @Getter
    public native String getName(  );

    @SlotId( -1 ) 
    public native void upload( flash.net.URLRequest arg1, String arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native void upload( flash.net.URLRequest arg1, String arg2 );

    @SlotId( -1 ) 
    public native void upload( flash.net.URLRequest arg1 );

    @SlotId( -1 ) 
    public native void cancel(  );

    @SlotId( -1 ) 
    public native void download( flash.net.URLRequest arg1, String arg2 );

    @SlotId( -1 ) 
    public native void download( flash.net.URLRequest arg1 );

    @SlotId( -1 ) @Getter
    public native flash.FlashDate getModificationDate(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashDate getCreationDate(  );

}
