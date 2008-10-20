package flash.system;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class Security extends flash.FlashObject   {

    @SlotId( 3 )
    public static final String LOCAL_TRUSTED = "localTrusted";	

    @SlotId( 0 )
    public static final String REMOTE = "remote";	

    @SlotId( 1 )
    public static final String LOCAL_WITH_FILE = "localWithFile";	

    @SlotId( 2 )
    public static final String LOCAL_WITH_NETWORK = "localWithNetwork";	



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Security(  ) { 
		super(  );
	}




    @SlotId( 9 ) 
    public static final native void showSettings( String arg1 );

    @SlotId( 9 ) 
    public static final native void showSettings(  );

    @SlotId( 10 ) @Getter
    public static final native String getSandboxType(  );

    @SlotId( 5 ) @Getter
    public static final native boolean getExactSettings(  );

    @SlotId( 6 ) @Setter
    public static final native void setExactSettings( boolean arg1 );

    @SlotId( 2 ) 
    public static final native void allowDomain(  );

    @SlotId( 3 ) 
    public static final native void allowInsecureDomain(  );

    @SlotId( 4 ) 
    public static final native void loadPolicyFile( String arg1 );

    @SlotId( 8 ) @Setter
    public static final native void setDisableAVM1Loading( boolean arg1 );

    @SlotId( 7 ) @Getter
    public static final native boolean getDisableAVM1Loading(  );

}
