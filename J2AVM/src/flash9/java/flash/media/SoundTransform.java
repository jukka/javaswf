package flash.media;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public final class SoundTransform extends flash.FlashObject   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SoundTransform( double arg1, double arg2 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SoundTransform( double arg1 ) { 
		super(  );
	}


    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public SoundTransform(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Setter
    public native void setPan( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setRightToLeft( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getRightToRight(  );

    @SlotId( -1 ) @Getter
    public native double getLeftToLeft(  );

    @SlotId( -1 ) @Setter
    public native void setRightToRight( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setLeftToLeft( double arg1 );

    @SlotId( -1 ) @Setter
    public native void setVolume( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getVolume(  );

    @SlotId( -1 ) @Setter
    public native void setLeftToRight( double arg1 );

    @SlotId( -1 ) @Getter
    public native double getLeftToRight(  );

    @SlotId( -1 ) @Getter
    public native double getPan(  );

    @SlotId( -1 ) @Getter
    public native double getRightToLeft(  );

}
