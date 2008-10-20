package flash.printing;

import org.epistem.j2avm.annotations.runtime.*;

//---------------------------------------------------------------------------
// THIS FILE WAS AUTOMATICALLY GENERATED - HAND ALTERATIONS MAY BE LOST
//---------------------------------------------------------------------------
@FlashNativeClass
public class PrintJob extends flash.events.EventDispatcher   {



    
	/**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public PrintJob(  ) { 
		super( null );
	}




    @SlotId( -1 ) 
    public native void send(  );

    @SlotId( -1 ) @Getter
    public native String getOrientation(  );

    @SlotId( -1 ) @Getter
    public native int getPaperHeight(  );

    @SlotId( -1 ) @Getter
    public native int getPageHeight(  );

    @SlotId( -1 ) @Getter
    public native int getPaperWidth(  );

    @SlotId( -1 ) 
    public native void addPage( flash.display.Sprite arg1, flash.geom.Rectangle arg2, flash.printing.PrintJobOptions arg3, int arg4 );

    @SlotId( -1 ) 
    public native void addPage( flash.display.Sprite arg1, flash.geom.Rectangle arg2, flash.printing.PrintJobOptions arg3 );

    @SlotId( -1 ) 
    public native void addPage( flash.display.Sprite arg1, flash.geom.Rectangle arg2 );

    @SlotId( -1 ) 
    public native void addPage( flash.display.Sprite arg1 );

    @SlotId( -1 ) 
    public native boolean start(  );

    @SlotId( -1 ) @Getter
    public native int getPageWidth(  );

}
