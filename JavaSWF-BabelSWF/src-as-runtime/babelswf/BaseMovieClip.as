/**
 * This class is included in BabelSWF translated files and is the superclass
 * for the main timeline and sub-clips.
 * 
 * This class should be used to implement runtime functionality that
 * simulates that expected by an AVM1 movieclip. 
 */
package babelswf
{
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.display.MovieClip;

	public class BaseMovieClip extends MovieClip
	{		
		private var swfName:String;

		public function BaseMovieClip( swfName:String )
		{
			super();
			
			this.swfName = swfName;
			trace( "babelswf.BaseMovieClip >> " + swfName );
		}
		
		/**
		 * This method is called in place of the AVM1 trace instruction.
		 */
		public function babelswf_trace( message:* ):void 
		{
			trace( "babelswf_trace >> " + message ); 		
		}
		
		/**
		 * This function is called once all the frame scripts have been added to 
		 * the timeline. 
		 */
		public function babelswf_framesAdded( frameCount:int ):void 
		{
		    trace( frameCount + " frames added" );			
		} 
		
		/**
		 * Simulation of the AVM1 ASSetPropFlags function.
		 */
		public static function ASSetPropFlags( targetObject:Object, 
		                                       propertyList:Object, 
		                                       flags:int ):* {
		    
		    trace( "ASSetPropFlags called" );
		
		    //TODO - simulate this
		                                	
		    return undefined;                                	
        }
        
        /**
         * Utility to dump the props of an object 
         */
        public static function babelswf_dump( object:Object ):void {
        	for( var n:String in object ) {
        		trace( "dump >> " + n );         		
        	}         	
        } 
        
        /**
         * Simulate _root 
         */
        public function get _root():DisplayObject 
        { 
            return root; 
        }
        
        /**
         * Simulate _parent 
         */
        public function get _parent():DisplayObjectContainer
        {
            return parent;	
        } 
        
        /**
         * Simulate _global 
         */
        public static function get _global():Object
        {
            trace( "babelswf >> accessing _global" );
        	
            return GlobalObject._global;  
        } 
    }    
}