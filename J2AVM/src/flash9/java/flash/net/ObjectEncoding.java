package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class ObjectEncoding extends flash.FlashObject   {

    @SlotId( 2 )
    public static final int DEFAULT = 3;	

    @SlotId( 0 )
    public static final int AMF0 = 0;	

    @SlotId( 1 )
    public static final int AMF3 = 3;	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ObjectEncoding(  ) { 
		super(  );
	}




    @SlotId( 2 ) @Getter
    public static final native flash.net.IDynamicPropertyWriter getDynamicPropertyWriter(  );

    @SlotId( 3 ) @Setter
    public static final native void setDynamicPropertyWriter( flash.net.IDynamicPropertyWriter arg1 );

}
