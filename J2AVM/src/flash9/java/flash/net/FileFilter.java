package flash.net;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class FileFilter extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FileFilter( String arg1, String arg2, String arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public FileFilter( String arg1, String arg2 ) { 
		super(  );
	}




    @SlotId( -1 ) @Setter
    public native void setMacType( String arg1 );

    @SlotId( -1 ) @Getter
    public native String getExtension(  );

    @SlotId( -1 ) @Setter
    public native void setDescription( String arg1 );

    @SlotId( -1 ) @Getter
    public native String getMacType(  );

    @SlotId( -1 ) @Getter
    public native String getDescription(  );

    @SlotId( -1 ) @Setter
    public native void setExtension( String arg1 );

}
