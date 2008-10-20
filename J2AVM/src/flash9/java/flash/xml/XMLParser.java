package flash.xml;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class XMLParser extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLParser(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native void startParse( String arg1, boolean arg2 );

    @SlotId( -1 ) 
    public native int getNext( flash.xml.XMLTag arg1 );

}
