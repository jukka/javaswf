package flash.system;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class ApplicationDomain extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ApplicationDomain( flash.system.ApplicationDomain arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ApplicationDomain(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native flash.FlashObject getDefinition( String arg1 );

    @SlotId( -1 ) 
    public native boolean hasDefinition( String arg1 );

    @SlotId( -1 ) @Getter
    public native flash.system.ApplicationDomain getParentDomain(  );

    @SlotId( 2 ) @Getter
    public static final native flash.system.ApplicationDomain getCurrentDomain(  );

}
