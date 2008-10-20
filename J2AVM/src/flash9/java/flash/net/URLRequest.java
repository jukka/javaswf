package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class URLRequest extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public URLRequest( String arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public URLRequest(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Setter
    public native void setMethod( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setContentType( String arg1 );

    @SlotId( -1 ) @Getter
    public native String getMethod(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getData(  );

    @SlotId( -1 ) @Setter
    public native void setRequestHeaders( flash.FlashArray arg1 );

    @SlotId( -1 ) @Setter
    public native void setData( flash.FlashObject arg1 );

    @SlotId( -1 ) @Getter
    public native String getUrl(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getRequestHeaders(  );

    @SlotId( -1 ) @Getter
    public native String getContentType(  );

    @SlotId( -1 ) @Setter
    public native void setUrl( String arg1 );

}
