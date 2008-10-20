package flash.text;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class TextSnapshot extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public TextSnapshot(  ) { 
		super(  );
	}




    @SlotId( -1 ) 
    public native String getText( int arg1, int arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native String getText( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native void setSelectColor( int arg1 );

    @SlotId( -1 ) 
    public native void setSelectColor(  );

    @SlotId( -1 ) 
    public native int findText( int arg1, String arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native boolean getSelected( int arg1, int arg2 );

    @SlotId( -1 ) 
    public native flash.FlashArray getTextRunInfo( int arg1, int arg2 );

    @SlotId( -1 ) @Getter
    public native int getCharCount(  );

    @SlotId( -1 ) 
    public native double hitTestTextNearPos( double arg1, double arg2, double arg3 );

    @SlotId( -1 ) 
    public native double hitTestTextNearPos( double arg1, double arg2 );

    @SlotId( -1 ) 
    public native void setSelected( int arg1, int arg2, boolean arg3 );

    @SlotId( -1 ) 
    public native String getSelectedText( boolean arg1 );

    @SlotId( -1 ) 
    public native String getSelectedText(  );

}
