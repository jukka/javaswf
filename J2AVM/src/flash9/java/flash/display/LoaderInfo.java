package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class LoaderInfo extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public LoaderInfo(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native boolean dispatchEvent( flash.events.Event arg1 );

    @SlotId( -1 ) @Getter
    public native int getBytesLoaded(  );

    @SlotId( -1 ) @Getter
    public native String getContentType(  );

    @SlotId( -1 ) @Getter
    public native boolean getChildAllowsParent(  );

    @SlotId( -1 ) @Getter
    public native int getBytesTotal(  );

    @SlotId( -1 ) @Getter
    public native flash.display.Loader getLoader(  );

    @SlotId( -1 ) @Getter
    public native String getLoaderURL(  );

    @SlotId( -1 ) @Getter
    public native int getWidth(  );

    @SlotId( -1 ) @Getter
    public native boolean getSameDomain(  );

    @SlotId( -1 ) @Getter
    public native flash.events.EventDispatcher getSharedEvents(  );

    @SlotId( -1 ) @Getter
    public native int getHeight(  );

    @SlotId( -1 ) @Getter
    public native boolean getParentAllowsChild(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashObject getParameters(  );

    @SlotId( -1 ) @Getter
    public native double getFrameRate(  );

    @SlotId( -1 ) @Getter
    public native String getUrl(  );

    @SlotId( -1 ) @Getter
    public native int getSwfVersion(  );

    @SlotId( -1 ) @Getter
    public native int getActionScriptVersion(  );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getContent(  );

    @SlotId( -1 ) @Getter
    public native flash.system.ApplicationDomain getApplicationDomain(  );

}
