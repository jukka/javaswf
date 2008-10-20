package flash.xml;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class XMLDocument extends flash.xml.XMLNode   {

    @SlotId( -1 )
    public flash.FlashObject idMap;	

    @SlotId( -1 )
    public flash.FlashObject xmlDecl = null;	

    @SlotId( -1 )
    public flash.FlashObject docTypeDecl = null;	

    @SlotId( -1 )
    public boolean ignoreWhite = false;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLDocument( String arg1 ) { 
		super( 0, null );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLDocument(  ) { 
		super( 0, null );
	}




    @SlotId( -1 ) 
    public native flash.xml.XMLNode createElement( String arg1 );

    @SlotId( -1 ) 
    public native void parseXML( String arg1 );

    @SlotId( -1 ) 
    public native flash.xml.XMLNode createTextNode( String arg1 );

}
