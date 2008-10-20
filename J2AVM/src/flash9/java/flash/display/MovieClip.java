package flash.display;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class MovieClip extends flash.display.Sprite   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public MovieClip(  ) { 
		super(  );
	}




    @SlotId( -1 ) @Getter
    public native flash.FlashArray getCurrentLabels(  );

    @SlotId( -1 ) 
    public native void stop(  );

    @SlotId( -1 ) @Getter
    public native String getCurrentLabel(  );

    @SlotId( -1 ) @Getter
    public native int getTotalFrames(  );

    @SlotId( -1 ) 
    public native void prevScene(  );

    @SlotId( -1 ) 
    public native void play(  );

    @SlotId( -1 ) 
    public native void addFrameScript(  );

    @SlotId( -1 ) 
    public native void nextFrame(  );

    @SlotId( -1 ) @Getter
    public native boolean getEnabled(  );

    @SlotId( -1 ) @Getter
    public native int getFramesLoaded(  );

    @SlotId( -1 ) @Getter
    public native flash.FlashArray getScenes(  );

    @SlotId( -1 ) 
    public native void nextScene(  );

    @SlotId( -1 ) @Getter
    public native int getCurrentFrame(  );

    @SlotId( -1 ) @Setter
    public native void setEnabled( boolean arg1 );

    @SlotId( -1 ) 
    public native void gotoAndStop( flash.FlashObject arg1, String arg2 );

    @SlotId( -1 ) 
    public native void gotoAndStop( flash.FlashObject arg1 );

    @SlotId( -1 ) @Getter
    public native flash.display.Scene getCurrentScene(  );

    @SlotId( -1 ) @Setter
    public native void setTrackAsMenu( boolean arg1 );

    @SlotId( -1 ) 
    public native void gotoAndPlay( flash.FlashObject arg1, String arg2 );

    @SlotId( -1 ) 
    public native void gotoAndPlay( flash.FlashObject arg1 );

    @SlotId( -1 ) @Getter
    public native boolean getTrackAsMenu(  );

    @SlotId( -1 ) 
    public native void prevFrame(  );

}
