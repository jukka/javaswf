package flash.xml;

import org.javaswf.j2avm.runtime.annotations.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class XMLTag extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public XMLTag(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Setter
    public native void setValue( String arg1 );

    @SlotId( -1 ) @Setter
    public native void setAttrs( flash.FlashObject arg1 );

    @SlotId( -1 ) @Getter
    public native int getType(  );

    @SlotId( -1 ) @Getter
    public native boolean getEmpty(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getAttrs(  );

    @SlotId( -1 ) @Getter
    public native String getValue(  );

    @SlotId( -1 ) @Setter
    public native void setType( int arg1 );

    @SlotId( -1 ) @Setter
    public native void setEmpty( boolean arg1 );

}
