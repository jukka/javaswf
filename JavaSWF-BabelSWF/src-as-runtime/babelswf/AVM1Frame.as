/**
 * Represents a frame in a translated AVM1Movie 
 */
package babelswf
{
	public class AVM1Frame
	{
		/**
		 * The function containing the frame code
		 */
		private var frameFunction:Function;
		
		/**
		 * The movieclip this frame belongs to
		 */
		private var movieclip:AVM1MovieClip;
		
		/**
		 * The AVM1InitActions in this frame
		 */
		private var initActions:Array;
		
		/**
		 * The frame number
		 */
		private var frameNumber:int;		
		
		public function AVM1Frame( movieclip    :AVM1MovieClip,
		                           frameNumber  :int,
		                           frameFunction:Function )
		{
			this.frameFunction = frameFunction;
			this.frameNumber   = frameNumber;
			this.movieclip     = movieclip;
		}

        /**
         * Add some init actions to be performed in this frame
         */ 
        public final function addInitActions( acts:AVM1InitActions ):void {
        	if( initActions == null ) initActions = [];
        	initActions.push( acts ); 
        }

        /** Get the frame number */
        public final function get number():int { return frameNumber; }

        /**
         * Method closure that is called by the AVM2 when the playhead
         * reached this frame 
         */ 
        public final function callFrame():void
        {
        	//TODO - call any init actions pending for this frame - once only
        	
        	//TODO - call frame function
        	trace( "AVM1Frame >> " + movieclip.avm1_id + "[" + frameNumber + "]" );
        }
	}
}