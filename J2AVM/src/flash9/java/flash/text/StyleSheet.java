package flash.text;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class StyleSheet extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public StyleSheet(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void parseCSS( String arg1 );

    @SlotId( -1 ) 
    public native void clear(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getStyleNames(  );

    @SlotId( -1 ) 
    public native flash.text.TextFormat transform( flash.FlashObject arg1 );

    @SlotId( -1 ) 
    public native void setStyle( String arg1, flash.FlashObject arg2 );

    @SlotId( -1 ) 
    public native flash.FlashObject getStyle( String arg1 );

}
