package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class FileReferenceList extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FileReferenceList(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native boolean browse( flash.FlashArray arg1 );

    @SlotId( -1 ) 
    public native boolean browse(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getFileList(  );

}
