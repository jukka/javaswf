package flash.text;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class Font extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public Font(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native String getFontStyle(  );

    @SlotId( -1 ) 
    public native boolean hasGlyphs( String arg1 );

    @SlotId( -1 ) @Getter
    public native String getFontType(  );

    @SlotId( -1 ) @Getter
    public native String getFontName(  );

    @SlotId( 2 ) 
    public static final native flash.FlashArray enumerateFonts( boolean arg1 );

    @SlotId( 2 ) 
    public static final native flash.FlashArray enumerateFonts(  );

    @SlotId( 3 ) 
    public static final native void registerFont( Class arg1 );

}
