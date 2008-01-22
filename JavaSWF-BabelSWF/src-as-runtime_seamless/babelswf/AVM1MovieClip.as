/**
 * Base for the different movieclip types.  
 */
package babelswf
{
	import flash.display.MovieClip;
	
	public dynamic class AVM1MovieClip extends MovieClip
	{
		private var symbolId:int;
		private var frames  :Array = [];
		private var pendingInitActs:Array = [];

        private var proxyWrapper:AVM1Object = new AVM1Object( this );
		
		public function AVM1MovieClip( symbolId:int )
	    {
			this.symbolId = symbolId;
			this["__proto__"] = AVM1Runtime.avm1_global.MovieClip.prototype;
		}

        /** Get the symbol id */
        public final function get avm1_id():int { return symbolId; }
        
        /** Get the AVM1 object proxy for the movieclip */
        public final function get avm1_proxy():AVM1Object { return proxyWrapper; }
        
        /**
         * Get the parent
         */ 
        public function avm1_getParent():AVM1MovieClip
        {
            if( parent is AVM1MovieClip ) return (parent as AVM1MovieClip);
            return undefined; 
        }
        
        /**
         * Add a frame to the timeline
         */ 
        public final function avm1_addFrame( number:int, actions:Function ):void 
        {
        	var frame:AVM1Frame = new AVM1Frame( this, number, actions );
        	
			trace( "AVM1MovieClip[" + symbolId + "].addFrame >> " + number );        	
        	
        	frames[ frame.number ] = frame;
        	
        	//gather any pending init actions
        	var initsForFrame:Array = pendingInitActs[number];
        	if( initsForFrame != null ) {
        		delete pendingInitActs[number];
        		
        		for each( var initActs:AVM1InitActions in initsForFrame )
        		{
        			frame.addInitActions( initActs );
        		} 
        	}
        	
        	addFrameScript( number, frame.callFrame() );
        }

        /**        
         * Add some initclip actions
         */ 
        public final function avm1_addInitActs( frameNumber:int, 
                                                symbolId   :int,
                                                acts       :Function ):void
        {
        	var initActs:AVM1InitActions = new AVM1InitActions( symbolId, acts );

            trace( "avm1_addInitActs >> frame=" + frameNumber + " id=" + symbolId );           
        	
            var frame:AVM1Frame = frames[frameNumber];
            if( frame == null ) 
            {
            	//enqueue the init actions for later addition to frame
            	var initsForFrame:Array = pendingInitActs[frameNumber];
            	if( initsForFrame == null )
            	{
            		initsForFrame = [];
            		pendingInitActs[frameNumber] = initsForFrame;
            	} 
            	initsForFrame.push( initActs );
            } 
        	else 
        	{        	
        	   frame.addInitActions( initActs );
        	} 
        }
	}
}