/**
 * Represents a frame in a translated AVM1Movie 
 */
package babelswf
{
	public class AVM1Frame
	{
		/**
		 * The frame actions
		 */
		private var actions:Function; //may be null if there are only init-actions
		                              //in this frame
		
		/**
		 * The movieclip this frame belongs to
		 */
		private var movieclip:AVM1MovieClip;
		
		/**
		 * The AVM1InitActions in this frame
		 */
		private var initActions:Array = [];
		
		/**
		 * The frame number
		 */
		private var frameNumber:int;		
		
		public function AVM1Frame( movieclip   :AVM1MovieClip,
		                           frameNumber :int,
		                           actions     :Function )
		{
			this.actions     = actions;
			this.frameNumber = frameNumber;
			this.movieclip   = movieclip;
		}

        /**
         * Add some init actions to be performed in this frame
         */ 
        public final function addInitActions( acts:AVM1InitActions ):void {
        	initActions.push( acts ); 
        }

        /** Get the frame number */
        public final function get number():int { return frameNumber; }

        /**
         * Method closure that is called by the AVM2 when the playhead
         * reaches this frame 
         */ 
        public final function callFrame():void
        {
        	//call any init actions pending for this frame - once only
        	while( initActions.length > 0 ) 
        	{
        		var initActs:AVM1InitActions = initActions.shift() as AVM1InitActions;
        		initActs.call( movieclip.getExecutionContext() );        		
        	} 
        	
        	//TODO - call frame function
        	trace( "AVM1Frame >> " + movieclip.avm1_id + "[" + frameNumber + "]" );
        	        	
        	if( actions == null ) return;         	
			actions.call( movieclip.getExecutionContext() );
        }
	}
}