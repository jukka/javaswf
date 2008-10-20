package flash.system;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class SecurityDomain extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SecurityDomain(  ) { 
		super(  );
	}




    @SlotId( 2 ) @Getter
    public static final native flash.system.SecurityDomain getCurrentDomain(  );

}
