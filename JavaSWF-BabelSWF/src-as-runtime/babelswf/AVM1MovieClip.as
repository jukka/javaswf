/**
 * Base for the different movieclip types.  
 */
package babelswf
{
	import flash.display.MovieClip;
	
	public class AVM1MovieClip extends MovieClip
	{
		private var symbolId:int;
		private var frames:Array = [];
		
		public function AVM1MovieClip( symbolId:int )
	    {
			this.symbolId = symbolId;
		}

        /** Get the symbol id */
        public final function get avm1_id():int { return symbolId; }
        
        /**
         * Add a frame to the timeline
         */ 
        public final function addFrame( frame:AVM1Frame ):void 
        {
        	super.addFrameScript();
        	
        	frames[ frame.number ] = frame;
        }
	}
}