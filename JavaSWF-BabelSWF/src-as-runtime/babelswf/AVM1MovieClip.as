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
		
	    private var execContext:AVM1ExecutionContext; 
		//private var avm1mc:Object = AVM1Runtime.global.MovieClip.avm1_new([]);
		
		public function AVM1MovieClip( symbolId:int )
	    {
			this.symbolId = symbolId;
			
			this["__proto__"] = AVM1Runtime.global.MovieClip.prototype;
			this["this"]      = this;
			
			//avm1mc["this"] = avm1mc;
            //avm1mc["AVM1MovieClip"] = this;
            //avm1mc.setPropertyIsEnumerable( "AVM1MovieClip", false );
			//execContext = new AVM1ExecutionContext( avm1mc, null, this ); 
            
            execContext = new AVM1ExecutionContext( this, null, this ); 
		}

        /** Get the symbol id */
        public final function get avm1_id():int { return symbolId; }
        
        /**
         * Access the movieclip from the AVM1 object
         */ 
        public static function mc( obj:Object ):AVM1MovieClip
        {
        	return obj as AVM1MovieClip; //obj["AVM1MovieClip"];
        }
        
        /**
         * Get the parent mc
         */ 
        public function avm1_getParent():AVM1MovieClip
        {
        	if( parent is AVM1MovieClip ) return parent as AVM1MovieClip;
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
        
        /**
         * Get the execution context for frames of this clip 
         */
        public function getExecutionContext():AVM1ExecutionContext
        {
        	return execContext;
        }
        
        /** Access the AVM1 movieclip object */
       // public function get avm1Object():Object { return avm1mc; }
	}
}