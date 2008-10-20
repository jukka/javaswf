package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Bitmap extends flash.display.DisplayObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Bitmap( flash.display.BitmapData arg1, String arg2, boolean arg3 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Bitmap( flash.display.BitmapData arg1, String arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Bitmap( flash.display.BitmapData arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Bitmap(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native flash.display.BitmapData getBitmapData(  );

    @SlotId( -1 ) @Setter
    public native void setBitmapData( flash.display.BitmapData arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getSmoothing(  );

    @SlotId( -1 ) @Setter
    public native void setPixelSnapping( String arg1 );

    @SlotId( -1 ) @Getter
    public native String getPixelSnapping(  );

    @SlotId( -1 ) @Setter
    public native void setSmoothing( boolean arg1 );

}
