package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class URLLoader extends flash.events.EventDispatcher   {

    @SlotId( -1 )
    public String dataFormat = "text";	

    @SlotId( -1 )
    public int bytesLoaded = 0;	

    @SlotId( -1 )
    public int bytesTotal = 0;	

    @SlotId( -1 )
    public Object data;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public URLLoader( flash.net.URLRequest arg1 ) { 
		super( null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public URLLoader(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void load( flash.net.URLRequest arg1 );

    @SlotId( -1 ) 
    public native void close(  );

}
