package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Loader extends flash.display.DisplayObjectContainer   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Loader(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native flash.display.DisplayObject addChild( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) 
    public native void unload(  );

    @SlotId( -1 ) 
    public native void setChildIndex( flash.display.DisplayObject arg1, int arg2 );

    @SlotId( -1 ) 
    public native void loadBytes( flash.utils.ByteArray arg1, flash.system.LoaderContext arg2 );

    @SlotId( -1 ) 
    public native void loadBytes( flash.utils.ByteArray arg1 );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject removeChildAt( int arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.DisplayObject getContent(  );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject addChildAt( flash.display.DisplayObject arg1, int arg2 );

    @SlotId( -1 ) @Getter
    public native flash.display.LoaderInfo getContentLoaderInfo(  );

    @SlotId( -1 ) 
    public native void load( flash.net.URLRequest arg1, flash.system.LoaderContext arg2 );

    @SlotId( -1 ) 
    public native void load( flash.net.URLRequest arg1 );

    @SlotId( -1 ) 
    public native flash.display.DisplayObject removeChild( flash.display.DisplayObject arg1 );

    @SlotId( -1 ) 
    public native void close(  );

}
