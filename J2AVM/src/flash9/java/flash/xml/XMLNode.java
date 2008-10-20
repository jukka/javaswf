package flash.xml;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class XMLNode extends flash.FlashObject   {

    @SlotId( -1 )
    public flash.xml.XMLNode previousSibling;	

    @SlotId( -1 )
    public flash.xml.XMLNode parentNode;	

    @SlotId( -1 )
    public String nodeValue;	

    @SlotId( -1 )
    public flash.xml.XMLNode firstChild;	

    @SlotId( -1 )
    public flash.xml.XMLNode nextSibling;	

    @SlotId( -1 )
    public int nodeType;	

    @SlotId( -1 )
    public flash.xml.XMLNode lastChild;	

    @SlotId( -1 )
    public String nodeName;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLNode( int arg1, String arg2 ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native String getNamespaceURI(  );

    @SlotId( -1 ) @Getter
    public native String getPrefix(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getAttributes(  );

    @SlotId( -1 ) 
    public native void appendChild( flash.xml.XMLNode arg1 );

    @SlotId( -1 ) 
    public native void insertBefore( flash.xml.XMLNode arg1, flash.xml.XMLNode arg2 );

    @SlotId( -1 ) 
    public native String getNamespaceForPrefix( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setAttributes( flash.FlashObject arg1 );

    @SlotId( -1 ) 
    public native boolean hasChildNodes(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getChildNodes(  );

    @SlotId( -1 ) @Getter
    public native String getLocalName(  );

    @SlotId( -1 ) 
    public native String getPrefixForNamespace( String arg1 );

    @SlotId( -1 ) 
    public native void removeNode(  );

    @SlotId( -1 ) 
    public native flash.xml.XMLNode cloneNode( boolean arg1 );

}
