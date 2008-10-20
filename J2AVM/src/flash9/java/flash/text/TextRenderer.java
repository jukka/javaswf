package flash.text;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class TextRenderer extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public TextRenderer(  ) { 
		super(  );
	}




    @SlotId( 6 ) @Setter
    public static final native void setMaxLevel( int arg1 );

    @SlotId( 7 ) @Getter
    public static final native String getDisplayMode(  );

    @SlotId( 4 ) 
    public static final native void setAdvancedAntiAliasingTable( String arg1, String arg2, String arg3, flash.FlashArray arg4 );

    @SlotId( 5 ) @Getter
    public static final native int getMaxLevel(  );

    @SlotId( 2 ) @Getter
    public static final native String getAntiAliasType(  );

    @SlotId( 8 ) @Setter
    public static final native void setDisplayMode( String arg1 );

    @SlotId( 3 ) @Setter
    public static final native void setAntiAliasType( String arg1 );

}
